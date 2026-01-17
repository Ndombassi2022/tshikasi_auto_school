package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateLocalServiceUseCaseImpl @Inject constructor(
 private val localServiceRepository: LocalServiceRepository
): CreateLocalServiceUseCase {
    override suspend fun invoke(localServiceModel: LocalServiceModel): Either<NetworkError, LocalServiceModel> {
        return localServiceRepository.createLocalService(localServiceModel = localServiceModel)
    }
}