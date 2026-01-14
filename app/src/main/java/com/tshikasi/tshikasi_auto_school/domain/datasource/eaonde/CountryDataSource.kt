package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel

interface CountryDataSource {
    suspend fun createCountry(countryModel: CountryModel) : CountryModel
    suspend fun getAllCountry() : List<CountryModel>
    suspend fun getAllCountryByContinent(value: String) : List<CountryModel>
    suspend fun getAllCountryByAny(value: String) : List<CountryModel>
    suspend fun getCountryById(id: String) : CountryModel
}