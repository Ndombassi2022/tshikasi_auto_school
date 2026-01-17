package com.tshikasi.tshikasi_auto_school.presentation.form_state.local

import android.net.Uri
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel

data class LocalRegistrationFormState(
    val localId : String = "",
    val description : String = "",
    val nif : String = "",
    val phone : String="",
    val email: String = "",
    val fullName: String = "",
    val address : String = "",
    val reference : String = "",
    val details : String = "",
    val mobile : String = "",
    val urlImage : Uri? =null,
    val logotipo : Uri? =null,
    val longitude : String = "",
    val latitude : String = "",
    val localType : LocalTypeModel = LocalTypeModel(),
    val country : CountryModel = CountryModel(),
    val descriptionError : String = "",
    val nifError : String = "",
    val phoneError : String="",
    val logotipoError : String="",
    val fullNameError: String = "",
    val emailError: String = "",
    val addressError : String = "",
    val mobileError : String = "",
    val detailsError : String = ""
)