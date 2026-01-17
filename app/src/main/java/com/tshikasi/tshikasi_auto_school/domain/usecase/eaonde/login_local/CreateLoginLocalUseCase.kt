package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.login_local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateLoginLocalUseCase {
    suspend operator fun invoke(loginLocalModel: LoginLocalModel): Either<NetworkError, LoginLocalModel>
}