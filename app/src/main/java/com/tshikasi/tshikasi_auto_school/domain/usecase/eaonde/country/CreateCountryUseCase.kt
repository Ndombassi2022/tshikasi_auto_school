package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateCountryUseCase {
    suspend operator fun invoke(countryModel: CountryModel): Either<NetworkError, CountryModel>
}