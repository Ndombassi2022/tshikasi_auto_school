package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetLocalServiceAccessTypeByIdUseCaseImpl @Inject constructor(
    private val localServiceAccessTypeRepository: LocalServiceAccessTypeRepository
) : GetLocalServiceAccessTypeByIdUseCase {
    override suspend fun invoke(id: String): Either<NetworkError, LocalServiceAccessTypeModel> {
        return  localServiceAccessTypeRepository.getLocalServiceAccessTypeById(id = id)
    }
}