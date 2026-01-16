package com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStatus
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamListPage(
    onLiveClick: (LiveSessionModel) -> Unit,
    onCreateLive: () -> Unit,
    onBackClick: (() -> Unit)? = null  // ✅ Parâmetro opcional para voltar
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    // ✅ BackHandler opcional
    onBackClick?.let {
        BackHandler(onBack = it)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lives",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Aulas ao vivo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                },
                navigationIcon = {
                    // ✅ Mostrar botão voltar se onBackClick fornecido
                    onBackClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onCreateLive) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Criar Live",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
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
                    placeholder = { Text("Procurar lives...") },
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

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Agendadas")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                            )
                            Text("Ao Vivo")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Passadas")
                        }
                    }
                )
            }

            // Content
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn()
            ) {
                when (selectedTab) {
                    0 -> UpcomingLivesContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                    1 -> LiveNowContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                    2 -> PastLivesContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                }
            }
        }
    }
}

// ==================== UPCOMING LIVES ====================

@Composable
fun UpcomingLivesContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(upcomingLives.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveSessionCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== LIVE NOW ====================

@Composable
fun LiveNowContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(liveNowSessions.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveNowCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== PAST LIVES ====================

@Composable
fun PastLivesContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pastLives.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveSessionCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== LIVE SESSION CARD ====================

@Composable
fun LiveSessionCard(
    session: LiveSessionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Subject Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = session.subjectColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = session.subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = session.subjectColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Price Badge
                        if (session.price > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFBBF24).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "${session.price} Kz",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFFF59E0B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "GRÁTIS",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Status Badge
                StatusBadge(status = session.status)
            }

            // Teacher
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(session.subjectColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.teacherName.first().toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = session.subjectColor
                    )
                }

                Column {
                    Text(
                        text = session.teacherName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Professor",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Divider()

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = session.scheduledStart.take(10), // Data
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${session.durationMinutes} min",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${session.participantCount} participantes",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// ==================== LIVE NOW CARD ====================

@Composable
fun LiveNowCard(
    session: LiveSessionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEF4444).copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Live Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEF4444)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = "AO VIVO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFEF4444)
                    )
                    Text(
                        text = "${session.participantCount}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            // Title
            Text(
                text = session.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Subject
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = session.subjectColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = session.subject,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = session.subjectColor,
                    fontWeight = FontWeight.Medium
                )
            }

            // Teacher
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(session.subjectColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.teacherName.first().toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = session.subjectColor
                    )
                }

                Text(
                    text = session.teacherName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Join Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ENTRAR AGORA",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==================== STATUS BADGE ====================

@Composable
fun StatusBadge(status: LiveStatus) {
    val (color, text) = when (status) {
        LiveStatus.SCHEDULED -> Color(0xFF3B82F6) to "AGENDADA"
        LiveStatus.LIVE -> Color(0xFFEF4444) to "AO VIVO"
        LiveStatus.PAUSED -> Color(0xFFF59E0B) to "PAUSADA"
        LiveStatus.ENDED -> Color.Gray to "FINALIZADA"
        LiveStatus.CANCELLED -> Color.Red to "CANCELADA"
        else -> Color.Gray to "DESCONHECIDO"
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

// ==================== DADOS DE EXEMPLO ====================

private val upcomingLives = listOf(
    LiveSessionModel(
        id = "1",
        title = "Equações do 2º Grau - Revisão Completa",
        description = "Revisão completa de equações do segundo grau",
        subject = "Matemática",
        teacherName = "Prof. João Silva",
        scheduledStart = "2026-01-17T15:00:00",
        durationMinutes = 60,
        status = LiveStatus.SCHEDULED,
        price = 0.0,
        participantCount = 0,
        viewCount = 0,
        isPublic = true
    ),
    LiveSessionModel(
        id = "2",
        title = "Análise Sintática - Teoria e Prática",
        description = "Análise sintática com exemplos práticos",
        subject = "Português",
        teacherName = "Profa. Maria Santos",
        scheduledStart = "2026-01-17T16:30:00",
        durationMinutes = 90,
        status = LiveStatus.SCHEDULED,
        price = 500.0,
        participantCount = 0,
        viewCount = 0,
        isPublic = true
    )
)

private val liveNowSessions = listOf(
    LiveSessionModel(
        id = "3",
        title = "Leis de Newton - Aplicações Práticas",
        description = "Aplicações práticas das Leis de Newton",
        subject = "Física",
        teacherName = "Prof. Carlos Mendes",
        scheduledStart = "2026-01-16T14:00:00",
        durationMinutes = 60,
        status = LiveStatus.LIVE,
        price = 0.0,
        participantCount = 45,
        viewCount = 120,
        isPublic = true
    )
)

private val pastLives = listOf(
    LiveSessionModel(
        id = "4",
        title = "Reações Químicas - Balanceamento",
        description = "Como balancear reações químicas",
        subject = "Química",
        teacherName = "Profa. Ana Costa",
        scheduledStart = "2026-01-15T10:00:00",
        durationMinutes = 75,
        status = LiveStatus.ENDED,
        price = 0.0,
        participantCount = 67,
        viewCount = 150,
        isPublic = true
    )
)
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamListPage(
    onLiveClick: (LiveSessionModel) -> Unit,
    onCreateLive: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lives",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Aulas ao vivo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCreateLive) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Criar Live",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
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
                    placeholder = { Text("Procurar lives...") },
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

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Agendadas")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                            )
                            Text("Ao Vivo")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Passadas")
                        }
                    }
                )
            }

            // Content
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn()
            ) {
                when (selectedTab) {
                    0 -> UpcomingLivesContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                    1 -> LiveNowContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                    2 -> PastLivesContent(
                        searchQuery = searchQuery,
                        onLiveClick = onLiveClick
                    )
                }
            }
        }
    }
}

