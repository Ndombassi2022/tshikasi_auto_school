package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

class UpdateLocalServiceUseCaseImpl(private val localServiceRepository: LocalServiceRepository) :
    UpdateLocalServiceUseCase {
    override suspend fun invoke(localServiceModel: LocalServiceModel): Either<NetworkError, LocalServiceModel> {
        return localServiceRepository.updateLocalService(localServiceModel = localServiceModel)
    }
}