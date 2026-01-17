package com.tshikasi.tshikasi_auto_school.presentation.form_event.local

import android.net.Uri
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel

sealed class LocalAddRegistrationFormEvent {
    data class LocalIdChanged(val localId: String): LocalAddRegistrationFormEvent()
    data class DescriptionChanged(val description: String): LocalAddRegistrationFormEvent()
    data class DetailsChanged(val details: String): LocalAddRegistrationFormEvent()
    data class EmailChanged(val email : String): LocalAddRegistrationFormEvent()
    data class NifChanged(val nif : String): LocalAddRegistrationFormEvent()
    data class MobileChanged(val mobile : String): LocalAddRegistrationFormEvent()
    data class PhoneChanged(val phone: String): LocalAddRegistrationFormEvent()
    data class FullNameChanged(val fullName: String): LocalAddRegistrationFormEvent()
    data class AddressChanged(val address: String): LocalAddRegistrationFormEvent()
    data class UrlImageChanged(val urlImage: Uri?): LocalAddRegistrationFormEvent()
    data class UrlLogotipoChanged(val logotipo: Uri?): LocalAddRegistrationFormEvent()
    data class LatitudeChanged(val latitude: String): LocalAddRegistrationFormEvent()
    data class LongitudeChanged(val longitude: String): LocalAddRegistrationFormEvent()
    data class LocalTypeChanged(val localType: LocalTypeModel): LocalAddRegistrationFormEvent()
    data class CountryChanged(val country: CountryModel): LocalAddRegistrationFormEvent()
    object Submit: LocalAddRegistrationFormEvent()
}

sealed class LocalServiceAddRegistrationFormEvent {

    data class DescriptionChanged(val description: String): LocalServiceAddRegistrationFormEvent()
    data class DetailsChanged(val details: String): LocalServiceAddRegistrationFormEvent()
    data class UrlImageChanged(val urlImage : String): LocalServiceAddRegistrationFormEvent()

    object Submit: LocalServiceAddRegistrationFormEvent()
}

sealed class LocalServiceAddRegistrationFormEventDialog {

    data class DescriptionDialogChanged(val descriptionDialog: String): LocalServiceAddRegistrationFormEventDialog()
    data class DetailsDialogChanged(val detailsDialog: String): LocalServiceAddRegistrationFormEventDialog()
    data class UrlImageDialogChanged(val urlImageDialog : String):
        LocalServiceAddRegistrationFormEventDialog()

    object SubmitDialog: LocalServiceAddRegistrationFormEventDialog()
}