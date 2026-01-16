package com.tshikasi.tshikasi_auto_school.presentation.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
/*
@Composable
fun WelcomePage(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A),
                        Color(0xFF3B82F6),
                        Color(0xFF60A5FA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // ✅ SCROLLABLE
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo e Título
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo temporário
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📚",
                            fontSize = 60.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "TSHIKASI",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Auto School",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Features
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 800, delayMillis = 200))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeatureItem(
                        icon = Icons.Default.School,
                        title = "Aprende ao teu ritmo",
                        description = "Estuda quando e onde quiseres"
                    )

                    FeatureItem(
                        icon = Icons.Default.VideoLibrary,
                        title = "Aulas em vídeo",
                        description = "Conteúdo completo da 1ª à 12ª classe"
                    )

                    FeatureItem(
                        icon = Icons.Default.CloudDownload,
                        title = "Modo offline",
                        description = "Baixa e estuda sem internet"
                    )

                    FeatureItem(
                        icon = Icons.Default.EmojiEvents,
                        title = "Gamificação",
                        description = "Ganha pontos e badges ao estudar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp)) // ✅ Espaço fixo ao invés de weight

            // Botões
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 800, delayMillis = 400))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botão Começar
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E3A8A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Começar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Botão Login
                    OutlinedButton(
                        onClick = onLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 2.dp
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Já tenho conta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Ícone
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Texto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
*/
@Composable
fun WelcomePage(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit,
    onExploreMap: () -> Unit = {}
) {
    var isVisible by remember { mutableStateOf(false) }
    var activeFeature by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(key1 = true) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // slate-900
                        Color(0xFF1E3A8A), // blue-900
                        Color(0xFF0F172A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // ===== HEADER ANIMADO =====
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000)) + slideInVertically(tween(1000))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo com efeito glow
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow effect
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF3B82F6).copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📚",
                                fontSize = 50.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "TSHIKASI",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "AUTO SCHOOL",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF93C5FD),
                        letterSpacing = 6.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Linha decorativa
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF60A5FA),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))


            // ===== BENEFITS BAR =====
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 400))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BenefitItem(icon = "☁️", text = "Estuda offline sem internet")
                        BenefitItem(icon = "🏆", text = "Sistema de badges e conquistas")
                        BenefitItem(icon = "📖", text = "Biblioteca digital completa")
                        BenefitItem(icon = "⭐", text = "Certificados validados")
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
            // ===== MAIN FEATURES GRID =====
            /*AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 200)) + slideInVertically(tween(1000, delayMillis = 200))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = "📹",
                            title = "Videoaulas Completas",
                            description = "Currículo da 1ª à 12ª classe em sequência progressiva",
                            gradient = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4)),
                            isActive = activeFeature == 0,
                            onClick = { activeFeature = 0 }
                        )

                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = "📡",
                            title = "Aulas ao Vivo",
                            description = "Live streams interativos com professores",
                            gradient = listOf(Color(0xFFA855F7), Color(0xFFEC4899)),
                            isActive = activeFeature == 1,
                            onClick = { activeFeature = 1 }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = "🗺️",
                            title = "Localiza a Tua Escola",
                            description = "Mapa com todas escolas de Angola",
                            gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
                            isActive = activeFeature == 2,
                            onClick = { activeFeature = 2 }
                        )

                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = "📊",
                            title = "Progresso Real",
                            description = "Acompanha o desempenho escolar",
                            gradient = listOf(Color(0xFFF97316), Color(0xFFEF4444)),
                            isActive = activeFeature == 3,
                            onClick = { activeFeature = 3 }
                        )
                    }
                }
            }*/
            // ===== MAIN FEATURES GRID =====
            // ✅ SUBSTITUIR O BLOCO "MAIN FEATURES GRID" POR ESTE:

