package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CommuneRepository {
    suspend fun createCommune(communeModel: CommuneModel) : Either<NetworkError, CommuneModel>
    suspend fun getAllCommune() : Either<NetworkError, List<CommuneModel>>
    suspend fun getAllCommuneByAny(value: String) : Either<NetworkError, List<CommuneModel>>
    suspend fun getAllCommuneByMunicipalityId(municipalityId: String) : Either<NetworkError, List<CommuneModel>>
    suspend fun getCommuneById(id: String) : Either<NetworkError, CommuneModel>
}