package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class ValidateNif {
    fun execute(nif: String,context: Context): ValidationResult {
        if(nif.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

       else if(nif.length < 8){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.minimum_characters, 8 ).toString()
            )
        }
       else if(nif.length > 25){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 25 ).toString()
            )
        }else {
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }

    }
}