package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface ProvinceRepository {
    suspend fun createProvince(provinceModel: ProvinceModel) : Either<NetworkError, ProvinceModel>
    suspend fun getAllProvince() : Either<NetworkError, List<ProvinceModel>>
    suspend fun getAllProvinceByCountry(value: String) : Either<NetworkError, List<ProvinceModel>>
    suspend fun getProvinceBy(value: String) : Either<NetworkError, ProvinceModel>
    suspend fun getAllProvinceByAny(value: String) : Either<NetworkError, List<ProvinceModel>>
    suspend fun getProvinceById(id: String) : Either<NetworkError, ProvinceModel>
}