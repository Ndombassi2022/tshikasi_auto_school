package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.municipality

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.MunicipalityRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllMunicipalityUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
) : GetAllMunicipalityUseCase {
    override suspend fun invoke(): Either<NetworkError, List<MunicipalityModel>> {
        return municipalityRepository.getAllMunicipality()
    }
}