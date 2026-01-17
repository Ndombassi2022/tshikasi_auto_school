package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.CountryDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CountryModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class CountryDataSourceImpl: CountryDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createCountry(countryModel: CountryModel): CountryModel {
        return try {
            client.from("tb_country")
                .insert(countryModel)
                .decodeSingle<CountryModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllCountry(): List<CountryModel> {
        return try {
            client.from("tb_country")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<CountryModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllCountryByContinent(value: String): List<CountryModel> {
        return try {
            client.from("tb_country")
                .select {
                    filter {
                        or {
                            eq("continent_id", value)
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<CountryModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getAllCountryByAny(value: String): List<CountryModel> {
        return try {
            client.from("tb_country")
                .select {
                    filter {
                        or {
                            ilike("description", "%$value%")
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<CountryModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getCountryById(id: String): CountryModel {
        return try {
            client.from("tb_country")
                .select {
                    filter {
                        or {
                            eq("id", id)
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeSingle<CountryModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

}