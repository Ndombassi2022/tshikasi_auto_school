package com.tshikasi.tshikasi_auto_school.presentation.pages.exercise


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun ExercisePage() {
    var selectedTab by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Disponíveis") },
                icon = { Icon(Icons.Default.Assignment, null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Em Progresso") },
                icon = { Icon(Icons.Default.Edit, null) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Concluídos") },
                icon = { Icon(Icons.Default.CheckCircle, null) }
            )
        }

        // Lista de Exercícios baseado na tab
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        // Exercícios Disponíveis
                        items(availableExercises) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }
                    1 -> {
                        // Exercícios Em Progresso
                        items(inProgressExercises) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }
                    2 -> {
                        // Exercícios Concluídos
                        items(completedExercises) { exercise ->
                            ExerciseCard(exercise = exercise)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Abrir exercício */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header com Subject Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title
                    Text(
                        text = exercise.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Subject and Grade
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = exercise.subjectColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = exercise.subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = exercise.subjectColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Gray.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = exercise.grade,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Difficulty Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (exercise.difficulty) {
                                "Fácil" -> Color(0xFF10B981).copy(alpha = 0.1f)
                                "Médio" -> Color(0xFFF59E0B).copy(alpha = 0.1f)
                                "Difícil" -> Color(0xFFEF4444).copy(alpha = 0.1f)
                                else -> Color.Gray.copy(alpha = 0.1f)
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = exercise.difficulty,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (exercise.difficulty) {
                            "Fácil" -> Color(0xFF10B981)
                            "Médio" -> Color(0xFFF59E0B)
                            "Difícil" -> Color(0xFFEF4444)
                            else -> Color.Gray
                        }
                    )
                }
            }

            // Exercise Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Questions Count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QuestionAnswer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${exercise.questionsCount} questões",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Time Limit
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${exercise.timeLimit} min",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Points
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFBBF24)
                    )
                    Text(
                        text = "${exercise.points} pts",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Progress or Score
            when (exercise.status) {
                ExerciseStatus.AVAILABLE -> {
                    // Deadline
                    if (exercise.deadline != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFEF4444)
                            )
                            Text(
                                text = "Prazo: ${exercise.deadline}",
                                fontSize = 14.sp,
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Button(
                        onClick = { /* Iniciar exercício */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar Exercício")
                    }
                }

                ExerciseStatus.IN_PROGRESS -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progresso",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${exercise.progress}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3B82F6)
                            )
                        }

                        LinearProgressIndicator(
                            progress = (exercise.progress ?: 0) / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF3B82F6)
                        )

                        Button(
                            onClick = { /* Continuar exercício */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Continuar")
                        }
                    }
                }

                ExerciseStatus.COMPLETED -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Score Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if ((exercise.score ?: 0) >= 50)
                                    Color(0xFF10B981).copy(alpha = 0.1f)
                                else
                                    Color(0xFFEF4444).copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if ((exercise.score ?: 0) >= 50)
                                            Icons.Default.CheckCircle
                                        else
                                            Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if ((exercise.score ?: 0) >= 50)
                                            Color(0xFF10B981)
                                        else
                                            Color(0xFFEF4444),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if ((exercise.score ?: 0) >= 50) "Aprovado" else "Reprovado",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if ((exercise.score ?: 0) >= 50)
                                                Color(0xFF10B981)
                                            else
                                                Color(0xFFEF4444)
                                        )
                                        Text(
                                            text = "Nota: ${exercise.score}%",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Text(
                                    text = "+${exercise.earnedPoints} pts",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFBBF24)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { /* Ver correção */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ver Correção")
                            }

                            Button(
                                onClick = { /* Refazer */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3B82F6)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Refazer")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== DATA CLASSES ====================

enum class ExerciseStatus {
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED
}

data class Exercise(
    val id: Long,
    val title: String,
    val subject: String,
    val subjectColor: Color,
    val grade: String,
    val difficulty: String,
    val questionsCount: Int,
    val timeLimit: Int,
    val points: Int,
    val deadline: String?,
    val status: ExerciseStatus,
    val progress: Int? = null,
    val score: Int? = null,
    val earnedPoints: Int? = null
)

// ==================== DADOS DE EXEMPLO ====================

private val availableExercises = listOf(
    Exercise(
        id = 1,
        title = "Equações do 2º Grau - Exercícios",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        difficulty = "Médio",
        questionsCount = 15,
        timeLimit = 30,
        points = 100,
        deadline = "25 Jan 2026",
        status = ExerciseStatus.AVAILABLE
    ),
    Exercise(
        id = 2,
        title = "Classes Gramaticais - Teste",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        difficulty = "Fácil",
        questionsCount = 10,
        timeLimit = 20,
        points = 80,
        deadline = "28 Jan 2026",
        status = ExerciseStatus.AVAILABLE
    ),
    Exercise(
        id = 3,
        title = "Leis de Newton - Questões",
        subject = "Física",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        difficulty = "Difícil",
        questionsCount = 20,
        timeLimit = 45,
        points = 150,
        deadline = null,
        status = ExerciseStatus.AVAILABLE
    )
)

private val inProgressExercises = listOf(
    Exercise(
        id = 4,
        title = "Tabela Periódica - Exercícios",
        subject = "Química",
        subjectColor = Color(0xFF8B5CF6),
        grade = "10ª Classe",
        difficulty = "Médio",
        questionsCount = 12,
        timeLimit = 25,
        points = 90,
        deadline = "30 Jan 2026",
        status = ExerciseStatus.IN_PROGRESS,
        progress = 60
    ),
    Exercise(
        id = 5,
        title = "Citologia - Teste Rápido",
        subject = "Biologia",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        difficulty = "Fácil",
        questionsCount = 8,
        timeLimit = 15,
        points = 60,
        deadline = "27 Jan 2026",
        status = ExerciseStatus.IN_PROGRESS,
        progress = 25
    )
)

private val completedExercises = listOf(
    Exercise(
        id = 6,
        title = "Funções - Avaliação Final",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        difficulty = "Difícil",
        questionsCount = 25,
        timeLimit = 60,
        points = 200,
        deadline = null,
        status = ExerciseStatus.COMPLETED,
        score = 85,
        earnedPoints = 170
    ),
    Exercise(
        id = 7,
        title = "Verbos - Conjugação",
        subject = "Português",
        subjectColor = Color(0xFF3B82F6),
        grade = "10ª Classe",
        difficulty = "Médio",
        questionsCount = 15,
        timeLimit = 30,
        points = 100,
        deadline = null,
        status = ExerciseStatus.COMPLETED,
        score = 92,
        earnedPoints = 92
    ),
    Exercise(
        id = 8,
        title = "Cinemática - Questões",
        subject = "Física",
        subjectColor = Color(0xFF10B981),
        grade = "11ª Classe",
        difficulty = "Médio",
        questionsCount = 10,
        timeLimit = 20,
        points = 80,
        deadline = null,
        status = ExerciseStatus.COMPLETED,
        score = 45,
        earnedPoints = 36
    )
)

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePage() {
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