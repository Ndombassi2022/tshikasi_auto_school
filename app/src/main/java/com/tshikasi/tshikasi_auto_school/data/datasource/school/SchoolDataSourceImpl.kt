package com.tshikasi.tshikasi_auto_school.data.datasource.school

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.school.SchoolDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.Clock


class SchoolDataSourceImpl : SchoolDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_school"

    override suspend fun createSchool(school: SchoolModel): SchoolModel {
        return try {


            client.postgrest[schema, table]
                .insert(school) {
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SCHOOL: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolById(id: Long): SchoolModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SchoolModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOL POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolByCode(code: String): SchoolModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("code", code) }
                }
                .decodeList<SchoolModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOL POR CODE $code: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllSchools(
        active: Boolean?,
        schoolTypeId: Long?,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): List<SchoolModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        schoolTypeId?.let { eq("school_type_id", it) }
                        communeId?.let { eq("commune_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODAS AS SCHOOLS: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolsByCommune(communeId: Long, page: Int, pageSize: Int): List<SchoolModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("commune_id", communeId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOLS POR COMMUNE $communeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolsByType(schoolTypeId: Long, page: Int, pageSize: Int): List<SchoolModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("school_type_id", schoolTypeId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOLS POR TYPE $schoolTypeId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchSchools(
        query: String,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): List<SchoolModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("name", "%$query%")
                            ilike("code", "%$query%")
                        }
                        communeId?.let { eq("commune_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR SCHOOLS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getTopSchoolsByStudents(limit: Int): List<SchoolModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    order("student_count", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TOP SCHOOLS POR ALUNOS: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSchool(school: SchoolModel): SchoolModel {
        return try {
            client.postgrest[schema, table]
                .update(school) {
                    filter { eq("id", school.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SCHOOL ${school.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSchoolStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DA SCHOOL $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSchoolStatistics(id: Long): Boolean {
        return try {
            // Placeholder - atualiza contagens (students, teachers, etc.)
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DA SCHOOL $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSubscription(id: Long, plan: String?, expiresAt: String?): Boolean {
        return try {
            val updates = buildMap {
                plan?.let { put("subscription_plan", it) }
                expiresAt?.let { put("subscription_expires_at", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SUBSCRIPTION DA SCHOOL $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteSchool(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SCHOOL $id: ${e.message}")
            false
        }
    }

    override suspend fun countSchoolsByCommune(communeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("commune_id", communeId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SCHOOLS POR COMMUNE $communeId: ${e.message}")
            throw e
        }
    }

    override suspend fun countSchoolsByType(schoolType:String): Map<Long, Int> {
        return try {
            // Placeholder: RPC com group by school_type_id
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SCHOOLS POR TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalSchoolsCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE SCHOOLS: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolPerformanceMetrics(schoolId: Long): Map<String, Any> {
        return try {
            // Placeholder: alunos, professores, taxa de aprovação, etc.
            mapOf(
                "total_students" to 0,
                "total_teachers" to 0,
                "average_performance" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER MÉTRICAS DA SCHOOL $schoolId: ${e.message}")
            emptyMap()
        }
    }
}