package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel


interface ContinentDataSource {
    suspend fun createContinent(continentModel: ContinentModel) : ContinentModel
    suspend fun getAllContinent() : List<ContinentModel>
    suspend fun getAllContinentByAny(value: String) : List<ContinentModel>
    suspend fun getContinentById(id: String) : ContinentModel
}