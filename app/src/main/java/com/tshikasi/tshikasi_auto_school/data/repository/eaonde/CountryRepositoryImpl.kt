package com.tshikasi.tshikasi_auto_school.data.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CountryRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.CountryDataSource
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val dataSource: CountryDataSource
): CountryRepository {
    override suspend fun createCountry(countryModel: CountryModel): Either<NetworkError, CountryModel> {
        return Either.catch {
            dataSource.createCountry(countryModel = countryModel)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllCountry(): Either<NetworkError, List<CountryModel>> {
        return Either.catch {
            dataSource.getAllCountry()
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllCountryByContinent(value: String): Either<NetworkError, List<CountryModel>> {
        return Either.catch {
            dataSource.getAllCountryByContinent(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllCountryByAny(value: String): Either<NetworkError, List<CountryModel>> {
        return Either.catch {
            dataSource.getAllCountryByAny(value = value)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getCountryById(id: String): Either<NetworkError, CountryModel> {
        return Either.catch {
            dataSource.getCountryById(id = id)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}