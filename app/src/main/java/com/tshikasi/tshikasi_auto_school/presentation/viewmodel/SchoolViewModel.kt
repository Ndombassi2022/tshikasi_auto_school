package com.tshikasi.tshikasi_auto_school.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.config.ApiService
import com.tshikasi.tshikasi_auto_school.config.DirectionsApiService
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.SendEmailsModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent.GetAllContinentUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country.GetAllCountryByContinent
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.CreateLocalUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.GetAllLocalByLocalTypeIdByAnyUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.GetAllLocalByLocalTypeUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.GetAllLocalUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.UpdateLocalLocationUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local.UpdateLocalUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service.CreateLocalServiceUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service.GetAllLocalServiceByLocalUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_type.GetAllLocalTypeUseCase
import com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.login_local.CreateLoginLocalUseCase
import com.tshikasi.tshikasi_auto_school.presentation.form_event.school.SchoolAddRegistrationFormEvent
import com.tshikasi.tshikasi_auto_school.presentation.form_state.school.SchoolAddRegistrationFormState
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateDescription
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateDetails
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateEmail
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateFullName
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateMobile
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateNif
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidatePhone
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateUri
import com.tshikasi.tshikasi_auto_school.presentation.validation.school.ValidateSchoolEmail
import com.tshikasi.tshikasi_auto_school.presentation.validation.school.*
import com.tshikasi.tshikasi_auto_school.utils.CompressionProgress
import com.tshikasi.tshikasi_auto_school.utils.DataState
import com.tshikasi.tshikasi_auto_school.utils.LocationManager
import com.tshikasi.tshikasi_auto_school.utils.SendEmail
import com.tshikasi.tshikasi_auto_school.utils.SendNotificationMyself
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays
import javax.inject.Inject

