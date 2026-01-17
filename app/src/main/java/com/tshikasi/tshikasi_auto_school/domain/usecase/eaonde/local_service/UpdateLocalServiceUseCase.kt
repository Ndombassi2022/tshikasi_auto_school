package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface UpdateLocalServiceUseCase {
    suspend operator fun  invoke(localServiceModel: LocalServiceModel):Either<NetworkError, LocalServiceModel>
}