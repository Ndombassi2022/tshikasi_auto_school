package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ContinentRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject
class UseCases {
}


interface CreateContinentUseCase {
    suspend operator fun invoke(continentModel: ContinentModel):Either<NetworkError, ContinentModel>
}

class CreateContinentUseCaseImpl @Inject constructor(
    private val repository: ContinentRepository
): CreateContinentUseCase
{
    override suspend fun invoke(continentModel: ContinentModel): Either<NetworkError, ContinentModel> {
        return repository.createContinent(continentModel = continentModel)
    }

}

interface GetAllContinentUseCase {
    suspend  operator fun invoke(): Either<NetworkError, List<ContinentModel>>
}

class GetAllContinentUseCaseImpl @Inject constructor(
    private val continentRepository: ContinentRepository
): GetAllContinentUseCase {
    override suspend fun invoke(): Either<NetworkError, List<ContinentModel>> {
        return continentRepository.getAllContinent()
    }
}