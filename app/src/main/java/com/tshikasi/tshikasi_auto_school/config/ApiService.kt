package com.tshikasi.tshikasi_auto_school.config

import retrofit2.http.GET

data class HealthResponse(
    val status: String,
)

interface ApiService {
    @GET("actuator/health")
    suspend fun healthCheck(): HealthResponse
}