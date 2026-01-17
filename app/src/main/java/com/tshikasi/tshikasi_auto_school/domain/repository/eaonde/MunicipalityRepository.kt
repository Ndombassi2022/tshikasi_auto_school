package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface MunicipalityRepository {
    suspend fun createMunicipality(municipalityModel: MunicipalityModel) : Either<NetworkError, MunicipalityModel>
    suspend fun getAllMunicipality() : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getAllMunicipalityByProvince(value: String) : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getMunicipalityBy(value: String) : Either<NetworkError, MunicipalityModel>
    suspend fun getAllMunicipalityByAny(value: String) : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getMunicipalityById(id: String) : Either<NetworkError, MunicipalityModel>
}