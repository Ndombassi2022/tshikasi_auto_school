package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class ValidateDetails {
    fun execute(details: String,context: Context): ValidationResult {
        if(details.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString(R.string.the_filed_is_required ).toString())

        }

      else  if(details.length < 20){
            return  ValidationResult(
                successFul = false,
              errorMessage =  context.getString( R.string.minimum_characters, 20 ).toString()
            )
        }
      else  if(details.length > 1000){
            return  ValidationResult(
                successFul = false,
                errorMessage = context.getString( R.string.maximum_characters, 1000 ).toString()
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }

    }
}