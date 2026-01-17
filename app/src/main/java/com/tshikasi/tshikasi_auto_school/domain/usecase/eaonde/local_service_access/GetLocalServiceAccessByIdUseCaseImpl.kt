package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetLocalServiceAccessByIdUseCaseImpl @Inject constructor(
    private val localServiceAccessRepository: LocalServiceAccessRepository
) : GetLocalServiceAccessByIdUseCase {
    override suspend fun invoke(id: String): Either<NetworkError, LocalServiceAccessModel> {
        return  localServiceAccessRepository.getLocalServiceAccessById(id=id)
    }

}