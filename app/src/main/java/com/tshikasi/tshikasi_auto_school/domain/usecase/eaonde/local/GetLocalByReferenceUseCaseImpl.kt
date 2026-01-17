package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetLocalByReferenceUseCaseImpl @Inject constructor(
    private val localRepository: LocalRepository
): GetLocalByReferenceUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, LocalModel> {
        return  localRepository.getAllLocalByReference(value = value)
    }
}