package com.tshikasi.tshikasi_auto_school.presentation.form_event.school

import android.net.Uri
import com.tshikasi.tshikasi_auto_school.presentation.validation.local.ValidateFullName


sealed class SchoolAddRegistrationFormEvent {
    data class DescriptionChanged(val description: String): SchoolAddRegistrationFormEvent()
    data class FullNameChanged(val fullName: String): SchoolAddRegistrationFormEvent()
    data class DetailsChanged(val details: String): SchoolAddRegistrationFormEvent()
    data class NumberChanged(val number: String): SchoolAddRegistrationFormEvent()
    data class LicenceChanged(val licence: String): SchoolAddRegistrationFormEvent()
    data class ReferenceChanged(val reference: String): SchoolAddRegistrationFormEvent()
    data class CommuneIdChanged(val communeId: String): SchoolAddRegistrationFormEvent()
    data class LatitudeChanged(val latitude: String): SchoolAddRegistrationFormEvent()
    data class LongitudeChanged(val longitude: String): SchoolAddRegistrationFormEvent()
    data class SchoolTypeIdChanged(val schoolTypeId: String): SchoolAddRegistrationFormEvent()
    data class LogoUrlChanged(val logoUrl: Uri?): SchoolAddRegistrationFormEvent()
    data class UrlImageChanged(val urlImage: Uri?): SchoolAddRegistrationFormEvent()
    data class FoundedDateChanged(val foundedDate: String): SchoolAddRegistrationFormEvent()
    data class WebsiteChanged(val website: String): SchoolAddRegistrationFormEvent()
    data class SubscriptionExpiresAtChanged(val subscriptionExpiresAt: String): SchoolAddRegistrationFormEvent()
    data class CodeChanged(val code: String): SchoolAddRegistrationFormEvent()
    data class PhoneChanged(val phone: String): SchoolAddRegistrationFormEvent()
    data class MobileChanged(val mobile: String): SchoolAddRegistrationFormEvent()
    data class EmailChanged(val email: String): SchoolAddRegistrationFormEvent()
    data class AddressChanged(val address: String): SchoolAddRegistrationFormEvent()
    data class NifChanged(val nif: String): SchoolAddRegistrationFormEvent()
    object Submit: SchoolAddRegistrationFormEvent()
}