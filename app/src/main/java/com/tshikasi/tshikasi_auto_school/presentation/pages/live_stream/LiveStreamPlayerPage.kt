package com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream


import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.tshikasi.tshikasi_auto_school.domain.model.LiveChatMessageModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveParticipantModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStatus
import com.tshikasi.tshikasi_auto_school.domain.model.ParticipantRole
import kotlinx.coroutines.delay
/*
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamPlayerPage(
    session: LiveSessionModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showChat by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(0) }
    var messageText by remember { mutableStateOf("") }

    // ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            session.playbackUrl?.let {
                setMediaItem(MediaItem.fromUri(it))
                prepare()
                playWhenReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (session.status == LiveStatus.LIVE) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                            }
                            Text(
                                text = if (session.status == LiveStatus.LIVE) "AO VIVO" else "LIVE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = session.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    // Online count
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "${session.participantCount}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
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

            // Content below video
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Session Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title & Subject
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

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = session.subjectColor.copy(alpha = 0.1f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = session.subject,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = session.subjectColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
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

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // React Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* React */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Reagir",
                                tint = Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Reagir",
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

                        // Chat Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showChat = !showChat }
                        ) {
                            Icon(
                                imageVector = if (showChat) Icons.Default.ChatBubble else Icons.Default.ChatBubbleOutline,
                                contentDescription = "Chat",
                                tint = if (showChat) Color(0xFF3B82F6) else Color.Gray
                            )
                            Text(
                                text = "Chat",
                                fontSize = 12.sp,
                                color = if (showChat) Color(0xFF3B82F6) else Color.Gray
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
                        text = { Text("Chat") }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        text = { Text("Participantes") }
                    )
                    Tab(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        text = { Text("Sobre") }
                    )
                }

                // Tab Content
                Box(modifier = Modifier.weight(1f)) {
                    when (currentTab) {
                        0 -> ChatTab(
                            messages = sampleMessages,
                            onSendMessage = { message ->
                                // Send message
                            }
                        )
                        1 -> ParticipantsTab(participants = sampleParticipants)
                        2 -> AboutTab(session = session)
                    }
                }

                // Message Input (only for chat tab)
                if (currentTab == 0) {
                    MessageInput(
                        value = messageText,
                        onValueChange = { messageText = it },
                        onSend = {
                            // Send message
                            messageText = ""
                        }
                    )
                }
            }
        }
    }
}

// ==================== CHAT TAB ====================

@Composable
fun ChatTab(
    messages: List<LiveChatMessageModel>,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            ChatMessageItem(message)
        }
    }
}

@Composable
fun ChatMessageItem(message: LiveChatMessageModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.userName.first().toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Message
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = message.userName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B82F6)
            )
            Text(
                text = message.message,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

// ==================== PARTICIPANTS TAB ====================

@Composable
fun ParticipantsTab(participants: List<LiveParticipantModel>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "${participants.size} participantes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(participants) { participant ->
            ParticipantItem(participant)
        }
    }
}

@Composable
fun ParticipantItem(participant: LiveParticipantModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = participant.userName.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = participant.userName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = participant.role.name,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Role Badge
        if (participant.role == ParticipantRole.TEACHER) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEF4444).copy(alpha = 0.1f)
            ) {
                Text(
                    text = "PROFESSOR",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp,
                    color = Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==================== ABOUT TAB ====================

@Composable
fun AboutTab(session: LiveSessionModel) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Sobre esta live",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = session.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }

        item {
            Divider()

            Text(
                text = "Informações",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(label = "Disciplina", value = session.subject)
            InfoRow(label = "Professor", value = session.teacherName)
            InfoRow(label = "Duração", value = "${session.durationMinutes} minutos")
            InfoRow(label = "Preço", value = if (session.price > 0) "${session.price} Kz" else "Grátis")
            InfoRow(label = "Visualizações", value = "${session.viewCount}")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==================== MESSAGE INPUT ====================

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escreva uma mensagem...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = if (value.isNotBlank()) Color(0xFF3B82F6) else Color.Gray
                )
            }
        }
    }
}

// ==================== SAMPLE DATA ====================

private val sampleMessages = listOf(
    LiveChatMessageModel(
        id = "1",
        liveSessionId = "1",
        userId = "1",
        userName = "João Silva",
        message = "Excelente explicação, professor!",
        createdAt = "2026-01-16T14:30:00"
    ),
    LiveChatMessageModel(
        id = "2",
        liveSessionId = "1",
        userId = "2",
        userName = "Maria Santos",
        message = "Poderia repetir a última parte?",
        createdAt = "2026-01-16T14:31:00"
    ),
    LiveChatMessageModel(
        id = "3",
        liveSessionId = "1",
        userId = "3",
        userName = "Pedro Costa",
        message = "Muito bom! 👏",
        createdAt = "2026-01-16T14:32:00"
    )
)

