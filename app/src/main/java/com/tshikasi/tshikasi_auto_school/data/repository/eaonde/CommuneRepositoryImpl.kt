package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.CommuneDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CommuneRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CommuneRepositoryImpl @Inject constructor(
    private val communeDataSource: CommuneDataSource
): CommuneRepository {
    override suspend fun createCommune(communeModel: CommuneModel): Either<NetworkError, CommuneModel> {
        return  Either.catch {
            communeDataSource.createCommune(communeModel = communeModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllCommune(): Either<NetworkError, List<CommuneModel>> {
        return  Either.catch {
            communeDataSource.getAllCommune()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllCommuneByAny(value: String): Either<NetworkError, List<CommuneModel>> {
        return  Either.catch {
            communeDataSource.getAllCommuneByAny(value = value)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllCommuneByMunicipalityId(municipalityId: String): Either<NetworkError, List<CommuneModel>> {
        return  Either.catch {
            communeDataSource.getAllCommuneByMunicipalityId(municipalityId = municipalityId)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCommuneById(id: String): Either<NetworkError, CommuneModel> {
        return  Either.catch {
            communeDataSource.getCommuneById(id = id)
        }.mapLeft { it.toNetworkError() }
    }
}