package com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tshikasi.tshikasi_auto_school.domain.model.CreateLiveSessionRequest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLivePage(
    onBackClick: () -> Unit,
    onCreateClick: (CreateLiveSessionRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Matemática") }
    var scheduledDate by remember { mutableStateOf("") }
    var scheduledTime by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("60") }
    var maxParticipants by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var requiresApproval by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("0") }
    var expanded by remember { mutableStateOf(false) }

    val subjects = listOf(
        "Matemática",
        "Português",
        "Inglês",
        "Física",
        "Química",
        "Biologia",
        "História",
        "Geografia"
    )

    // ✅ Interceptar botão back do Android
    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Live") },
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
                    containerColor = Color(0xFF3B82F6),
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informações da Live",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Título
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Title, contentDescription = null)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Descrição
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                },
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Disciplina (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = subject,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Disciplina *") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    subjects.forEach { subj ->
                        DropdownMenuItem(
                            text = { Text(subj) },
                            onClick = {
                                subject = subj
                                expanded = false
                            }
                        )
                    }
                }
            }

            Divider()

            Text(
                text = "Agendamento",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Data
            OutlinedTextField(
                value = scheduledDate,
                onValueChange = { scheduledDate = it },
                label = { Text("Data (AAAA-MM-DD) *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                },
                placeholder = { Text("2026-01-20") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Hora
            OutlinedTextField(
                value = scheduledTime,
                onValueChange = { scheduledTime = it },
                label = { Text("Hora (HH:MM) *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                },
                placeholder = { Text("14:00") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Duração
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duração (minutos) *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Timer, contentDescription = null)
                },
                placeholder = { Text("60") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            Divider()

            Text(
                text = "Configurações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Máximo de participantes
            OutlinedTextField(
                value = maxParticipants,
                onValueChange = { maxParticipants = it },
                label = { Text("Máximo de participantes (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.People, contentDescription = null)
                },
                placeholder = { Text("Deixe vazio para ilimitado") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Preço
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Preço (Kz)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.AttachMoney, contentDescription = null)
                },
                placeholder = { Text("0 = Grátis") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6)
                )
            )

            // Switches
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Live pública",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Qualquer pessoa pode entrar",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = isPublic,
                            onCheckedChange = { isPublic = it }
                        )
                    }

                    Divider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Requer aprovação",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Você deve aprovar participantes",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = requiresApproval,
                            onCheckedChange = { requiresApproval = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Criar Button
            Button(
                onClick = {
                    val request = CreateLiveSessionRequest(
                        title = title,
                        description = description,
                        subject = subject,
                        scheduledStart = "${scheduledDate}T${scheduledTime}:00",
                        durationMinutes = duration.toIntOrNull() ?: 60,
                        maxParticipants = maxParticipants.toIntOrNull(),
                        isPublic = isPublic,
                        requiresApproval = requiresApproval,
                        price = price.toDoubleOrNull() ?: 0.0
                    )
                    onCreateClick(request)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && scheduledDate.isNotBlank() && scheduledTime.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CRIAR LIVE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}