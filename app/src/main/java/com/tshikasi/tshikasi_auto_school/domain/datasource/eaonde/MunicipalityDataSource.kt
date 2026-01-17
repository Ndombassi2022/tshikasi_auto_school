package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel

interface MunicipalityDataSource {
    suspend fun createMunicipality(municipalityModel: MunicipalityModel) :  MunicipalityModel
    suspend fun getAllMunicipality() :  List<MunicipalityModel>
    suspend fun getAllMunicipalityByProvince(value: String) :  List<MunicipalityModel>
    suspend fun getMunicipalityBy(value: String) :  MunicipalityModel
    suspend fun getAllMunicipalityByAny(value: String) : List<MunicipalityModel>
    suspend fun getMunicipalityById(id: String) :  MunicipalityModel
}