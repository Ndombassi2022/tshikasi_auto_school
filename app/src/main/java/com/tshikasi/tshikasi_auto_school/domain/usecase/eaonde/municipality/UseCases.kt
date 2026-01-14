package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.municipality
import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.MunicipalityRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject
class UseCases {
}


interface CreateMunicipalityUseCase {
    suspend operator fun invoke(municipalityModel: MunicipalityModel):Either<NetworkError, MunicipalityModel>
}

class CreateMunicipalityUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
) : CreateMunicipalityUseCase {
    override suspend fun invoke(model: MunicipalityModel): Either<NetworkError, MunicipalityModel> {
        return municipalityRepository.createMunicipality(municipalityModel = model)
    }
}

interface GetAllMunicipalityByAnyUseCase {
    suspend operator fun invoke(value: String): Either<NetworkError, List<MunicipalityModel>>
}

class GetAllMunicipalityByAnyUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
) : GetAllMunicipalityByAnyUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<MunicipalityModel>> {
        return municipalityRepository.getAllMunicipalityByAny(value = value)
    }
}

interface GetAllMunicipalityByProvinceUseCase {
    suspend operator fun invoke(value: String):Either<NetworkError, List<MunicipalityModel>>
}

class GetAllMunicipalityByProvinceUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
): GetAllMunicipalityByProvinceUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<MunicipalityModel>> {
        return municipalityRepository.getAllMunicipalityByProvince(value = value)
    }
}

interface GetAllMunicipalityUseCase {
    suspend operator fun invoke():Either<NetworkError, List<MunicipalityModel>>
}

class GetAllMunicipalityUseCaseImpl @Inject constructor(
    private val municipalityRepository: MunicipalityRepository
) : GetAllMunicipalityUseCase {
    override suspend fun invoke(): Either<NetworkError, List<MunicipalityModel>> {
        return municipalityRepository.getAllMunicipality()
    }
}