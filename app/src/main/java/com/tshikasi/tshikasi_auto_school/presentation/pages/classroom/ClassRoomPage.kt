package com.tshikasi.tshikasi_auto_school.presentation.pages.classroom
import androidx.compose.animation.*
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoomPage(
    onNavigateToVideoLesson: (VideoLesson) -> Unit // ✅ Recebe o callback
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val filters = listOf("Todas", "Em Progresso", "Novas", "Favoritas")

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Search Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                .padding(horizontal = 16.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Aulas
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(videoLessons) { lesson ->
                    VideoLessonCard(
                        lesson = lesson,
                        onClick = { onNavigateToVideoLesson(lesson) } // ✅ Chama callback ao clicar
                    )
                }
            }
        }
    }
}

@Composable
fun VideoLessonCard(
    lesson: VideoLesson,
    onClick: () -> Unit // ✅ Recebe callback
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // ✅ Chama ao clicar
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
                        tint = Color(0xFF3B82F6),
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
                        color = Color(0xFF3B82F6),
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
                Text(
                    text = lesson.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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

// ==================== DATA CLASS ====================

data class VideoLesson1(
    val id: Long,
    val title: String,
    val subject: String,
    val subjectColor: Color,
    val grade: String,
    val teacher: String,
    val duration: String,
    val videoUrl: String = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", // ✅ URL de exemplo
    val views: Int,
    val progress: Int,
    val hasAudio: Boolean,
    val isDownloaded: Boolean,
    val isFavorite: Boolean
)

// ==================== DADOS DE EXEMPLO ====================

/*private val videoLessons = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "25:30",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 1250,
        progress = 45,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3" // ✅ URL de áudio de exemplo
    ),

)*/
private val videoLessons1 = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444), // Vermelho
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "18:45",
        videoUrl = "https://www.youtube.com/embed/qD7lq_1CJ_c", // Vídeo real sobre Bhaskara
        views = 3250,
        progress = 60,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    ),
    VideoLesson(
        id = 2,
        title = "Funções Exponenciais - Crescimento e Decaimento",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. Maria Santos",
        duration = "22:10",
        videoUrl = "https://www.youtube.com/watch?v=6vLw4Tk65XI",
        views = 2100,
        progress = 30,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = ""
    ),
    VideoLesson(
        id = 3,
        title = "Sistema Respiratório Humano",
        subject = "Biologia",
        subjectColor = Color(0xFF10B981), // Verde
        grade = "10ª Classe",
        teacher = "Prof. Carlos Mendes",
        duration = "27:20",
        videoUrl = "https://www.youtube.com/watch?v=9Pk7bH8OZ9w",
        views = 4100,
        progress = 75,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
    ),
    VideoLesson(
        id = 4,
        title = "Estrutura Atômica - Modelos Atômicos",
        subject = "Química",
        subjectColor = Color(0xFF8B5CF6), // Roxo
        grade = "10ª Classe",
        teacher = "Prof. Ana Oliveira",
        duration = "31:15",
        videoUrl = "https://www.youtube.com/watch?v=s-V5VKhXSWQ",
        views = 2850,
        progress = 90,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
    ),
    VideoLesson(
        id = 5,
        title = "Renascimento Cultural na Europa",
        subject = "História",
        subjectColor = Color(0xFFF59E0B), // Laranja
        grade = "10ª Classe",
        teacher = "Prof. Pedro Costa",
        duration = "35:05",
        videoUrl = "https://www.youtube.com/watch?v=UcLpR5M2eUw",
        views = 1900,
        progress = 10,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = ""
    ),
    VideoLesson(
        id = 6,
        title = "Movimentos Literários - Romantismo",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6), // Azul
        grade = "10ª Classe",
        teacher = "Prof. Sofia Almeida",
        duration = "24:50",
        videoUrl = "https://www.youtube.com/watch?v=2QnZ6p5qL_s",
        views = 3200,
        progress = 50,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
    ),
    VideoLesson(
        id = 7,
        title = "Física - Leis de Newton",
        subject = "Física",
        subjectColor = Color(0xFFEC4899), // Rosa
        grade = "10ª Classe",
        teacher = "Prof. Ricardo Fernandes",
        duration = "29:40",
        videoUrl = "https://www.youtube.com/watch?v=5oi5j11FkQg",
        views = 5100,
        progress = 100,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
    ),
    VideoLesson(
        id = 8,
        title = "Geografia - Clima e Vegetação",
        subject = "Geografia",
        subjectColor = Color(0xFF059669), // Verde escuro
        grade = "10ª Classe",
        teacher = "Prof. Luísa Ramos",
        duration = "26:25",
        videoUrl = "https://www.youtube.com/watch?v=4WtcL-pH4KI",
        views = 1800,
        progress = 0,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = ""
    ),
    VideoLesson(
        id = 9,
        title = "Inglês - Present Perfect Tense",
        subject = "Inglês",
        subjectColor = Color(0xFFDC2626), // Vermelho escuro
        grade = "10ª Classe",
        teacher = "Teacher Michael",
        duration = "20:15",
        videoUrl = "https://www.youtube.com/watch?v=4WtcL-pH4KI",
        views = 2900,
        progress = 80,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"
    ),
    VideoLesson(
        id = 10,
        title = "Geometria Analítica - Distância entre Pontos",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "19:30",
        videoUrl = "https://www.youtube.com/watch?v=YAuR4b4Nv-k",
        views = 2350,
        progress = 40,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3"
    )
)
private val videoLessons = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "18:45",
        videoUrl = "https://yucjmzzontuplfsyipro.supabase.co/storage/v1/object/public/videos/chat/video/EQUACAO%20DO%202%20GRAU.mp4",//"https://www.youtube.com/embed/qD7lq_1CJ_c", // Vídeo educativo real
        views = 3250,
        progress = 60,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    ),
    VideoLesson(
        id = 2,
        title = "Funções Exponenciais - Aula Completa",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. Maria Santos",
        duration = "22:10",
        videoUrl = "https://yucjmzzontuplfsyipro.supabase.co/storage/v1/object/public/videos/chat/video/funcao_exponencial_10classe.mp4",
        views = 2100,
        progress = 30,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = ""
    ),
    VideoLesson(
        id = 3,
        title = "Sistema Respiratório - Biologia 10ª Classe",
        subject = "Biologia",
        subjectColor = Color(0xFF10B981),
        grade = "10ª Classe",
        teacher = "Prof. Carlos Mendes",
        duration = "27:20",
        videoUrl = "https://www.youtube.com/embed/9Pk7bH8OZ9w",
        views = 4100,
        progress = 75,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
    ),
    VideoLesson(
        id = 4,
        title = "Estrutura Atômica - Modelo de Bohr",
        subject = "Química",
        subjectColor = Color(0xFF8B5CF6),
        grade = "10ª Classe",
        teacher = "Prof. Ana Oliveira",
        duration = "31:15",
        videoUrl = "https://www.youtube.com/embed/s-V5VKhXSWQ",
        views = 2850,
        progress = 90,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
    ),
    VideoLesson(
        id = 5,
        title = "Renascimento - História 10ª Classe",
        subject = "História",
        subjectColor = Color(0xFFF59E0B),
        grade = "10ª Classe",
        teacher = "Prof. Pedro Costa",
        duration = "35:05",
        videoUrl = "https://www.youtube.com/embed/UcLpR5M2eUw",
        views = 1900,
        progress = 10,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = ""
    ),
    VideoLesson(
        id = 6,
        title = "Romantismo em Portugal",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        teacher = "Prof. Sofia Almeida",
        duration = "24:50",
        videoUrl = "https://www.youtube.com/embed/2QnZ6p5qL_s",
        views = 3200,
        progress = 50,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
    ),
    VideoLesson(
        id = 7,
        title = "Leis de Newton - Física",
        subject = "Física",
        subjectColor = Color(0xFFEC4899),
        grade = "10ª Classe",
        teacher = "Prof. Ricardo Fernandes",
        duration = "29:40",
        videoUrl = "https://www.youtube.com/embed/5oi5j11FkQg",
        views = 5100,
        progress = 100,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
    ),
    VideoLesson(
        id = 8,
        title = "Climas do Mundo - Geografia",
        subject = "Geografia",
        subjectColor = Color(0xFF059669),
        grade = "10ª Classe",
        teacher = "Prof. Luísa Ramos",
        duration = "26:25",
        videoUrl = "https://www.youtube.com/embed/4WtcL-pH4KI",
        views = 1800,
        progress = 0,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = ""
    ),
    VideoLesson(
        id = 9,
        title = "Present Perfect - Inglês",
        subject = "Inglês",
        subjectColor = Color(0xFFDC2626),
        grade = "10ª Classe",
        teacher = "Teacher Michael",
        duration = "20:15",
        videoUrl = "https://www.youtube.com/embed/4WtcL-pH4KI",
        views = 2900,
        progress = 80,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = false,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3"
    ),
    VideoLesson(
        id = 10,
        title = "Geometria Analítica - Matemática",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "19:30",
        videoUrl = "https://www.youtube.com/embed/YAuR4b4Nv-k",
        views = 2350,
        progress = 40,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = true,
        audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3"
    )
)
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassRoomPage() {
    var selectedFilter by remember { mutableStateOf("Todas") }
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val filters = listOf("Todas", "Em Progresso", "Novas", "Favoritas")

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Search Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                .padding(horizontal = 16.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Aulas
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(videoLessons) { lesson ->
                    VideoLessonCard(lesson = lesson)
                }
            }
        }
    }
}

