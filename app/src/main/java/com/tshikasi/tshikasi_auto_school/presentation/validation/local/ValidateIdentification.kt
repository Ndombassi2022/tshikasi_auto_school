package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult
class ValidateIdentification {
    fun execute(identification: String, context: Context): ValidationResult {
        if(identification.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

        else  if(identification.length < 5){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 5 )
            )
        }
        else  if(identification.length > 20){
            return  ValidationResult(
                successFul = false,
                errorMessage = context.getString( R.string.maximum_characters, 20 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }


    }
}