package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CountryRepository {
    suspend fun createCountry(countryModel: CountryModel) : Either<NetworkError, CountryModel>
    suspend fun getAllCountry() : Either<NetworkError, List<CountryModel>>
    suspend fun getAllCountryByContinent(value: String) : Either<NetworkError, List<CountryModel>>
    suspend fun getAllCountryByAny(value: String) : Either<NetworkError, List<CountryModel>>
    suspend fun getCountryById(id: String) : Either<NetworkError, CountryModel>
}