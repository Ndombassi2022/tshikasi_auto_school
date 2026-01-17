package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class UpdateLocalUseCaseImpl @Inject constructor(
    private val localRepository: LocalRepository
) : UpdateLocalUseCase {
    override suspend fun invoke(localModel: LocalModel): Either<NetworkError, LocalModel> {
        return localRepository.updateLocal(localModel = localModel)
    }
}