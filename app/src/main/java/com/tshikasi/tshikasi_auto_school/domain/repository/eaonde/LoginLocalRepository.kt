package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface LoginLocalRepository {
    suspend fun createLoginLocal(loginLocalModel: LoginLocalModel) : Either<NetworkError, LoginLocalModel>
    suspend fun  getLoginLocalByReferenceAndUserNameAndPassword(reference: String, userName : String, password:String) :Either<NetworkError, LoginLocalModel>
}