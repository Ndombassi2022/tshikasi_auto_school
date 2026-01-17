package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllContinentUseCase {
    suspend  operator fun invoke(): Either<NetworkError, List<ContinentModel>>
}