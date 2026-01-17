package com.tshikasi.tshikasi_auto_school.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import com.google.firebase.messaging.FirebaseMessaging

//const val APP_ID = "554f6e57a42a483b980d3199aebb1691"
@HiltViewModel
class PushNotificationMessageViewModel @Inject constructor(
) : ViewModel(){


    init {
        getTokenFromFirebase()
    }



    var fcmToken by mutableStateOf("")
        private set

    var notificationMessage by mutableStateOf("")
        private set

    var title by mutableStateOf("")
    var body by mutableStateOf("")

    private fun getTokenFromFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener {token ->
                fcmToken = token
                println("TOKEN :"+fcmToken)
            }
    }

    fun sendNotification(
        deviceToken: String,
        type:String?,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) {
        viewModelScope.launch {
            try {
                val client = OkHttpClient()

                val messageJson = JSONObject().apply {
                    put("message", JSONObject().apply {
                        put("token", deviceToken)

                        // ✅ notification COM APENAS os campos permitidos
                        put("notification", JSONObject().apply {
                            put("title", title)
                            put("body", body)
                        })

                        // ✅ Dados personalizados DEVEM estar em "data"
                        val dataObject = JSONObject()
                        data.forEach { (key, value) ->
                            dataObject.put(key, value)
                        }
                        dataObject.put("type", type) // Aqui sim pode
                        put("data", dataObject)

                        // ✅ Prioridade alta no Android
                        put("android", JSONObject().apply {
                            put("priority", "HIGH")
                        })
                    })
                }

                val jsonMediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = messageJson.toString().toRequestBody(jsonMediaType)

                val request = Request.Builder()
                    .url("http://10.0.2.2:8081/v1/pushNotification/token") // seu endpoint local
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        notificationMessage = "Erro ao enviar token ao backend: ${e.message}"
                    }

                    override fun onResponse(call: Call, response: Response) {
                        notificationMessage = if (response.isSuccessful) {
                            "Token enviado com sucesso"
                        } else {
                            "Falha ao enviar token: ${response.code} - ${response.body?.string()}"
                        }
                    }
                })

            } catch (e: Exception) {
                Log.e("FCM", "Erro ao enviar notificação: ${e.message}")
                println("ERRO AO ENVIAR NOTIFICAÇÃO: ${e.message}")
                throw e
            }
        }
    }


    fun sendTokenToBackend(token: String, title: String, body: String) {
        val json = """
        {
          "token": "$token",
          "title": "$title",
          "body": "$body"
        }
    """.trimIndent()

        val request = Request.Builder()
            .url("http://10.0.2.2:8081/v1/pushNotification/token")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                notificationMessage = "Erro ao enviar token ao backend: ${e.message}"
            }

            override fun onResponse(call: Call, response: Response) {
                notificationMessage = if (response.isSuccessful) {
                    "Token enviado com sucesso "
                } else {
                    "Falha ao enviar token: ${response.code} - ${response.body?.string()}"
                }
            }
        })
    }
    fun updateNotification(message: String) {
        notificationMessage = message
    }

}