@Composable
fun VideoLessonCard(lesson: VideoLesson) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Abrir player */ },
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
                // Aqui vai o thumbnail do vídeo

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
                        tint = Color(0xFF3B82F6),
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
                        color = Color(0xFF3B82F6),
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

                // Subject and Grade
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
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

// ==================== DATA CLASS ====================

data class VideoLesson(
    val id: Long,
    val title: String,
    val subject: String,
    val subjectColor: Color,
    val grade: String,
    val teacher: String,
    val duration: String,
    val views: Int,
    val progress: Int,
    val hasAudio: Boolean,
    val isDownloaded: Boolean,
    val isFavorite: Boolean
)

// ==================== DADOS DE EXEMPLO ====================

private val videoLessons = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "25:30",
        views = 1250,
        progress = 45,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true
    ),
    VideoLesson(
        id = 2,
        title = "Classes Gramaticais - Substantivos e Adjetivos",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        teacher = "Profa. Maria Santos",
        duration = "18:45",
        views = 890,
        progress = 70,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false
    ),
    VideoLesson(
        id = 3,
        title = "Leis de Newton - Princípios da Dinâmica",
        subject = "Física",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        teacher = "Prof. Carlos Mendes",
        duration = "22:15",
        views = 2100,
        progress = 0,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false
    ),
    VideoLesson(
        id = 4,
        title = "Tabela Periódica - Grupos e Períodos",
        subject = "Química",
        subjectColor = Color(0xFF8B5CF6),
        grade = "10ª Classe",
        teacher = "Profa. Ana Costa",
        duration = "30:00",
        views = 1580,
        progress = 0,
        hasAudio = false,
        isDownloaded = false,
        isFavorite = true
    ),
    VideoLesson(
        id = 5,
        title = "Citologia - Estrutura da Célula",
        subject = "Biologia",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        teacher = "Prof. Pedro Lima",
        duration = "27:45",
        views = 950,
        progress = 15,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false
    )
)

 */