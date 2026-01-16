package com.tshikasi.tshikasi_auto_school.utils

import android.app.Activity
import android.content.Context
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.presentation.viewmodel.GlobalViewModel

class Lenguage {
}


fun showLanguageSelectorDialog(context: Context, globalViewModel: GlobalViewModel) {
    val languages = mapOf(
        "pt" to "Português",
        "en" to "English",
        "fr" to "Français",
        "ar" to "العربية",
        "zh" to "中文"
    )

    val currentActivity = context as? Activity ?: return

    val items = languages.entries.map { it.value }.toTypedArray()
    val codes = languages.entries.map { it.key }

    android.app.AlertDialog.Builder(context)
        .setTitle( context.getString(R.string.select_language))
        .setItems(items) { _, which ->
            val selectedCode = codes[which]
            globalViewModel.setLanguage(context, selectedCode)

            currentActivity.recreate()
        }
        .setNegativeButton( context.getString(R.string.cancel), null)
        .show()
}