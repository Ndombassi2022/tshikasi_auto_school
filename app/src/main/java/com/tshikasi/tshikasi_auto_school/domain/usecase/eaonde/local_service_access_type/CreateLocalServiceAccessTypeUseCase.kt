package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateLocalServiceAccessTypeUseCase {
    suspend operator fun invoke(localServiceAccessTypeModel: LocalServiceAccessTypeModel):Either<NetworkError, LocalServiceAccessTypeModel>
}