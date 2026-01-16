package com.tshikasi.tshikasi_auto_school.presentation.pages

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.DisciplinasPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.VideoLesson
import com.tshikasi.tshikasi_auto_school.presentation.pages.exercise.ExercisePage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomePage(
    onLogout: () -> Unit,
    onSubjectClick: (Subject) -> Unit,
    onNavigateToVideoLesson: (VideoLesson) -> Unit,
    onNavigateToLives: () -> Unit  // ✅ NOVO PARÂMETRO
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showMenu by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }



    // Dados de exemplo
    val userName = "Maria Santos"
    val userEmail = "maria.santos@email.com"
    val userGrade = "10ª Classe"
    val userPoints = 1250
    val userLevel = 5

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }
/*
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = userName,
                userEmail = userEmail,
                userGrade = userGrade,
                userLevel = userLevel,
                userPoints = userPoints,
                onItemClick = { item ->
                    scope.launch { drawerState.close() }
                    when (item.title) {
                        "Início" -> selectedTab = 0
                        "Disciplinas" -> selectedTab = 1
                        "Exercícios" -> selectedTab = 2
                        "Ranking" -> selectedTab = 3
                    }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "TSHIKASI",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Auto School",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                ) {
                                    Text("3")
                                }
                            }
                        ) {
                            IconButton(onClick = { /* Abrir notificações */ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificações",
                                    tint = Color.White
                                )
                            }
                        }

                        Box {
                            IconButton(onClick = { showMenu = !showMenu }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Meu Perfil") },
                                    onClick = { /* Navegar para perfil */ },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, null)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Configurações") },
                                    onClick = { /* Navegar para configurações */ },
                                    leadingIcon = {
                                        Icon(Icons.Default.Settings, null)
                                    }
                                )
                                Divider()
                                DropdownMenuItem(
                                    text = { Text("Sair") },
                                    onClick = {
                                        showMenu = false
                                        onLogout()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Logout,
                                            null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF3B82F6),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, "Início") },
                        label = { Text("Início") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.School, "Disciplinas") },
                        label = { Text("Disciplinas") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Assignment, "Exercícios") },
                        label = { Text("Exercícios") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.EmojiEvents, "Ranking") },
                        label = { Text("Ranking") }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> InicioContent(
                        userName = userName,
                        userGrade = userGrade,
                        userPoints = userPoints,
                        userLevel = userLevel,
                        onSubjectClick = onSubjectClick,
                        onVideoClick = onNavigateToVideoLesson
                    )
                    1 -> DisciplinasPage(onSubjectClick = {onSubjectClick(it)})
                    2 -> ExercisePage()
                    3 -> RankingPage()
                }
            }
        }
    }
    */
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = userName,
                userEmail = userEmail,
                userGrade = userGrade,
                userLevel = userLevel,
                userPoints = userPoints,
                onItemClick = { item ->
                    scope.launch { drawerState.close() }
                    when (item.title) {
                        "Início" -> selectedTab = 0
                        "Disciplinas" -> selectedTab = 1
                        "Exercícios" -> selectedTab = 2
                        "Ranking" -> selectedTab = 3
                        "Lives" -> onNavigateToLives()  // ✅ ADICIONAR AQUI
                    }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                },
                onClose = {  // ✅ NOVO
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "TSHIKASI",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Auto School",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error
                                ) {
                                    Text("3")
                                }
                            }
                        ) {
                            IconButton(onClick = { /* Abrir notificações */ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificações",
                                    tint = Color.White
                                )
                            }
                        }

                        Box {
                            IconButton(onClick = { showMenu = !showMenu }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Meu Perfil") },
                                    onClick = { /* Navegar para perfil */ },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, null)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Configurações") },
                                    onClick = { /* Navegar para configurações */ },
                                    leadingIcon = {
                                        Icon(Icons.Default.Settings, null)
                                    }
                                )
                                Divider()
                                DropdownMenuItem(
                                    text = { Text("Sair") },
                                    onClick = {
                                        showMenu = false
                                        onLogout()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Logout,
                                            null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF3B82F6),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, "Início") },
                        label = { Text("Início") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.School, "Disciplinas") },
                        label = { Text("Disciplinas") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.Assignment, "Exercícios") },
                        label = { Text("Exercícios") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.EmojiEvents, "Ranking") },
                        label = { Text("Ranking") }
                    )
                    // ✅ NOVA TAB - LIVES
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = {
                            selectedTab = 4
                            onNavigateToLives()
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    // Badge vermelho se houver lives ao vivo
                                    Badge(
                                        containerColor = Color(0xFFEF4444)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Circle,
                                            contentDescription = null,
                                            modifier = Modifier.size(8.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            ) {
                                Icon(Icons.Default.LiveTv, "Lives")
                            }
                        },
                        label = { Text("Lives") }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> InicioContent(
                        userName = userName,
                        userGrade = userGrade,
                        userPoints = userPoints,
                        userLevel = userLevel,
                        onSubjectClick = onSubjectClick,
                        onVideoClick = onNavigateToVideoLesson
                    )
                    1 -> DisciplinasPage(onSubjectClick = {onSubjectClick(it)})
                    2 -> ExercisePage()
                    3 -> RankingPage()
                    4 -> {
                        // Quando clicar na tab Lives, navega para LiveStreamListPage
                        LaunchedEffect(Unit) {
                            onNavigateToLives()
                            selectedTab = 0 // Volta para Início
                        }
                    }
                }
            }
        }
    }
}

