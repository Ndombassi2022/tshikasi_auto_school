package com.tshikasi.tshikasi_auto_school.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.tshikasi.tshikasi_auto_school.config.ApiService
import com.tshikasi.tshikasi_auto_school.config.DirectionsApiService
import com.tshikasi.tshikasi_auto_school.utils.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays
import javax.inject.Inject

@HiltViewModel // ou use @Inject constructor(...) se não usar Hilt
class SchoolViewModel @Inject constructor(
    @ApplicationContext private val context: Context,

) : ViewModel() {



    private var _selectedLatitude = mutableDoubleStateOf(0.0)
    var selecteLatitude : MutableState<Double> = _selectedLatitude

    private var _selectedLongitude = mutableDoubleStateOf(0.0)
    var selectedLongitude : MutableState<Double> = _selectedLongitude


    private var _currentUserLatitude = mutableDoubleStateOf(0.0)
    var currentUserLatitude : MutableState<Double> = _currentUserLatitude


    private var _currentUserLongitude= mutableDoubleStateOf(0.0)
    var currentUserLongitude : MutableState<Double> = _currentUserLongitude


    private var _mapTypeNormal = MutableLiveData<Boolean>(false)
    var mapTypeNormal : MutableLiveData<Boolean> = _mapTypeNormal

    private var _mapTypeTerrain = MutableLiveData<Boolean>(false)
    var mapTypeTerrain : MutableLiveData<Boolean> = _mapTypeTerrain

    private var _mapTypeSatellite = MutableLiveData<Boolean>(false)
    var mapTypeSatellite : MutableLiveData<Boolean> = _mapTypeSatellite

    private var _mapTypeHybrid = MutableLiveData<Boolean>(false)
    var mapTypeHybrid : MutableLiveData<Boolean> = _mapTypeHybrid

    private var _mapTypeNone = MutableLiveData<Boolean>(false)
    var mapTypeNone : MutableLiveData<Boolean> = _mapTypeNone

    private val _directionsState = MutableLiveData<List<LatLng>>()
    val directionsState: LiveData<List<LatLng>> get() = _directionsState


    var properties = mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))


    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://tshikasi-microservice-rh.fly.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    private val directionsApiService: DirectionsApiService = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DirectionsApiService::class.java)

    fun changeMapType(){
        if(_mapTypeTerrain.value!!){
            properties.value = MapProperties(mapType = MapType.TERRAIN)
            _mapTypeSatellite.value = false
            _mapTypeHybrid.value = false
            _mapTypeNormal.value = false
            _mapTypeNone.value = false
            return

        }
        else if(_mapTypeSatellite.value!!){
            properties.value = MapProperties(mapType = MapType.SATELLITE)
            _mapTypeTerrain.value = false
            _mapTypeHybrid.value = false
            _mapTypeNormal.value = false
            _mapTypeNone.value = false
            return
        }
        else if(_mapTypeHybrid.value!!){
            properties.value = MapProperties(mapType = MapType.HYBRID)
            _mapTypeTerrain.value = false
            _mapTypeSatellite.value = false
            _mapTypeNormal.value = false
            _mapTypeNone.value = false
            return
        }
        else  if(_mapTypeNormal.value!!){
            properties.value = MapProperties(mapType = MapType.NORMAL)
            _mapTypeTerrain.value = false
            _mapTypeSatellite.value = false
            _mapTypeHybrid.value = false
            _mapTypeNone.value = false
            return
        }
    }
    fun getCurrentLocation(context: Context){
        viewModelScope.launch {
            val locationManager by lazy {
                LocationManager(context)
            }
            locationManager.trackeLocation().collect{location ->
                _currentUserLatitude.value = location.latitude
                _currentUserLongitude.value = location.longitude
            }
        }
    }
  /*  fun generateCodeConfirmation(context: Context){
        val result  = (10..10000000).random()
        _codeConfirmacao.value = result.toString()
        val sendEmailModel : SendEmailsModel = SendEmailsModel(
            from = state.email,
            subject = "Codigo Confirmação ",
            context = context,
            content = "Insira este codigo para continuar o cadastramento\n ${_codeConfirmacao.value} "
        )
        SendEmail(sendEmailsModel = sendEmailModel)
        SendNotificationMyself(titulo = "Codigo de confirmação", mensagem = _codeConfirmacao.value.toString(), context = context)
    }*/

    fun fetchDirections(origin: String, destination: String,apiKey:String) {
        viewModelScope.launch {
            try {
                // Replace with actual Directions API call
                val response = directionsApiService.getDirections(origin, destination, apiKey)
                val points = decodePolyline(response.routes[0].legs[0].steps[0].polyLine.points)
                _directionsState.postValue(points)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getCurrentLocationInfo(context: Context){
        Places.initialize(context, "AIzaSyDAiFYLmGv-c1po_tz4_qi9dlcOgVJYoy0")

        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields: List<Place.Field> = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS,

            )
        val fields = Arrays.asList(Place.Field.NAME)
        val request : FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
        val request1 : FindCurrentPlaceRequest = FindCurrentPlaceRequest.builder(placeFields).build()
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            placesClient.findCurrentPlace(request1).addOnSuccessListener {response ->
                for(placeLikelihoods in response.placeLikelihoods){
                    Log.d("LocationInfo", "Name: ${placeLikelihoods}")
                }
            }.addOnFailureListener {task->
                Log.e("LocationInfo", "Place not found: ${task.stackTrace}")
            }
        }
    }
}


// Dummy function for decoding polyline
private fun decodePolyline(encoded: String): List<LatLng> {
    val path = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dLat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dLng

        path.add(LatLng(lat / 1E5, lng / 1E5))
    }


    return path
}