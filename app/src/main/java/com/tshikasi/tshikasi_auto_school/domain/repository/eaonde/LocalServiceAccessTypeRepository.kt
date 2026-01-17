package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface LocalServiceAccessTypeRepository {
    suspend  fun  createLocalServiceAccessType(localServiceAccessTypeModel: LocalServiceAccessTypeModel): Either<NetworkError, LocalServiceAccessTypeModel>
    suspend fun getAllLocalServiceAccessType(): Either<NetworkError, List<LocalServiceAccessTypeModel>>
    suspend fun getLocalServiceAccessTypeById(id : String): Either<NetworkError, LocalServiceAccessTypeModel>
}