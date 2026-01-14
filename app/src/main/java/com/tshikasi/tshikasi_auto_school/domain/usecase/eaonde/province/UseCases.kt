package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.province



import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.ProvinceRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class UseCases {
}
interface CreateProvinceUseCase {
    suspend operator fun invoke(provinceModel: ProvinceModel):Either<NetworkError, ProvinceModel>
}

class CreateProvinceUseCaseImpl @Inject constructor(
    private val provinceRepository: ProvinceRepository
) : CreateProvinceUseCase {
    override suspend fun invoke(provinceModel: ProvinceModel): Either<NetworkError, ProvinceModel> {
        return provinceRepository.createProvince(provinceModel)
    }
}


interface GetAllProvinceByAnyUseCase {
    suspend operator fun invoke(value: String): Either<NetworkError, List<ProvinceModel>>
}

class GetAllProvinceByAnyUseCaseImpl @Inject constructor(
    private  val provinceRepository: ProvinceRepository
) : GetAllProvinceByAnyUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<ProvinceModel>> {
        return provinceRepository.getAllProvinceByAny(value)
    }
}

interface GetAllProvinceByCountryUseCase {
    suspend operator fun invoke(value: String):Either<NetworkError, List<ProvinceModel>>
}

class GetAllProvinceByCountryUseCaseImpl @Inject constructor(
    private val provinceRepository: ProvinceRepository
): GetAllProvinceByCountryUseCase {
    override suspend fun invoke(value: String): Either<NetworkError, List<ProvinceModel>> {
        return provinceRepository.getAllProvinceByCountry(value = value)
    }
}


interface GetAllProvinceUseCase {
    suspend operator fun invoke():Either<NetworkError, List<ProvinceModel>>
}

class GetAllProvinceUseCaseImpl @Inject constructor(
    private val provinceRepository: ProvinceRepository
): GetAllProvinceUseCase {
    override suspend fun invoke(): Either<NetworkError, List<ProvinceModel>> {
        return  provinceRepository.getAllProvince()
    }
}