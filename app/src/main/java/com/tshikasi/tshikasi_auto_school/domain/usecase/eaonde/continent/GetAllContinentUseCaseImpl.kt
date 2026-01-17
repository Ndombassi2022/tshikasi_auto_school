package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.continent

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ContinentRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllContinentUseCaseImpl @Inject constructor(
    private val continentRepository: ContinentRepository
): GetAllContinentUseCase {
    override suspend fun invoke(): Either<NetworkError, List<ContinentModel>> {
        return continentRepository.getAllContinent()
    }
}