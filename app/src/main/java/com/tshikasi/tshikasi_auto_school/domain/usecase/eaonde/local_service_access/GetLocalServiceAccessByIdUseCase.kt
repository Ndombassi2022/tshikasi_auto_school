package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetLocalServiceAccessByIdUseCase {
    suspend operator fun invoke(id: String):Either<NetworkError, LocalServiceAccessModel>
}