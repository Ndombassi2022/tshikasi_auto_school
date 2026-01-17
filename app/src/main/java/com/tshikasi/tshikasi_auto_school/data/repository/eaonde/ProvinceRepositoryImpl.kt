package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.ProvinceDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ProvinceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class ProvinceRepositoryImpl @Inject constructor(
    private val provinceDataSource: ProvinceDataSource
): ProvinceRepository {
    override suspend fun createProvince(provinceModel: ProvinceModel): Either<NetworkError, ProvinceModel> {
        return Either.catch {
            provinceDataSource.createProvince(provinceModel = provinceModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllProvince(): Either<NetworkError, List<ProvinceModel>> {
        return Either.catch {
            provinceDataSource.getAllProvince()
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllProvinceByCountry(value: String): Either<NetworkError, List<ProvinceModel>> {
        return Either.catch {
            provinceDataSource.getAllProvinceByCountry(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getProvinceBy(value: String): Either<NetworkError, ProvinceModel> {
        return Either.catch {
            provinceDataSource.getProvinceBy(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllProvinceByAny(value: String): Either<NetworkError, List<ProvinceModel>> {
        return Either.catch {
            provinceDataSource.getAllProvinceByAny(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getProvinceById(id: String): Either<NetworkError, ProvinceModel> {
        return Either.catch {
            provinceDataSource.getProvinceById(id = id)
        }.mapLeft {
            it.toNetworkError()
        }
    }

}