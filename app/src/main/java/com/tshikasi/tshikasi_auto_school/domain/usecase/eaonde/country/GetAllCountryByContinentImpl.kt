package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CountryRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllCountryByContinentImpl @Inject constructor(
    private val countryRepository: CountryRepository
): GetAllCountryByContinent {
    override suspend fun invoke(value: String): Either<NetworkError, List<CountryModel>> {
        return countryRepository.getAllCountryByContinent(value = value)
    }

}