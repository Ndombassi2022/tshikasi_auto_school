package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalByLocalTypeUseCaseImpl @Inject constructor(
    private val localRepository: LocalRepository
) : GetAllLocalByLocalTypeUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<LocalModel>> {
        return localRepository.getAllLocalByLocalType(value = value)
    }

}