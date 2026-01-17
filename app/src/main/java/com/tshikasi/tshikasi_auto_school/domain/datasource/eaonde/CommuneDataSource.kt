package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel

interface CommuneDataSource {
    suspend fun createCommune(communeModel: CommuneModel) : CommuneModel
    suspend fun getAllCommune() : List<CommuneModel>
    suspend fun getAllCommuneByAny(value: String) : List<CommuneModel>
    suspend fun getAllCommuneByMunicipalityId(municipalityId: String) : List<CommuneModel>
    suspend fun getCommuneById(id: String) :  CommuneModel
}