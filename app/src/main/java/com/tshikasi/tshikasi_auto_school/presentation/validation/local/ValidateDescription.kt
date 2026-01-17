package com.tshikasi.tshikasi_auto_school.presentation.validation.local

import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.validation.ValidationResult

class ValidateDescription {
    fun execute(description: String,context: Context): ValidationResult {
        if(description.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString(R.string.the_filed_is_required))
        }

        else  if(description.length < 3){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 3 )
            )
        }
        else  if(description.length > 150){
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


class ValidateInvoiceNumber {
    fun execute(invoiceNumber: String,context: Context): ValidationResult {
        if(invoiceNumber.isBlank()){
            return ValidationResult(successFul = false,errorMessage = context.getString(R.string.the_filed_is_required))
        }

        else  if(invoiceNumber.length < 3){
            return  ValidationResult(
                successFul = false,
                errorMessage =    context.getString( R.string.minimum_characters, 3 )
            )
        }
        else  if(invoiceNumber.length > 30){
            return  ValidationResult(
                successFul = false,
                errorMessage =  context.getString( R.string.maximum_characters, 30 )
            )
        } else{
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }

    }
}

class ValidateUri {
    fun execute(uri: String,context: Context): ValidationResult {
        if(uri.isBlank()){
            return ValidationResult(
                successFul = true,
                errorMessage = ""
            )
        }else{

            return ValidationResult(successFul = false,errorMessage = context.getString(R.string.the_filed_is_required))
        }



    }
}