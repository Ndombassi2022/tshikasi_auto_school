package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LoginLocalDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LoginLocalRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class LoginLocalRepositoryImpl @Inject constructor(
        private val loginLocalDataSource: LoginLocalDataSource
) : LoginLocalRepository {
    override suspend fun createLoginLocal(loginLocalModel: LoginLocalModel): Either<NetworkError, LoginLocalModel> {
        return Either.catch {
            loginLocalDataSource.createLoginLocal(loginLocalModel = loginLocalModel)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLoginLocalByReferenceAndUserNameAndPassword(
        reference: String,
        userName: String,
        password: String
    ): Either<NetworkError, LoginLocalModel>  {
     return Either.catch {
         loginLocalDataSource. getLoginLocalByReferenceAndUserNameAndPassword(reference = reference, userName = userName, password = password)
     }.mapLeft { it.toNetworkError() }
    }
}