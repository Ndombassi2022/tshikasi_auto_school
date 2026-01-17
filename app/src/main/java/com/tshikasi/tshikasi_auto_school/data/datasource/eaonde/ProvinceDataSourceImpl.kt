package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.ProvinceDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

/*
class ProvinceDataSourceImpl: ProvinceDataSource {
    val db = Firebase.firestore
    override suspend fun createProvince( provinceModel: ProvinceModel): ProvinceModel {
        return suspendCoroutine { continuation ->
            db.collection("tb_province")
                .document(provinceModel.id)
                .set(provinceModel)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(provinceModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getAllProvince(): List<ProvinceModel> {
        return  suspendCoroutine {continuation ->
            val pronvinceReference = db.collection("tb_province")
            pronvinceReference.get().addOnSuccessListener {querySnapshot ->
                var provinceList= mutableListOf(ProvinceModel())
                for (province in querySnapshot){
                    province.toObject(ProvinceModel::class.java).run {
                        provinceList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(provinceList))
            }
            pronvinceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllProvinceByCountry(value: String): List<ProvinceModel> {
        return  suspendCoroutine {continuation ->
            val pronvinceReference = db.collection("tb_province").whereEqualTo("country.id",value )
            pronvinceReference.get().addOnSuccessListener {querySnapshot ->
                var provinceList= mutableListOf(ProvinceModel())
                for (province in querySnapshot){
                    province.toObject(ProvinceModel::class.java).run {
                        provinceList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(provinceList))
            }
            pronvinceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getProvinceBy(value: String): ProvinceModel {
        return  suspendCoroutine {continuation ->
            val pronvinceReference = db.collection("tb_province").whereEqualTo("id",value )
            pronvinceReference.get().addOnSuccessListener {querySnapshot ->
                var provinceModel= mutableStateOf(ProvinceModel())
                for (province in querySnapshot){
                    province.toObject(ProvinceModel::class.java).run {
                        provinceModel.value = this
                    }
                }
                continuation.resumeWith(Result.success(provinceModel.value))
            }
            pronvinceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllProvinceByAny(value: String): List<ProvinceModel> {
        return  suspendCoroutine {continuation ->
            val pronvinceReference = db.collection("tb_province").whereEqualTo("id",value )
            pronvinceReference.get().addOnSuccessListener {querySnapshot ->
                var provinceList= mutableListOf(ProvinceModel())
                for (province in querySnapshot){
                    province.toObject(ProvinceModel::class.java).run {
                        provinceList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(provinceList))
            }
            pronvinceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getProvinceById(id: String): ProvinceModel {
        return  suspendCoroutine {continuation ->
            val pronvinceReference = db.collection("tb_province").whereEqualTo("id", id)
            pronvinceReference.get().addOnSuccessListener {querySnapshot ->
                var provinceModel = mutableStateOf(ProvinceModel())
                for (province in querySnapshot){
                    province.toObject(ProvinceModel::class.java).run {
                        provinceModel.value = this
                    }
                }
                continuation.resumeWith(Result.success(provinceModel.value))
            }
            pronvinceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}
*/
/*
interface ProvinceApi {
    @GET("v1/province")
    suspend fun getAllProvince(): List<ProvinceModel>

    @GET("v1/province/any/{value}")
    suspend fun getAllMunicipalityByAny(
        @Path("value") value: String
    ): List<ProvinceModel>

    @GET("v1/province/provinceId/{provinceId}")
    suspend fun getAllMunicipalityByCountry(@Path("countryId") countryId: String): List<ProvinceModel>

    @GET("v1/province/{id}")
    suspend fun getMunicipalityById(@Path("id") id: String): ProvinceModel

    @POST("v1/province")
    suspend fun createProvince(@Body provinceModel: ProvinceModel): ProvinceModel
}

// DataSource usando Retrofit, mantendo o mesmo padrão
class ProvinceDataSourceImpl : ProvinceDataSource {

    private val api: ProvinceApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kong-gateway-tshikasi-914021610984.us-central1.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ProvinceApi::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun getAllProvince(): List<ProvinceModel> {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getAllProvince()
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

    override suspend fun getAllProvinceByCountry(value: String): List<ProvinceModel> {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getAllMunicipalityByCountry(countryId = value)
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

    override suspend fun getProvinceBy(value: String): ProvinceModel {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProvinceByAny(value: String): List<ProvinceModel> {
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

    override suspend fun getProvinceById(id: String): ProvinceModel {
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

    override suspend fun createProvince(provinceModel: ProvinceModel): ProvinceModel {
        return suspendCoroutine { continuation ->
            try {
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val result = api.createProvince(provinceModel = provinceModel)
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
class ProvinceDataSourceImpl : ProvinceDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createProvince(provinceModel: ProvinceModel): ProvinceModel {
        return try {
            client.from("tb_province")
                .insert(provinceModel)
                .decodeSingle<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllProvince(): List<ProvinceModel> {
        return try {
            client.from("tb_province")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllProvinceByCountry(value: String): List<ProvinceModel> {
        return try {
            client.from("tb_province")
                .select {
                    filter {
                        eq("country_id",value)
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getProvinceBy(value: String): ProvinceModel {
        return try {
            client.from("tb_province")
                .select {
                    filter {
                        eq("id",value)
                    }
                }
                .decodeSingle<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllProvinceByAny(value: String): List<ProvinceModel> {
        return try {
            client.from("tb_province")
                .select {
                    filter {
                        ilike("description","%${value}%")
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getProvinceById(id: String): ProvinceModel {
        return try {
            client.from("tb_province")
                .select {
                    filter {
                        eq("id",id)
                    }
                }
                .decodeSingle<ProvinceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }
}