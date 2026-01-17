package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.province

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllProvinceUseCase {
    suspend operator fun invoke():Either<NetworkError, List<ProvinceModel>>
}