// ===== MAIN FEATURES GRID - FULL WIDTH =====
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 200)) + slideInVertically(tween(1000, delayMillis = 200))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Card 1 - Videoaulas (largura completa)
                    FeatureCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = "📹",
                        title = "Videoaulas Completas",
                        description = "Currículo da 1ª à 12ª classe em sequência progressiva com conteúdo aprovado pelo Ministério da Educação",
                        gradient = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4)),
                        isActive = activeFeature == 0,
                        onClick = {
                            activeFeature = 0
                            onLogin()
                        }
                    )

                    // Card 2 - Aulas ao Vivo (largura completa)
                    FeatureCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = "📡",
                        title = "Aulas ao Vivo",
                        description = "Live streams interativos com professores especializados. Faz perguntas em tempo real e participa de enquetes",
                        gradient = listOf(Color(0xFFA855F7), Color(0xFFEC4899)),
                        isActive = activeFeature == 1,
                        onClick = { activeFeature = 1 }
                    )

                    // Card 3 - Mapa de Escolas (largura completa)
                    FeatureCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = "🗺️",
                        title = "Localiza a Tua Escola",
                        description = "Mapa interativo com todas escolas cadastradas em Angola. Visualiza informações completas e localização",
                        gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
                        isActive = activeFeature == 2,
                        onClick = { activeFeature = 2 }
                    )

                    // Card 4 - Progresso (largura completa)
                    FeatureCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = "📊",
                        title = "Progresso Real",
                        description = "Acompanha o desempenho escolar com estatísticas detalhadas, gráficos e relatórios de evolução",
                        gradient = listOf(Color(0xFFF97316), Color(0xFFEF4444)),
                        isActive = activeFeature == 3,
                        onClick = { activeFeature = 3 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Spacer(modifier = Modifier.height(24.dp))

            // ===== MAPA INTERATIVO PREVIEW =====
            /*AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 600))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExploreMap() },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF064E3B).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(Color(0xFF10B981), Color(0xFF059669))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🗺️", fontSize = 24.sp)
                            }

                            Text(
                                text = "Encontra a Escola do Teu Filho",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Descobre todas as escolas cadastradas em Angola. Visualiza o progresso académico, entra em aulas virtuais e mantém-te conectado com a educação do teu filho.",
                            fontSize = 14.sp,
                            color = Color(0xFF6EE7B7),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Preview do mapa
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF10B981).copy(alpha = 0.1f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(1.dp, Color(0xFF10B981).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🗺️", fontSize = 80.sp, color = Color(0xFF10B981).copy(alpha = 0.3f))

                            // Pins animados
                            Box(modifier = Modifier.fillMaxSize()) {
                                MapPin(Modifier.align(Alignment.TopStart).padding(top = 20.dp, start = 30.dp))
                                MapPin(Modifier.align(Alignment.BottomEnd).padding(bottom = 30.dp, end = 40.dp))
                                MapPin(Modifier.align(Alignment.Center).padding(top = 10.dp, start = 20.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Ver mapa interativo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6EE7B7)
                            )
                            Text(text = "→", fontSize = 20.sp, color = Color(0xFF6EE7B7))
                        }
                    }
                }
            }
*/
            Spacer(modifier = Modifier.height(32.dp))

            // ===== CTA BUTTONS =====
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 700))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF2563EB), Color(0xFF9333EA))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Começar Agora",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(text = "→", fontSize = 20.sp, color = Color.White)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = onLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White.copy(alpha = 0.1f),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Já Tenho Conta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ===== FOOTER STATS =====
          /*  AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(tween(1000, delayMillis = 900))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "5000+",
                        label = "Videoaulas",
                        gradient = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4))
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "300+",
                        label = "Escolas",
                        gradient = listOf(Color(0xFFA855F7), Color(0xFFEC4899))
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        number = "24/7",
                        label = "Acesso",
                        gradient = listOf(Color(0xFF10B981), Color(0xFF059669))
                    )
                }
            }*/

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


/*
@Composable
private fun FeatureCard(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    description: String,
    gradient: List<Color>,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            if (isActive) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(brush = Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF93C5FD),
                lineHeight = 16.sp
            )
        }
    }
}*/

// ✅ SUBSTITUIR A FUNÇÃO FeatureCard POR ESTA:

@Composable
private fun FeatureCard(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    description: String,
    gradient: List<Color>,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = if (isActive) 2.dp else 1.dp,
            color = if (isActive) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone com gradiente
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush = Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )
            }

            // Conteúdo
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 22.sp
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF93C5FD),
                    lineHeight = 18.sp
                )
            }

            // Indicador de seleção (opcional)
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(brush = Brush.linearGradient(gradient))
                )
            }
        }
    }
}

@Composable
private fun BenefitItem(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF3B82F6), Color(0xFF9333EA))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 20.sp)
        }

        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF93C5FD)
        )
    }
}

@Composable
private fun MapPin(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        modifier = modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(Color(0xFF10B981).copy(alpha = alpha))
    )
}
/*
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    gradient: List<Color>
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.Transparent,
                modifier = Modifier.background(
                    brush = Brush.linearGradient(gradient),
                    alpha = 1f
                ),
                style = TextStyle(
                    brush = Brush.linearGradient(gradient)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF93C5FD)
            )
        }
    }
}*/