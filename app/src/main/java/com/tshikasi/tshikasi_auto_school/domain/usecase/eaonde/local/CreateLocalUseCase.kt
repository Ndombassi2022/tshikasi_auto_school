package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateLocalUseCase {

    suspend operator fun invoke(localModel: LocalModel):Either<NetworkError, LocalModel>
}