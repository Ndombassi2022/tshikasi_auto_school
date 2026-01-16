package com.tshikasi.tshikasi_auto_school.presentation.pages.school

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolTypeModel
import com.tshikasi.tshikasi_auto_school.presentation.pages.Subject
import com.tshikasi.tshikasi_auto_school.presentation.viewmodel.GlobalViewModel
import com.tshikasi.tshikasi_auto_school.presentation.viewmodel.SchoolViewModel
import com.tshikasi.tshikasi_auto_school.utils.ToastContainer
import com.tshikasi.tshikasi_auto_school.utils.ToastManager
import com.tshikasi.tshikasi_auto_school.utils.ToastStyle
import com.tshikasi.tshikasi_auto_school.utils.getCurrenteDistance
import com.tshikasi.tshikasi_auto_school.utils.showLanguageSelectorDialog
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolListPage(
    onNavigateToLocationGuidePageClick : (SchoolModel) -> Unit,
    onPopupBack: ()-> Unit,
    onNavigateToLocalServiceListPage:(SchoolModel)-> Unit,
    onNavigateToSchoolAddPage: (SchoolTypeModel) -> Unit,
){
    val context = LocalContext.current
    val schoolViewModel = SchoolViewModel()
    var isSheetOpen = rememberSaveable { mutableStateOf(false)}

    //localViewModel!!.selectedLocalType.value = localTypeModel


    LaunchedEffect(key1 = context) {
       // localViewModel.getCurrentLocation(context)
      //  localViewModel.getAllLocalByLocalTypeId(localViewModel.selectedLocalType.value!!.id.toString(),context = context)
    }

    //BackgoundContent()
    Column {
        TopContentLocalList(
            onPopupBack = {onPopupBack()},
            localViewModel = localViewModel,
            onNavigateToLocalServiceListPage = {onNavigateToLocalServiceListPage(it)},
            onNavigateToLocalServiceAddPageClick = { onNavigateToLocalServiceAddPageClick(it) }
        )
        Box(modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxSize()
        ) {
            ExtendedFloatingActionButton(
                onClick = {

                    localViewModel.getCurrentLocation(context)
                    onNavigateToLocalAddPage(localViewModel.selectedLocalType.value!!)
                },
                Modifier
                    .padding(bottom = 6.dp, end = 10.dp)
                    .align(Alignment.BottomEnd)
                    .zIndex(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = stringResource(id = R.string.add_new_place))
                    Icon(Icons.Rounded.Add, contentDescription ="New place " )
                }
            }

            ContentBodyLocalList(
                localViewModel = localViewModel,
                onNavigateToLocationGuidePageClick = {onNavigateToLocationGuidePageClick(it)},
                onPopBackStack = { /*TODO*/ },
                onNavigateToLocalServiceAddPageClick = {onNavigateToLocalServiceAddPageClick(it)},
                onNavigateToPoliceHomePageClick = { /*TODO*/ },
                onNavigateToVirtualStorePageClick = {onNavigateToVirtualStorePageClick(it)},
                onNavigateToLocalAddPageClick = {},
                isSheetOpen = isSheetOpen.value

            )
        }
    }
}
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolListPage(
    onNavigateToLocationGuidePageClick : (SchoolModel) -> Unit,
    onPopupBack: ()-> Unit,
    onNavigateToLocalServiceListPage:(SchoolModel)-> Unit,
    onNavigateToSchoolAddPage: (SchoolTypeModel) -> Unit,
) {
    val schoolViewModel :SchoolViewModel = koinViewModel()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(100)
        isVisible = true
    }


    Column {
        TopContentSchoolList(
            onPopupBack = {onPopupBack()},
           schoolViewModel = schoolViewModel
        )
        Box(modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxSize()
        ) {
            ExtendedFloatingActionButton(
                onClick = {

                    schoolViewModel.getCurrentLocation(context)
                    onNavigateToSchoolAddPage(SchoolTypeModel())
                },
                Modifier
                    .padding(bottom = 6.dp, end = 10.dp)
                    .align(Alignment.BottomEnd)
                    .zIndex(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = stringResource(id = R.string.add_new_place))
                    Icon(Icons.Rounded.Add, contentDescription ="New place " )
                }
            }

            ContentBodySchoolList(
                isSheetOpen = false,
                onNavigateToLocationGuidePageClick ={},
                schoolViewModel =schoolViewModel ,
                onPopBackStack = { onPopupBack() },
                onNavigateToSchoolAddPageClick = {  }
            )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentBodySchoolList(
    isSheetOpen: Boolean,
    onNavigateToLocationGuidePageClick: (SchoolModel) -> Unit,
    schoolViewModel: SchoolViewModel,
    onPopBackStack: () -> Unit,
    onNavigateToSchoolAddPageClick: () -> Unit
) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    // Verificar permissão de localização
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // LocationManager para obter localização atual
    val locationManager = remember { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    // Solicitar permissão na inicialização
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            hasLocationPermission = true
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Obter localização atual quando tiver permissão
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    userLocation = lastKnownLocation
                }
            } catch (e: Exception) {
                Log.e("Location", "Erro ao obter localização: ${e.message}")
            }
        }
    }

    Column {

        var selectedLocal = remember { mutableStateOf(SchoolModel()) }
        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen = rememberSaveable { mutableStateOf(isSheetOpen) }

        //  Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.matchParentSize(),
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)

                        val locationProvider = GpsMyLocationProvider(context).apply {
                            setLocationUpdateMinDistance(0f)  // Atualiza sempre
                            setLocationUpdateMinTime(500)     // A cada 0.5 segundos
                        }

                        val myLocationOverlay = MyLocationNewOverlay(locationProvider, this).apply {
                            enableMyLocation()     // 👈 Mostra o ponto azul
                            enableFollowLocation() // 👈 Segue automaticamente
                        }

                        overlays.add(myLocationOverlay)
                        controller.setZoom(16.0)
                    }
                },
                update = { mapView ->
                    // Remover apenas os marcadores de locais que nós adicionamos
                    // MAS manter o MyLocationNewOverlay que mostra seu ponto azul!
                    mapView.overlays.removeAll { it is org.osmdroid.views.overlay.Marker }
/*
                    // Adicionar marcadores dos locais do ViewModel
                    when (val result = localViewModel.responseLocal.value) {
                        is DataState.SuccessList -> {
                            result.data?.forEach { local ->
                                val marker = org.osmdroid.views.overlay.Marker(mapView).apply {
                                    position = GeoPoint(local.latitude.toDouble(), local.longitude.toDouble())
                                    setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                                    title = local.description ?: ""
                                    snippet = local.description

                                    setOnMarkerClickListener { marker, mapView ->
                                        selectedLocal.value = local
                                        localViewModel.selectedLocalModel = local
                                        isSheetOpen.value = true
                                        true
                                    }
                                }
                                mapView.overlays.add(marker)
                            }
                        }
                        else -> {
                            if (localViewModel.selectedLocalModel.latitude.isNotEmpty()) {
                                val marker = org.osmdroid.views.overlay.Marker(mapView).apply {
                                    position = GeoPoint(
                                        localViewModel.selectedLocalModel.latitude.toDouble(),
                                        localViewModel.selectedLocalModel.longitude.toDouble()
                                    )
                                    setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                                    title = localViewModel.selectedLocalModel.description
                                }
                                mapView.overlays.add(marker)
                            }
                        }
                    }
                    */
                    mapView.invalidate()
                }
            )

           // SetLocalData(localViewModel = localViewModel, onClick = { isSheetOpen.value = !isSheetOpen.value }) {}

            // Botão para centralizar na localização do usuário
            if (hasLocationPermission && userLocation != null) {
                FloatingActionButton(
                    onClick = {
                        userLocation?.let { location ->
                            // Aqui você precisaria acessar o MapView para centralizar
                            // Isso pode requerer uma referência ao MapView
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Minha Localização"
                    )
                }
            }

            if (isSheetOpen.value) {
                ModalBottomSheet(
                    onDismissRequest = { isSheetOpen.value = false },
                    sheetState = sheetState,
                    scrimColor = Color.Transparent
                ) {
                    /*SheetContentLocalList(
                        isSheetVisible = isSheetOpen,
                        localViewModel =localViewModel ,
                        onNavigateToLocationGuide = {onNavigateToLocationGuidePageClick(it)},
                        onPopBackStack = { /*TODO*/ },
                        onNavigateToVirtualStore = {
                            onNavigateToVirtualStorePageClick(it)
                            isSheetOpen.value = false
                        },
                        onNavigateToLocalServiceAdd = {
                            onNavigateToLocalServiceAddPageClick(it)
                            isSheetOpen.value = false
                        },
                        onNavigateToPoliceHome = { /*TODO*/ }
                    )*/
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopContentSchoolList(
    schoolViewModel: SchoolViewModel,
    onPopupBack:()->Unit
) {
    val context = LocalContext.current
    var isSearchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isLoginLocalOpen = remember {
        mutableStateOf(false)
    }

    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.zIndex(2f)
    ) {
        Column {
            // TopAppBar sem título
            TopAppBar(
                modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                title = { /* Título removido da barra superior */ },
                navigationIcon = {
                    Surface(
                        onClick = { onPopupBack() },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Voltar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = !isSearchMode,
                        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                    ) {
                        Row {
                            IconButton(
                                onClick = { isSearchMode = true },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Pesquisar",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Box {
                                IconButton(
                                    onClick = { isMenuExpanded = true },
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.MoreVert,
                                        contentDescription = "Menu",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                MenuOptions(
                                  schoolViewModel = schoolViewModel,
                                    expanded = isMenuExpanded,
                                    onDismiss = { isMenuExpanded = false },
                                    schoolModel = SchoolModel()
                                )
                            }
                        }
                    }

                    // Barra de pesquisa expandida
                    AnimatedVisibility(
                        visible = isSearchMode,
                        enter = slideInHorizontally() + fadeIn() + expandHorizontally(),
                        exit = slideOutHorizontally() + fadeOut() + shrinkHorizontally()
                    ) {
                        SearchBarSection(
                            searchText = searchText,
                            onSearchTextChange = {
                                searchText = it
                            },
                            onSearch = { text ->

                                if (text.isNotEmpty()) {
                                    //localViewModel.getAllLocalByLocalTypeId(localViewModel.selectedLocalType.value!!.id.toString(),context = context)
                                }else{
                                    //localViewModel.getAllLocalByLocalTypeIdByAny(localTypeId = localViewModel.selectedLocalType.value!!.id.toString(), value = text,context = context)
                                }
                            },
                            onClearSearch = {
                                searchText = ""
                                isSearchMode = false
                                //localViewModel.getAllLocalType(context = context)
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )

            // Seção do título abaixo da barra
            AnimatedVisibility(
                visible = !isSearchMode,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column {
                        Text(
                            text = "",//localViewModel.selectedLocalType.value?.description.orEmpty().uppercase(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "",//localViewModel.selectedLocalType.value?.details.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Divider sutil
            if (!isSearchMode) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    if(isLoginLocalOpen.value){
        ShowLoginLocalDialog(
            onDismissRequest = {isLoginLocalOpen.value = !isLoginLocalOpen.value },
            onConfirmation = {  },
            schoolViewModel = schoolViewModel

        )
    }
}

@Composable
private fun SearchBarSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onSearch(searchText) }
                    ),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Buscar por tipo ou código...",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchTextChange("") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Limpar",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Botão de pesquisa
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            onClick = { onSearch(searchText) }
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Pesquisar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Botão de fechar
        IconButton(
            onClick = onClearSearch,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Fechar busca",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}



@Composable
private fun MenuOptions(
    schoolViewModel: SchoolViewModel,
    expanded: Boolean,
    schoolModel: SchoolModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val globalViewModel: GlobalViewModel = koinViewModel()
    var showSmartSearch by remember { mutableStateOf(false) }
    var isSearchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isLoginLocalOpen = remember {
        mutableStateOf(false)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(x = 0.dp, y = 8.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 8.dp
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = R.string.language),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onClick = {
                onDismiss()
                showLanguageSelectorDialog(context, globalViewModel)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        DropdownMenuItem(
            text = {
                Text(text = "Administrator")
            },
            onClick = {
                // onNavigateToLocalServiceAddPageClick(localViewModel.selectedLocalOfLogin.value!!)
                isLoginLocalOpen.value= true
                onDismiss()
            },
            leadingIcon = {Icon(imageVector = Icons.Rounded.AdminPanelSettings, contentDescription ="" )}
        )
    }
    if(isLoginLocalOpen.value){
        ShowLoginLocalDialog(
            onDismissRequest = {isLoginLocalOpen.value = !isLoginLocalOpen.value },
            onConfirmation = {  },
            schoolViewModel = schoolViewModel
        )
    }
}


@Composable
private fun ShowLoginLocalDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    schoolViewModel: SchoolViewModel
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header com ícone e título
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Login Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Acesso Local",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Faça login para acessar os serviços locais",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Conteúdo do formulário
                FieldContentLoginLocalBody(
                    onAccess = onConfirmation,
                    onNavigateToSigUpPage = { },
                    onDismissRequest = onDismissRequest,
                    schoolViewModel = schoolViewModel

                )
            }
        }
    }
}

@Composable
private fun FieldContentLoginLocalBody(
    onAccess: () -> Unit,
    onNavigateToSigUpPage: () -> Unit,
    schoolViewModel: SchoolViewModel,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current

    val localModelResult = remember {
        mutableStateOf(SchoolModel())
    }
    val toastManager = remember { ToastManager() }
    ToastContainer(
        toastManager = toastManager,
        toastStyle = ToastStyle.MODERN_SNACKBAR
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo Referência
            OutlinedTextField(
                value = "",
                onValueChange = {

                },
                label = { Text("Referência") },
                placeholder = { Text("Digite a referência") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Referência",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
              //  isError = localViewModel.stateLoginLocal.referenceError != null,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true
            )

           /* if (localViewModel.stateLoginLocal.referenceError.isNotBlank()) {
                Text(
                    text = localViewModel.stateLoginLocal.referenceError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                )
            }*/

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Username/Email
            OutlinedTextField(
                value = "",//localViewModel.stateLoginLocal.userName,
                onValueChange = {
                    /*localViewModel.onEventGetByUserNameAndPassword(
                        LoginLocalRegistrationFormEvent.UserNameChanged(it.trim()),
                        context = context
                    )*/
                },
                label = { Text("Usuário ou Email") },
                placeholder = { Text("Digite seu usuário ou email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = "Usuário",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
               // isError = localViewModel.stateLoginLocal.userNameError != null,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true
            )

           /* if (localViewModel.stateLoginLocal.userNameError.isNotBlank()) {
                Text(
                    text = localViewModel.stateLoginLocal.userNameError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                )
            }*/

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Password
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                value ="", //localViewModel.stateLoginLocal.password,
                onValueChange = {
                   /* localViewModel.onEventGetByUserNameAndPassword(
                        LoginLocalRegistrationFormEvent.PasswordChanged(it.trim()),
                        context = context
                    )*/
                },
                label = { Text("Senha") },
                placeholder = { Text("Digite sua senha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Senha",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                //isError = localViewModel.stateLoginLocal.passwordError != null,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true
            )

            /*if (localViewModel.stateLoginLocal.passwordError.isNotEmpty()) {
                Text(
                    text = localViewModel.stateLoginLocal.passwordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                )
            }*/

            Spacer(modifier = Modifier.height(24.dp))

            // Estados de resposta
           /* when(val result = localViewModel.responseLoginLocal.value) {
                is DataState.Empty -> {
                    LaunchedEffect(result) {
                        toastManager.showToast(
                            message = "Referência, usuário ou senha não confêrem. Verifique e tente novamente",
                            type = ToastType.ERROR
                        )

                    }
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
                is DataState.Failure -> {
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
                is DataState.FailureLocalService -> {
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
                is DataState.Loading -> {
                    CustomerCircularProgressBarUtil(text = localViewModel.loadingMessage.value ?: stringResource(id = R.string.processing_please_wait ))
                }
                is DataState.Start -> {
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
                is DataState.Success -> {
                    if(result.data.isNotNull()) {
                        localModelResult.value = result.data.local
                        onNavigateToLocalServiceListPage(localModelResult.value)
                        localViewModel.responseLoginLocal.value = DataState.Start
                        localViewModel.onClearLoginLocalField(context = context)
                        onDismissRequest()

                    }
                }
                is DataState.SuccessAdded -> {
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
                is DataState.SuccessList -> {
                    ButtonDialog(localViewModel, context, onDismissRequest)
                }
            }*/

            // Links de ação
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onNavigateToSigUpPage,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Criar conta",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                TextButton(
                    onClick = { /* Implementar recuperação de senha */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Esqueci minha senha",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonDialog(
    schoolViewModel: SchoolViewModel,
    context: Context,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
               /* localViewModel.onEventGetByUserNameAndPassword(
                    LoginLocalRegistrationFormEvent.Submit,
                    context = context
                )*/
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Text(
                text = "Entrar",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onDismissRequest,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}





@Composable
private fun AddMarkers(localList: List<SchoolModel>?) {
    localList?.let {
        for (localModel in localList) {
            Marker(
                state = rememberMarkerState(
                    position = LatLng(
                        0.0,0.0
                      //  localModel.latitude.toDouble(),
                      //  localModel.longitude.toDouble()
                    )
                ),
                title = "",//localModel.description,
                snippet = "",//localModel.latitude
            )
        }
    }
}



/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalListCard(
    localViewModel: LocalViewModel,
    local: LocalModel,
    distance: Float,
    modifier: Modifier = Modifier,
    elevation: Dp,
    color : Color,
    onClick: ()-> Unit
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = {onClick()}
    ) {
        Row (

        ){
            Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .width(100.dp)
                .height(100.dp)){
               if(local.urlImage.isNotEmpty()){
                    AsyncImage(contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize(), model = local.urlImage, contentDescription = "Image")
               }
            }

            Spacer(modifier = Modifier.size(5.dp))

            Column {
                Text(
                    text = local.description,
                    modifier = Modifier.padding(top = 5.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = local.address,
                    modifier = Modifier.padding(top = 6.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text =if(distance > 1000) String.format("%.2f", distance / 1000)+ " KM" else  String.format("%.2f", distance) +" M",
                    modifier = Modifier.padding(top = 2.dp)
                )

            }

        }
    }
}
*/

@Composable
private fun SchoolListCard(
     schoolViewModel: SchoolViewModel,
    schoolModel: SchoolModel,
    distance: Float,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagem do local
            SchoolImage(
                imageUrl = schoolModel.logoUrl.toString(),
                contentDescription = "School Image"//local.description
            )

            // Informações do local
            LocalInfo(
                description = schoolModel.name,
                address = schoolModel.address,
                distance = distance,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SchoolImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.size(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                //error = painterResource(R.drawable.image), // Opcional: imagem de erro
               // placeholder = painterResource(R.drawable.image) // Opcional: placeholder
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LocalInfo(
    description: String,
    address: String,
    distance: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatDistance(distance),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatDistance(distance: Float): String {
    return if (distance >= 1000) {
        String.format("%.2f km", distance / 1000)
    } else {
        String.format("%.0f m", distance)
    }
}


@Composable
private fun ShowLazySchoolList(onClick: () -> Unit, schoolList: List<SchoolModel>, schoolViewModel: SchoolViewModel, onNavigateToLocalListPage: () -> Unit){
    LazyRow(modifier = Modifier
        .height(120.dp)
        .padding(6.dp)
        .fillMaxWidth()) {
        items(schoolList){school->
            SchoolListCard(
                distance = getCurrenteDistance(
                    startLatLng = LatLng(
                        schoolViewModel.currentUserLatitude.value,
                        schoolViewModel.currentUserLongitude.value
                    ),
                    endLatLng = LatLng(

                        0.0,0.0
                       // local.latitude.toDouble(),
                       // local.longitude.toDouble()
                    )
                ),
                schoolModel = school,
                onClick = {
                    onClick()
                    //localViewModel.SelectedLocalModel(local)

                },


                modifier = Modifier
                    .padding(6.dp)
                    .width(300.dp),
                elevation = 10.dp,
                schoolViewModel = schoolViewModel
            )
        }
    }
}

