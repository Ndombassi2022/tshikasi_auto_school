package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface ContinentRepository {
    suspend fun createContinent(continentModel: ContinentModel) : Either<NetworkError, ContinentModel>
    suspend fun getAllContinent() : Either<NetworkError, List<ContinentModel>>
    suspend fun getAllContinentByAny(value: String) :  Either<NetworkError, List<ContinentModel>>
    suspend fun getContinentById(id: String) :  Either<NetworkError, ContinentModel>

}