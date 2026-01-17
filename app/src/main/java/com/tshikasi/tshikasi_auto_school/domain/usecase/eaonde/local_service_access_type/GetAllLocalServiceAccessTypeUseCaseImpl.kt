package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalServiceAccessTypeUseCaseImpl @Inject constructor(
    private val localServiceAccessTypeRepository: LocalServiceAccessTypeRepository
) : GetAllLocalServiceAccessTypeUseCase
{
    override suspend fun invoke(): Either<NetworkError, List<LocalServiceAccessTypeModel>> {
        return localServiceAccessTypeRepository.getAllLocalServiceAccessType()
    }
}