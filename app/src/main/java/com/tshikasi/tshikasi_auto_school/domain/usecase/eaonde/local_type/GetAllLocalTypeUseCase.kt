package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllLocalTypeUseCase {
    suspend operator fun invoke():Either<NetworkError, List<LocalTypeModel>>
}