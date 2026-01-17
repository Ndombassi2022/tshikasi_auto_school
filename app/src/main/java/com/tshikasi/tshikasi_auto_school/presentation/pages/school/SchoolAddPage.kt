package com.tshikasi.tshikasi_auto_school.presentation.pages.school


import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.presentation.form_event.school.SchoolAddRegistrationFormEvent
import com.tshikasi.tshikasi_auto_school.presentation.viewmodel.GlobalViewModel
import com.tshikasi.tshikasi_auto_school.presentation.viewmodel.SchoolViewModel
import com.tshikasi.tshikasi_auto_school.utils.CustomLinearProgressWithUploadImageTextUtil
import com.tshikasi.tshikasi_auto_school.utils.CustomTextField
import com.tshikasi.tshikasi_auto_school.utils.CustomerCircularProgressBarUtil
import com.tshikasi.tshikasi_auto_school.utils.DataState
import com.tshikasi.tshikasi_auto_school.utils.ShowWelcomeDialog
import com.tshikasi.tshikasi_auto_school.utils.ToastContainer
import com.tshikasi.tshikasi_auto_school.utils.ToastManager
import com.tshikasi.tshikasi_auto_school.utils.ToastStyle
import com.tshikasi.tshikasi_auto_school.utils.ToastType
import com.tshikasi.tshikasi_auto_school.utils.showLanguageSelectorDialog
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SchoolAddPage(
    onPopupBack: ()-> Unit
) {

    val context = LocalContext.current
    val schoolViewModel : SchoolViewModel = koinViewModel()
    val schoolModelResult = schoolViewModel.schoolModelResult.collectAsState()
    //schoolViewModel.selectedLocalType.value =  localTypeModel
    val uploadProgress by schoolViewModel.uploadProgress.collectAsState()
    var isOpenInfoDialog = remember {
        mutableStateOf(false)
    }

    val loadingMessage by schoolViewModel.loadingMessage
    LaunchedEffect(key1 = context) {
        schoolViewModel.getCurrentLocation(context = context)
        schoolViewModel.getAllLocalType(context = context)

    }

    val toastManager = remember { ToastManager() }
    ToastContainer(
        toastManager = toastManager,
        toastStyle = ToastStyle.MODERN_SNACKBAR
    ) {
        when (val result = schoolViewModel.responseAddLocal.value) {
            is DataState.Empty -> {
                SchoolAddBody(
                    onNavigateToLocalListPage = {  },
                    schoolViewModel = schoolViewModel,
                    onPopupBack = { onPopupBack() }
                )
            }

            is DataState.Failure -> {
                SchoolAddBody(
                    onNavigateToLocalListPage = { },
                    schoolViewModel = schoolViewModel,
                    onPopupBack = { onPopupBack() }
                )
            }
            is DataState.FailureLocalService -> {}
            is DataState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (uploadProgress >= 0f && uploadProgress < 100f) {
                        CostumeProgressBarUpoloadImage(uploadProgress, loadingMessage)
                    } else if (uploadProgress == 100f) {
                        CustomerCircularProgressBarUtil(text = loadingMessage)
                    }
                }
            }

            is DataState.Start -> {
                SchoolAddBody(
                    onPopupBack = { onPopupBack() },
                    onNavigateToLocalListPage = { },
                    schoolViewModel = schoolViewModel,
                )
            }

            is DataState.Success -> {}
            is DataState.SuccessAdded -> {
                //localViewModel.onClearFieldProduct(context = context)
                schoolViewModel.responseAddLocal.value = DataState.Start

                toastManager.showToast(
                    message ="Por favor consulte o seu email enviamos as crêdenciais " ,
                    type = ToastType.SUCCESS
                )
                isOpenInfoDialog.value = true
            }
            is DataState.SuccessList -> {}
        }
    }


    ShowWelcomeDialog(
        showDialog =isOpenInfoDialog.value,
       schoolModel = schoolModelResult.value,
        onDismiss = {
           // onNavigateToLocalServiceListPage(schoolModelResult.value)
            isOpenInfoDialog.value = !isOpenInfoDialog.value
        }
    )
}

// 🎨 DESIGN MELHORADO - SchoolAddPage

