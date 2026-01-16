package com.tshikasi.tshikasi_auto_school.presentation.pages


import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
/*
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Azul escuro profissional
                        Color(0xFF3B82F6), // Azul médio
                        Color(0xFF60A5FA)  // Azul claro
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        ) {
            // Logo/Ícone (por enquanto usando texto estilizado)
            Text(
                text = "📚",
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nome do app
            Text(
                text = "TSHIKASI",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "Auto School",
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.9f),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Slogan
            Text(
                text = "Aprende ao teu ritmo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        // Versão no rodapé
        Text(
            text = "v1.0.0",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(alphaAnim.value)
        )
    }
}
*/

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showSlogan by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val subtitleAlpha = animateFloatAsState(
        targetValue = if (showSubtitle) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "subtitleAlpha"
    )

    val sloganAlpha = animateFloatAsState(
        targetValue = if (showSlogan) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "sloganAlpha"
    )

    val infiniteTransition = rememberInfiniteTransition()
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(500)
        showSubtitle = true
        delay(400)
        showSlogan = true
        delay(2100)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // slate-900
                        Color(0xFF1E3A8A), // blue-900
                        Color(0xFF0F172A)  // slate-900
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // ===== EFEITO DE GLOW ANIMADO NO FUNDO =====
        Box(
            modifier = Modifier
                .size(300.dp)
                .scale(scaleAnim.value)
                .alpha(glowPulse * alphaAnim.value)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF3B82F6).copy(alpha = 0.4f),
                            Color(0xFF9333EA).copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        ) {
            // ===== LOGO COM GLASSMORPHISM =====
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Círculo externo com glow
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

                // Container glassmorphism
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📚",
                        fontSize = 60.sp
                    )
                }
            }

            // ===== NOME DO APP =====
            Text(
                text = "TSHIKASI",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 3.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFF3B82F6).copy(alpha = 0.5f),
                        offset = Offset(0f, 4f),
                        blurRadius = 12f
                    )
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== SUBTÍTULO ANIMADO =====
            Box(
                modifier = Modifier
                    .alpha(subtitleAlpha.value)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AUTO SCHOOL",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF93C5FD),
                        letterSpacing = 6.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Linha decorativa animada
                    Box(
                        modifier = Modifier
                            .width(100.dp)
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

            Spacer(modifier = Modifier.height(32.dp))

            // ===== SLOGAN COM CARD GLASSMORPHISM =====
            Box(
                modifier = Modifier
                    .alpha(sloganAlpha.value)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.05f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = "Aprende ao teu ritmo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF93C5FD),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }
        }

        // ===== VERSÃO NO RODAPÉ COM ESTILO =====
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Loading indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .padding(bottom = 12.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color(0xFF60A5FA),
                    strokeWidth = 3.dp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "v1.0.0",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 2.sp
            )
        }

        // ===== PARTÍCULAS FLUTUANTES DECORATIVAS =====
        FloatingParticle(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 100.dp, start = 40.dp)
                .alpha(alphaAnim.value * 0.6f)
        )
        FloatingParticle(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 150.dp, end = 60.dp)
                .alpha(alphaAnim.value * 0.4f)
        )
        FloatingParticle(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 200.dp, start = 50.dp)
                .alpha(alphaAnim.value * 0.5f)
        )
    }
}

@Composable
private fun FloatingParticle(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particleFloat"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particleAlpha"
    )

    Box(
        modifier = modifier
            .offset(y = offsetY.dp)
            .size(12.dp)
            .alpha(alpha)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF60A5FA),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )
}