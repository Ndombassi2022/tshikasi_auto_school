package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.login_local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetLoginLocalByReferenceAndUserNameAndPasswordUseCase {
    suspend operator fun invoke(reference:String, userName:String,password:String):Either<NetworkError, LoginLocalModel>
}