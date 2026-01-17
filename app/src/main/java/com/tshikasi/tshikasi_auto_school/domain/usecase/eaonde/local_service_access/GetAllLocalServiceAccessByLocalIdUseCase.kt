package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllLocalServiceAccessByLocalIdUseCase {
    suspend operator fun invoke(localId : String):Either<NetworkError, List<LocalServiceAccessModel>>
}