@HiltViewModel // ou use @Inject constructor(...) se não usar Hilt
class SchoolViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAllContinentUseCase: GetAllContinentUseCase,
    private val getAllCountryByContinentUseCase: GetAllCountryByContinent,
    private val getAllLocalByLocalTypeIdByAnyUseCase: GetAllLocalByLocalTypeIdByAnyUseCase,
    private val getAllLocalTypeUseCase: GetAllLocalTypeUseCase,
    private val getAllLocalUseCase: GetAllLocalUseCase,
    private val createLocalUseCase: CreateLocalUseCase,
    private val updateLocalLocationUseCase: UpdateLocalLocationUseCase,
    private val updateLocalUseCase: UpdateLocalUseCase,
    private val getAllLocalByLocalType: GetAllLocalByLocalTypeUseCase,
    private val createLocalServiceUseCase: CreateLocalServiceUseCase,
    private val createLoginLocalUseCase: CreateLoginLocalUseCase,
    private val getAllLocalServiceByLocalUseCase: GetAllLocalServiceByLocalUseCase,


    ) : ViewModel() {

    private val validateEmail : ValidateSchoolEmail = ValidateSchoolEmail()
    private val validateName = ValidateSchoolName()
    private val validateCode = ValidateSchoolCode()

    private val validatePhone = ValidateSchoolPhone()
    private val validateAddress = ValidateSchoolAddress()
    private val validateCommuneId = ValidateCommuneId()
    private val validateSchoolTypeId = ValidateSchoolTypeId()
    private val validateSchoolNif = ValidateSchoolNif()

    private val validateDescription: ValidateDescription = ValidateDescription()
    private val validateNif: ValidateNif = ValidateNif()
    private val validateFullName: ValidateFullName = ValidateFullName()
    private val validateMobile: ValidateMobile = ValidateMobile()
    private val validateDetails: ValidateDetails = ValidateDetails()
    private val validateUrlImage: ValidateUri = ValidateUri()

    val responseLocal: MutableState<DataState<LocalModel>> = mutableStateOf(DataState.Start)
    val responseLocalType: MutableState<DataState<LocalTypeModel>> = mutableStateOf(DataState.Start)
    val responseContinent: MutableState<DataState<ContinentModel>> = mutableStateOf(DataState.Start)
    val responseCountry: MutableState<DataState<CountryModel>> = mutableStateOf(DataState.Start)
    val responseProvince: MutableState<DataState<ProvinceModel>> = mutableStateOf(DataState.Start)
    val responseMunicipality: MutableState<DataState<MunicipalityModel>> = mutableStateOf(DataState.Start)
    val responseCommune: MutableState<DataState<CommuneModel>> = mutableStateOf(DataState.Start)
    val responseAddLocal: MutableState<DataState<LocalModel>> = mutableStateOf(DataState.Start)

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()



    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()

    private val _downloadUrl = mutableStateOf<String?>(null)
    val downloadUrl: State<String?> = _downloadUrl

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // private var _loadingMessage = MutableLiveData<String>()
    // var loadingMessage : MutableLiveData<String> = _loadingMessage

    private val _loadingMessage = mutableStateOf("")
    val loadingMessage: State<String> = _loadingMessage


    //private val _bitmap= MutableLiveData<Bitmap?>(null)
    var bitmap = mutableStateOf<Bitmap?>(null)

    private var _isNewLocationSelected = MutableLiveData(false)
    var isNewLocationSelected : MutableLiveData<Boolean> = _isNewLocationSelected

    //private var _selectedgaleryUri = MutableLiveData<Uri>(null)
    var selectedgaleryUri = mutableStateOf<Uri?>(null)

    private var _localList = MutableLiveData<List<LocalModel>>()
    var localList : MutableLiveData<List<LocalModel>> = _localList

    private var _localTypeList = MutableLiveData<List<LocalTypeModel>>()
    var localTypeList : MutableLiveData<List<LocalTypeModel>> = _localTypeList

    //private var _localServiceList = MutableLiveData<List<LocalServiceModel>>()
    // var localServiceList : MutableLiveData<List<LocalServiceModel>> = _localServiceList
    private val _localServiceList = MutableStateFlow<List<LocalServiceModel>>(emptyList())
    val localServiceList: StateFlow<List<LocalServiceModel>> = _localServiceList

    private val _selectVideoUrl = MutableStateFlow<List<String>>(emptyList())
    val selectVideoUrl: StateFlow<List<String>> = _selectVideoUrl

    private val _compressionProgress = MutableStateFlow<CompressionProgress?>(null)
    val compressionProgress: StateFlow<CompressionProgress?> = _compressionProgress.asStateFlow()

    private var _codeConfirmacao = MutableLiveData<String>()
    var codeConfirmacao : MutableLiveData<String> = _codeConfirmacao

    private var _selectedLatitude = mutableDoubleStateOf(0.0)
    var selecteLatitude : MutableState<Double> = _selectedLatitude

    private var _selectedLongitude = mutableDoubleStateOf(0.0)
    var selectedLongitude : MutableState<Double> = _selectedLongitude


    private var _currentUserLatitude = mutableDoubleStateOf(0.0)
    var currentUserLatitude : MutableState<Double> = _currentUserLatitude


    private var _currentUserLongitude= mutableDoubleStateOf(0.0)
    var currentUserLongitude : MutableState<Double> = _currentUserLongitude


    private var _continentList = MutableLiveData<List<ContinentModel>>()
    var continentList : MutableLiveData<List<ContinentModel>> = _continentList

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

    // var openConfirmatioCodeDialog =   mutableStateOf(false)
    private val _openConfirmationCodeDialog = MutableStateFlow<Boolean>(false)
    val openConfirmationCodeDialog: StateFlow<Boolean> = _openConfirmationCodeDialog.asStateFlow()

    private val _schoolModelResult = MutableStateFlow(SchoolModel())
    val schoolModelResult: StateFlow<SchoolModel> = _schoolModelResult.asStateFlow()


    private var _countryList = MutableLiveData<List<CountryModel>>()
    var countryList : MutableLiveData<List<CountryModel>> = _countryList

    var description by mutableStateOf("")
    var selectedLocalModel by mutableStateOf(LocalModel())
    var returnedLocalModel by mutableStateOf(LocalModel())

    private var _selectedLocalType = MutableLiveData<LocalTypeModel>()
    var selectedLocalType : MutableLiveData<LocalTypeModel> = _selectedLocalType

    private var _selectedLocal = MutableLiveData<LocalModel>()
    var selectedLocal : MutableLiveData<LocalModel> = _selectedLocal

    private var _selectedCountry = MutableLiveData<CountryModel>()
    var selectedCountry : MutableLiveData<CountryModel> = _selectedCountry

    private var _selectedContinent = MutableLiveData<ContinentModel>()
    var selectedContinent : MutableLiveData<ContinentModel> = _selectedContinent

    private var _selectedLocalOfLogin = MutableLiveData<LocalModel>()
    var selectedLocalOfLogin : MutableLiveData<LocalModel> = _selectedLocalOfLogin
    private var _openDialog = MutableLiveData<Boolean>(false)
    var openDialog : MutableLiveData<Boolean> = _openDialog


    fun setOpenConfirmationCodeDialog(value: Boolean){
        _openConfirmationCodeDialog.value = value
    }

    fun onOpenDialog(){
        _openDialog.value = true
    }
    fun onCloseDialog(){
        _openDialog.value = false
    }



    var properties = mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true))

   var state by mutableStateOf(SchoolAddRegistrationFormState())
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

    fun schoolAddEvent( event: SchoolAddRegistrationFormEvent, context: Context){
        when(event){
            is SchoolAddRegistrationFormEvent.AddressChanged -> {
                state = state.copy(address = event.address)
            }
            is SchoolAddRegistrationFormEvent.CodeChanged -> {
                state = state.copy(code = event.code)
            }
            is SchoolAddRegistrationFormEvent.CommuneIdChanged -> {
                state = state.copy(communeId = event.communeId)
            }
            is SchoolAddRegistrationFormEvent.DetailsChanged -> {
                state = state.copy(details = event.details)
            }
            is SchoolAddRegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is SchoolAddRegistrationFormEvent.FoundedDateChanged -> {
                state = state.copy(foundedDate = event.foundedDate)
            }
            is SchoolAddRegistrationFormEvent.LatitudeChanged -> {
                state = state.copy(latitude = event.latitude)
            }
            is SchoolAddRegistrationFormEvent.LicenceChanged -> {
                state = state.copy(licence = event.licence)
            }
            is SchoolAddRegistrationFormEvent.LogoUrlChanged -> {
                state = state.copy(logoUrl = event.logoUrl)
            }
            is SchoolAddRegistrationFormEvent.LongitudeChanged -> {
                state = state.copy(longitude = event.longitude)
            }
            is SchoolAddRegistrationFormEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }
            is SchoolAddRegistrationFormEvent.NifChanged -> {
                state = state.copy(nif = event.nif)
            }
            is SchoolAddRegistrationFormEvent.NumberChanged -> {
                state = state.copy(number = event.number)
            }
            is SchoolAddRegistrationFormEvent.FullNameChanged -> {
                state = state.copy(fullName = event.fullName)
            }
            is SchoolAddRegistrationFormEvent.PhoneChanged -> {
                state = state.copy(phone = event.phone)
            }
            is SchoolAddRegistrationFormEvent.ReferenceChanged -> {
                state = state.copy(reference = event.reference)
            }
            is SchoolAddRegistrationFormEvent.SchoolTypeIdChanged -> {
                state = state.copy(schoolTypeId = event.schoolTypeId)
            }
            is SchoolAddRegistrationFormEvent.Submit -> {}
            is SchoolAddRegistrationFormEvent.SubscriptionExpiresAtChanged -> {
                state = state.copy(subscriptionExpiresAt = event.subscriptionExpiresAt)
            }
            is SchoolAddRegistrationFormEvent.WebsiteChanged -> {
                state = state.copy(website = event.website)
            }
            is SchoolAddRegistrationFormEvent.UrlImageChanged -> {
                state = state.copy(urlImage = event.urlImage)
            }

            is SchoolAddRegistrationFormEvent.MobileChanged -> {
                state = state.copy(mobile = event.mobile)
            }
        }
    }


    private fun onSubmitCodeConfirm(context: Context){
        val descriptionResult = validateDescription.execute(state.description, context = context)
        val nifResult = validateNif.execute(state.nif, context = context)
        val phoneResult = validatePhone.execute(state.phone, context = context)
        val mobileResult = validateMobile.execute(state.mobile, context = context)
        val emailResult = validateEmail.execute(state.email, context = context)
        val addressResult = validateAddress.execute(state.address, context = context)
        val detailsResult = validateDetails.execute(state.details, context = context)
        val fullNameResult = validateFullName.execute(state.fullName, context = context)
        val urlImageResult = validateUrlImage.execute(state.urlImage!!.toString(), context = context)

        if ( descriptionResult.successFul && nifResult.successFul && phoneResult.successFul && emailResult.successFul && addressResult.successFul && detailsResult.successFul && fullNameResult.successFul){
            generateCodeConfirmation(context = context)
            _openConfirmationCodeDialog.value = true

        }else{
            state = state.copy(
                descriptionError = descriptionResult.errorMessage!!,
                nifError = nifResult.errorMessage!!,
                phoneError = phoneResult.errorMessage!!,
                addressError = addressResult.errorMessage!!,
                mobileError = mobileResult.errorMessage!!,
                emailError = emailResult.errorMessage!!,
                fullNameError = fullNameResult.errorMessage!!,
                detailsError = detailsResult.errorMessage!!
            )
            return
        }
    }

    fun getAllContinent(context: Context) {
        responseContinent.value = DataState.Loading
        viewModelScope.launch {
            _loadingMessage.value =  context.getString(R.string.processing_please_wait)
            _continentList.value = emptyList()
            getAllContinentUseCase().onRight {
                responseContinent.value = DataState.SuccessList(it)
                _continentList.value = it
            }.onLeft {
                _continentList.value = emptyList()
                responseContinent.value = DataState.Failure(it.getUserFriendlyMessage())
            }
        }
    }
    fun getAllCountryByContinent(value : String,context: Context) {
        responseCountry.value = DataState.Loading
        viewModelScope.launch {
            _loadingMessage.value =   context.getString(R.string.processing_please_wait)
            _countryList.value = emptyList()
            getAllCountryByContinentUseCase(value = value).onRight {
                responseCountry.value = DataState.SuccessList(it)
                _countryList.value = it
            }.onLeft {
                _countryList.value = emptyList()
                responseCountry.value = DataState.Failure(it.getUserFriendlyMessage())
            }
        }
    }
    fun getAllLocalType(context: Context) {
        responseLocalType.value = DataState.Loading
        viewModelScope.launch {
            _loadingMessage.value =    context.getString(R.string.processing_please_wait)
            _localTypeList.value = emptyList()
            getAllLocalTypeUseCase().onRight {
                responseLocalType.value = DataState.SuccessList(it)
                _localTypeList.value = it
            }.onLeft {
                _localTypeList.value = emptyList()
                responseLocalType.value = DataState.Failure(it.getUserFriendlyMessage())
            }
        }
    }


    fun generateCodeConfirmation(context: Context){
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

