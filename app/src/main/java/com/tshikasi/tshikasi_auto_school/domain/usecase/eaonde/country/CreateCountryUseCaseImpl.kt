package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CountryRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateCountryUseCaseImpl @Inject constructor(
    private val countryRepository: CountryRepository
): CreateCountryUseCase {
    override suspend fun invoke(countryModel: CountryModel): Either<NetworkError, CountryModel> {
      return  countryRepository.createCountry(countryModel = countryModel)
    }
}