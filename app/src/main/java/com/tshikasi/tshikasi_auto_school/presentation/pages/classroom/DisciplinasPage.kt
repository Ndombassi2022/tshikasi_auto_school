package com.tshikasi.tshikasi_auto_school.presentation.pages.classroom
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.presentation.pages.Subject
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplinasPage(
    onSubjectClick: (Subject) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
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
                placeholder = { Text("Procurar disciplinas...") },
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

        // Header Info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${subjects.size} Disciplinas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6)
                    )
                    Text(
                        text = "Classe 10ª",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Grid de Disciplinas
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(subjects.filter {
                    searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true)
                }) { subject ->
                    SubjectGridCard(
                        subject = subject,
                        onClick = { onSubjectClick(subject) }
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectGridCard(
    subject: Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon e Badge
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(subject.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subject.icon,
                        fontSize = 36.sp
                    )
                }

                Text(
                    text = subject.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            // Info e Progress
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${subject.lessonsCount} aulas",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${subject.progress}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = subject.color
                    )
                }

                LinearProgressIndicator(
                    progress = subject.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = subject.color,
                    trackColor = subject.color.copy(alpha = 0.2f)
                )
            }
        }
    }
}


// ==================== DADOS DE EXEMPLO ====================

private val subjects = listOf(
    Subject("Matemática", "📐", 45, 65, Color(0xFFEF4444)),
    Subject("Português", "📚", 38, 45, Color(0xFF3B82F6)),
    Subject("Física", "⚛️", 32, 30, Color(0xFF10B981)),
    Subject("Química", "🧪", 28, 55, Color(0xFF8B5CF6)),
    Subject("Biologia", "🧬", 35, 40, Color(0xFF10B981)),
    Subject("História", "📜", 30, 25, Color(0xFFF59E0B)),
    Subject("Geografia", "🌍", 25, 35, Color(0xFF14B8A6)),
    Subject("Inglês", "🇬🇧", 40, 50, Color(0xFF6366F1))
)