private val sampleParticipants = listOf(
    LiveParticipantModel(
        id = "1",
        liveSessionId = "1",
        userId = "1",
        userName = "Prof. Carlos Mendes",
        role = ParticipantRole.TEACHER,
        joinedAt = "2026-01-16T14:00:00"
    ),
    LiveParticipantModel(
        id = "2",
        liveSessionId = "1",
        userId = "2",
        userName = "João Silva",
        role = ParticipantRole.VIEWER,
        joinedAt = "2026-01-16T14:05:00"
    ),
    LiveParticipantModel(
        id = "3",
        liveSessionId = "1",
        userId = "3",
        userName = "Maria Santos",
        role = ParticipantRole.VIEWER,
        joinedAt = "2026-01-16T14:07:00"
    )
)

 */


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamPlayerPage(
    session: LiveSessionModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showChat by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(0) }
    var messageText by remember { mutableStateOf("") }

    // ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            session.playbackUrl?.let {
                setMediaItem(MediaItem.fromUri(it))
                prepare()
                playWhenReady = true
            }
        }
    }

    // ✅ Interceptar botão back do Android
    BackHandler(onBack = onBackClick)

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (session.status == LiveStatus.LIVE) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                            }
                            Text(
                                text = if (session.status == LiveStatus.LIVE) "AO VIVO" else "LIVE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = session.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    // Online count
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "${session.participantCount}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
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

            // Content below video
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Session Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Title & Subject
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

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = session.subjectColor.copy(alpha = 0.1f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = session.subject,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = session.subjectColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
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

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // React Button
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { /* React */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Reagir",
                                tint = Color(0xFF3B82F6)
                            )
                            Text(
                                text = "Reagir",
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

                        // Chat Toggle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showChat = !showChat }
                        ) {
                            Icon(
                                imageVector = if (showChat) Icons.Default.ChatBubble else Icons.Default.ChatBubbleOutline,
                                contentDescription = "Chat",
                                tint = if (showChat) Color(0xFF3B82F6) else Color.Gray
                            )
                            Text(
                                text = "Chat",
                                fontSize = 12.sp,
                                color = if (showChat) Color(0xFF3B82F6) else Color.Gray
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
                        text = { Text("Chat") }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        text = { Text("Participantes") }
                    )
                    Tab(
                        selected = currentTab == 2,
                        onClick = { currentTab = 2 },
                        text = { Text("Sobre") }
                    )
                }

                // Tab Content
                Box(modifier = Modifier.weight(1f)) {
                    when (currentTab) {
                        0 -> ChatTab(
                            messages = sampleMessages,
                            onSendMessage = { message ->
                                // Send message
                            }
                        )
                        1 -> ParticipantsTab(participants = sampleParticipants)
                        2 -> AboutTab(session = session)
                    }
                }

                // Message Input (only for chat tab)
                if (currentTab == 0) {
                    MessageInput(
                        value = messageText,
                        onValueChange = { messageText = it },
                        onSend = {
                            // Send message
                            messageText = ""
                        }
                    )
                }
            }
        }
    }
}

// ==================== CHAT TAB ====================

@Composable
fun ChatTab(
    messages: List<LiveChatMessageModel>,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            ChatMessageItem(message)
        }
    }
}

@Composable
fun ChatMessageItem(message: LiveChatMessageModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.userName.first().toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Message
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = message.userName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B82F6)
            )
            Text(
                text = message.message,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

// ==================== PARTICIPANTS TAB ====================

@Composable
fun ParticipantsTab(participants: List<LiveParticipantModel>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "${participants.size} participantes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(participants) { participant ->
            ParticipantItem(participant)
        }
    }
}

@Composable
fun ParticipantItem(participant: LiveParticipantModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3B82F6)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = participant.userName.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = participant.userName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = participant.role.name,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Role Badge
        if (participant.role == ParticipantRole.TEACHER) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEF4444).copy(alpha = 0.1f)
            ) {
                Text(
                    text = "PROFESSOR",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp,
                    color = Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==================== ABOUT TAB ====================

@Composable
fun AboutTab(session: LiveSessionModel) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Sobre esta live",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = session.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }

        item {
            Divider()

            Text(
                text = "Informações",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(label = "Disciplina", value = session.subject)
            InfoRow(label = "Professor", value = session.teacherName)
            InfoRow(label = "Duração", value = "${session.durationMinutes} minutos")
            InfoRow(label = "Preço", value = if (session.price > 0) "${session.price} Kz" else "Grátis")
            InfoRow(label = "Visualizações", value = "${session.viewCount}")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ==================== MESSAGE INPUT ====================

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escreva uma mensagem...") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )

            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = if (value.isNotBlank()) Color(0xFF3B82F6) else Color.Gray
                )
            }
        }
    }
}

// ==================== SAMPLE DATA ====================

private val sampleMessages = listOf(
    LiveChatMessageModel(
        id = "1",
        liveSessionId = "1",
        userId = "1",
        userName = "João Silva",
        message = "Excelente explicação, professor!",
        createdAt = "2026-01-16T14:30:00"
    ),
    LiveChatMessageModel(
        id = "2",
        liveSessionId = "1",
        userId = "2",
        userName = "Maria Santos",
        message = "Poderia repetir a última parte?",
        createdAt = "2026-01-16T14:31:00"
    ),
    LiveChatMessageModel(
        id = "3",
        liveSessionId = "1",
        userId = "3",
        userName = "Pedro Costa",
        message = "Muito bom! 👏",
        createdAt = "2026-01-16T14:32:00"
    )
)

private val sampleParticipants = listOf(
    LiveParticipantModel(
        id = "1",
        liveSessionId = "1",
        userId = "1",
        userName = "Prof. Carlos Mendes",
        role = ParticipantRole.TEACHER,
        joinedAt = "2026-01-16T14:00:00"
    ),
    LiveParticipantModel(
        id = "2",
        liveSessionId = "1",
        userId = "2",
        userName = "João Silva",
        role = ParticipantRole.VIEWER,
        joinedAt = "2026-01-16T14:05:00"
    ),
    LiveParticipantModel(
        id = "3",
        liveSessionId = "1",
        userId = "3",
        userName = "Maria Santos",
        role = ParticipantRole.VIEWER,
        joinedAt = "2026-01-16T14:07:00"
    )
)