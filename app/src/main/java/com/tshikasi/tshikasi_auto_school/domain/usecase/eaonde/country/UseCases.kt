package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.country
import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CountryRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject
class UseCases {
}


interface CreateCountryUseCase {
    suspend operator fun invoke(countryModel: CountryModel): Either<NetworkError, CountryModel>
}

class CreateCountryUseCaseImpl @Inject constructor(
    private val countryRepository: CountryRepository
): CreateCountryUseCase {
    override suspend fun invoke(countryModel: CountryModel): Either<NetworkError, CountryModel> {
        return  countryRepository.createCountry(countryModel = countryModel)
    }
}

interface GetAllCountryByContinent {
    suspend operator fun  invoke(value: String):Either<NetworkError, List<CountryModel>>
}

class GetAllCountryByContinentImpl @Inject constructor(
    private val countryRepository: CountryRepository
): GetAllCountryByContinent {
    override suspend fun invoke(value: String): Either<NetworkError, List<CountryModel>> {
        return countryRepository.getAllCountryByContinent(value = value)
    }

}

interface GetAllCountryUseCase {
    suspend operator fun invoke(): Either<NetworkError, List<CountryModel>>
}

class GetAllCountryUseCaseImpl @Inject constructor(
    private val countryRepository: CountryRepository
): GetAllCountryUseCase {
    override suspend fun invoke(): Either<NetworkError, List<CountryModel>> {
        return countryRepository.getAllCountry()
    }
}