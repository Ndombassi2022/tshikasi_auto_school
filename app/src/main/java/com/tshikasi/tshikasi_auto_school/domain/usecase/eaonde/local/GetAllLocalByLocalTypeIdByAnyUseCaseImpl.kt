package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalByLocalTypeIdByAnyUseCaseImpl @Inject constructor(
    private val localRepository: LocalRepository
) : GetAllLocalByLocalTypeIdByAnyUseCase {
    override suspend fun invoke(
        localTypeId: String,
        value: String
    ): Either<NetworkError, List<LocalModel>> {
        return localRepository.getAllLocalByLocalTypeIdByAny(localTypeId = localTypeId, value = value)
    }
}