// ==================== CONTEÚDO DA TAB INÍCIO ====================

@Composable
fun InicioContent(
    userName: String,
    userGrade: String,
    userPoints: Int,
    userLevel: Int,
    onSubjectClick: (Subject) -> Unit,
    onVideoClick: (VideoLesson) -> Unit
) {
    val todayProgress = 75
    val streak = 7
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header do Usuário
            item {
                UserHeaderCard(
                    userName = userName,
                    userGrade = userGrade,
                    userPoints = userPoints,
                    userLevel = userLevel
                )
            }

            // Progresso do Dia
            item {
                DailyProgressCard(
                    progress = todayProgress,
                    streak = streak
                )
            }

            // Atalhos Rápidos
            item {
                Text(
                    text = "Atalhos Rápidos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        title = "Continuar",
                        subtitle = "Última aula",
                        icon = Icons.Default.PlayCircle,
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            recentVideoLessons.firstOrNull { it.progress > 0 }?.let {
                                onVideoClick(it)
                            }
                        }
                    )

                    QuickActionCard(
                        title = "Downloads",
                        subtitle = "Offline",
                        icon = Icons.Default.CloudDownload,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        onClick = { /* Ver downloads */ }
                    )
                }
            }

            // Disciplinas
            item {
                Text(
                    text = "Minhas Disciplinas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(subjects) { subject ->
                        SubjectCard(
                            subject = subject,
                            onClick = { onSubjectClick(subject) }
                        )
                    }
                }
            }

            // Aulas Recentes
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Continuar a Assistir",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { /* Ver todas */ }) {
                        Text("Ver todas")
                    }
                }
            }

            items(recentVideoLessons) { videoLesson ->
                VideoLessonCard(
                    lesson = videoLesson,
                    onClick = { onVideoClick(videoLesson) }
                )
            }

            // Conquistas Recentes
            item {
                Text(
                    text = "Conquistas Recentes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(achievements) { achievement ->
                        AchievementBadge(achievement)
                    }
                }
            }
        }
    }
}

// ==================== RANKING PAGE ====================

@Composable
fun RankingPage() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3B82F6)
                )
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
                        Text(
                            text = "#15",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Column {
                            Text(
                                text = "Você",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "1250 pontos",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Top 10",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(10) { index ->
            RankingCard(
                position = index + 1,
                name = "Estudante ${index + 1}",
                points = 2000 - (index * 150),
                isTop3 = index < 3
            )
        }
    }
}

