package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceAccessTypeDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel

class LocalServiceAccessTypeDataSourceImpl: LocalServiceAccessTypeDataSource {
    /* val db = Firebase.firestore
    override suspend fun createLocalServiceAccessType(localServiceAccessTypeModel: LocalServiceAccessTypeModel): LocalServiceAccessTypeModel {
        return suspendCoroutine { continuation ->
            db.collection("tb_local_service_access_type")
                .document(localServiceAccessTypeModel.id)
                .set(localServiceAccessTypeModel)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(localServiceAccessTypeModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getAllLocalServiceAccessType(): List<LocalServiceAccessTypeModel> {
        return suspendCoroutine { continuation ->
            val localServiceAccessTypeReference = db.collection("tb_local_service_access_type")
            localServiceAccessTypeReference.get().addOnSuccessListener { documents->
                val localServiceAccessTypeList = mutableListOf<LocalServiceAccessTypeModel>()
                for(document in documents){
                    document.toObject(LocalServiceAccessTypeModel::class.java).run {
                        localServiceAccessTypeList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localServiceAccessTypeList))
            }
            localServiceAccessTypeReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getLocalServiceAccessTypeById(id: String): LocalServiceAccessTypeModel {
        return suspendCoroutine { continuation ->
            val localServiceAccessTypeReference = db.collection("tb_local_service_access_type").whereEqualTo("id", id)
            localServiceAccessTypeReference.get().addOnSuccessListener { documents->
                val localServiceAccessTypeModel = mutableStateOf(LocalServiceAccessTypeModel())
                for(document in documents){
                    document.toObject(LocalServiceAccessTypeModel::class.java).run {
                        localServiceAccessTypeModel.value = this
                    }
                }
                continuation.resumeWith(Result.success(localServiceAccessTypeModel.value))
            }
            localServiceAccessTypeReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }*/
    override suspend fun createLocalServiceAccessType(localServiceAccessTypeModel: LocalServiceAccessTypeModel): LocalServiceAccessTypeModel {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocalServiceAccessType(): List<LocalServiceAccessTypeModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalServiceAccessTypeById(id: String): LocalServiceAccessTypeModel {
        TODO("Not yet implemented")
    }
}