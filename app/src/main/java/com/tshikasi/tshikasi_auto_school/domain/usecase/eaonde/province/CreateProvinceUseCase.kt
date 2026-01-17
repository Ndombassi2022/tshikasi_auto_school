package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.province

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateProvinceUseCase {
    suspend operator fun invoke(provinceModel: ProvinceModel):Either<NetworkError, ProvinceModel>
}