@Composable
private fun SchoolAddBody(
    onNavigateToLocalListPage: (LocalTypeModel) -> Unit,
    onPopupBack: (LocalTypeModel) -> Unit,
    schoolViewModel: SchoolViewModel,
) {
    val globalViewModel: GlobalViewModel = koinViewModel()
    val context = LocalContext.current

    // Animações suaves
    val contentAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "contentFade"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .alpha(contentAlpha)
        ) {
            // ✅ HEADER MODERNO COM GRADIENTE
            ModernHeader(
                onBackClick = { /* onNavigateToLocalListPage() */ },
                onLanguageClick = { showLanguageSelectorDialog(context, globalViewModel) }
            )

            // ✅ TÍTULO E SUBTÍTULO MELHORADOS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✨ Cadastre sua Empresa",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Expanda seu negócio e alcance milhares de clientes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // ✅ FORMULÁRIO COM CARDS
            LocalAddFormImproved(
                schoolViewModel = schoolViewModel,
                onPopupBack = { /* onPopupBack() */ }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ✅ HEADER MODERNO
@Composable
private fun ModernHeader(
    onBackClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2)
                    )
                )
            )
    ) {
        // Círculos decorativos
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = size.width * 0.3f,
                center = Offset(size.width * 0.2f, size.height * 0.5f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.8f, size.height * 0.7f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = onLanguageClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Idioma",
                    tint = Color.White
                )
            }
        }
    }
}