@Composable
fun RankingCard(
    position: Int,
    name: String,
    points: Int,
    isTop3: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTop3) Color(0xFFFFF8E1) else Color.White
        )
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (position) {
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> Color.LightGray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$position",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (position <= 3) Color.White else Color.DarkGray
                    )
                }

                Column {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$points pontos",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            if (isTop3) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ==================== DRAWER CONTENT ====================
/*
@Composable
fun DrawerContent(
    userName: String,
    userEmail: String,
    userGrade: String,
    userLevel: Int,
    userPoints: Int,
    onItemClick: (DrawerItem) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF60A5FA)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.first().toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = userEmail,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = userGrade,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Nível $userLevel",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Text(
                        text = "$userPoints pontos totais",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(drawerMenuItems) { item ->
                    DrawerMenuItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }

            Divider()

            DrawerMenuItem(
                item = DrawerItem(
                    icon = Icons.Default.Logout,
                    title = "Sair",
                    iconTint = MaterialTheme.colorScheme.error
                ),
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
*/
// ✅ SUBSTITUIR A FUNÇÃO DrawerContent COMPLETA
/*
@Composable
fun DrawerContent(
    userName: String,
    userEmail: String,
    userGrade: String,
    userLevel: Int,
    userPoints: Int,
    onItemClick: (DrawerItem) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ✅ HEADER COM BOTÃO FECHAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF60A5FA)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ✅ BOTÃO FECHAR NO CANTO SUPERIOR DIREITO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                // Precisa passar o scope aqui - ver solução abaixo
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar menu",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.first().toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = userEmail,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = userGrade,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Nível $userLevel",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Text(
                        text = "$userPoints pontos totais",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(drawerMenuItems) { item ->
                    DrawerMenuItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }

            Divider()

            DrawerMenuItem(
                item = DrawerItem(
                    icon = Icons.Default.Logout,
                    title = "Sair",
                    iconTint = MaterialTheme.colorScheme.error
                ),
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}*/
@Composable
fun DrawerContent(
    userName: String,
    userEmail: String,
    userGrade: String,
    userLevel: Int,
    userPoints: Int,
    onItemClick: (DrawerItem) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit  // ✅ NOVO
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF60A5FA)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ✅ BOTÃO FECHAR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar menu",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.first().toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = userEmail,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = userGrade,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Nível $userLevel",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Text(
                        text = "$userPoints pontos totais",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(drawerMenuItems) { item ->
                    DrawerMenuItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }

            Divider()

            DrawerMenuItem(
                item = DrawerItem(
                    icon = Icons.Default.Logout,
                    title = "Sair",
                    iconTint = MaterialTheme.colorScheme.error
                ),
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DrawerMenuItem(
    item: DrawerItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = item.iconTint ?: Color.DarkGray,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = item.title,
            fontSize = 16.sp,
            color = item.iconTint ?: Color.DarkGray
        )

        if (item.badge != null) {
            Spacer(modifier = Modifier.weight(1f))
            Badge(
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Text(item.badge)
            }
        }
    }
}

// ==================== COMPONENTES UI ====================

@Composable
fun UserHeaderCard(
    userName: String,
    userGrade: String,
    userPoints: Int,
    userLevel: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3B82F6)
        )
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
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.first().toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = userGrade,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Nível $userLevel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = "$userPoints pontos",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun DailyProgressCard(
    progress: Int,
    streak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progresso de Hoje",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$progress%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
            }

            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF10B981),
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "$streak dias seguidos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "Meta: 2h/dia",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun SubjectCard(
    subject:com.tshikasi.tshikasi_auto_school.presentation.pages.Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(subject.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subject.icon,
                    fontSize = 24.sp
                )
            }

            Text(
                text = subject.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${subject.lessonsCount} aulas",
                fontSize = 12.sp,
                color = Color.Gray
            )

            LinearProgressIndicator(
                progress = subject.progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = subject.color
            )
        }
    }
}

