package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.login_local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LoginLocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateLoginLocalUseCaseImpl @Inject constructor(
    private val loginLocalRepository: LoginLocalRepository
): CreateLoginLocalUseCase {
    override suspend fun invoke(loginLocalModel: LoginLocalModel): Either<NetworkError, LoginLocalModel> {
        return loginLocalRepository.createLoginLocal(loginLocalModel = loginLocalModel)
    }

}