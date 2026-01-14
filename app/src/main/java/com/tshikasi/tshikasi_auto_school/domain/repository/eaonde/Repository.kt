package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde
import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ProvinceModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError


class Repository {
}


//Continent Repository
interface ContinentRepository {
    suspend fun createContinent(continentModel: ContinentModel) : Either<NetworkError, ContinentModel>
    suspend fun getAllContinent() : Either<NetworkError, List<ContinentModel>>
    suspend fun getAllContinentByAny(value: String) :  Either<NetworkError, List<ContinentModel>>
    suspend fun getContinentById(id: String) :  Either<NetworkError, ContinentModel>

}

//Country Repository
interface CountryRepository {
    suspend fun createCountry(countryModel: CountryModel) : Either<NetworkError, CountryModel>
    suspend fun getAllCountry() : Either<NetworkError, List<CountryModel>>
    suspend fun getAllCountryByContinent(value: String) : Either<NetworkError, List<CountryModel>>
    suspend fun getAllCountryByAny(value: String) : Either<NetworkError, List<CountryModel>>
    suspend fun getCountryById(id: String) : Either<NetworkError, CountryModel>
}

//Province Repository
interface ProvinceRepository {
    suspend fun createProvince(provinceModel: ProvinceModel) : Either<NetworkError, ProvinceModel>
    suspend fun getAllProvince() : Either<NetworkError, List<ProvinceModel>>
    suspend fun getAllProvinceByCountry(value: String) : Either<NetworkError, List<ProvinceModel>>
    suspend fun getProvinceBy(value: String) : Either<NetworkError, ProvinceModel>
    suspend fun getAllProvinceByAny(value: String) : Either<NetworkError, List<ProvinceModel>>
    suspend fun getProvinceById(id: String) : Either<NetworkError, ProvinceModel>
}

//Municipality Repository
interface MunicipalityRepository {
    suspend fun createMunicipality(municipalityModel: MunicipalityModel) : Either<NetworkError, MunicipalityModel>
    suspend fun getAllMunicipality() : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getAllMunicipalityByProvince(value: String) : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getMunicipalityBy(value: String) : Either<NetworkError, MunicipalityModel>
    suspend fun getAllMunicipalityByAny(value: String) : Either<NetworkError, List<MunicipalityModel>>
    suspend fun getMunicipalityById(id: String) : Either<NetworkError, MunicipalityModel>
}

//Commune Repository
interface CommuneRepository {
    suspend fun createCommune(communeModel: CommuneModel) : Either<NetworkError, CommuneModel>
    suspend fun getAllCommune() : Either<NetworkError, List<CommuneModel>>
    suspend fun getAllCommuneByAny(value: String) : Either<NetworkError, List<CommuneModel>>
    suspend fun getAllCommuneByMunicipalityId(municipalityId: String) : Either<NetworkError, List<CommuneModel>>
    suspend fun getCommuneById(id: String) : Either<NetworkError, CommuneModel>
}
