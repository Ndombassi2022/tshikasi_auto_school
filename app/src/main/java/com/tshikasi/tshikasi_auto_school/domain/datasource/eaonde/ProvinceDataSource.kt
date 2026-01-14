package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel

interface ProvinceDataSource {
    suspend fun createProvince(provinceModel: ProvinceModel) : ProvinceModel
    suspend fun getAllProvince() :  List<ProvinceModel>
    suspend fun getAllProvinceByCountry(value: String) :  List<ProvinceModel>
    suspend fun getProvinceBy(value: String) : ProvinceModel
    suspend fun getAllProvinceByAny(value: String) : List<ProvinceModel>
    suspend fun getProvinceById(id: String) : ProvinceModel
}