package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceAccessDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class LocalServiceAccessRepositoryImpl @Inject constructor(
    private val localServiceAccessDataSource: LocalServiceAccessDataSource
) : LocalServiceAccessRepository {
    override suspend fun createLocalServiceAccess(localServiceAccessModel: LocalServiceAccessModel): Either<NetworkError, LocalServiceAccessModel> {
        return Either.catch {
            localServiceAccessDataSource.createLocalServiceAccess(localServiceAccessModel = localServiceAccessModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocalServiceAccess(): Either<NetworkError, List<LocalServiceAccessModel>> {
        return Either.catch {
            localServiceAccessDataSource.getAllLocalServiceAccess()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocalServiceAccessByLocalId(localId: String): Either<NetworkError, List<LocalServiceAccessModel>> {
        return Either.catch {
            localServiceAccessDataSource.getAllLocalServiceAccessByLocalId(localId = localId)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLocalServiceAccessById(id: String): Either<NetworkError, LocalServiceAccessModel> {
        return Either.catch {
            localServiceAccessDataSource.getAllLocalServiceAccessById(id = id)
        }.mapLeft { it.toNetworkError() }
    }
}