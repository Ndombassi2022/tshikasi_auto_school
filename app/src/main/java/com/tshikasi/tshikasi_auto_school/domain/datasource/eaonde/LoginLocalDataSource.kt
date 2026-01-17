package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel

interface LoginLocalDataSource {
    suspend fun createLoginLocal(loginLocalModel: LoginLocalModel) : LoginLocalModel
    suspend fun getLoginLocalByReferenceAndUserNameAndPassword(reference: String, userName : String, password:String) :  LoginLocalModel
}