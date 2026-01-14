package com.tshikasi.tshikasi_auto_school.data.mapper

import coil.network.HttpException
import com.tshikasi.tshikasi_auto_school.utils.ApiError
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import okio.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Throwable.toNetworkError(): NetworkError {
    val errorMessage = this.message ?: "Erro desconhecido"

    val error = when {
        // 🔥 IMPORTANTE: Trata HttpRequestException ANTES de outros checks
        this is HttpRequestException -> {
            // Verifica a causa raiz
            when(this.cause) {
                is SocketTimeoutException -> ApiError.NetworkError
                is UnknownHostException -> ApiError.NetworkError
                is ConnectException -> ApiError.NetworkError
                else -> {
                    // Se mensagem vazia, provavelmente é problema de rede
                    if (errorMessage.isBlank() || errorMessage.contains("failed with message:", ignoreCase = true)) {
                        ApiError.NetworkError
                    } else {
                        ApiError.UnknowResponse
                    }
                }
            }
        }

        // Sem conexão com a internet
        this is UnknownHostException ||
                this is ConnectException -> ApiError.NetworkError

        // Timeout
        this is SocketTimeoutException -> ApiError.NetworkError

        // Exceptions específicas do Supabase
        this is UnauthorizedRestException -> {
            if (errorMessage.contains("permission denied", ignoreCase = true)) {
                ApiError.PermissionDenied
            } else {
                ApiError.Unauthorized
            }
        }

        this is BadRequestRestException -> ApiError.ValidationError

        // Outras exceptions do Supabase (RestException é a base)
        this is RestException -> ApiError.UnknowResponse

        // Fallback para IOException e HttpException
        this is IOException -> ApiError.NetworkError
        this is HttpException -> ApiError.UnknowResponse

        // Detecta erros por mensagem (fallback)
        errorMessage.contains("timeout", ignoreCase = true) -> ApiError.NetworkError

        errorMessage.contains("constraint", ignoreCase = true) ||
                errorMessage.contains("duplicate", ignoreCase = true) ||
                errorMessage.contains("violates", ignoreCase = true) -> ApiError.ValidationError

        else -> {
            // Log detalhado para erros não mapeados
            println("⚠️ Erro não mapeado:")
            println("   Tipo: ${this::class.qualifiedName}")
            println("   Mensagem: $errorMessage")
            println("   Cause: ${this.cause?.message}")
            println("   Cause Type: ${this.cause?.javaClass?.simpleName}")
            ApiError.UnknownError
        }
    }

    return NetworkError(
        error = error,
        t = this
    )
}