package com.tshikasi.tshikasi_auto_school.presentation.validation.school

import android.content.Context
import android.util.Patterns
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class Validation {
}

class ValidateAddress {
    fun execute(address: String, context: Context): ValidationResult {
        if(address.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required))
        }
        else  if(address.length < 5){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 5 )
            )
        }
        else  if(address.length > 150){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 150 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateSchoolName {
    fun execute(name: String, context: Context): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            name.length < 3 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.minimum_characters, 3)
            )
            name.length > 100 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 100)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolNif {
    fun execute(nif: String, context: Context): ValidationResult {
        return when {
            nif.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            nif.length < 6 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.minimum_characters, 6)
            )
            nif.length > 30 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 30)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolCode {
    fun execute(code: String, context: Context): ValidationResult {
        return when {
            code.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            code.length < 3 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.minimum_characters, 3)
            )
            code.length > 20 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 20)
            )
            !code.matches(Regex("^[A-Za-z0-9-]+$")) -> ValidationResult(
                successFul = false,
                errorMessage = "" // só letras, números e hífen
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolEmail {
    fun execute(email: String, context: Context): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.thats_not_a_valid_email)
            )
            email.length > 100 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 100)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolPhone {
    fun execute(phone: String, context: Context): ValidationResult {
        return when {
            phone.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            phone.length < 9 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.minimum_characters, 9)
            )
            phone.length > 20 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 20)
            )
            !phone.matches(Regex("^[0-9+\\-\\s()]+$")) -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.thats_not_a_valid_phone_number)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolAddress {
    fun execute(address: String, context: Context): ValidationResult {
        return when {
            address.isBlank() -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
            address.length < 5 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.minimum_characters, 5)
            )
            address.length > 200 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.maximum_characters, 200)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}

class ValidateCommuneId {
    fun execute(communeId: Long, context: Context): ValidationResult {
        return if (communeId <= 0) {
            ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
        } else {
            ValidationResult(successFul = true)
        }
    }
}

class ValidateSchoolTypeId {
    fun execute(schoolTypeId: Long, context: Context): ValidationResult {
        return if (schoolTypeId <= 0) {
            ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.the_filed_is_required)
            )
        } else {
            ValidationResult(successFul = true)
        }
    }
}

class ValidateCoordinates {
    fun execute(latitude: Double, longitude: Double, context: Context): ValidationResult {
        return when {
            latitude == 0.0 && longitude == 0.0 -> ValidationResult(successFul = true) // opcional
            latitude < -90 || latitude > 90 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.invite_friend_to_interact)
            )
            longitude < -180 || longitude > 180 -> ValidationResult(
                successFul = false,
                errorMessage = context.getString(R.string.invite_friend_to_interact)
            )
            else -> ValidationResult(successFul = true)
        }
    }
}