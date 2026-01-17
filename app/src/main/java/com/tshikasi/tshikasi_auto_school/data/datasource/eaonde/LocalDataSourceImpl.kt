package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LocalDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

class LocalDataSourceImpl : LocalDataSource {
    private val client = TshikasiAutoSchool.supabase

    override suspend fun getAllLocal(): List<LocalModel> {
        return try {
            client.from("tb_local")
                .select {
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun updateLocal(localModel: LocalModel): LocalModel {
        return try {
            client.postgrest.from("tb_local")
                .update(localModel) {
                    filter {
                        eq("id", localModel.id) // Assumindo que 'id' é a chave primária
                    }
                }
                .decodeSingle<LocalModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun updateLocalLocation(localModel: LocalModel): LocalModel {
        return try {
            client.postgrest.from("tb_local")
                .update(localModel) {
                    filter {
                        eq("id", localModel.id) // Assumindo que 'id' é a chave primária
                    }
                }
                .decodeSingle<LocalModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun createLocal(localModel: LocalModel): LocalModel {
        return try {
            client.from("tb_local")
                .insert(localModel)

            client.from("tb_local")
                .select {
                    order("id", Order.DESCENDING)
                    limit(1)
                }
                .decodeSingle<LocalModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

    override suspend fun getAllLocalByLocalType(value: String): List<LocalModel> {
        return try {
            client.from("tb_local")
                .select {
                    filter {
                        // Primeiro filtra por localTypeId (obrigatório)
                        eq("local_type_id", value)
                    }
                    // Ordena primeiro por localType, depois por descrição
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getAllLocalByReference(value: String): LocalModel {
        return try {
            client.from("tb_local")
                .select {
                    filter {
                        eq("reference", value)
                    }
                }
                .decodeSingle<LocalModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getAllLocalByAny(value: String): List<LocalModel> {
        return try {
            client.from("tb_local")
                .select {
                    filter {
                        or {
                            ilike("description", "%$value%")
                            ilike("nif", "%$value%")
                            ilike("reference", "%$value%")
                            ilike("phone", "%$value%")
                            eq("code", value)
                        }
                    }
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }
    override suspend fun getAllLocalByLocalTypeIdByAny(
        localTypeId: String,
        value: String
    ): List<LocalModel> {
        return try {
            client.from("tb_local")
                .select {
                    filter {
                        // Primeiro filtra por localTypeId (obrigatório)
                        eq("local_type_id", localTypeId)
                        // E também por qualquer um dos campos (opcional)
                        or {
                            ilike("description", "%$value%")
                            ilike("nif", "%$value%")
                            ilike("reference", "%$value%")
                            ilike("phone", "%$value%")
                        }
                    }
                    // Ordena primeiro por localType, depois por descrição
                    order("description", Order.ASCENDING)
                }
                .decodeList<LocalModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

    override suspend fun getLocalById(id: String): LocalModel {
        return try {
            client.from("tb_local")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<LocalModel>()
        } catch (e: Exception) {
            println("Erro na consulta: ${e.stackTraceToString()}")
            throw  e
        }
    }

}