// ✅ FORMULÁRIO MELHORADO COM SECTIONS
@Composable
private fun LocalAddFormImproved(
    onPopupBack: () -> Unit,
    schoolViewModel: SchoolViewModel
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var focusedField by remember { mutableStateOf<String?>(null) }
    var isOpenInfo by remember { mutableStateOf(false) }
    val toastManager = remember { ToastManager() }

    // Camera setup (igual ao original)
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var displayedImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageFile(context)
            capturedImageUri = uri
            // Launch camera
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri?.let { uri ->
                displayedImageUri = uri
                schoolViewModel.schoolAddEvent(
                    SchoolAddRegistrationFormEvent.UrlImageChanged(urlImage = uri),
                    context = context
                )
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            displayedImageUri = it
            schoolViewModel.schoolAddEvent(
                SchoolAddRegistrationFormEvent.UrlImageChanged(urlImage = it),
                context = context
            )
        }
    }

    ToastContainer(
        toastManager = toastManager,
        toastStyle = ToastStyle.MODERN_SNACKBAR
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ✅ SEÇÃO: FOTO DA EMPRESA
            FormSection(
                title = "📸 Foto da Empresa",
                subtitle = "Adicione uma imagem profissional"
            ) {
                ModernPhotoSection(
                    photoUri = displayedImageUri ?: schoolViewModel.state.urlImage,
                    onTakePhoto = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                val uri = createImageFile(context)
                                capturedImageUri = uri
                                cameraLauncher.launch(uri)
                            }
                            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    onChooseFromGallery = { galleryLauncher.launch("image/*") },
                    onRemovePhoto = {
                        displayedImageUri = null
                        schoolViewModel.schoolAddEvent(
                            SchoolAddRegistrationFormEvent.UrlImageChanged(urlImage = null),
                            context = context
                        )
                    }
                )
            }

            // ✅ SEÇÃO: LOCALIZAÇÃO
            FormSection(
                title = "🌍 Localização",
                subtitle = "Onde sua empresa está localizada?"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Continente, País e Tipo Local aqui
                    DropDownContinent(schoolViewModel, null)
                    DropDownCountry(schoolViewModel, null)
                    DropDownLocalType(schoolViewModel, null)
                }
            }

            // ✅ SEÇÃO: INFORMAÇÕES BÁSICAS
            FormSection(
                title = "ℹ️ Informações Básicas",
                subtitle = "Dados principais da empresa"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ModernTextField(
                        value = schoolViewModel.state.description,
                        onValueChange = {
                            schoolViewModel.schoolAddEvent(
                                SchoolAddRegistrationFormEvent.DescriptionChanged(it),
                                context
                            )
                        },
                        label = "Nome da Empresa",
                        leadingIcon = Icons.Default.Info,
                        error = schoolViewModel.state.descriptionError
                    )

                    ModernTextField(
                        value = schoolViewModel.state.email,
                        onValueChange = {
                            schoolViewModel.schoolAddEvent(
                                SchoolAddRegistrationFormEvent.EmailChanged(it),
                                context
                            )
                        },
                        label = "Email",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        error = schoolViewModel.state.emailError
                    )

                    ModernTextField(
                        value = schoolViewModel.state.nif,
                        onValueChange = {
                            schoolViewModel.schoolAddEvent(
                                SchoolAddRegistrationFormEvent.NifChanged(it),
                                context
                            )
                        },
                        label = "NIF",
                        leadingIcon = Icons.Default.Badge,
                        error = schoolViewModel.state.nifError
                    )
                }
            }

            // ✅ SEÇÃO: CONTATO
            FormSection(
                title = "📞 Contato",
                subtitle = "Como os clientes podem te encontrar?"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ModernTextField(
                            value = schoolViewModel.state.mobile,
                            onValueChange = {
                                schoolViewModel.schoolAddEvent(
                                    SchoolAddRegistrationFormEvent.MobileChanged(it),
                                    context
                                )
                            },
                            label = "Celular",
                            leadingIcon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone,
                            error = schoolViewModel.state.mobileError,
                            modifier = Modifier.weight(1f)
                        )

                        ModernTextField(
                            value = schoolViewModel.state.phone,
                            onValueChange = {
                                schoolViewModel.schoolAddEvent(
                                    SchoolAddRegistrationFormEvent.PhoneChanged(it),
                                    context
                                )
                            },
                            label = "Telefone",
                            leadingIcon = Icons.Default.Call,
                            keyboardType = KeyboardType.Phone,
                            error = schoolViewModel.state.phoneError,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    ModernTextField(
                        value = schoolViewModel.state.fullName,
                        onValueChange = {
                            schoolViewModel.schoolAddEvent(
                                SchoolAddRegistrationFormEvent.FullNameChanged(it),
                                context
                            )
                        },
                        label = "Responsável",
                        leadingIcon = Icons.Default.Person,
                        error = schoolViewModel.state.fullNameError
                    )

                    ModernTextField(
                        value = schoolViewModel.state.address,
                        onValueChange = {
                            schoolViewModel.schoolAddEvent(
                                SchoolAddRegistrationFormEvent.AddressChanged(it),
                                context
                            )
                        },
                        label = "Endereço",
                        leadingIcon = Icons.Default.LocationOn,
                        error = schoolViewModel.state.addressError
                    )
                }
            }

            // ✅ SEÇÃO: DETALHES
            FormSection(
                title = "📝 Descrição",
                subtitle = "Conte mais sobre seu negócio"
            ) {
                OutlinedTextField(
                    value = schoolViewModel.state.details,
                    onValueChange = {
                        schoolViewModel.schoolAddEvent(
                            SchoolAddRegistrationFormEvent.DetailsChanged(it),
                            context
                        )
                    },
                    label = { Text("Detalhes adicionais") },
                    placeholder = { Text("Descreva os serviços, horário de atendimento...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    minLines = 4,
                    maxLines = 6,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
            }

            // ✅ BOTÕES MODERNOS
            Spacer(modifier = Modifier.height(8.dp))

            ModernActionButtons(
                onSave = {
                    if (schoolViewModel.state.urlImage.isNotNull()) {
                        schoolViewModel.schoolAddEvent(
                            SchoolAddRegistrationFormEvent.Submit,
                            context
                        )
                    } else {
                        toastManager.showToast(
                            message = "Por favor, adicione uma foto",
                            type = ToastType.WARNING
                        )
                    }
                },
                onCancel = { onPopupBack() }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ✅ COMPONENTE: SECTION HEADER
@Composable
private fun FormSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            content()
        }
    }
}

// ✅ TEXTFIELD MODERNO
@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String = ""
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (error.isEmpty())
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            isError = error.isNotEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// ✅ SEÇÃO DE FOTO MODERNA
@Composable
private fun ModernPhotoSection(
    photoUri: Uri?,
    onTakePhoto: () -> Unit,
    onChooseFromGallery: () -> Unit,
    onRemovePhoto: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        if (photoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = photoUri),
                contentDescription = "Foto",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onRemovePhoto,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    )
                    .size(40.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remover",
                    tint = Color.White
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Adicione uma foto",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onTakePhoto,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Câmera")
                    }

                    OutlinedButton(
                        onClick = onChooseFromGallery,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Galeria")
                    }
                }
            }
        }
    }
}