@Composable
fun VideoLessonCard(
    lesson: VideoLesson,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = lesson.duration,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (lesson.progress > 0) {
                    LinearProgressIndicator(
                        progress = lesson.progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .align(Alignment.BottomCenter),
                        color = Color(0xFF3B82F6),
                        trackColor = Color.Transparent
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = lesson.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = lesson.subjectColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = lesson.subject,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        color = lesson.subjectColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = lesson.teacher,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    if (lesson.progress > 0) {
                        Text(
                            text = "${lesson.progress}% concluído",
                            fontSize = 12.sp,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
    Card(
        modifier = Modifier.width(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = achievement.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = achievement.icon,
                fontSize = 32.sp
            )
            Text(
                text = achievement.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==================== DATA CLASSES ====================

data class DrawerItem(
    val icon: ImageVector,
    val title: String,
    val badge: String? = null,
    val iconTint: Color? = null
)

data class Subject(
    val name: String,
    val icon: String,
    val lessonsCount: Int,
    val progress: Int,
    val color: Color
)

data class Achievement(
    val name: String,
    val icon: String,
    val color: Color
)

// ==================== DADOS DE EXEMPLO ====================
/*
private val drawerMenuItems = listOf(
    DrawerItem(Icons.Default.Home, "Início"),
    DrawerItem(Icons.Default.Person, "Meu Perfil"),
    DrawerItem(Icons.Default.School, "Disciplinas"),
    DrawerItem(Icons.Default.Assignment, "Exercícios"),
    DrawerItem(Icons.Default.EmojiEvents, "Conquistas"),
    DrawerItem(Icons.Default.Leaderboard, "Ranking"),
    DrawerItem(Icons.Default.CloudDownload, "Downloads"),
    DrawerItem(Icons.Default.Notifications, "Notificações", badge = "3"),
    DrawerItem(Icons.Default.Settings, "Configurações"),
    DrawerItem(Icons.Default.Help, "Ajuda & Suporte"),
    DrawerItem(Icons.Default.Info, "Sobre")
)*/

private val subjects = listOf(
    Subject("Matemática", "📐", 45, 65, Color(0xFFEF4444)),
    Subject("Português", "📚", 38, 45, Color(0xFF3B82F6)),
    Subject("Física", "⚛️", 32, 30, Color(0xFF10B981)),
    Subject("Química", "🧪", 28, 55, Color(0xFF8B5CF6)),
    Subject("Biologia", "🧬", 35, 40, Color(0xFF10B981))
)

private val recentVideoLessons = listOf(
    VideoLesson(
        id = 1,
        title = "Equações do 2º Grau - Fórmula de Bhaskara",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "25:30",
        videoUrl = "https://yucjmzzontuplfsyipro.supabase.co/storage/v1/object/public/videos/chat/video/EQUACAO%20DO%202%20GRAU.mp4",
        views = 1250,
        progress = 45,
        hasAudio = true,
        isDownloaded = true,
        isFavorite = true,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    ),
    VideoLesson(
        id = 2,
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
        id = 3,
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
    ),
    VideoLesson(
        id = 4,
        title = "Funções Quadráticas - Gráficos e Raízes",
        subject = "Matemática",
        subjectColor = Color(0xFFEF4444),
        grade = "10ª Classe",
        teacher = "Prof. João Silva",
        duration = "30:15",
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        views = 980,
        progress = 0,
        hasAudio = true,
        isDownloaded = false,
        isFavorite = false,
        audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
    )
)

private val achievements = listOf(
    Achievement("Primeira Semana", "🔥", Color(0xFFEF4444)),
    Achievement("10 Aulas", "📚", Color(0xFF3B82F6)),
    Achievement("100 Pontos", "⭐", Color(0xFFFBBF24)),
    Achievement("Mestre", "🏆", Color(0xFF10B981))
)

private val drawerMenuItems = listOf(
    DrawerItem(Icons.Default.Home, "Início"),
    DrawerItem(Icons.Default.Person, "Meu Perfil"),
    DrawerItem(Icons.Default.School, "Disciplinas"),
    DrawerItem(Icons.Default.Assignment, "Exercícios"),
    DrawerItem(Icons.Default.LiveTv, "Lives"),  // ✅ NOVO
    DrawerItem(Icons.Default.EmojiEvents, "Conquistas"),
    DrawerItem(Icons.Default.Leaderboard, "Ranking"),
    DrawerItem(Icons.Default.CloudDownload, "Downloads"),
    DrawerItem(Icons.Default.Notifications, "Notificações", badge = "3"),
    DrawerItem(Icons.Default.Settings, "Configurações"),
    DrawerItem(Icons.Default.Help, "Ajuda & Suporte"),
    DrawerItem(Icons.Default.Info, "Sobre")
)
