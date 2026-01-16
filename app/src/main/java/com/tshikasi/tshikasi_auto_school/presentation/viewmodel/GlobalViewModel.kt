package com.tshikasi.tshikasi_auto_school.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tshikasi.tshikasi_auto_school.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel // ou use @Inject constructor(...) se não usar Hilt
class GlobalViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
 //   private val updateTaxiDriverStatusAndReturnTaxiVehicleUseCase : UpdateTaxiDriverStatusAndReturnTaxiVehicleUseCase,
) : ViewModel() {

    //val responseTaxiDriver: MutableState<DataState<TaxiDriverModel>> = mutableStateOf(DataState.Start)

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private var _currentUserLatitude = mutableDoubleStateOf(0.0)
    var currentUserLatitude: MutableState<Double> = _currentUserLatitude

    private var _currentUserLongitude = mutableDoubleStateOf(0.0)
    var currentUserLongitude: MutableState<Double> = _currentUserLongitude

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    var currentLanguage = mutableStateOf("pt")
        private set

    private val supportedLanguages = listOf("pt", "en", "fr", "ar", "zh")

    private var lastLocation: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    // Estado do motorista ativo
    private val _activeDriverId = MutableStateFlow<String?>(null)
    val activeDriverId = _activeDriverId.asStateFlow()

  //  val globalSharedPreferences = GlobalSharedPreferences(context)

    @SuppressLint("MissingPermission")
    fun startDriverLocationTracking(
        context: Context,
        driverId: String,
        onLocationUpdate: (latitude: String, longitude: String, distance: Float) -> Unit
    ) {
        _activeDriverId.value = driverId
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000L
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { current ->
                    lastLocation?.let { last ->
                        val distance = last.distanceTo(current)
                        if (distance >= 5f) {
                            onLocationUpdate(
                                current.latitude.toString(),
                                current.longitude.toString(),
                                distance
                            )
                            lastLocation = current
                            Log.d("GlobalVM", "📍 Moveu ${distance}m")
                        }
                    } ?: run {
                        lastLocation = current
                    }
                }
            }
        }

        fusedLocationClient?.requestLocationUpdates(
            request,
            locationCallback!!,
            Looper.getMainLooper()
        )

        Log.d("GlobalVM", "🚀 Rastreamento iniciado para driver $driverId")
    }

/*
    fun activateTaxiDriverActivity(id: String,status:String,actualLatitude:String,actualLongitude:String){
        viewModelScope.launch {
            responseTaxiDriver.value = DataState.Loading

            updateTaxiDriverStatusAndReturnTaxiVehicleUseCase(id = id, status = status, actualLatitude = actualLatitude, actualLongitude = actualLongitude).onRight{
                if (it.id > 0){
                    responseTaxiDriver.value = DataState.SuccessAdded(it.taxiDriver!!)
                    //setSelectedTaxiDriver(taxiDriverModel = it.taxiDriver)
                    /*if (status == "ENABLED"){
                        addOrUpdateTaxiVehicleInList(taxiVehicle = it)
                        //setupRealtimeForRides()
                        setupRealtimeForTaxiDrivers()
                        setupRealtimeForTaxiVehicles()
                    }else{
                        removeTaxiVehicleFromList(taxiVehicleId = it.id)
                       // stopDriverLocationUpdates()
                        setupRealtimeForTaxiDrivers()
                        setupRealtimeForTaxiVehicles()
                    }*/
                }else{
                    responseTaxiDriver.value= DataState.Empty
                }
            }.onLeft {
                responseTaxiDriver.value = DataState.Failure(message = it.getUserFriendlyMessage())
            }
        }
    }
*/
    fun stopDriverLocationTracking() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            Log.d("GlobalVM", "🛑 Rastreamento parado")
        }
        locationCallback = null
        _activeDriverId.value = null
        lastLocation = null
    }

    override fun onCleared() {
        super.onCleared()
        stopDriverLocationTracking()
    }

    // 🌍 Inicializa o idioma salvo ou detecta o do sistema
    fun initializeLanguage(context: Context) {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val savedLang = prefs.getString("lang", null)

        val language = savedLang ?: detectSystemLanguage()
        currentLanguage.value = language

        if (savedLang == null) {
            prefs.edit().putString("lang", language).apply()
        }
    }

    // 🌍 Detecta idioma do sistema
    private fun detectSystemLanguage(): String {
        val systemLang = Locale.getDefault().language
        return if (systemLang in supportedLanguages) systemLang else "en"
    }

    // 🌍 Troca o idioma e persiste
    fun setLanguage(context: Context, code: String) {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("lang", code).apply()
        currentLanguage.value = code
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return prefs.getString("lang", "pt") ?: "pt"
    }

    // 📍 Obtém a localização via Flow
   /* fun getCurrentLocation(context: Context) {
        viewModelScope.launch {
            val locationManager by lazy { LocationManager(context) }
            locationManager.trackeLocation().collect { location ->
                _currentUserLatitude.value = location.latitude
                _currentUserLongitude.value = location.longitude
            }
        }
    }*/

    // 📍 Inicializa localização com Fused API
    fun initializeLocation(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    viewModelScope.launch {
                        _location.emit(location)
                    }
                }
            }
        }
    }

    fun startLocationUpdates() {
        try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateDistanceMeters(1f)
                .build()

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            println("Error getting location: ${e.message}")
        }
    }
/*
    // 🚕 Atualiza localização do táxi no Firebase
    fun verify(latitude: String, longitude: String) {
        val db = Firebase.database
        val id = "-OEroq8RBIgFhD0pGq1F"

        val resultTaxiModel = mutableStateOf(TaxiModel())

        db.getReference("taxi").child(id).get().addOnSuccessListener {
            if (it.exists()) {
                val taxi = it.getValue(TaxiModel::class.java)
                taxi?.let { t ->
                    val updatedTaxi = t.copy(
                        actualLatitude = currentUserLatitude.value.toString(),
                        actualLongitude = currentUserLongitude.value.toString()
                    )
                    db.getReference("taxi").child(t.id.toString()).setValue(updatedTaxi)
                    resultTaxiModel.value = updatedTaxi
                }
            }
        }
    }
    */
}
