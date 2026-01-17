package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class ValidateCommune {

    fun execute(commune: String,context: Context): ValidationResult {
        if(commune.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString( R.string.the_filed_is_required).toString())
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }

    }
}