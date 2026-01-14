package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.commune
import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CommuneRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject
class UseCases {
}

interface CreateCommuneUseCase {
    suspend operator fun  invoke(communeModel: CommuneModel): Either<NetworkError, CommuneModel>
}

class CreateCommuneUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
): CreateCommuneUseCase {
    override suspend fun invoke(communeModel: CommuneModel): Either<NetworkError, CommuneModel> {
        return  communeRepository.createCommune(communeModel = communeModel)
    }
}

interface GetAllCommuneByAnyUseCase {
    suspend operator fun invoke(value : String):Either<NetworkError, List<CommuneModel>>
}

class GetAllCommuneByAnyUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
) : GetAllCommuneByAnyUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<CommuneModel>> {
        return communeRepository.getAllCommuneByAny(value = value)
    }
}

interface GetAllCommuneByMunicipalityIdUseCase {
    suspend operator fun  invoke(municipalityId: String):Either<NetworkError, List<CommuneModel>>
}

class GetAllCommuneByMunicipalityIdUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
): GetAllCommuneByMunicipalityIdUseCase {
    override suspend fun invoke(municipalityId: String): Either<NetworkError, List<CommuneModel>> {
        return communeRepository.getAllCommuneByMunicipalityId(municipalityId = municipalityId)
    }
}

interface GetAllCommuneUseCase {
    suspend operator fun  invoke():Either<NetworkError, List<CommuneModel>>
}

class GetAllCommuneUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
) : GetAllCommuneUseCase {
    override suspend fun invoke(): Either<NetworkError, List<CommuneModel>> {
        return  communeRepository.getAllCommune()
    }
}