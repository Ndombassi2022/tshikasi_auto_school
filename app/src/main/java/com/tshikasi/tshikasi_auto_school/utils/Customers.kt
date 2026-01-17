package com.tshikasi.tshikasi_auto_school.utils


import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import kotlinx.coroutines.delay
import kotlin.random.Random
fun Interpolator.asEasing(): Easing = Easing { x -> getInterpolation(x) }
@Composable
fun ShowWelcomeDialog(
    showDialog: Boolean,
    schoolModel: SchoolModel?,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    var animationStep by remember { mutableStateOf(0) }

    // Inicia as etapas de animação em sequência
    LaunchedEffect(Unit) {
        delay(300); animationStep = 1
        delay(500); animationStep = 2
        delay(500); animationStep = 3
        delay(500); animationStep = 4
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false // evita fechar sem querer
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()), // ✅ Scroll em telas pequenas
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ✨ Partículas otimizadas
                    if (animationStep >= 1) AnimatedParticles()

                    Spacer(Modifier.height(16.dp))

                    // 🎉 Ícone principal
                    AnimatedVisibility(
                        visible = animationStep >= 1,
                        enter = scaleIn(tween(800, easing = OvershootInterpolator(2f).asEasing()))
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Sucesso",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // 🎯 Título
                    AnimatedVisibility(
                        visible = animationStep >= 2,
                        enter = fadeIn(tween(800)) + slideInVertically { -40 }
                    ) {
                        Text(
                            text = "Seja Bem-vindo!",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    AnimatedVisibility(
                        visible = animationStep >= 2,
                        enter = fadeIn(tween(800)) + slideInVertically { -40 }
                    ) {
                        if (schoolModel != null) {
                            Text(

                                text = schoolModel.description,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraLight,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // 📧 Email do usuário
                    AnimatedVisibility(
                        visible = animationStep >= 3,
                        enter = fadeIn(tween(800)) + slideInVertically { 40 }
                    ) {

                        if (schoolModel != null) {
                            Text(
                                text = "Enviamos as suas credenciais de acesso para email ${schoolModel.email}. Essas credênciais darão acesso a seguinte:" ,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }

                    }

                    Spacer(Modifier.height(16.dp))

                    // ✅ Lista de benefícios
                    AnimatedVisibility(
                        visible = animationStep >= 4,
                        enter = fadeIn(tween(1000))
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            BenefitItem("Sistema de Gestão Comercial")
                            BenefitItem("Vendas fisícas")
                            BenefitItem("Loja Virtual")
                            BenefitItem("Relatórios etc..")
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // 🚀 Botão de começar
                    AnimatedVisibility(
                        visible = animationStep >= 4,
                        enter = scaleIn(tween(800, easing = OvershootInterpolator(2f).asEasing()))
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Adicione Serviços", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    val particles = remember {
        List(10) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                duration = (3000..6000).random()
            )
        }
    }

    val animatedOffsets = particles.map { particle ->
        infiniteTransition.animateFloat(
            initialValue = particle.y,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = particle.duration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "particleY"
        )
    }

    // 👇 Captura a cor ANTES de entrar no Canvas
    val particleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEachIndexed { index, particle ->
            val animatedY = animatedOffsets[index].value * size.height
            drawCircle(
                color = particleColor, // ✅ já resolvido fora
                radius = 3.dp.toPx(),
                center = Offset(particle.x * size.width, animatedY)
            )
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val duration: Int
)


@Composable
private fun BenefitItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Benefício",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface))
    }
}

@Composable
fun ShowConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    message: String,
    icon: ImageVector
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Congratulation")
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )

    }
}

@Composable
fun ShowConfirmationLoginCommercialOptionDialog(
    onDismissRequest: () -> Unit,
    onAdministratorClick: () -> Unit,
    onSaleClick: () -> Unit,
    title: String,
    message: String,
    icon: ImageVector
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ícone do topo
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(68.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Título
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descrição adicional para o usuário
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selecione o tipo de acesso adequado para suas atividades no sistema",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Área scrollável com os cards de opções
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Card de Administração
                    OptionCard(
                        title = stringResource(id = R.string.administration),
                        description = stringResource(id = R.string.administration_description),
                        imageRes = R.drawable.dashboard1,
                        onClick = onAdministratorClick
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Card de Vendas
                    OptionCard(
                        title = stringResource(id = R.string.sales),
                        description = stringResource(id = R.string.sales_description),
                        imageRes = R.drawable.commercial,
                        onClick = onSaleClick
                    )


                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botão Cancelar
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionCard(
    title: String,
    description: String,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem lateral
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Conteúdo textual
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Ícone de seta
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
@Composable
private fun PrincipalContentCard(title: String, detail: String, modifier: Modifier = Modifier, elevation: Dp, painter: Painter, color : Color, onClick: () -> Unit){

    val principalPageContext = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.background
        ),
        onClick = {
            onClick()
        }
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(modifier = Modifier
                .background(color)
                .height(110.dp)
                .fillMaxWidth()){
                Image(modifier = Modifier
                    .fillMaxSize(),
                    contentScale = ContentScale.Fit

                    ,  painter = painter
                    , contentDescription = ""
                )
            }
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()){
                Column {
                    Text(
                        text = title,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(3.dp)

                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = detail,
                            style = TextStyle(fontWeight = FontWeight.Light, fontSize = 11.sp),
                            modifier = Modifier
                                .padding( 3.dp)
                        )
                    }
                }
            }
        }
    }
}
class Customers {
}