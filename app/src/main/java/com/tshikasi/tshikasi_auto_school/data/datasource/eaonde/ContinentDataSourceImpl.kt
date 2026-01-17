package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.ContinentDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.ContinentModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class ContinentDataSourceImpl: ContinentDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createContinent(continentModel: ContinentModel): ContinentModel {
        return try {
            client.from("tb_continent")
                .insert(continentModel)
                .decodeSingle<ContinentModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllContinent(): List<ContinentModel> {
        return try {
            client.from("tb_continent")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<ContinentModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllContinentByAny(value: String): List<ContinentModel> {
        return try {
            client.from("tb_continent")
                .select {
                    filter {
                        or {
                            ilike("description", "%$value%")
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<ContinentModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getContinentById(id: String): ContinentModel {
        TODO("Not yet implemented")
    }

}