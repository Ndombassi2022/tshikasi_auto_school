package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface LocalServiceAccessRepository {
    suspend  fun  createLocalServiceAccess(localServiceAccessModel: LocalServiceAccessModel): Either<NetworkError, LocalServiceAccessModel>
    suspend fun getAllLocalServiceAccess(): Either<NetworkError, List<LocalServiceAccessModel>>
    suspend fun getAllLocalServiceAccessByLocalId(localId : String): Either<NetworkError, List<LocalServiceAccessModel>>
    suspend fun getLocalServiceAccessById(id : String): Either<NetworkError, LocalServiceAccessModel>
}