package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalServiceDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class LocalServiceRepositoryImpl @Inject constructor(
    private val localServiceDataSource: LocalServiceDataSource
) : LocalServiceRepository {
    override suspend fun createLocalService(localServiceModel: LocalServiceModel): Either<NetworkError, LocalServiceModel> {
        return Either.catch {
            localServiceDataSource.createLocalService(localServiceModel = localServiceModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllLocalService(): Either<NetworkError, List<LocalServiceModel>> {
        return Either.catch {
            localServiceDataSource.getAllLocalService()
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun updateLocalService(localServiceModel: LocalServiceModel): Either<NetworkError, LocalServiceModel> {
        return Either.catch {
            localServiceDataSource.updateLocalService(localServiceModel = localServiceModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllLocalServiceByLocalDataSource(value: String): Either<NetworkError, List<LocalServiceModel>> {
        return Either.catch {
            localServiceDataSource.getAllLocalServiceByLocal(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}