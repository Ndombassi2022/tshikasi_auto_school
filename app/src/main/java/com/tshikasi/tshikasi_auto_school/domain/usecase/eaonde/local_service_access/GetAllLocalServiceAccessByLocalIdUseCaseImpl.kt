package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_service_access

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalServiceAccessRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalServiceAccessByLocalIdUseCaseImpl @Inject constructor(
    private val localServiceAccessRepository: LocalServiceAccessRepository
) : GetAllLocalServiceAccessByLocalIdUseCase {
    override suspend fun invoke(localId: String): Either<NetworkError, List<LocalServiceAccessModel>> {
        return localServiceAccessRepository.getAllLocalServiceAccessByLocalId(localId = localId)
    }
}