// ✅ BOTÕES DE AÇÃO MODERNOS
@Composable
private fun ModernActionButtons(
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.Close, null, Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Cancelar", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Save, null, Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Cadastrar", fontWeight = FontWeight.Bold)
        }
    }
}

// ✅ FUNÇÃO AUXILIAR
private fun createImageFile(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("SCHOOL_${timeStamp}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

@Composable
private fun DropDownContinent(schoolViewModel: SchoolViewModel, continentList: List<ContinentModel>?) {
    val context = LocalContext.current
    CustomerOutlinedDropDownContinent(
        label = stringResource(id = R.string.continent),
        placehold = stringResource(id = R.string.please_select_continent),
        icon = Icons.Rounded.ArrowDropDown,
        showCaracter = true,
        onTextChage = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 15.dp, end = 20.dp),
        onRefresh = {
            schoolViewModel.selectedContinent.value = null
            schoolViewModel.selectedCountry.value = null
            schoolViewModel.countryList.value = null
            schoolViewModel.continentList.value = null
            schoolViewModel.getAllContinent(context = context)

        },
        schoolViewModel = schoolViewModel,
        continentList = continentList
    )
}

@Composable
private fun DropDownLocalType(schoolViewModel: SchoolViewModel, localTypeList: List<LocalTypeModel>?) {
    val context = LocalContext.current
    CustomerOutlinedDropDownLocalType(
        label = stringResource(id = R.string.local_type),
        placehold = stringResource(id = R.string.select_local_type),
        icon = Icons.Rounded.ArrowDropDown,
        showCaracter = true,
        onTextChage = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 15.dp, end = 20.dp),
        onRefresh = {
            schoolViewModel.getAllLocalType(context = context)
        },
        schoolViewModel = schoolViewModel,
        localTypeList = localTypeList
    )
}

@Composable
private fun DropDownCountry(schoolViewModel: SchoolViewModel, countryList: List<CountryModel>?) {
    val context = LocalContext.current
    val toastManager = remember { ToastManager() }
    ToastContainer(
        toastManager = toastManager,
        toastStyle = ToastStyle.MODERN_SNACKBAR
    ) {
        CustomerOutlinedDropDownCountry(
            label = stringResource(id = R.string.country),
            placehold = stringResource(id = R.string.select_country),
            icon = Icons.Rounded.ArrowDropDown,
            showCaracter = true,
            onTextChage = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 15.dp, end = 20.dp),
            onRefresh = {
                if(schoolViewModel.selectedContinent.value != null){
                    schoolViewModel.getAllCountryByContinent(value = schoolViewModel.selectedContinent.value!!.id.toString(),context = context)
                }else{
                    toastManager.showToast(
                        message = context.getString(R.string.please_select_continent),
                        type = ToastType.INFO
                    )
                }
            },
            schoolViewModel = schoolViewModel,
            countryList = countryList
        )
    }
}

@Composable
private fun CostumeProgressBarUpoloadImage(uploadProgress: Float, loadingMessage: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomLinearProgressWithUploadImageTextUtil(uploadProgress = uploadProgress / 100f, loadingMessage = loadingMessage)
    }
}

