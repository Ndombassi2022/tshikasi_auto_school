package com.tshikasi.tshikasi_auto_school.presentation.pages.classroom


import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerPage(
    lesson: VideoLesson,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Adicionar o vídeo
            setMediaItem(MediaItem.fromUri(lesson.videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    // Liberar player quando sair da tela
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(0) } // 0=Descrição, 1=Comentários

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assistir Aula") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            useController = true
                            controllerShowTimeoutMs = 3000
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Content abaixo do vídeo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Lesson Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = lesson.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Teacher and Views
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = lesson.teacher,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "${lesson.views} visualizações",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Subject and Grade Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = lesson.subjectColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = lesson.subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = lesson.subjectColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Gray.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = lesson.grade,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Audio Button
                        if (lesson.hasAudio) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { /* Play audio */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headset,
                                    contentDescription = "Áudio",
                                    tint = Color(0xFF3B82F6)
                                )
                                Text(
                                    text = "Áudio",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Download Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Download */ }
                        ) {
                            Icon(
                                imageVector = if (lesson.isDownloaded)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Download,
                                contentDescription = "Download",
                                tint = if (lesson.isDownloaded)
                                    Color(0xFF10B981)
                                else
                                    Color(0xFF3B82F6)
                            )
                            Text(
                                text = if (lesson.isDownloaded) "Baixado" else "Baixar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Share Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Share */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Partilhar",
                                tint = Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Partilhar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Favorite Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Toggle favorite */ }
                        ) {
                            Icon(
                                imageVector = if (lesson.isFavorite)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (lesson.isFavorite)
                                    Color(0xFFEF4444)
                                else
                                    Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Favorito",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Divider()

                // Tabs
                TabRow(
                    selectedTabIndex = currentTab,
                    containerColor = Color.White
                ) {
                    Tab(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        text = { Text("Descrição") }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        text = { Text("Materiais") }
                    )
                    Tab(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        text = { Text("Comentários") }
                    )
                }

                // Tab Content
                when (currentTab) {
                    0 -> DescriptionTab(lesson)
                    1 -> MaterialsTab()
                    2 -> CommentsTab()
                }
            }
        }
    }
}

