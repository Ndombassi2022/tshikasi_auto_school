package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ContinentRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateContinentUseCaseImpl @Inject constructor(
    private val repository: ContinentRepository
): CreateContinentUseCase
{
    override suspend fun invoke(continentModel: ContinentModel): Either<NetworkError, ContinentModel> {
        return repository.createContinent(continentModel = continentModel)
    }

}