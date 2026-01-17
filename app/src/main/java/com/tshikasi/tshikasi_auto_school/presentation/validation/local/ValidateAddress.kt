package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class ValidateAddress {
    fun execute(address: String, context: Context): ValidationResult {
        if(address.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())

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