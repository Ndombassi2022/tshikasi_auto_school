package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.login_local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LoginLocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetLoginLocalByReferenceAndUserNameAndPasswordUseCaseImpl @Inject constructor(
        private val loginLocalRepository: LoginLocalRepository
) : GetLoginLocalByReferenceAndUserNameAndPasswordUseCase {
    override suspend fun invoke(
        reference: String,
        userName: String,
        password: String
    ): Either<NetworkError, LoginLocalModel> {
        return loginLocalRepository.getLoginLocalByReferenceAndUserNameAndPassword(reference = reference, userName = userName, password = password)
    }
}