package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalTypeDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject


class LocalTypeRepositoryImpl @Inject constructor(
    private val datasource: LocalTypeDataSource
): LocalTypeRepository {
    override suspend fun getAllLocalType(): Either<NetworkError, List<LocalTypeModel>> {
        return Either.catch {
            datasource.getAllLocalType()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLocalTypeByAny(value: String,limit:Int, ): Either<NetworkError, List<LocalTypeModel>> {
        return Either.catch {
            datasource.getAllLocalTypeByAny(value=value, limit=limit)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun createLocalType(localTypeModel: LocalTypeModel): Either<NetworkError, LocalTypeModel> {
        return Either.catch {
            datasource.createLocalType( localTypeModel = localTypeModel)
        }.mapLeft { it.toNetworkError() }
    }


}