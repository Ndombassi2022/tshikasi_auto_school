package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.CommuneDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order

class CommuneDataSourceImpl: CommuneDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createCommune(communeModel: CommuneModel): CommuneModel {
        return try {
            client.from("tb_commune")
                .insert(communeModel)
                .decodeSingle<CommuneModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllCommune(): List<CommuneModel> {
        return try {
            client.from("tb_commune")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<CommuneModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllCommuneByAny(value: String): List<CommuneModel> {
        return try {
            client.from("tb_commune")
                .select {
                    filter {
                        or {
                            ilike("description", "%$value%")
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<CommuneModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getAllCommuneByMunicipalityId(municipalityId: String): List<CommuneModel> {
        return try {
            client.from("tb_commune")
                .select {
                    filter {
                            eq("municipality_is", municipalityId)
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<CommuneModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getCommuneById(id: String): CommuneModel {
        return try {
            client.from("tb_commune")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<CommuneModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

}