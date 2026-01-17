package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceAccessTypeDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class LocalServiceAccessTypeRepositoryImpl @Inject constructor(
    private val localServiceAccessTypeDataSource: LocalServiceAccessTypeDataSource
) : LocalServiceAccessTypeRepository {
    override suspend fun createLocalServiceAccessType(localServiceAccessTypeModel: LocalServiceAccessTypeModel): Either<NetworkError, LocalServiceAccessTypeModel> {
        return Either.catch {
            localServiceAccessTypeDataSource.createLocalServiceAccessType(localServiceAccessTypeModel = localServiceAccessTypeModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocalServiceAccessType(): Either<NetworkError, List<LocalServiceAccessTypeModel>> {
        return Either.catch {
            localServiceAccessTypeDataSource.getAllLocalServiceAccessType()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLocalServiceAccessTypeById(id: String): Either<NetworkError, LocalServiceAccessTypeModel> {
        return Either.catch {
            localServiceAccessTypeDataSource.getLocalServiceAccessTypeById(id = id)
        }.mapLeft { it.toNetworkError() }
    }
}