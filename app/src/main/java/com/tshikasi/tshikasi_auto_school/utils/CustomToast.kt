package com.tshikasi.tshikasi_auto_school.utils


import android.content.Context

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// 1. ENUM PARA TIPOS DE TOAST
enum class ToastType(
    val backgroundColor: Color,
    val icon: ImageVector,
    val iconTint: Color = Color.White
) {
    SUCCESS(
        backgroundColor = Color(0xFF4CAF50),
        icon = Icons.Default.CheckCircle
    ),
    ERROR(
        backgroundColor = Color(0xFFE53E3E),
        icon = Icons.Default.Error
    ),
    WARNING(
        backgroundColor = Color(0xFFFF9800),
        icon = Icons.Default.Warning
    ),
    INFO(
        backgroundColor = Color(0xFF2196F3),
        icon = Icons.Default.Info
    )
}

class CustomToast {
}

// 2. COMPOSABLE DO TOAST PERSONALIZADO
@Composable
fun CustomToast(
    message: String,
    type: ToastType = ToastType.INFO,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Auto-dismiss após 3 segundos
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(200)
        ) + fadeOut(animationSpec = tween(200))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = type.backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = null,
                    tint = type.iconTint,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// 3. TOAST MINIMALISTA MODERNO
@Composable
fun MinimalToast(
    message: String,
    type: ToastType = ToastType.INFO,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(2500)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .fillMaxWidth(),
                color = type.backgroundColor.copy(alpha = 0.9f),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = type.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// 4. SNACKBAR PERSONALIZADO MODERNO
@Composable
fun ModernSnackbar(
    message: String,
    type: ToastType = ToastType.INFO,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    action: (@Composable () -> Unit)? = null
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(4000)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra colorida lateral
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(40.dp)
                        .background(
                            type.backgroundColor,
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = type.icon,
                    contentDescription = null,
                    tint = type.backgroundColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                action?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    it()
                }
            }
        }
    }
}

// 5. CLASSE PARA GERENCIAR ESTADO DOS TOASTS
@Stable
class ToastManager {
    var isVisible by mutableStateOf(false)
        private set
    var message by mutableStateOf("")
        private set
    var type by mutableStateOf(ToastType.INFO)
        private set

    fun showToast(message: String, type: ToastType = ToastType.INFO) {
        this.message = message
        this.type = type
        this.isVisible = true
    }

    fun hideToast() {
        isVisible = false
    }
}

// 6. COMPOSABLE PRINCIPAL COM TOAST OVERLAY
@Composable
fun ToastContainer(
    toastManager: ToastManager,
    toastStyle: ToastStyle = ToastStyle.CUSTOM,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()

        // Toast overlay
        Box(
            modifier = Modifier
                .align(
                    when (toastStyle) {
                        ToastStyle.MINIMAL -> Alignment.TopCenter
                        ToastStyle.MODERN_SNACKBAR -> Alignment.BottomCenter
                        ToastStyle.CUSTOM -> Alignment.BottomCenter
                    }
                )
                .fillMaxWidth()
        ) {
            when (toastStyle) {
                ToastStyle.CUSTOM -> {
                    CustomToast(
                        message = toastManager.message,
                        type = toastManager.type,
                        isVisible = toastManager.isVisible,
                        onDismiss = toastManager::hideToast
                    )
                }
                ToastStyle.MINIMAL -> {
                    MinimalToast(
                        message = toastManager.message,
                        type = toastManager.type,
                        isVisible = toastManager.isVisible,
                        onDismiss = toastManager::hideToast
                    )
                }
                ToastStyle.MODERN_SNACKBAR -> {
                    ModernSnackbar(
                        message = toastManager.message,
                        type = toastManager.type,
                        isVisible = toastManager.isVisible,
                        onDismiss = toastManager::hideToast
                    )
                }
            }
        }
    }
}

enum class ToastStyle {
    CUSTOM, MINIMAL, MODERN_SNACKBAR
}


// 8. VERSÃO SIMPLIFICADA PARA USO RÁPIDO
@Composable
fun rememberToastManager(): ToastManager {
    return remember { ToastManager() }
}

// FUNÇÃO DE EXTENSÃO PARA FACILITAR O USO
fun ToastManager.showSelectImageToast(context: Context) {
    showToast(
        message = "Seleciona a imagem e continue",//context.getString(R.string.select_image_toast_message),
        type = ToastType.INFO
    )
}

