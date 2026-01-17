package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

/*
class LocalServiceDataSourceImpl:LocalServiceDataSource {
    val db = Firebase.firestore
    override suspend fun createLocalService(localServiceModel: LocalServiceModel): LocalServiceModel {

        return suspendCoroutine { continuation ->
            db.collection("tb_local_service")
                .document(localServiceModel.id)
                .set(localServiceModel)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(localServiceModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getAllLocalService(): List<LocalServiceModel> {
        return suspendCoroutine { continuation ->
            val localServiceReference = db.collection("tb_local_service")
            localServiceReference.get().addOnSuccessListener { documents->
                val localServiceList = mutableListOf<LocalServiceModel>()
                for(document in documents){
                    document.toObject(LocalServiceModel::class.java).run {
                        localServiceList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localServiceList))
            }
            localServiceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }


    override suspend fun getAllLocalServiceByLocal(value: String): List<LocalServiceModel> {
        return suspendCoroutine { continuation ->
            val localServiceReference = db.collection("tb_local_service").whereEqualTo("local.id",value)
            localServiceReference.get().addOnSuccessListener { documents->
                val localServiceList = mutableListOf<LocalServiceModel>()
                for(document in documents){
                    document.toObject(LocalServiceModel::class.java).run {
                        localServiceList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localServiceList))
            }
            localServiceReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
            }
        }

}
*/
/*interface LocalServiceApi {
    @GET("v1/localService")
    suspend fun getAllLocalService(): List<LocalServiceModel>

    @GET("v1/localService/any/{value}")
    suspend fun getAllLocalServiceByAny(
        @Path("value") value: String
    ): List<LocalModel>

    @GET("v1/localService/localTypeIdAny/{localTypeId}/{value}")
    suspend fun getLocalServicesByLocalTypeAndAny(
        @Path("localTypeId") localTypeId: Long,
        @Path("value") value: String
    ): List<LocalModel>

    @POST("v1/localService")
    suspend fun createLocalService(@Body localServiceModel: LocalServiceModel): LocalServiceModel

    @PUT("v1/localService/{id}")
    suspend fun updateLocalService(
        @Path("id") id: String, // ou Long, se for compatível
        @Body localServiceModel: LocalServiceModel
    ): LocalServiceModel

    @GET("v1/localService/localId/{localId}")
    suspend fun getAllLocalServiceByLocal(@Path("localId") localTypeId: String): List<LocalServiceModel>

    @GET("v1/localService/reference")
    suspend fun getAllLocalByReference(@Query("value") reference: String): LocalServiceModel

    @GET("v1/localService/{id}")
    suspend fun getLocalById(@Path("id") id: String): LocalServiceModel
}*/
class LocalServiceDataSourceImpl: LocalServiceDataSource {
    /*private val api: LocalServiceApi by lazy {
        ApiClientEaonde.createService(LocalServiceApi::class.java)
    }*/
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createLocalService(localServiceModel: LocalServiceModel): LocalServiceModel {

            return try {
                client.from("tb_local_service")
                    .insert(localServiceModel)

                /*client.from("tb_local_service")
                    .select {
                        order("id", Order.DESCENDING)
                        limit(1)
                    }*/
                    .decodeSingle<LocalServiceModel>()
            } catch (e: Exception) {
                println("Erro: ${e.message}")
                throw  e
            }

    }

    override suspend fun getAllLocalService(): List<LocalServiceModel> {
        return try {
            client.from("tb_local_service")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalServiceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllLocalServiceByLocal(value: String): List<LocalServiceModel> {
        return try {
            client.from("tb_local_service")
                .select {
                    filter {
                        eq("local_id",value.toLong())
                    }
                }
                .decodeList<LocalServiceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun updateLocalService(localServiceModel: LocalServiceModel): LocalServiceModel {
        return try {
            client.from("tb_local_service")
                .update(localServiceModel)

                /*client.from("tb_local_service")
                    .select {
                        order("id", Order.DESCENDING)
                        limit(1)
                    }*/
                .decodeSingle<LocalServiceModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }

    }
}