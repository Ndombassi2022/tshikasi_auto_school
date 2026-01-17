package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError


interface LocalRepository {
    suspend fun createLocal(localModel: LocalModel) : Either<NetworkError, LocalModel>
    suspend fun updateLocal(localModel: LocalModel) : Either<NetworkError, LocalModel>
    suspend fun updateLocalLocation(localModel: LocalModel) : Either<NetworkError, LocalModel>
    suspend fun getAllLocal() : Either<NetworkError, List<LocalModel>>
    suspend fun getAllLocalByLocalType(value: String) : Either<NetworkError, List<LocalModel>>
    suspend fun getAllLocalByReference(value: String) : Either<NetworkError, LocalModel>
    suspend fun getAllLocalByAny(value: String) : Either<NetworkError, List<LocalModel>>
    suspend fun getAllLocalByLocalTypeIdByAny(localTypeId:String,value: String) : Either<NetworkError, List<LocalModel>>
    suspend fun getLocalById(id: String) : Either<NetworkError, LocalModel>
}