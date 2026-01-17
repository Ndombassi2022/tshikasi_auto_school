package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CountryRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllCountryUseCaseImpl @Inject constructor(
        private val countryRepository: CountryRepository
): GetAllCountryUseCase {
    override suspend fun invoke(): Either<NetworkError, List<CountryModel>> {
        return countryRepository.getAllCountry()
    }
}