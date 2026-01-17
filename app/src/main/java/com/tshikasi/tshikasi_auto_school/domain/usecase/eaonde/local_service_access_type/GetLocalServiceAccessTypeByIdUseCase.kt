package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetLocalServiceAccessTypeByIdUseCase {
    suspend operator fun invoke(id:String):Either<NetworkError, LocalServiceAccessTypeModel>
}