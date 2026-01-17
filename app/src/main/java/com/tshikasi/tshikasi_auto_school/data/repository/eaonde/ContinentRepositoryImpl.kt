package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ContinentRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.ContinentDataSource
import javax.inject.Inject


class ContinentRepositoryImpl @Inject constructor(
    private val dataSource: ContinentDataSource
): ContinentRepository {
    override suspend fun createContinent(continentModel: ContinentModel): Either<NetworkError, ContinentModel> {
        return Either.catch {
            dataSource.createContinent(continentModel = continentModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllContinent(): Either<NetworkError, List<ContinentModel>> {
        return Either.catch {
            dataSource.getAllContinent()
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllContinentByAny(value: String): Either<NetworkError, List<ContinentModel>> {
        return Either.catch {
            dataSource.getAllContinentByAny(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getContinentById(id: String): Either<NetworkError, ContinentModel> {
        return Either.catch {
            dataSource.getContinentById(id=id)
        }.mapLeft {
            it.toNetworkError()
        }
    }


}