package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.province

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ProvinceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllProvinceByCountryUseCaseImpl @Inject constructor(
    private val provinceRepository: ProvinceRepository
): GetAllProvinceByCountryUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<ProvinceModel>> {
        return provinceRepository.getAllProvinceByCountry(value = value)
    }
}