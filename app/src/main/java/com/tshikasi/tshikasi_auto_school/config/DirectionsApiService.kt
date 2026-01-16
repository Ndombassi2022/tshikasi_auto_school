package com.tshikasi.tshikasi_auto_school.config


import org.osmdroid.util.Distance
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.time.Duration


interface DirectionsApiService {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}
data class OverviewPolyline(
    val points: String // Encoded polyline points
)
data class Route(
    val legs: List<Leg>,
    val overviewPolyline: OverviewPolyline
)

data class DirectionsResponse(
    val routes: List<Route>
)
data class Leg(
    val start_address: String,
    val end_address: String,
    val distance: Distance,
    val duration: Duration,
    val steps: List<Step>
)
data class Step(
    val html_instructions: String,
    val distance: Distance,
    val polyLine: PolyLine
)
data class PolyLine(
    val points:String
)