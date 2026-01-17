package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject


class LocalRepositoryImpl @Inject constructor(
    private val datasource: LocalDataSource
): LocalRepository {
    override suspend fun createLocal(localModel: LocalModel): Either<NetworkError, LocalModel> {
       return Either.catch {
           datasource.createLocal(localModel = localModel)
       }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLocal(localModel: LocalModel): Either<NetworkError, LocalModel> {
        return Either.catch {
            datasource.updateLocal(localModel = localModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLocalLocation(localModel: LocalModel): Either<NetworkError, LocalModel> {
        return Either.catch {
            datasource.updateLocalLocation(localModel = localModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocal(): Either<NetworkError, List<LocalModel>> {
        return Either.catch {
            datasource.getAllLocal()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocalByLocalType(value: String): Either<NetworkError, List<LocalModel>> {
       return Either.catch {
           datasource.getAllLocalByLocalType(value = value)
       }.mapLeft {
           it.toNetworkError()
       }
    }

    override suspend fun getAllLocalByReference(value: String): Either<NetworkError, LocalModel> {
        return Either.catch {
            datasource.getAllLocalByReference(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllLocalByAny(value: String): Either<NetworkError, List<LocalModel>> {
        return Either.catch {
            datasource.getAllLocalByAny(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllLocalByLocalTypeIdByAny(
        localTypeId: String,
        value: String
    ): Either<NetworkError, List<LocalModel>> {
        return Either.catch {
            datasource.getAllLocalByLocalTypeIdByAny(localTypeId = localTypeId, value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getLocalById(id: String): Either<NetworkError, LocalModel> {
        return Either.catch {
            datasource.getLocalById(id=id)
        }.mapLeft {
            it.toNetworkError()
        }
    }


}