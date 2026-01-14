package com.tshikasi.tshikasi_auto_school.presentation.pages.classroom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.presentation.pages.Subject
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.VideoLesson
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailPage(
    subject: Subject,
    onBackClick: () -> Unit,
    onVideoClick: (VideoLesson) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val filters = listOf("Todas", "Em Progresso", "Não Iniciadas", "Concluídas")

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = subject.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${subject.lessonsCount} aulas • ${subject.progress}% concluído",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = subject.color,
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
                .background(Color(0xFFF5F5F5))
        ) {
            // Header Card com Progresso
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = subject.color
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = subject.icon,
                                    fontSize = 32.sp
                                )
                            }

                            Column {
                                Text(
                                    text = "${subject.lessonsCount} aulas",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${(subject.lessonsCount * subject.progress / 100)} concluídas",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "${subject.progress}%",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Progresso",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = subject.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Procurar aulas...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // Filters
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters.size) { index ->
                    FilterChip(
                        selected = selectedFilter == filters[index],
                        onClick = { selectedFilter = filters[index] },
                        label = { Text(filters[index]) },
                        leadingIcon = if (selectedFilter == filters[index]) {
                            { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }

            // Lista de Aulas
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getSubjectLessons(subject.name)) { lesson ->
                        SubjectVideoLessonCard(
                            lesson = lesson,
                            subjectColor = subject.color,
                            onClick = { onVideoClick(lesson) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectVideoLessonCard(
    lesson: VideoLesson,
    subjectColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray)
            ) {
                // Play Button
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = subjectColor,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Duration Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = lesson.duration,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Progress Bar
                if (lesson.progress > 0) {
                    LinearProgressIndicator(
                        progress = lesson.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .align(Alignment.BottomCenter),
                        color = subjectColor,
                        trackColor = Color.Transparent
                    )
                }
            }

            // Content
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Grade Badge
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

                // Teacher and Views
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = "${lesson.views} views",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Audio Button
                        if (lesson.hasAudio) {
                            OutlinedButton(
                                onClick = { /* Play audio */ },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headset,
                                    contentDescription = "Áudio",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Áudio", fontSize = 14.sp)
                            }
                        }

                        // Download Button
                        OutlinedButton(
                            onClick = { /* Download */ },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = if (lesson.isDownloaded)
                                    Icons.Default.CheckCircle
                                else
                                    Icons.Default.Download,
                                contentDescription = "Download",
                                modifier = Modifier.size(18.dp),
                                tint = if (lesson.isDownloaded) Color(0xFF10B981) else Color.Gray
                            )
                        }
                    }

                    // Favorite Button
                    IconButton(onClick = { /* Toggle favorite */ }) {
                        Icon(
                            imageVector = if (lesson.isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (lesson.isFavorite) Color(0xFFEF4444) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// ==================== DADOS DE EXEMPLO ====================

fun getSubjectLessons(subjectName: String): List<VideoLesson> {
    return when (subjectName) {
        "Matemática" -> mathLessons
        "Português" -> portugueseLessons
        "Física" -> physicLessons
        "Química" -> chemistryLessons
        "Biologia" -> biologyLessons
        else -> emptyList()
    }
}

private val mathLessons = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "25:30",
        videoUrl = "https://yucjmzzontuplfsyipro.supabase.co/storage/v1/object/public/videos/chat/video/EQUACAO%20DO%202%20GRAU.mp4",//"https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 1250,
        progress = 45,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    ),
    VideoLesson(
        id = 2,
        title = "Funções Quadráticas - Gráficos e Raízes",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "30:15",
        videoUrl = "https://yucjmzzontuplfsyipro.supabase.co/storage/v1/object/public/videos/chat/video/funcao_exponencial_10classe.mp4",
        views = 980,
        progress = 0,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    ),
    VideoLesson(
        id = 3,
        title = "Geometria Analítica - Distância entre Pontos",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "28:45",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 1500,
        progress = 100,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    ),
    VideoLesson(
        id = 4,
        title = "Trigonometria - Seno, Cosseno e Tangente",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "35:20",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 2200,
        progress = 20,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)

private val portugueseLessons = listOf(
    VideoLesson(
        id = 5,
        title = "Classes Gramaticais - Substantivos e Adjetivos",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        teacher = "Profa. Maria Santos",
        duration = "18:45",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 890,
        progress = 70,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    ),
    VideoLesson(
        id = 6,
        title = "Análise Sintática - Sujeito e Predicado",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        teacher = "Profa. Maria Santos",
        duration = "22:30",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 1100,
        progress = 0,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)

private val physicLessons = listOf(
    VideoLesson(
        id = 7,
        title = "Leis de Newton - Princípios da Dinâmica",
        subject = "Física",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        teacher = "Prof. Carlos Mendes",
        duration = "22:15",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 2100,
        progress = 0,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)

private val chemistryLessons = listOf(
    VideoLesson(
        id = 8,
        title = "Tabela Periódica - Grupos e Períodos",
        subject = "Química",
        subjectColor = Color(0xFF8B5CF6),
        grade = "10ª Classe",
        teacher = "Profa. Ana Costa",
        duration = "30:00",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 1580,
        progress = 0,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)

private val biologyLessons = listOf(
    VideoLesson(
        id = 9,
        title = "Citologia - Estrutura da Célula",
        subject = "Biologia",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        teacher = "Prof. Pedro Lima",
        duration = "27:45",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 950,
        progress = 15,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)