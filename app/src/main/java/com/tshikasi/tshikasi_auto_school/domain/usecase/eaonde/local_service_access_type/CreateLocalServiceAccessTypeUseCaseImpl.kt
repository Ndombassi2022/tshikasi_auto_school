package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateLocalServiceAccessTypeUseCaseImpl @Inject constructor(
    private val localServiceAccessTypeRepository: LocalServiceAccessTypeRepository
) : CreateLocalServiceAccessTypeUseCase {
    override suspend fun invoke(localServiceAccessTypeModel: LocalServiceAccessTypeModel): Either<NetworkError, LocalServiceAccessTypeModel> {
        return  localServiceAccessTypeRepository.createLocalServiceAccessType(localServiceAccessTypeModel = localServiceAccessTypeModel)
    }
}