@Composable
fun DescriptionTab(lesson: VideoLesson) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Sobre esta aula",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    Nesta aula vamos aprender sobre ${lesson.title}.
                    
                    O professor ${lesson.teacher} vai explicar de forma detalhada todos os conceitos importantes para você dominar este tema.
                    
                    📚 Conteúdo abordado:
                    • Conceitos fundamentais
                    • Exemplos práticos
                    • Exercícios resolvidos
                    • Dicas para o exame
                    
                    ⏱️ Duração: ${lesson.duration}
                    📊 Dificuldade: Intermediária
                    🎯 Classe: ${lesson.grade}
                """.trimIndent(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MaterialsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Materiais de apoio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(materials) { material ->
            MaterialCard(material)
        }
    }
}

@Composable
fun MaterialCard(material: Material) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Download material */ },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (material.type) {
                        "PDF" -> Icons.Default.PictureAsPdf
                        "DOC" -> Icons.Default.Description
                        else -> Icons.Default.AttachFile
                    },
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        text = material.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${material.size} • ${material.type}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Baixar",
                tint = Color(0xFF3B82F6)
            )
        }
    }
}

@Composable
fun CommentsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "12 comentários",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = { /* Sort */ }) {
                    Text("Mais recentes")
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        }

        items(5) { index ->
            CommentCard(
                name = "Estudante ${index + 1}",
                comment = "Excelente aula! Consegui entender muito melhor o conceito.",
                timeAgo = "${index + 1}h atrás",
                likes = (10 - index) * 2
            )
        }
    }
}

@Composable
fun CommentCard(
    name: String,
    comment: String,
    timeAgo: String,
    likes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = comment,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { /* Like */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Gostar",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = likes.toString(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "Responder",
                    fontSize = 12.sp,
                    color = Color(0xFF3B82F6),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* Reply */ }
                )
            }
        }
    }
}

// ==================== DATA CLASSES ====================

data class VideoLesson(
    val id: Long,
    val title: String,
    val subject: String,
    val subjectColor: Color,
    val grade: String,
    val teacher: String,
    val duration: String,
    val videoUrl: String, // URL do vídeo
    val audioUrl: String?, // URL do áudio (opcional)
    val views: Int,
    val progress: Int,
    val hasAudio: Boolean,
    val isDownloaded: Boolean,
    val isFavorite: Boolean
)

data class Material(
    val name: String,
    val type: String,
    val size: String
)

private val materials = listOf(
    Material("Resumo da Aula.pdf", "PDF", "2.5 MB"),
    Material("Exercícios Práticos.pdf", "PDF", "1.8 MB"),
    Material("Slides da Apresentação.pptx", "PPT", "5.2 MB")
)

/*
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerPage(
    lesson: VideoLesson,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Adicionar o vídeo
            setMediaItem(MediaItem.fromUri(lesson.))
            prepare()
            playWhenReady = true
        }
    }

    // Liberar player quando sair da tela
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    var showControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(0) } // 0=Descrição, 1=Comentários

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assistir Aula") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            useController = true
                            controllerShowTimeoutMs = 3000
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Content abaixo do vídeo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Lesson Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = lesson.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Teacher and Views
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = lesson.teacher,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "${lesson.views} visualizações",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Subject and Grade Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = lesson.subjectColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = lesson.subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = lesson.subjectColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Gray.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = lesson.grade,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Audio Button
                        if (lesson.hasAudio) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { /* Play audio */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headset,
                                    contentDescription = "Áudio",
                                    tint = Color(0xFF3B82F6)
                                )
                                Text(
                                    text = "Áudio",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Download Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Download */ }
                        ) {
                            Icon(
                                imageVector = if (lesson.isDownloaded)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Download,
                                contentDescription = "Download",
                                tint = if (lesson.isDownloaded)
                                    Color(0xFF10B981)
                                else
                                    Color(0xFF3B82F6)
                            )
                            Text(
                                text = if (lesson.isDownloaded) "Baixado" else "Baixar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Share Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Share */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Partilhar",
                                tint = Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Partilhar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Favorite Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* Toggle favorite */ }
                        ) {
                            Icon(
                                imageVector = if (lesson.isFavorite)
                                    Icons.Default.Favorite
                                else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (lesson.isFavorite)
                                    Color(0xFFEF4444)
                                else
                                    Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Favorito",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Divider()

                // Tabs
                TabRow(
                    selectedTabIndex = currentTab,
                    containerColor = Color.White
                ) {
                    Tab(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        text = { Text("Descrição") }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        text = { Text("Materiais") }
                    )
                    Tab(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        text = { Text("Comentários") }
                    )
                }

                // Tab Content
                when (currentTab) {
                    0 -> DescriptionTab(lesson)
                    1 -> MaterialsTab()
                    2 -> CommentsTab()
                }
            }
        }
    }
}

@Composable
fun DescriptionTab(lesson: VideoLesson) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Sobre esta aula",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    Nesta aula vamos aprender sobre ${lesson.title}.
                    
                    O professor ${lesson.teacher} vai explicar de forma detalhada todos os conceitos importantes para você dominar este tema.
                    
                    📚 Conteúdo abordado:
                    • Conceitos fundamentais
                    • Exemplos práticos
                    • Exercícios resolvidos
                    • Dicas para o exame
                    
                    ⏱️ Duração: ${lesson.duration}
                    📊 Dificuldade: Intermediária
                    🎯 Classe: ${lesson.grade}
                """.trimIndent(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MaterialsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Materiais de apoio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(materials) { material ->
            MaterialCard(material)
        }
    }
}

@Composable
fun MaterialCard(material: Material) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Download material */ },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (material.type) {
                        "PDF" -> Icons.Default.PictureAsPdf
                        "DOC" -> Icons.Default.Description
                        else -> Icons.Default.AttachFile
                    },
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        text = material.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${material.size} • ${material.type}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Baixar",
                tint = Color(0xFF3B82F6)
            )
        }
    }
}

@Composable
fun CommentsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "12 comentários",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = { /* Sort */ }) {
                    Text("Mais recentes")
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        }

        items(5) { index ->
            CommentCard(
                name = "Estudante ${index + 1}",
                comment = "Excelente aula! Consegui entender muito melhor o conceito.",
                timeAgo = "${index + 1}h atrás",
                likes = (10 - index) * 2
            )
        }
    }
}

@Composable
fun CommentCard(
    name: String,
    comment: String,
    timeAgo: String,
    likes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = timeAgo,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = comment,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { /* Like */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Gostar",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = likes.toString(),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = "Responder",
                    fontSize = 12.sp,
                    color = Color(0xFF3B82F6),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* Reply */ }
                )
            }
        }
    }
}

// ==================== DATA CLASSES ====================
/*
data class VideoLesson(
    val id: Long,
    val title: String,
    val subject: String,
    val subjectColor: Color,
    val grade: String,
    val teacher: String,
    val duration: String,
    val videoUrl: String, // URL do vídeo
    val audioUrl: String?, // URL do áudio (opcional)
    val views: Int,
    val progress: Int,
    val hasAudio: Boolean,
    val isDownloaded: Boolean,
    val isFavorite: Boolean
)*/

data class Material(
    val name: String,
    val type: String,
    val size: String
)

private val materials = listOf(
    Material("Resumo da Aula.pdf", "PDF", "2.5 MB"),
    Material("Exercícios Práticos.pdf", "PDF", "1.8 MB"),
    Material("Slides da Apresentação.pptx", "PPT", "5.2 MB")
)

 */