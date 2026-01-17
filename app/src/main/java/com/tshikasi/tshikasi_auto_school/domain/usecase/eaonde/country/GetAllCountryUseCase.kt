package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllCountryUseCase {
    suspend operator fun invoke(): Either<NetworkError, List<CountryModel>>
}