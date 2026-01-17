package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.province

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ProvinceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllProvinceUseCaseImpl @Inject constructor(
    private val provinceRepository: ProvinceRepository
): GetAllProvinceUseCase {
    override suspend fun invoke(): Either<NetworkError, List<ProvinceModel>> {
        return  provinceRepository.getAllProvince()
    }
}