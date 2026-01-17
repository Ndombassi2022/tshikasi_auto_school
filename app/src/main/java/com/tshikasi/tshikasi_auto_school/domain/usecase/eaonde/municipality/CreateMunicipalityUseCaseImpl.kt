package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.municipality

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.MunicipalityRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateMunicipalityUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
) : CreateMunicipalityUseCase {
    override suspend fun invoke(model: MunicipalityModel): Either<NetworkError, MunicipalityModel> {
        return municipalityRepository.createMunicipality(municipalityModel = model)
    }
}