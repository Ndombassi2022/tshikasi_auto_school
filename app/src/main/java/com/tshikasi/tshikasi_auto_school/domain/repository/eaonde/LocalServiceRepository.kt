package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface LocalServiceRepository{
    suspend  fun  createLocalService(localServiceModel: LocalServiceModel): Either<NetworkError, LocalServiceModel>
    suspend fun getAllLocalService():Either<NetworkError, List<LocalServiceModel>>
    suspend fun updateLocalService(localServiceModel: LocalServiceModel):Either<NetworkError, LocalServiceModel>
    suspend fun getAllLocalServiceByLocalDataSource(value : String):Either<NetworkError, List<LocalServiceModel>>

}