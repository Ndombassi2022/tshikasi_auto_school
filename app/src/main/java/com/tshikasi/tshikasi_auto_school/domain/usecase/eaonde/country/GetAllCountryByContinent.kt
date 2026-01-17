package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllCountryByContinent {
    suspend operator fun  invoke(value: String):Either<NetworkError, List<CountryModel>>
}