// ==================== UPCOMING LIVES ====================

@Composable
fun UpcomingLivesContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(upcomingLives.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveSessionCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== LIVE NOW ====================

@Composable
fun LiveNowContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(liveNowSessions.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveNowCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== PAST LIVES ====================

@Composable
fun PastLivesContent(
    searchQuery: String,
    onLiveClick: (LiveSessionModel) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pastLives.filter {
            searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        }) { live ->
            LiveSessionCard(
                session = live,
                onClick = { onLiveClick(live) }
            )
        }
    }
}

// ==================== LIVE SESSION CARD ====================

@Composable
fun LiveSessionCard(
    session: LiveSessionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Subject Badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = session.subjectColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = session.subject,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = session.subjectColor,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Price Badge
                        if (session.price > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFBBF24).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "${session.price} Kz",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFFF59E0B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "GRÁTIS",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Status Badge
                StatusBadge(status = session.status)
            }

            // Teacher
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(session.subjectColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.teacherName.first().toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = session.subjectColor
                    )
                }

                Column {
                    Text(
                        text = session.teacherName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Professor",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Divider()

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = session.scheduledStart.take(10), // Data
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${session.durationMinutes} min",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "${session.participantCount} participantes",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// ==================== LIVE NOW CARD ====================

@Composable
fun LiveNowCard(
    session: LiveSessionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEF4444).copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Live Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEF4444)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = "AO VIVO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFEF4444)
                    )
                    Text(
                        text = "${session.participantCount}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            // Title
            Text(
                text = session.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Subject
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = session.subjectColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = session.subject,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = session.subjectColor,
                    fontWeight = FontWeight.Medium
                )
            }

            // Teacher
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(session.subjectColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.teacherName.first().toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = session.subjectColor
                    )
                }

                Text(
                    text = session.teacherName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Join Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ENTRAR AGORA",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==================== STATUS BADGE ====================

@Composable
fun StatusBadge(status: LiveStatus) {
    val (color, text) = when (status) {
        LiveStatus.SCHEDULED -> Color(0xFF3B82F6) to "AGENDADA"
        LiveStatus.LIVE -> Color(0xFFEF4444) to "AO VIVO"
        LiveStatus.PAUSED -> Color(0xFFF59E0B) to "PAUSADA"
        LiveStatus.ENDED -> Color.Gray to "FINALIZADA"
        LiveStatus.CANCELLED -> Color.Red to "CANCELADA"
        else -> Color.Gray to "DESCONHECIDO"
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

// ==================== DADOS DE EXEMPLO ====================

private val upcomingLives = listOf(
    LiveSessionModel(
        id = "1",
        title = "Equações do 2º Grau - Revisão Completa",
        description = "Revisão completa de equações do segundo grau",
        subject = "Matemática",
        teacherName = "Prof. João Silva",
        scheduledStart = "2026-01-17T15:00:00",
        durationMinutes = 60,
        status = LiveStatus.SCHEDULED,
        price = 0.0,
        participantCount = 0,
        viewCount = 0,
        isPublic = true
    ),
    LiveSessionModel(
        id = "2",
        title = "Análise Sintática - Teoria e Prática",
        description = "Análise sintática com exemplos práticos",
        subject = "Português",
        teacherName = "Profa. Maria Santos",
        scheduledStart = "2026-01-17T16:30:00",
        durationMinutes = 90,
        status = LiveStatus.SCHEDULED,
        price = 500.0,
        participantCount = 0,
        viewCount = 0,
        isPublic = true
    )
)

private val liveNowSessions = listOf(
    LiveSessionModel(
        id = "3",
        title = "Leis de Newton - Aplicações Práticas",
        description = "Aplicações práticas das Leis de Newton",
        subject = "Física",
        teacherName = "Prof. Carlos Mendes",
        scheduledStart = "2026-01-16T14:00:00",
        durationMinutes = 60,
        status = LiveStatus.LIVE,
        price = 0.0,
        participantCount = 45,
        viewCount = 120,
        isPublic = true
    )
)

private val pastLives = listOf(
    LiveSessionModel(
        id = "4",
        title = "Reações Químicas - Balanceamento",
        description = "Como balancear reações químicas",
        subject = "Química",
        teacherName = "Profa. Ana Costa",
        scheduledStart = "2026-01-15T10:00:00",
        durationMinutes = 75,
        status = LiveStatus.ENDED,
        price = 0.0,
        participantCount = 67,
        viewCount = 150,
        isPublic = true
    )
)*/