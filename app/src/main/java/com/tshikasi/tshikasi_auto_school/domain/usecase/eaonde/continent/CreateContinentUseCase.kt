package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateContinentUseCase {
    suspend operator fun invoke(continentModel: ContinentModel):Either<NetworkError, ContinentModel>
}