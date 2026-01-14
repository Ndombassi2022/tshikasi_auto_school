package com.tshikasi.tshikasi_auto_school.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class NetworkError(
    val error: ApiError,
    val t: Throwable?= null
){
    fun getUserFriendlyMessage(): String {
        return when(error) {
            ApiError.NetworkError -> {
                when(t) {
                    is SocketTimeoutException -> "A conexão demorou muito. Tente novamente."
                    is UnknownHostException,
                    is ConnectException -> "Sem conexão com a internet. Verifique sua conexão."
                    else -> "Erro de conexão. Verifique sua internet."
                }
            }
            ApiError.PermissionDenied -> "Você não tem permissão para realizar esta ação."
            ApiError.Unauthorized -> "Sessão expirada. Faça login novamente."
            ApiError.ValidationError -> "Dados inválidos. Verifique as informações."
            ApiError.UnknowResponse -> "Erro no servidor. Tente novamente mais tarde."
            ApiError.UnknownError -> t?.message ?: "Erro desconhecido. Tente novamente."
        }
    }
}

enum class ApiError(val message:String){
    NetworkError("Network Error"),
    ValidationError("Validation Error"),
    PermissionDenied("Permission Denied"),
    UnknowResponse("Unknown Response"),
    Unauthorized("Unauthorized"),
    UnknownError("Unknown Error")
}