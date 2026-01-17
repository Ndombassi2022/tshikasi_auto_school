package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.MunicipalityDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.MunicipalityRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class MunicipalityRepositoryImpl @Inject constructor(
    private val municipalityDataSource : MunicipalityDataSource
) : MunicipalityRepository {
    override suspend fun createMunicipality(municipalityModel: MunicipalityModel): Either<NetworkError, MunicipalityModel> {
        return Either.catch {
            municipalityDataSource.createMunicipality(municipalityModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllMunicipality(): Either<NetworkError, List<MunicipalityModel>> {
        return Either.catch {
            municipalityDataSource.getAllMunicipality()
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllMunicipalityByProvince(value: String): Either<NetworkError, List<MunicipalityModel>> {
        return Either.catch {
            municipalityDataSource.getAllMunicipalityByProvince(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getMunicipalityBy(value: String): Either<NetworkError, MunicipalityModel> {
        return Either.catch {
            municipalityDataSource.getMunicipalityBy(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllMunicipalityByAny(value: String): Either<NetworkError, List<MunicipalityModel>> {
        return Either.catch {
            municipalityDataSource.getAllMunicipalityByAny(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getMunicipalityById(id: String): Either<NetworkError, MunicipalityModel> {
        return Either.catch {
            municipalityDataSource.getMunicipalityById(id = id)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}