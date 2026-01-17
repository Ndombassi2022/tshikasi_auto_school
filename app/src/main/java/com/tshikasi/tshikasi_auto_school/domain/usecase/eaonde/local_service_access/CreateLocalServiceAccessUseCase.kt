package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateLocalServiceAccessUseCase {
    suspend operator fun invoke(localServiceAccessModel: LocalServiceAccessModel):Either<NetworkError, LocalServiceAccessModel>
}