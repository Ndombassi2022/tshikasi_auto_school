package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.MunicipalityDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

/*
class MunicipalityDataSourceImpl : MunicipalityDataSource {
    val db = Firebase.firestore
    override suspend fun createMunicipality(model: MunicipalityModel): MunicipalityModel {
        return suspendCoroutine { continuation ->
            db.collection("tb_municipality")
                .document(model.id)
                .set(model)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(model))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getAllMunicipality(): List<MunicipalityModel> {
        return  suspendCoroutine {continuation ->
            val municipalityReference = db.collection("tb_municipality")
            municipalityReference.get().addOnSuccessListener {querySnapshot ->
                var municipalityList= mutableListOf(MunicipalityModel())
                for (municipality in querySnapshot){
                    municipality.toObject(MunicipalityModel::class.java).run {
                        municipalityList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(municipalityList))
            }
            municipalityReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllMunicipalityByProvince(value: String): List<MunicipalityModel> {
        return  suspendCoroutine {continuation ->
            val municipalityReference = db.collection("tb_municipality").whereEqualTo("province.id", value)
            municipalityReference.get().addOnSuccessListener {querySnapshot ->
                var municipalityList= mutableListOf(MunicipalityModel())
                for (municipality in querySnapshot){
                    municipality.toObject(MunicipalityModel::class.java).run {
                        municipalityList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(municipalityList))
            }
            municipalityReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getMunicipalityBy(value: String): MunicipalityModel {
        return  suspendCoroutine {continuation ->
            val municipalityReference = db.collection("tb_municipality").whereEqualTo("id", value)
            municipalityReference.get().addOnSuccessListener {querySnapshot ->
                var municipalityModel= mutableStateOf(MunicipalityModel())
                for (municipality in querySnapshot){
                    municipality.toObject(MunicipalityModel::class.java).run {
                        municipalityModel.value = this
                    }
                }
                continuation.resumeWith(Result.success(municipalityModel.value))
            }
            municipalityReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllMunicipalityByAny(value: String): List<MunicipalityModel> {
        return  suspendCoroutine {continuation ->
            val municipalityReference = db.collection("tb_municipality").whereEqualTo("province.id", value)
            municipalityReference.get().addOnSuccessListener {querySnapshot ->
                var municipalityList= mutableListOf(MunicipalityModel())
                for (municipality in querySnapshot){
                    municipality.toObject(MunicipalityModel::class.java).run {
                        municipalityList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(municipalityList))
            }
            municipalityReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getMunicipalityById(id: String): MunicipalityModel {
        return  suspendCoroutine {continuation ->
            val municipalityReference = db.collection("tb_municipality").whereEqualTo("id", id)
            municipalityReference.get().addOnSuccessListener {querySnapshot ->
                var municipalityModel= mutableStateOf(MunicipalityModel())
                for (municipality in querySnapshot){
                    municipality.toObject(MunicipalityModel::class.java).run {
                        municipalityModel.value =this
                    }
                }
                continuation.resumeWith(Result.success(municipalityModel.value))
            }
            municipalityReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}*/
/*
interface MunicipalityApi {
    @GET("v1/municipality")
    suspend fun getAllMunicipality(): List<MunicipalityModel>

    @GET("v1/municipality/any/{value}")
    suspend fun getAllMunicipalityByAny(
        @Path("value") value: String
    ): List<MunicipalityModel>

    @GET("v1/municipality/provinceId/{provinceId}")
    suspend fun getAllMunicipalityByProvince(@Path("provinceId") provinceId: String): List<MunicipalityModel>

    @GET("v1/municipality/{id}")
    suspend fun getMunicipalityById(@Path("id") id: String): MunicipalityModel

    @POST("v1/municipality")
    suspend fun createMunicipality(@Body municipalityModel: MunicipalityModel): MunicipalityModel
}

// DataSource usando Retrofit, mantendo o mesmo padrão
class MunicipalityDataSourceImpl : MunicipalityDataSource {

    private val api: MunicipalityApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kong-gateway-tshikasi-914021610984.us-central1.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MunicipalityApi::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getAllMunicipality(): List<MunicipalityModel> {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getAllMunicipality()
                        continuation.resumeWith(Result.success(response))

                        println("Response: $response")
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

    override suspend fun getAllMunicipalityByProvince(value: String): List<MunicipalityModel> {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getAllMunicipalityByProvince(provinceId = value)
                        continuation.resumeWith(Result.success(response))

                        println("Response: $response")
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

    override suspend fun getMunicipalityBy(value: String): MunicipalityModel {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMunicipalityByAny(value: String): List<MunicipalityModel> {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getAllMunicipalityByAny(value = value)
                        continuation.resumeWith(Result.success(response))

                        println("Response: $response")
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

    override suspend fun getMunicipalityById(id: String): MunicipalityModel {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getMunicipalityById(id = id)
                        continuation.resumeWith(Result.success(response))

                        println("Response: $response")
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

    override suspend fun createMunicipality(municipalityModel: MunicipalityModel): MunicipalityModel {
        return suspendCoroutine { continuation ->
            try {
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val result = api.createMunicipality(municipalityModel = municipalityModel)
                        continuation.resumeWith(Result.success(result))
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }
}
*/
class MunicipalityDataSourceImpl : MunicipalityDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createMunicipality(municipalityModel: MunicipalityModel): MunicipalityModel {
        return try {
            client.from("tb_municipality")
                .insert(municipalityModel)
                .decodeSingle<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllMunicipality(): List<MunicipalityModel> {
        return try {
            client.from("tb_municipality")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllMunicipalityByProvince(value: String): List<MunicipalityModel> {
        return try {
            client.from("tb_municipality")
                .select {
                    filter {
                        eq("province_id",value)
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getMunicipalityBy(value: String): MunicipalityModel {
        return try {
            client.from("tb_municipality")
                .select {
                    filter {
                        eq("description",value)
                    }
                }
                .decodeSingle<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllMunicipalityByAny(value: String): List<MunicipalityModel> {
        return try {
            client.from("tb_municipality")
                .select {
                    filter {
                        ilike("description","%${value}%")
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getMunicipalityById(id: String): MunicipalityModel {
        return try {
            client.from("tb_municipality")
                .select {
                    filter {
                        eq("id",id)
                    }
                }
                .decodeSingle<MunicipalityModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }
}