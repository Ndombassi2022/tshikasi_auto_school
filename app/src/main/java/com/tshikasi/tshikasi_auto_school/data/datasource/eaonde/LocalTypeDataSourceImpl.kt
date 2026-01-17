package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde



import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalTypeDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

/*
class LocalTypeDataSourceImpl: LocalTypeDataSource {
    val db = Firebase.firestore
    //val firebaseFirestore= FirebaseFirestore.getInstance()
    //val firebaseStorage= FirebaseStorage.getInstance()
   // private val documentRefence = firebaseFirestore.document(
      //  "tb_local_type"
   // )
    override suspend fun getAllLocalType(): List<LocalTypeModel> {

        return  suspendCoroutine {continuation ->
            val localtypeReference = db.collection("tb_local_type").orderBy("description")
            localtypeReference.get().addOnSuccessListener {querySnapshot ->
                val localTypeList = mutableListOf<LocalTypeModel>()
                for (localType in querySnapshot){
                    localType.toObject(LocalTypeModel::class.java).run {
                        localTypeList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localTypeList))
            }
            localtypeReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllLocalTypeByAny(value: String,limit:Int, lastDocument: DocumentSnapshot?):List<LocalTypeModel> {
        return  suspendCoroutine {continuation ->
            val localtypeReference = db.collection("tb_local_type")
                .whereGreaterThanOrEqualTo("descriptionLowercase", value.lowercase())
                .whereLessThanOrEqualTo("descriptionLowercase", value.lowercase() + "\uf8ff")

            localtypeReference.get().addOnSuccessListener {querySnapshot ->
                val localTypeList = mutableListOf<LocalTypeModel>()
                for (localType in querySnapshot){
                    localType.toObject(LocalTypeModel::class.java).run {
                        localTypeList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localTypeList))
            }
            localtypeReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun createLocalType(localTypeModel: LocalTypeModel): LocalTypeModel{
        return suspendCoroutine { continuation ->
            db.collection("tb_localType")
                .document(localTypeModel.id)
                .set(localTypeModel)
                .addOnSuccessListener {
                   continuation.resumeWith(Result.success(localTypeModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }
}

 */
/*
// API Retrofit
interface LocalTypeApi {
    @GET("v1/localType")
    suspend fun getAllLocalTypes(): List<LocalTypeModel>

    @GET("v1/localType/any/{value}")
    suspend fun getAllLocalTypesByAny(
        @Path("value") value: String
    ): List<LocalTypeModel>

    @POST("v1/localType")
    suspend fun createLocalType(@Body localTypeModel: LocalTypeModel): LocalTypeModel
}*/


class LocalTypeDataSourceImpl: LocalTypeDataSource {

    private val client = TshikasiAutoSchool.supabase

    override suspend fun getAllLocalType(): List<LocalTypeModel> {
        return try {
            client.from("tb_local_type")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalTypeModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllLocalTypeByAny(
        value: String,
        limit: Int,
    ): List<LocalTypeModel> {
        return try {
            client.from("tb_local_type")
                .select {
                    filter {
                        or {
                            ilike("description", "%$value%")
                            eq("code", value)
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalTypeModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }


    override suspend fun createLocalType(localTypeModel: LocalTypeModel): LocalTypeModel {
        return try {
            client.from("tb_local_type")
                .insert(localTypeModel)
                .decodeSingle<LocalTypeModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }
}