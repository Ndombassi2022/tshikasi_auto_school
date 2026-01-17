package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalServiceByLocalUseCaseImpl @Inject constructor(
    private val localServiceRepository: LocalServiceRepository
): GetAllLocalServiceByLocalUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<LocalServiceModel>> {
        return  localServiceRepository.getAllLocalServiceByLocalDataSource(value= value)
    }
}