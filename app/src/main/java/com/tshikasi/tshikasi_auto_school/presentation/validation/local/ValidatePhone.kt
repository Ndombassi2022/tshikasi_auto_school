package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import android.util.Patterns
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult
class ValidatePhone {
    fun execute(phone: String,context: Context): ValidationResult {
        if(phone.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())

        }

     else  if(!Patterns.PHONE.matcher(phone).matches()){
           return ValidationResult(
               successFul = false,
               errorMessage =  context.getString( R.string.thats_not_a_valid_phone_number).toString()
           )
       } else {
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
       }

    }
}