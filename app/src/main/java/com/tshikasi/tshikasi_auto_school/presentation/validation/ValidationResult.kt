package com.tshikasi.tshikasi_auto_school.presentation.validation

data class ValidationResult(
    val successFul : Boolean,
    val errorMessage : String?= null
)