package com.tshikasi.tshikasi_auto_school.domain.model

import android.content.Context

data class SendEmailsModel(
    var from: String="",
    var subject: String,
    var  content: String="",
    var context: Context,
)