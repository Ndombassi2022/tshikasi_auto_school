package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult
class ValidateFullName {
    fun execute(fullName: String,context: Context): ValidationResult {
        if(fullName.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        }

       else if(fullName.length < 6){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.minimum_characters, 6 )
            )
        }
       else if(fullName.length > 105){
            return  ValidationResult(
                successFul = false,
                errorMessage = context.getString( R.string.maximum_characters, 105 )
            )
        }else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }

    }
}