package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceAccessDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel

class LocalServiceAccessDataSourceImpl: LocalServiceAccessDataSource {
    /*val db = Firebase.firestore
    override suspend fun createLocalServiceAccess(localServiceAccessModel: LocalServiceAccessModel): LocalServiceAccessModel {
        return suspendCoroutine { continuation ->
            db.collection("tb_local_service_access")
                .document(localServiceAccessModel.id)
                .set(localServiceAccessModel)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(localServiceAccessModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun getAllLocalServiceAccess(): List<LocalServiceAccessModel> {
        return suspendCoroutine { continuation ->
            val localServiceAccessReference = db.collection("tb_local_service_access")
            localServiceAccessReference.get().addOnSuccessListener { documents->
                val localServiceAccessList = mutableListOf<LocalServiceAccessModel>()
                for(document in documents){
                    document.toObject(LocalServiceAccessModel::class.java).run {
                        localServiceAccessList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localServiceAccessList))
            }
            localServiceAccessReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllLocalServiceAccessByLocalId(localId: String): List<LocalServiceAccessModel> {
        return suspendCoroutine { continuation ->
            val localServiceAccessReference = db.collection("tb_local_service_access").whereEqualTo("local.id",localId)
            localServiceAccessReference.get().addOnSuccessListener { documents->
                val localServiceAccessList = mutableListOf<LocalServiceAccessModel>()
                for(document in documents){
                    document.toObject(LocalServiceAccessModel::class.java).run {
                        localServiceAccessList.add(this)
                    }
                }
                continuation.resumeWith(Result.success(localServiceAccessList))
            }
            localServiceAccessReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun getAllLocalServiceAccessById(id: String): LocalServiceAccessModel {
        return suspendCoroutine { continuation ->
            val localServiceAccessReference = db.collection("tb_local_service_access").whereEqualTo("id", id)
            localServiceAccessReference.get().addOnSuccessListener { documents->
                val localServiceAccessModel = mutableStateOf(LocalServiceAccessModel())
                for(document in documents){
                    document.toObject(LocalServiceAccessModel::class.java).run {
                        localServiceAccessModel.value = this
                    }
                }
                continuation.resumeWith(Result.success(localServiceAccessModel.value))
            }
            localServiceAccessReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }*/
    override suspend fun createLocalServiceAccess(localServiceAccessModel: LocalServiceAccessModel): LocalServiceAccessModel {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocalServiceAccess(): List<LocalServiceAccessModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocalServiceAccessByLocalId(localId: String): List<LocalServiceAccessModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocalServiceAccessById(id: String): LocalServiceAccessModel {
        TODO("Not yet implemented")
    }
}