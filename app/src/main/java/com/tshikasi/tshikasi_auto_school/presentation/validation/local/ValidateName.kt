package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult
class ValidateName {
    fun execute(name: String, context: Context): ValidationResult {
        if(name.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(name.length < 5){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 5 )
            )
        }
        else  if(name.length > 150){
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
class ValidateVehicleBrand {
    fun execute(vehicleBrand: String, context: Context): ValidationResult {
        if(vehicleBrand.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(vehicleBrand.length < 2){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 2 )
            )
        }
        else  if(vehicleBrand.length > 150){
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
class ValidateVehicleModel {
    fun execute(vehicleModel: String, context: Context): ValidationResult {
        if(vehicleModel.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(vehicleModel.length < 2){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 2 )
            )
        }
        else  if(vehicleModel.length > 150){
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

class ValidateVehicleType {
    fun execute(vehicleType: String, context: Context): ValidationResult {
        if(vehicleType.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(vehicleType.length < 2){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 2 )
            )
        }
        else  if(vehicleType.length > 150){
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

class ValidateVehiclePlate {
    fun execute(plate: String, context: Context): ValidationResult {
        if(plate.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(plate.length < 5){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 5 )
            )
        }
        else  if(plate.length > 25){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 25 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateVehicleYear {
    fun execute(vehicleYear: String, context: Context): ValidationResult {
        if(vehicleYear.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(vehicleYear.length < 4){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 4 )
            )
        }
        else  if(vehicleYear.length > 4){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 4 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateVehicleColor {
    fun execute(color: String, context: Context): ValidationResult {
        if(color.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateVehicleFuelType {
    fun execute(fuelType: String, context: Context): ValidationResult {
        if(fuelType.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateVehicleSeats {
    fun execute(seats: String, context: Context): ValidationResult {
        if(seats.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}
class ValidateLicense {
    fun execute(license: String, context: Context): ValidationResult {
        if(license.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(license.length < 5){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 5 )
            )
        }
        else  if(license.length > 25){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 25 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }
    }
}