@Composable
fun CustomerOutlinedDropDownCountry(schoolViewModel: SchoolViewModel, onRefresh: ()-> Unit, countryList: List<CountryModel>?, label: String, placehold: String, errorStatus: Boolean = false, errorMessage:String = "", icon: ImageVector, showCaracter:Boolean, onTextChage: (String) -> Unit, modifier: Modifier) {
    val textValue = remember { mutableStateOf("")  }
    var passwordVisible = remember { mutableStateOf(showCaracter) }
    var expanded by remember{ mutableStateOf(false) }
    // val localTypeList = listOf("Ansembleia Nacional","Hospital","Hospedaria")
    var selecteItem by remember { mutableStateOf("") }
    var textFieldSize by remember{ mutableStateOf(Size.Zero) }
    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }

    Column(
        modifier = Modifier.padding(5.dp)
    ) {

        OutlinedTextField(
            value = selecteItem,
            onValueChange = {
                selecteItem = it
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {
                Text(text = stringResource(id = R.string.select_country))
            },
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "",
                        Modifier.clickable {
                            onRefresh()
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        Modifier.clickable { expanded = !expanded }
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = ! expanded },
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            countryList.let {
                if(!countryList.isNullOrEmpty()){
                    countryList.forEach { country->

                        DropdownMenuItem(
                            text = {
                                Text(text = country.description)
                            },
                            onClick = {
                                selecteItem = country.description
                                schoolViewModel.selectedCountry.value = country
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerOutlinedDropDownLocalType(schoolViewModel: SchoolViewModel, onRefresh: ()-> Unit, localTypeList: List<LocalTypeModel>?, label: String, placehold: String, errorStatus: Boolean = false, errorMessage:String = "", icon: ImageVector, showCaracter:Boolean, onTextChage: (String) -> Unit, modifier: Modifier) {
    val textValue = remember { mutableStateOf("")  }
    var passwordVisible = remember { mutableStateOf(showCaracter) }
    var expanded by remember{ mutableStateOf(false) }
    // val localTypeList = listOf("Ansembleia Nacional","Hospital","Hospedaria")
    var selecteItem by remember { mutableStateOf("") }
    var textFieldSize by remember{ mutableStateOf(Size.Zero) }
    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }
    schoolViewModel.selectedLocalType.value?.let {
        selecteItem = it.description
    }


    Column(
        modifier = Modifier.padding(5.dp)
    ) {

        OutlinedTextField(
            value = selecteItem,
            onValueChange = {
                selecteItem = it
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {
                Text(text =stringResource(id = R.string.select_local_type))
            },
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "",
                        Modifier.clickable {
                            onRefresh()
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        Modifier.clickable { expanded = !expanded }
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = ! expanded },
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            localTypeList.let {
                if(!localTypeList.isNullOrEmpty()){
                    localTypeList.forEach { localType->

                        DropdownMenuItem(
                            text = {
                                Text(text = localType.description)
                            },
                            onClick = {
                                selecteItem = localType.description

                                schoolViewModel.selectedLocalType.value = localType
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerOutlinedDropDownContinent(schoolViewModel: SchoolViewModel, onRefresh: ()-> Unit, continentList: List<ContinentModel>?, label: String, placehold: String, errorStatus: Boolean = false, errorMessage:String = "", icon: ImageVector, showCaracter:Boolean, onTextChage: (String) -> Unit, modifier: Modifier) {
    val context = LocalContext.current
    val textValue = remember { mutableStateOf("")  }
    var passwordVisible = remember { mutableStateOf(showCaracter) }
    var expanded by remember{ mutableStateOf(false) }
    // val localTypeList = listOf("Ansembleia Nacional","Hospital","Hospedaria")
    var selecteItem by remember { mutableStateOf("") }
    var textFieldSize by remember{ mutableStateOf(Size.Zero) }
    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }

    Column(
        modifier = Modifier.padding(5.dp)
    ) {

        OutlinedTextField(
            value = selecteItem,
            onValueChange = {
                selecteItem = it
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {
                Text(text = stringResource(id = R.string.continent), maxLines = 1 )
            },
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "",
                        Modifier.clickable {
                            onRefresh()
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        Modifier.clickable { expanded = !expanded }
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = ! expanded },
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            continentList.let {
                if(!continentList.isNullOrEmpty()){
                    continentList.forEach { continent->

                        DropdownMenuItem(
                            text = {
                                Text(text = continent.description)
                            },
                            onClick = {
                                // schoolViewModel.SelectedContinenntModel(continentModel = continent)
                                selecteItem = continent.description

                                schoolViewModel.selectedContinent.value = continent
                                schoolViewModel.getAllCountryByContinent(value = continent.id.toString(),context = context)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

