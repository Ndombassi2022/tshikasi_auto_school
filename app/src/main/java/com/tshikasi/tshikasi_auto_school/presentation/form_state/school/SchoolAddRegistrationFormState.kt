package com.tshikasi.tshikasi_auto_school.presentation.form_state.school

import android.net.Uri
import com.tshikasi.tshikasi_auto_school.presentation.form_event.school.SchoolAddRegistrationFormEvent
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateFullName

data class SchoolAddRegistrationFormState(
    val reference : String = "",
    val description : String = "",
    val fullName : String = "",
    val details : String = "",
    val email : String = "",
    val number : String = "",
    val licence : String = "",
    val communeId : String = "",
    val latitude : String = "",
    val longitude : String = "",
    val schoolTypeId : String = "",
    val logoUrl :  Uri? = null,
    val urlImage :  Uri? = null,
    val foundedDate : String = "",
    val website : String = "",
    val subscriptionExpiresAt : String = "",
    val code : String = "",
    val phone : String = "",
    val mobile : String = "",
    val address : String = "",
    val nif : String = "",
    val referenceError : String = "",
    val descriptionError : String = "",
    val detailsError : String = "",
    val numberError : String = "",
    val licenceError : String = "",
    val communeIdError : String = "",
    val latitudeError : String = "",
    val longitudeError : String = "",
    val schoolTypeIdError : String = "",
    val logoUrlError : String = "",
    val foundedDateError : String = "",
    val websiteError : String = "",
    val subscriptionExpiresAtError : String = "",
    val codeError : String = "",
    val phoneError : String = "",
    val fullNameError : String = "",
    val mobileError : String = "",
    val addressError : String = "",
    val nifError : String = "",
    val emailError : String = "",
    val urlImageError : String = "",

    ) {
}