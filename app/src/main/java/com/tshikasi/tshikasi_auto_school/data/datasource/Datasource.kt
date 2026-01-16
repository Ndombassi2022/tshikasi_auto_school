package com.tshikasi.tshikasi_auto_school.data.datasource

import co.yml.charts.common.extensions.isNotNull
import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.*
import com.tshikasi.tshikasi_auto_school.domain.datasource.AssignmentSubmissionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.AttendanceDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ClasseDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExamDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExamResultDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExerciseDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExerciseResultDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.GradeDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.UserDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentModel
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentStatus
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentSubmissionModel
import com.tshikasi.tshikasi_auto_school.domain.model.AttendanceModel
import com.tshikasi.tshikasi_auto_school.domain.model.AttendanceStatus
import com.tshikasi.tshikasi_auto_school.domain.model.ClasseModel
import com.tshikasi.tshikasi_auto_school.domain.model.DifficultyLevel
import com.tshikasi.tshikasi_auto_school.domain.model.ExamModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExamResultModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExerciseModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExerciseResultModel
import com.tshikasi.tshikasi_auto_school.domain.model.NotificationPreferences
import com.tshikasi.tshikasi_auto_school.domain.model.UserModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserStatus
import com.tshikasi.tshikasi_auto_school.domain.model.*
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.Clock

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.FileDescriptor.`in`
import java.lang.System.`in`
import java.util.Objects.isNull
import java.util.UUID
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.mapLatest
/*
class AssignmentDataSourceImpl : AssignmentDataSource {

    private val client = TshikasiAutoSchool.supabase

    private val schema = "db_education"       // Ajusta para o teu schema real (ex: db_flora_fauna, db_education...)
    private val table = "tb_assignment"

    override suspend fun createAssignment(assignment: AssignmentModel): AssignmentModel {
        return try {
            // Constrói JSON manualmente para controlar defaults e evitar nulls indesejados
            val data = buildJsonObject {
                put("title", assignment.title)
                put("description", assignment.description)
                put("teacher_id", assignment.teacherId)
                put("classe_id", assignment.classeId)
                put("subject_id", assignment.subjectId)
                put("grade_id", assignment.gradeId)
                put("content_url", assignment.contentUrl)
                put("content_type", assignment.contentType.name)
                put("due_date", assignment.dueDate)
                put("max_points", assignment.maxPoints)
                put("difficulty_level", assignment.difficultyLevel.name)
                put("estimated_time", assignment.estimatedTime)
                put("allow_late_submission", assignment.allowLateSubmission)
                put("late_submission_days", assignment.lateSubmissionDays)
                put("requires_file_upload", assignment.requiresFileUpload)
                put("allowed_file_types", assignment.allowedFileTypes)
                put("max_file_size", assignment.maxFileSize)
                put("status", assignment.status.name)  // Ou sempre "ACTIVE" se for default
                // created_at, updated_at, etc. o Supabase gera sozinho
            }

            // Verifica se já existe algo similar (opcional, mas segue teu padrão de upsert-like)
            val existing = client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("title", assignment.title)
                        eq("teacher_id", assignment.teacherId)
                        eq("classe_id", assignment.classeId)
                        // Adiciona mais filtros se quiser evitar duplicatas
                    }
                }
                .decodeList<AssignmentModel>()

            if (existing.isNotEmpty()) {
                // Se já existe → atualiza (ou retorna o existente, conforme teu negócio)
                val existingId = existing.first().id
                val updated = client.postgrest[schema, table]
                    .update(data) {
                        filter { eq("id", existingId) }
                        select(
                            columns = Columns.raw("""
                                *, 
                                tb_teacher!inner(*), 
                                tb_classe!inner(*), 
                                tb_subject!inner(*), 
                                tb_grade!inner(*)
                            """.trimIndent())
                        )
                    }
                    .decodeSingle<AssignmentModel>()
                updated
            } else {
                // Insert novo
                val result = client.postgrest[schema, table]
                    .insert(data) {
                        select(
                            columns = Columns.raw("""
                                *, 
                                tb_teacher!inner(*), 
                                tb_classe!inner(*), 
                                tb_subject!inner(*), 
                                tb_grade!inner(*)
                            """.trimIndent())
                        )
                    }
                    .decodeSingle<AssignmentModel>()
                result
            }
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentById(id: Long): AssignmentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENT POR ID: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            val offset = (page - 1) * pageSize
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range(
                        (page * pageSize).toLong(),
                        ((page + 1) * pageSize - 1).toLong()
                    )
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR TEACHER: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByClasse(
        classeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            val offset = (page - 1) * pageSize
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range(
                        (page * pageSize).toLong(),
                        ((page + 1) * pageSize - 1).toLong()
                    )
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR CLASSE: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsBySubject(
        subjectId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAssignmentsByGrade(
        gradeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingAssignments(studentId: Long, days: Int): List<AssignmentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverdueAssignments(studentId: Long): List<AssignmentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun searchAssignments(
        query: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        TODO("Not yet implemented")
    }

    // ... (continua com os outros métodos getAssignmentsBySubject, ByGrade, Upcoming, Overdue, Search de forma semelhante)

    override suspend fun updateAssignment(assignment: AssignmentModel): AssignmentModel {
        return try {
            client.postgrest[schema, table]
                .update(assignment) {
                    filter { eq("id", assignment.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_teacher!inner(*), 
                            tb_classe!inner(*), 
                            tb_subject!inner(*), 
                            tb_grade!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignmentStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentModel>()
                .isNotEmpty()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignmentStatistics(id: Long): Boolean {
        // Exemplo: atualiza médias, contagens, etc. (ajusta conforme tua lógica real)
        return try {
            // Pode ser um RPC ou update complexo - por agora um placeholder
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentModel>()
                .isNotEmpty()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTotalSubmissions(id: Long): Boolean {
        return try {
            // Supondo que tens uma coluna total_submissions
            client.rpc("increment_total_submissions", mapOf("assignment_id" to id))
                .execute()
                .status.isSuccess()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR TOTAL SUBMISSIONS: ${e.message}")
            throw e
        }

    }

    override suspend fun deleteAssignment(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentModel>()
                .isNotEmpty()
        } catch (e: Exception) {
            println("ERRO AO DELETAR ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun countAssignmentsByTeacher(teacherId: Long): Int {
        return try {
            val countResponse = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Int>>()
            countResponse.firstOrNull()?.get("count") ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ASSIGNMENTS POR TEACHER: ${e.message}")
            throw e
        }
    }

    override suspend fun countAssignmentsByClasse(classeId: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAverageScoreByAssignment(assignmentId: Long): Double {
        TODO("Not yet implemented")
    }

    override suspend fun getAssignmentCompletionRate(assignmentId: Long): Double {
        TODO("Not yet implemented")
    }

    // ... (continua com os outros count, average, completion rate de forma semelhante)
}
*/
class AssignmentDataSourceImpl : AssignmentDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_assignment"

    override suspend fun createAssignment(assignment: AssignmentModel): AssignmentModel {
        return try {
            val data = buildJsonObject {
                put("title", assignment.title)
                put("description", assignment.description)
                put("teacher_id", assignment.teacherId)
                put("classe_id", assignment.classeId)
                put("subject_id", assignment.subjectId)
                put("grade_id", assignment.gradeId)
                put("content_url", assignment.contentUrl)
                put("content_type", assignment.contentType.name)
                put("due_date", assignment.dueDate)
                put("max_points", assignment.maxPoints)
                put("difficulty_level", assignment.difficultyLevel.name)
                put("estimated_time", assignment.estimatedTime)
                put("allow_late_submission", assignment.allowLateSubmission)
                put("late_submission_days", assignment.lateSubmissionDays)
                put("requires_file_upload", assignment.requiresFileUpload)
                put("allowed_file_types", assignment.allowedFileTypes)
                put("max_file_size", assignment.maxFileSize)
                put("status", assignment.status.name)
                // created_at e updated_at gerados pelo Supabase
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_teacher!inner(*), 
                            tb_classe!inner(*), 
                            tb_subject!inner(*), 
                            tb_grade!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentById(id: Long): AssignmentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByClasse(
        classeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsBySubject(
        subjectId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("subject_id", subjectId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByGrade(
        gradeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("grade_id", gradeId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR GRADE $gradeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getUpcomingAssignments(studentId: Long, days: Int): List<AssignmentModel> {
        return try {
            // Lógica para upcoming: due_date >= hoje e <= hoje + days
            // Nota: precisas de uma data atual (podes usar kotlinx-datetime ou java.time)
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val future = today.plus(DatePeriod(days = days))

            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        gte("due_date", today.toString())
                        lte("due_date", future.toString())
                        // Filtro por student → pode precisar join com tb_classe → tb_student
                        // Se não tiver relação direta, ajusta a query ou faz no repositório
                    }
                    order("due_date", Order.ASCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR UPCOMING ASSIGNMENTS PARA STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getOverdueAssignments(studentId: Long): List<AssignmentModel> {
        return try {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        lt("due_date", today.toString())
                        // Filtro por student via classe
                    }
                    order("due_date", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR OVERDUE ASSIGNMENTS PARA STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchAssignments(
        query: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<AssignmentModel> {
        return try {
            val offsetStart = (page * pageSize).toLong()
            val offsetEnd = ((page + 1) * pageSize - 1).toLong()

            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*), 
                        tb_grade!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("title", "%$query%")
                            ilike("description", "%$query%")
                        }
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range(offsetStart, offsetEnd)
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR ASSIGNMENTS: $query - ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignment(assignment: AssignmentModel): AssignmentModel {
        return try {
            client.postgrest[schema, table]
                .update(assignment) {
                    filter { eq("id", assignment.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_teacher!inner(*), 
                            tb_classe!inner(*), 
                            tb_subject!inner(*), 
                            tb_grade!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ASSIGNMENT ${assignment.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignmentStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO ASSIGNMENT $id: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignmentStatistics(id: Long): Boolean {
        // Placeholder - ajusta com a lógica real de estatísticas (ex: média de notas, etc.)
        return try {
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO ASSIGNMENT $id: ${e.message}")
            throw e
        }
    }
    override suspend fun updateTotalSubmissions(id: Long): Boolean {
        return try {
            val newTotal = client.postgrest.rpc(
                function = "increment_total_submissions",
                parameters = mapOf("assignment_id" to id)
            ).decodeSingle<Int>()

            println("Novo total de submissões: $newTotal")
            true
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR TOTAL SUBMISSIONS: ${e.message}")
            false
        }
    }

    override suspend fun deleteAssignment(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR ASSIGNMENT $id: ${e.message}")
            throw e
        }
    }

    override suspend fun countAssignmentsByTeacher(teacherId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Long>>()
            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ASSIGNMENTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countAssignmentsByClasse(classeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("classe_id", classeId) }
                }
                .decodeList<Map<String, Long>>()
            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ASSIGNMENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageScoreByAssignment(assignmentId: Long): Double {
        return try {
            // Supondo que tens uma view ou RPC para média de notas das submissões
            // Placeholder: retorna 0.0 ou implementa com join/subquery
            0.0
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA POR ASSIGNMENT $assignmentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentCompletionRate(assignmentId: Long): Double {
        return try {
            // Placeholder: % de submissões / total alunos na classe
            0.0
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TAXA DE COMPLETION DO ASSIGNMENT $assignmentId: ${e.message}")
            throw e
        }
    }
}
class UserDataSourceImpl : UserDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_user"  // ajusta se for tb_users ou outro nome

    override suspend fun createUser(user: UserModel): UserModel {
        return try {
            val data = buildJsonObject {
                put("full_name", user.fullName)
                put("email", user.email)
                put("phone", user.phone)
                put("user_type", user.userType.name)
                put("status", user.status.name)
                // Adicione outros campos obrigatórios ou com default controlado
                // created_at, updated_at gerados pelo Supabase
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeSingle<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR USER: ${e.message}")
            throw e
        }
    }

    override suspend fun getUserById(id: Long): UserModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<UserModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USER POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getUserByEmail(email: String): UserModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("email", email) }
                }
                .decodeList<UserModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USER POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getUsersByType(
        userType: UserType,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<UserModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_type", userType.name)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USERS POR TYPE ${userType.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun searchUsers(
        query: String,
        userType: UserType?,
        page: Int,
        pageSize: Int
    ): List<UserModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("email", "%$query%")
                            ilike("phone", "%$query%")
                        }
                        userType?.let { eq("user_type", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR USERS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getUsersByStatus(
        status: UserStatus,
        page: Int,
        pageSize: Int
    ): List<UserModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", status.name) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USERS POR STATUS ${status.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentlyActiveUsers(days: Int, limit: Int): List<UserModel> {
        return try {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val sinceDate = today.minus(DatePeriod(days = days))

            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { gte("last_login", sinceDate.toString()) }
                    limit(limit.toLong())
                    order("last_login", Order.DESCENDING)
                }
                .decodeList<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USERS RECENTEMENTE ATIVOS: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUser(user: UserModel): UserModel {
        return try {
            client.postgrest[schema, table]
                .update(user) {
                    filter { eq("id", user.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<UserModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR USER ${user.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUserStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO USER $id: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUserProfile(
        id: Long,
        fullName: String?,
        phone: String?,
        profilePhoto: String?
    ): Boolean {
        return try {
            val updates = buildMap {
                fullName?.let { put("full_name", it) }
                phone?.let { put("phone", it) }
                profilePhoto?.let { put("profile_photo", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PERFIL DO USER $id: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUserPreferences(id: Long, preferences: NotificationPreferences): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("notification_preferences" to preferences)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PREFERENCES DO USER $id: ${e.message}")
            throw e
        }
    }

    override suspend fun updateLastLogin(id: Long, ipAddress: String?): Boolean {
        return try {
            val updates = buildMap {
                put("last_login", Clock.System.now().toString())
                ipAddress?.let { put("last_ip_address", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LAST LOGIN DO USER $id: ${e.message}")
            throw e
        }
    }

    override suspend fun incrementFailedLoginAttempts(id: Long): Boolean {
        return try {
            client.postgrest.rpc(
                function = "increment_failed_login_attempts",
                parameters = mapOf("user_id" to id)
            ).decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR FAILED LOGIN $id: ${e.message}")
            false
        }
    }

    override suspend fun resetFailedLoginAttempts(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("failed_login_attempts" to 0)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO RESETAR FAILED LOGIN $id: ${e.message}")
            false
        }
    }

    override suspend fun lockUserAccount(id: Long, until: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("locked_until" to until, "status" to "LOCKED")) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO LOCKAR USER $id: ${e.message}")
            false
        }
    }

    override suspend fun unlockUserAccount(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("locked_until" to null, "status" to "ACTIVE")) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO UNLOCKAR USER $id: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteUser(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE USER $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteUser(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETE USER $id: ${e.message}")
            false
        }
    }

    override suspend fun countUsersByType(): Map<UserType, Int> {
        return try {
            // Placeholder: implementa com RPC ou view no Supabase
            // Exemplo simples: retorna map vazio por agora
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR USERS POR TYPE: ${e.message}")
            throw e
        }
    }

    override suspend fun countUsersByStatus(): Map<UserStatus, Int> {
        return try {
            // Placeholder similar
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR USERS POR STATUS: ${e.message}")
            throw e
        }
    }

    override suspend fun getTotalUsersCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE USERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getNewUsersCount(days: Int): Int {
        return try {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val sinceDate = today.minus(DatePeriod(days = days))
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { gte("created_at", sinceDate.toString()) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR NOVOS USERS: ${e.message}")
            throw e
        }
    }
}
class AssignmentSubmissionDataSourceImpl : AssignmentSubmissionDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_assignment_submission"

    override suspend fun createSubmission(submission: AssignmentSubmissionModel): AssignmentSubmissionModel {
        return try {
            val data = buildJsonObject {
                put("assignment_id", submission.assignmentId)
                put("student_id", submission.studentId)
                put("submission_text", submission.submissionText)
                put("file_url", submission.fileUrl)
                put("file_name", submission.fileName)
                put("file_size", submission.fileSize)
                put("submitted_at", submission.submittedAt)
                put("submission_status", submission.submissionStatus.name)
                put("is_late", submission.isLate)
                put("late_days", submission.lateDays)
                put("grade", submission.grade)
                put("graded_by", submission.gradedBy)
                put("graded_at", submission.gradedAt)
                put("feedback", submission.feedback)
                put("points_deducted_late", submission.pointsDeductedLate)
                put("final_score", submission.finalScore)
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_assignment!inner(*), 
                            tb_student!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SUBMISSÃO: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubmissionById(id: Long): AssignmentSubmissionModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<AssignmentSubmissionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÃO POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubmissionsByAssignment(assignmentId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("assignment_id", assignmentId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("submitted_at", Order.DESCENDING)
                }
                .decodeList<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÕES POR ASSIGNMENT $assignmentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubmissionsByStudent(studentId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("submitted_at", Order.DESCENDING)
                }
                .decodeList<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÕES POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubmissionByAssignmentAndStudent(assignmentId: Long, studentId: Long): AssignmentSubmissionModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("assignment_id", assignmentId)
                        eq("student_id", studentId)
                    }
                }
                .decodeList<AssignmentSubmissionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÃO POR ASSIGNMENT $assignmentId E STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getLateSubmissions(assignmentId: Long?): List<AssignmentSubmissionModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("is_late", true)
                        assignmentId?.let { eq("assignment_id", it) }
                    }
                    order("submitted_at", Order.DESCENDING)
                }
                .decodeList<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÕES ATRASADAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getUngradedSubmissions(teacherId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*, tb_teacher!inner(*)), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        // Ungraded = grade is null e assignment do teacher
                        isNull("grade")
                        eq("tb_assignment.teacher_id", teacherId)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("submitted_at", Order.DESCENDING)
                }
                .decodeList<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBMISSÕES NÃO CORRIGIDAS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchSubmissions(
        query: String,
        assignmentId: Long?,
        page: Int,
        pageSize: Int
    ): List<AssignmentSubmissionModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_assignment!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("submission_text", "%$query%")
                            ilike("feedback", "%$query%")
                        }
                        assignmentId?.let { eq("assignment_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("submitted_at", Order.DESCENDING)
                }
                .decodeList<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR SUBMISSÕES: $query - ${e.message}")
            throw e
        }
    }

    override suspend fun updateSubmission(submission: AssignmentSubmissionModel): AssignmentSubmissionModel {
        return try {
            client.postgrest[schema, table]
                .update(submission) {
                    filter { eq("id", submission.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_assignment!inner(*), 
                            tb_student!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AssignmentSubmissionModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SUBMISSÃO ${submission.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun gradeSubmission(id: Long, grade: Double, feedback: String?, gradedBy: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "grade" to grade,
                        "feedback" to feedback,
                        "graded_by" to gradedBy,
                        "graded_at" to Clock.System.now().toString(),
                        "submission_status" to "GRADED"
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO CORRIGIR SUBMISSÃO $id: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSubmissionStatus(id: Long, status: AssignmentStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("submission_status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DA SUBMISSÃO $id: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteSubmission(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR SUBMISSÃO $id: ${e.message}")
            false
        }
    }

    override suspend fun countSubmissionsByAssignment(assignmentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("assignment_id", assignmentId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBMISSÕES POR ASSIGNMENT $assignmentId: ${e.message}")
            throw e
        }
    }

    override suspend fun countSubmissionsByStudent(studentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("student_id", studentId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBMISSÕES POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageGradeByAssignment(assignmentId: Long): Double {
        return try {
            // Placeholder: implementa com RPC ou subquery
            // Ex: SELECT AVG(grade) FROM tb_assignment_submission WHERE assignment_id = ?


           client.postgrest.rpc(
                function = "get_average_grade_by_assignment",
                parameters = mapOf("p_assignment_id" to assignmentId)
            ).decodeSingle<Double>()


        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA POR ASSIGNMENT $assignmentId: ${e.message}")
            0.0
        }
    }

    override suspend fun getSubmissionTimeliness(studentId: Long): Map<String, Any> {
        return try {
            // Placeholder: retorna mapa com atrasos, pontualidade, etc.
            // Pode ser um RPC que retorna JSON
            mapOf("on_time" to 75, "late" to 25, "total" to 100)
        } catch (e: Exception) {
            println("ERRO AO OBTER TIMELINESS DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }
}
class AttendanceDataSourceImpl : AttendanceDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_attendance"

    override suspend fun createAttendance(attendance: AttendanceModel): AttendanceModel {
        return try {
            val data = buildJsonObject {
                put("student_id", attendance.studentId)
                put("classe_id", attendance.classeId)
                put("teacher_id", attendance.teacherId)
                put("date", attendance.date)
                put("status", attendance.status.name)
                put("check_in_time", attendance.checkInTime)
                put("check_out_time", attendance.checkOutTime)
                put("notes", attendance.notes)
                put("excuse_reason", attendance.excuseReason)
                put("excuse_document_url", attendance.excuseDocumentUrl)
                put("late_minutes", attendance.lateMinutes)
                // created_at, updated_at gerados pelo Supabase
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_student!inner(*), 
                            tb_classe!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR ATTENDANCE: ${e.message}")
            throw e
        }
    }

    override suspend fun createBulkAttendance(attendances: List<AttendanceModel>): List<AttendanceModel> {
        return try {
            val dataList = attendances.map { att ->
                buildJsonObject {
                    put("student_id", att.studentId)
                    put("classe_id", att.classeId)
                    put("teacher_id", att.teacherId)
                    put("date", att.date)
                    put("status", att.status.name)
                    put("check_in_time", att.checkInTime)
                    put("check_out_time", att.checkOutTime)
                    put("notes", att.notes)
                    put("excuse_reason", att.excuseReason)
                    put("excuse_document_url", att.excuseDocumentUrl)
                    put("late_minutes", att.lateMinutes)
                }
            }

            client.postgrest[schema, table]
                .insert(dataList) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_student!inner(*), 
                            tb_classe!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR BULK ATTENDANCE: ${e.message}")
            throw e
        }
    }

    override suspend fun getAttendanceById(id: Long): AttendanceModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<AttendanceModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ATTENDANCE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getAttendanceByStudent(
        studentId: Long,
        startDate: String?,
        endDate: String?,
        page: Int,
        pageSize: Int
    ): List<AttendanceModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                        startDate?.let { gte("date", it) }
                        endDate?.let { lte("date", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ATTENDANCE POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAttendanceByClasse(
        classeId: Long,
        date: String?,
        page: Int,
        pageSize: Int
    ): List<AttendanceModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        date?.let { eq("date", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ATTENDANCE POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAttendanceByTeacher(
        teacherId: Long,
        date: String?,
        page: Int,
        pageSize: Int
    ): List<AttendanceModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        date?.let { eq("date", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ATTENDANCE POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getDailyAttendance(classeId: Long, date: String): List<AttendanceModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        eq("date", date)
                    }
                    order("student_id", Order.ASCENDING)
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ATTENDANCE DIÁRIA DA CLASSE $classeId EM $date: ${e.message}")
            throw e
        }
    }

    override suspend fun getAttendanceSummary(studentId: Long, startDate: String, endDate: String): Map<String, Any> {
        return try {
            // Placeholder - podes implementar com RPC que retorna JSON ou calcula no código
            // Exemplo: total dias, presentes, ausentes, taxa %
            val attendances = getAttendanceByStudent(studentId, startDate, endDate, 1, 1000) // pega todos

            val total = attendances.size
            val present = attendances.count { it.status == AttendanceStatus.PRESENT }
            val absent = attendances.count { it.status == AttendanceStatus.ABSENT }

            mapOf(
                "total_days" to total,
                "present_days" to present,
                "absent_days" to absent,
                "attendance_rate" to if (total > 0) (present.toDouble() / total) * 100 else 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER RESUMO DE ATTENDANCE DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getAbsentStudents(classeId: Long, date: String): List<AttendanceModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_student!inner(*), 
                        tb_classe!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        eq("date", date)
                        eq("status", "ABSENT")
                    }
                    order("student_id", Order.ASCENDING)
                }
                .decodeList<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ALUNOS AUSENTES NA CLASSE $classeId EM $date: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAttendance(attendance: AttendanceModel): AttendanceModel {
        return try {
            client.postgrest[schema, table]
                .update(attendance) {
                    filter { eq("id", attendance.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_student!inner(*), 
                            tb_classe!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<AttendanceModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ATTENDANCE ${attendance.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAttendanceStatus(id: Long, status: AttendanceStatus, notes: String?): Boolean {
        return try {
            val updates = buildMap {
                put("status", status.name)
                notes?.let { put("notes", it) }
                put("updated_at", Clock.System.now().toString())
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO ATTENDANCE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteAttendance(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR ATTENDANCE $id: ${e.message}")
            false
        }
    }

    override suspend fun countAttendanceByStatus(
        studentId: Long,
        startDate: String,
        endDate: String
    ): Map<AttendanceStatus, Int> {
        return try {
            val attendances = getAttendanceByStudent(studentId, startDate, endDate, 1, 1000)

            AttendanceStatus.entries.associateWith { status ->
                attendances.count { it.status == status }
            }
        } catch (e: Exception) {
            println("ERRO AO CONTAR ATTENDANCE POR STATUS: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun calculateAttendanceRate(
        studentId: Long,
        startDate: String,
        endDate: String
    ): Double {
        return try {
            val attendances = getAttendanceByStudent(studentId, startDate, endDate, 1, 1000)
            if (attendances.isEmpty()) return 0.0

            val presentCount = attendances.count { it.status == AttendanceStatus.PRESENT }
            (presentCount.toDouble() / attendances.size) * 100
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TAXA DE ATTENDANCE: ${e.message}")
            0.0
        }
    }

    override suspend fun getClassAttendanceStats(classeId: Long, date: String): Map<String, Any> {
        return try {
            val attendances = getDailyAttendance(classeId, date)

            val total = attendances.size
            val present = attendances.count { it.status == AttendanceStatus.PRESENT }
            val absent = attendances.count { it.status == AttendanceStatus.ABSENT }
            val late = attendances.count { it.status == AttendanceStatus.LATE }

            mapOf(
                "total_students" to total,
                "present" to present,
                "absent" to absent,
                "late" to late,
                "attendance_rate" to if (total > 0) (present.toDouble() / total) * 100 else 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DA CLASSE $classeId EM $date: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getStudentAttendanceTrend(studentId: Long, days: Int): Map<String, Double> {
        return try {
            val endDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val startDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .minus(DatePeriod(days = days))
                .toString()

            val attendances = getAttendanceByStudent(studentId, startDate, endDate, 1, 1000)

            // Agrupa por data e calcula taxa diária
            val trend = attendances
                .groupBy { it.date }
                .mapValues { entry ->
                    val daily = entry.value
                    val present = daily.count { it.status == AttendanceStatus.PRESENT }
                    if (daily.isNotEmpty()) (present.toDouble() / daily.size) * 100 else 0.0
                }

            trend
        } catch (e: Exception) {
            println("ERRO AO OBTER TENDÊNCIA DE ATTENDANCE DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }
}
class ClasseDataSourceImpl : ClasseDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_classe"

    override suspend fun createClasse(classe: ClasseModel): ClasseModel {
        return try {
            val data = buildJsonObject {
                put("name", classe.name)
                put("school_id", classe.schoolId)
                put("grade_id", classe.gradeId)
                put("teacher_id", classe.teacherId)
                put("capacity", classe.capacity)
                put("current_student_count", classe.currentStudents)
                put("school_year", classe.schoolYear)
                put("status", classe.status.name)

                // created_at, updated_at gerados pelo Supabase
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_school!inner(*), 
                            tb_grade!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR CLASSE: ${e.message}")
            throw e
        }
    }

    override suspend fun getClasseById(id: Long): ClasseModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_school!inner(*), 
                        tb_grade!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<ClasseModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CLASSE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getClassesBySchool(
        schoolId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ClasseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_school!inner(*), 
                        tb_grade!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("school_id", schoolId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CLASSES POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun getClassesByGrade(
        gradeId: Long,
        schoolId: Long?,
        page: Int,
        pageSize: Int
    ): List<ClasseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_school!inner(*), 
                        tb_grade!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("grade_id", gradeId)
                        schoolId?.let { eq("school_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CLASSES POR GRADE $gradeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getClassesByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ClasseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_school!inner(*), 
                        tb_grade!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CLASSES POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchClasses(
        query: String,
        schoolId: Long?,
        page: Int,
        pageSize: Int
    ): List<ClasseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_school!inner(*), 
                        tb_grade!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("name", "%$query%")
                            ilike("code", "%$query%")
                        }
                        schoolId?.let { eq("school_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR CLASSES '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getClassSchedule(classeId: Long): Map<String, Any> {
        return try {
            // Placeholder - pode ser um campo JSONB ou join com tb_timetable
            // Exemplo: retorna mapa com dias/horários
            mapOf(
                "monday" to listOf("08:00-09:30 Matemática", "10:00-11:30 Português"),
                "total_hours" to 30
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER HORÁRIO DA CLASSE $classeId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun updateClasse(classe: ClasseModel): ClasseModel {
        return try {
            client.postgrest[schema, table]
                .update(classe) {
                    filter { eq("id", classe.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_school!inner(*), 
                            tb_grade!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ClasseModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR CLASSE ${classe.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateClasseStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DA CLASSE $id: ${e.message}")
            false
        }
    }

    override suspend fun updateClasseTeacher(id: Long, teacherId: Long?): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("teacher_id" to teacherId)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PROFESSOR DA CLASSE $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentCount(id: Long): Boolean {
        return try {
            // Placeholder: incrementa ou recalcula total de alunos (pode usar RPC ou trigger)

            client.postgrest.rpc(
                function = "update_classe_student_count",
                parameters =mapOf("classe_id" to id)
            ).decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR CONTAGEM DE ALUNOS DA CLASSE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteClasse(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR CLASSE $id: ${e.message}")
            false
        }
    }

    override suspend fun countClassesBySchool(schoolId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR CLASSES POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun countClassesByGrade(schoolId: Long): Map<Long, Int> {
        return try {
            // Placeholder: retorna mapa grade_id -> count
            // Ideal: usar RPC ou group by via view
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR CLASSES POR GRADE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getClassCapacityUtilization(classeId: Long): Double {
        return try {
            val classe = getClasseById(classeId) ?: return 0.0
            if (classe.capacity == 0) return 0.0
            (classe.currentStudents.toDouble() / classe.capacity) * 100
        } catch (e: Exception) {
            println("ERRO AO CALCULAR UTILIZAÇÃO DA CLASSE $classeId: ${e.message}")
            0.0
        }
    }

    override suspend fun getSchoolClassDistribution(schoolId: Long): Map<String, Any> {
        return try {
            // Placeholder: distribuição por grade, turno, etc.
            mapOf(
                "total_classes" to countClassesBySchool(schoolId),
                "by_grade" to mapOf<Long, Int>()
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER DISTRIBUIÇÃO DE CLASSES DA SCHOOL $schoolId: ${e.message}")
            emptyMap()
        }
    }
}
class ExamDataSourceImpl : ExamDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_exam"

    override suspend fun createExam(exam: ExamModel): ExamModel {
        return try {
            val data = buildJsonObject {
                put("title", exam.title)
                put("description", exam.description)
                put("teacher_id", exam.teacherId)
                put("classe_id", exam.classeId)
                put("subject_id", exam.subjectId)
                put("exam_date", exam.examDate)
                put("duration_minutes", exam.durationMinutes)
                put("max_score", exam.maxScore)
                put("passing_score", exam.passingScore)
                put("published_at", exam.publishedAt)
                put("instructions", exam.instructions)
                put("difficulty_level", exam.difficultyLevel.name)
                // created_at, updated_at gerados
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_teacher!inner(*), 
                            tb_classe!inner(*), 
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR EXAM: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamById(id: Long): ExamModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<ExamModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXAM POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamsByTeacher(
        teacherId: Long,
        published: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ExamModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        published?.let { eq("published", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXAMS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamsByClasse(
        classeId: Long,
        published: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ExamModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        published?.let { eq("published", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXAMS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamsBySubject(
        subjectId: Long,
        published: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ExamModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("subject_id", subjectId)
                        published?.let { eq("published", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXAMS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getUpcomingExams(studentId: Long, days: Int): List<ExamModel> {
        return try {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val future = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .plus(DatePeriod(days = days))
                .toString()

            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        gte("date", today)
                        lte("date", future)
                        // Filtro por student → join com tb_classe → tb_student (ajusta se necessário)
                    }
                    order("date", Order.ASCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR UPCOMING EXAMS PARA STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getPublishedExams(
        classeId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExamModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("published", true)
                        classeId?.let { eq("classe_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXAMS PUBLICADOS: ${e.message}")
            throw e
        }
    }

    override suspend fun searchExams(
        query: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExamModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_teacher!inner(*), 
                        tb_classe!inner(*), 
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("title", "%$query%")
                            ilike("description", "%$query%")
                        }
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("date", Order.DESCENDING)
                }
                .decodeList<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR EXAMS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateExam(exam: ExamModel): ExamModel {
        return try {
            client.postgrest[schema, table]
                .update(exam) {
                    filter { eq("id", exam.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_teacher!inner(*), 
                            tb_classe!inner(*), 
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExamModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR EXAM ${exam.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun publishExam(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("published" to true)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO PUBLICAR EXAM $id: ${e.message}")
            false
        }
    }

    override suspend fun unpublishExam(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("published" to false)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DESPUBLICAR EXAM $id: ${e.message}")
            false
        }
    }

    override suspend fun updateExamStatistics(id: Long): Boolean {
        return try {
            // Placeholder - atualiza médias, participações, etc.
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO EXAM $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteExam(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR EXAM $id: ${e.message}")
            false
        }
    }

    override suspend fun countExamsByTeacher(teacherId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR EXAMS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countExamsByClasse(classeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("classe_id", classeId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR EXAMS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamPerformanceStatistics(examId: Long): Map<String, Any> {
        return try {
            // Placeholder: média, maior nota, menor nota, % aprovados
            mapOf(
                "average_score" to 0.0,
                "pass_rate" to 0.0,
                "total_students" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DO EXAM $examId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getUpcomingExamsCount(studentId: Long): Int {
        return try {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        gte("date", today)
                        // Filtro por student via classe
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR UPCOMING EXAMS PARA STUDENT $studentId: ${e.message}")
            0
        }
    }
}

class ExamResultDataSourceImpl : ExamResultDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_exam_result"

    override suspend fun createExamResult(result: ExamResultModel): ExamResultModel {
        return try {
            val data = buildJsonObject {
                put("exam_id", result.examId)
                put("student_id", result.studentId)
                put("score", result.score)

                put("percentage", result.percentage)
                put("rank_in_class", result.rankInClass)
                put("graded_by", result.gradedBy)
                put("graded_at", result.gradedAt)
                put("feedback", result.feedback)

            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_exam!inner(*), 
                            tb_student!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR RESULTADO DE EXAM: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamResultById(id: Long): ExamResultModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<ExamResultModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADO POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamResultsByExam(examId: Long, page: Int, pageSize: Int): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("exam_id", examId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("score", Order.DESCENDING)
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADOS POR EXAM $examId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamResultsByStudent(studentId: Long, page: Int, pageSize: Int): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("graded_at", Order.DESCENDING)
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADOS POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamResultByExamAndStudent(examId: Long, studentId: Long): ExamResultModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("exam_id", examId)
                        eq("student_id", studentId)
                    }
                }
                .decodeList<ExamResultModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADO POR EXAM $examId E STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTopPerformers(examId: Long, limit: Int): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("exam_id", examId) }
                    limit(limit.toLong())
                    order("score", Order.DESCENDING)
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TOP PERFORMERS DO EXAM $examId: ${e.message}")
            throw e
        }
    }

    override suspend fun getFailedStudents(examId: Long): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("exam_id", examId)
                        lt("percentage", 50.0) // Ajusta o critério de reprovação
                    }
                    order("score", Order.ASCENDING)
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ALUNOS REPROVADOS NO EXAM $examId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchExamResults(
        query: String,
        examId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("tb_student.full_name", "%$query%")
                            ilike("feedback", "%$query%")
                        }
                        examId?.let { eq("exam_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("score", Order.DESCENDING)
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR RESULTADOS DE EXAM '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateExamResult(result: ExamResultModel): ExamResultModel {
        return try {
            client.postgrest[schema, table]
                .update(result) {
                    filter { eq("id", result.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_exam!inner(*), 
                            tb_student!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RESULTADO ${result.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun gradeExamResult(id: Long, score: Double, feedback: String?, gradedBy: Long): Boolean {
        return try {
            val percentage = // Calcula % baseado no max_score do exam (pode precisar join)
                client.postgrest[schema, table]
                    .update(
                        mapOf(
                            "score" to score,
                            "feedback" to feedback,
                            "graded_by" to gradedBy,
                            "graded_at" to Clock.System.now().toString(),
                            "status" to "GRADED"
                        )
                    ) {
                        filter { eq("id", id) }
                    }
                    .decodeList<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO CORRIGIR RESULTADO $id: ${e.message}")
            false
        }
    }

    override suspend fun updateRankInClass(examId: Long): Boolean {
        return try {
            // Placeholder: recalcula ranks (ideal com trigger ou RPC)


            client.postgrest.rpc(
                function = "update_exam_ranks",
                parameters = mapOf("exam_id" to examId)
            ).decodeSingle<Int>()
            true

        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RANK NO EXAM $examId: ${e.message}")
            false
        }
    }

    override suspend fun deleteExamResult(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR RESULTADO $id: ${e.message}")
            false
        }
    }

    override suspend fun countExamResultsByExam(examId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("exam_id", examId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR RESULTADOS POR EXAM $examId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExamAverageScore(examId: Long): Double {
        return try {

            client.postgrest.rpc(
                function = "get_exam_average_score",
                parameters = mapOf("p_exam_id" to examId)
            ).decodeSingle<Double>()

        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DO EXAM $examId: ${e.message}")
            0.0
        }
    }

    override suspend fun getStudentExamPerformance(studentId: Long): Map<String, Any> {
        return try {
            // Placeholder: desempenho geral do aluno em exams
            mapOf(
                "average_score" to 0.0,
                "total_exams" to 0,
                "pass_rate" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER DESEMPENHO DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getClassExamRanking(examId: Long): List<ExamResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exam!inner(*), 
                        tb_student!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("exam_id", examId) }
                    order("score", Order.DESCENDING)
                    limit(50) // Top 50 ou ajusta
                }
                .decodeList<ExamResultModel>()
        } catch (e: Exception) {
            println("ERRO AO OBTER RANKING DA CLASSE NO EXAM $examId: ${e.message}")
            throw e
        }
    }
}

class ExerciseDataSourceImpl : ExerciseDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_exercise"

    override suspend fun createExercise(exercise: ExerciseModel): ExerciseModel {
        return try {
            val data = buildJsonObject {
                put("lesson_id", exercise.lessonId)
                put("question_text", exercise.questionType)
                put("question_type", exercise.questionType)
                put("difficulty_level", exercise.difficultyLevel.name)
                put("points", exercise.points)
                put("estimated_time", exercise.estimatedTime)
                put("order", exercise.order)
                put("is_active", exercise.isActive)
                put("options", exercise.options) // JSONB
                put("correct_answer", exercise.correctAnswer)
                put("explanation", exercise.explanation)
                // created_at, updated_at gerados
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_lesson!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR EXERCISE: ${e.message}")
            throw e
        }
    }

    override suspend fun createBulkExercises(exercises: List<ExerciseModel>): List<ExerciseModel> {
        return try {
            val dataList = exercises.map { ex ->
                buildJsonObject {
                    put("lesson_id", ex.lessonId)
                    put("question_text", ex.question)
                    put("question_type", ex.questionType)
                    put("difficulty_level", ex.difficultyLevel.name)
                    put("points", ex.points)
                    put("estimated_time", ex.estimatedTime)
                    put("order", ex.order)
                    put("is_active", ex.isActive)
                    put("options", ex.options)
                    put("correct_answer", ex.correctAnswer)
                    put("explanation", ex.explanation)
                }
            }

            client.postgrest[schema, table]
                .insert(dataList) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_lesson!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeList<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR BULK EXERCISES: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseById(id: Long): ExerciseModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<ExerciseModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXERCISE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getExercisesByLesson(
        lessonId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<ExerciseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("lesson_id", lessonId)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("order_index", Order.ASCENDING)
                }
                .decodeList<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXERCISES POR LESSON $lessonId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExercisesByType(
        questionType: String,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExerciseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("question_type", questionType)
                        lessonId?.let { eq("lesson_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("order_index", Order.ASCENDING)
                }
                .decodeList<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXERCISES POR TYPE $questionType: ${e.message}")
            throw e
        }
    }

    override suspend fun getExercisesByDifficulty(
        difficulty: DifficultyLevel,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExerciseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("difficulty_level", difficulty.name)
                        lessonId?.let { eq("lesson_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("order_index", Order.ASCENDING)
                }
                .decodeList<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXERCISES POR DIFICULDADE ${difficulty.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun searchExercises(
        query: String,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): List<ExerciseModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("question_text", "%$query%")
                            ilike("explanation", "%$query%")
                        }
                        lessonId?.let { eq("lesson_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("order_index", Order.ASCENDING)
                }
                .decodeList<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR EXERCISES '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateExercise(exercise: ExerciseModel): ExerciseModel {
        return try {
            client.postgrest[schema, table]
                .update(exercise) {
                    filter { eq("id", exercise.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_lesson!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExerciseModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR EXERCISE ${exercise.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateExerciseActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DO EXERCISE $id: ${e.message}")
            false
        }
    }

    override suspend fun reorderExercises(lessonId: Long, exerciseOrder: Map<Long, Int>): Boolean {
        return try {
            // Atualiza order_index para cada exercise
            exerciseOrder.forEach { (exerciseId, newOrder) ->
                client.postgrest[schema, table]
                    .update(mapOf("order_index" to newOrder)) {
                        filter {
                            eq("id", exerciseId)
                            eq("lesson_id", lessonId)
                        }
                    }
            }
            true
        } catch (e: Exception) {
            println("ERRO AO REORDENAR EXERCISES DA LESSON $lessonId: ${e.message}")
            false
        }
    }

    override suspend fun deleteExercise(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR EXERCISE $id: ${e.message}")
            false
        }
    }

    override suspend fun countExercisesByLesson(lessonId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("lesson_id", lessonId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR EXERCISES POR LESSON $lessonId: ${e.message}")
            throw e
        }
    }

    override suspend fun countExercisesByType(lessonId: Long): Map<String, Int> {
        return try {
            // Placeholder: usa RPC ou view para group by question_type
            mapOf("multiple_choice" to 0, "true_false" to 0, "essay" to 0)
        } catch (e: Exception) {
            println("ERRO AO CONTAR EXERCISES POR TYPE NA LESSON $lessonId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getExerciseDifficultyDistribution(lessonId: Long): Map<DifficultyLevel, Int> {
        return try {
            // Placeholder: conta por difficulty_level
            mapOf(
                DifficultyLevel.EASY to 0,
                DifficultyLevel.MEDIUM to 0,
                DifficultyLevel.HARD to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER DISTRIBUIÇÃO DE DIFICULDADE NA LESSON $lessonId: ${e.message}")
            emptyMap()
        }
    }
}

class ExerciseResultDataSourceImpl : ExerciseResultDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_exercise_result"

    override suspend fun createExerciseResult(result: ExerciseResultModel): ExerciseResultModel {
        return try {
            val data = buildJsonObject {
                put("exercise_id", result.exerciseId)
                put("student_id", result.studentId)
                put("attempts", result.attempts)
                put("score", result.score)
                put("is_correct", result.isCorrect)
                put("answer", result.answer)
                put("completed_at", result.completedAt)
                put("time_spent_seconds", result.timeSpentSeconds)
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_exercise!inner(*), 
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR RESULTADO DE EXERCISE: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseResultById(id: Long): ExerciseResultModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<ExerciseResultModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADO POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseResultsByExercise(exerciseId: Long, page: Int, pageSize: Int): List<ExerciseResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("exercise_id", exerciseId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADOS POR EXERCISE $exerciseId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseResultsByStudent(studentId: Long, page: Int, pageSize: Int): List<ExerciseResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADOS POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseResultByExerciseAndStudent(exerciseId: Long, studentId: Long): ExerciseResultModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("exercise_id", exerciseId)
                        eq("student_id", studentId)
                    }
                }
                .decodeList<ExerciseResultModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADO POR EXERCISE $exerciseId E STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentExerciseResults(studentId: Long, limit: Int): List<ExerciseResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    limit(limit.toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR RESULTADOS RECENTES DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getIncorrectExercises(studentId: Long, lessonId: Long?): List<ExerciseResultModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_exercise!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                        eq("is_correct", false)
                        lessonId?.let { eq("tb_exercise.lesson_id", it) }
                    }
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR EXERCISES INCORRETOS DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun updateExerciseResult(result: ExerciseResultModel): ExerciseResultModel {
        return try {
            client.postgrest[schema, table]
                .update(result) {
                    filter { eq("id", result.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_exercise!inner(*), 
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<ExerciseResultModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RESULTADO ${result.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun incrementAttempts(id: Long): Boolean {
        return try {


            client.postgrest.rpc(
                function = "increment_exercise_attempts",
                parameters = mapOf("result_id" to id)
            ).decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR TENTATIVAS DO RESULTADO $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteExerciseResult(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR RESULTADO $id: ${e.message}")
            false
        }
    }

    override suspend fun countExerciseResultsByStudent(studentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("student_id", studentId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR RESULTADOS POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getExerciseSuccessRate(studentId: Long, lessonId: Long?): Double {
        return try {
            val results = getExerciseResultsByStudent(studentId, 1, 1000).filter {
                lessonId == null || it.exercise?.lessonId == lessonId
            }

            if (results.isEmpty()) return 0.0
            val correct = results.count { it.isCorrect }
            (correct.toDouble() / results.size) * 100
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TAXA DE SUCESSO: ${e.message}")
            0.0
        }
    }

    override suspend fun getAverageAttemptsByExercise(exerciseId: Long): Double {
        return try {
            // Placeholder: média de attempts por exercise
            client.postgrest.rpc(
                function = "get_average_attempts_by_exercise",
                parameters = mapOf("p_exercise_id" to exerciseId)
            ).decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DE TENTATIVAS POR EXERCISE $exerciseId: ${e.message}")
            0.0
        }
    }

    override suspend fun getStudentExerciseProgress(studentId: Long, days: Int): Map<String, Any> {
        return try {
            // Placeholder: progresso por lição ou tipo de exercício nos últimos days
            mapOf(
                "completed" to 0,
                "success_rate" to 0.0,
                "total_attempts" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER PROGRESSO DE EXERCISES DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }
}

class GradeDataSourceImpl : GradeDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_grade"

    override suspend fun createGrade(grade: GradeModel): GradeModel {
        return try {
            val data = buildJsonObject {
                put("number", grade.number)
                put("level", grade.level.name)
                put("name", grade.name)
                put("age_range", grade.ageRange)
                put("subjects_count", grade.subjectsCount)
                put("is_active", grade.isActive)
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeSingle<GradeModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR GRADE: ${e.message}")
            throw e
        }
    }

    override suspend fun getGradeById(id: Long): GradeModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<GradeModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GRADE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getGradeByNumber(number: Int, level: GradeLevel): GradeModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("number", number)
                        eq("level", level.name)
                    }
                }
                .decodeList<GradeModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GRADE POR NÚMERO $number E LEVEL ${level.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllGrades(
        active: Boolean?,
        level: GradeLevel?,
        page: Int,
        pageSize: Int
    ): List<GradeModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("is_active", it) }
                        level?.let { eq("level", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("number", Order.ASCENDING)
                }
                .decodeList<GradeModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODAS AS GRADES: ${e.message}")
            throw e
        }
    }

    override suspend fun getGradesByLevel(
        level: GradeLevel,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<GradeModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("level", level.name)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("number", Order.ASCENDING)
                }
                .decodeList<GradeModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GRADES POR LEVEL ${level.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun searchGrades(query: String, page: Int, pageSize: Int): List<GradeModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("name", "%$query%")
                            ilike("description", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("number", Order.ASCENDING)
                }
                .decodeList<GradeModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR GRADES '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateGrade(grade: GradeModel): GradeModel {
        return try {
            client.postgrest[schema, table]
                .update(grade) {
                    filter { eq("id", grade.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<GradeModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR GRADE ${grade.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateGradeActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DA GRADE $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSubjectsCount(id: Long): Boolean {
        return try {
            // Placeholder - recalcula contagem de subjects (ideal com trigger)

            client.postgrest.rpc(
                function = "update_grade_subjects_count",
                parameters = mapOf("grade_id" to id)
            ).decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR CONTAGEM DE SUBJECTS DA GRADE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteGrade(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR GRADE $id: ${e.message}")
            false
        }
    }

    override suspend fun countGradesByLevel(): Map<GradeLevel, Int> {
        return try {
            // Placeholder: usa RPC ou view com group by level
            GradeLevel.entries.associateWith { 0 }
        } catch (e: Exception) {
            println("ERRO AO CONTAR GRADES POR LEVEL: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalGradesCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE GRADES: ${e.message}")
            throw e
        }
    }

    override suspend fun getGradeWithMostSubjects(): GradeModel? {
        return try {
            // Placeholder: usa RPC ou order by subjects_count desc limit 1
            null // Implementa com RPC real
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GRADE COM MAIS SUBJECTS: ${e.message}")
            null
        }
    }
}

class GuardianDataSourceImpl : GuardianDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_guardian"

    override suspend fun createGuardian(guardian: GuardianModel): GuardianModel {
        return try {
           /* val data = buildJsonObject {
                put("full_name", guardian.fullName)
                put("phone", guardian.phone)
                put("email", guardian.email)
                put("relation", guardian.relation.name)
                put("address", guardian.address)
                put("occupation", guardian.occupation)
                put("status", guardian.status.name)
                put("preferences", guardian.preferences)
                put("parental_control_active", guardian.parentalControlActive)
                put("blocked_categories", guardian.blockedCategories)
                put("time_limit_minutes", guardian.timeLimitMinutes)
                put("report_frequency", guardian.reportFrequency.name)
            }*/

            client.postgrest[schema, table]
                .insert(guardian) {
                    select(Columns.ALL)
                }
                .decodeSingle<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR GUARDIAN: ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardianById(id: Long): GuardianModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<GuardianModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GUARDIAN POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardianByPhone(phone: String): GuardianModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("phone", phone) }
                }
                .decodeList<GuardianModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GUARDIAN POR PHONE $phone: ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardianByEmail(email: String): GuardianModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("email", email) }
                }
                .decodeList<GuardianModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GUARDIAN POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllGuardians(
        active: Boolean?,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): List<GuardianModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        communeId?.let { eq("commune_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS GUARDIANS: ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardiansByCommune(communeId: Long, page: Int, pageSize: Int): List<GuardianModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("commune_id", communeId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GUARDIANS POR COMMUNE $communeId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchGuardians(
        query: String,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): List<GuardianModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("phone", "%$query%")
                            ilike("email", "%$query%")
                        }
                        communeId?.let { eq("commune_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR GUARDIANS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardiansWithMultipleStudents(minStudents: Int, page: Int, pageSize: Int): List<GuardianModel> {
        return try {
            // Placeholder: usa RPC ou view que conta students por guardian
            // Exemplo: join com tb_student e having count(*) >= minStudents
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    // Filtro simulado
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                }
                .decodeList<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR GUARDIANS COM MÚLTIPLOS ALUNOS: ${e.message}")
            throw e
        }
    }

    override suspend fun updateGuardian(guardian: GuardianModel): GuardianModel {
        return try {
            client.postgrest[schema, table]
                .update(guardian) {
                    filter { eq("id", guardian.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<GuardianModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR GUARDIAN ${guardian.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateGuardianStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateGuardianPreferences(id: Long, preferences: NotificationPreferences): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("preferences" to preferences)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PREFERENCES DO GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateParentalControl(
        id: Long,
        active: Boolean,
        blockedCategories: List<String>?,
        timeLimit: Int?
    ): Boolean {
        return try {
            val updates = buildMap {
                put("parental_control_active", active)
                blockedCategories?.let { put("blocked_categories", it) }
                timeLimit?.let { put("time_limit_minutes", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR CONTROLE PARENTAL DO GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateReportFrequency(id: Long, frequency: ReportFrequency): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("report_frequency" to frequency.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR FREQUÊNCIA DE RELATÓRIOS DO GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteGuardian(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteGuardian(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR GUARDIAN $id: ${e.message}")
            false
        }
    }

    override suspend fun countGuardiansByCommune(communeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("commune_id", communeId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR GUARDIANS POR COMMUNE $communeId: ${e.message}")
            throw e
        }
    }

    override suspend fun countGuardiansByRelation(): Map<GuardianRelation, Int> {
        return try {
            // Placeholder: RPC ou view com group by relation
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR GUARDIANS POR RELAÇÃO: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalGuardiansCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE GUARDIANS: ${e.message}")
            throw e
        }
    }

    override suspend fun getGuardianStudentStats(guardianId: Long): Map<String, Any> {
        return try {
            // Placeholder: número de filhos, desempenho médio, etc.
            mapOf(
                "student_count" to 0,
                "average_performance" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DO GUARDIAN $guardianId: ${e.message}")
            emptyMap()
        }
    }
}

class LessonDataSourceImpl : LessonDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_lesson"

    override suspend fun createLesson(lesson: LessonModel): LessonModel {
        return try {


            client.postgrest[schema, table]
                .insert(lesson) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_grade!inner(*), 
                            tb_subject!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR LESSON: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonById(id: Long): LessonModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<LessonModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSON POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonsByGrade(
        gradeId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("grade_id", gradeId)
                        approved?.let { eq("approved", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS POR GRADE $gradeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonsBySubject(
        subjectId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("subject_id", subjectId)
                        approved?.let { eq("approved", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonsByTeacher(
        teacherId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        approved?.let { eq("approved", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getFeaturedLessons(limit: Int): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("is_featured", true) }
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS FEATURED: ${e.message}")
            throw e
        }
    }

    override suspend fun getFreeLessons(page: Int, pageSize: Int): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("is_free", true) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS GRATUITAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getPopularLessons(limit: Int): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    order("view_count", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS POPULARES: ${e.message}")
            throw e
        }
    }

    override suspend fun searchLessons(
        query: String,
        gradeId: Long?,
        subjectId: Long?,
        page: Int,
        pageSize: Int
    ): List<LessonModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_grade!inner(*), 
                        tb_subject!inner(*), 
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("title", "%$query%")
                            ilike("description", "%$query%")
                        }
                        gradeId?.let { eq("grade_id", it) }
                        subjectId?.let { eq("subject_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR LESSONS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateLesson(lesson: LessonModel): LessonModel {
        return try {
            client.postgrest[schema, table]
                .update(lesson) {
                    filter { eq("id", lesson.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_grade!inner(*), 
                            tb_subject!inner(*), 
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<LessonModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LESSON ${lesson.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun approveLesson(id: Long, approvedBy: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "approved" to true,
                        "approved_by" to approvedBy,
                        "approved_at" to Clock.System.now().toString()
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO APROVAR LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun rejectLesson(id: Long, reason: String?): Boolean {
        return try {
            val updates = buildMap {
                put("approved", false)
                reason?.let { put("rejection_reason", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO REJEITAR LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun updateLessonStatistics(id: Long): Boolean {
        return try {
            // Placeholder - atualiza view_count, rating, etc.
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DA LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun incrementViewCount(id: Long): Boolean {
        return try {

            client.postgrest.rpc(
                function = "increment_lesson_view_count",
                parameters = mapOf("lesson_id" to id)
            ).decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR VIEW COUNT DA LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun markAsFeatured(id: Long, featured: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_featured" to featured)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO MARCAR LESSON $id COMO FEATURED: ${e.message}")
            false
        }
    }

    override suspend fun updateLessonRating(id: Long, rating: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("rating" to rating)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RATING DA LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteLesson(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Int>()
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun countLessonsByTeacher(teacherId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LESSONS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countLessonsBySubject(subjectId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("subject_id", subjectId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LESSONS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTotalLessonsCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE LESSONS: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonEngagementStatistics(lessonId: Long): Map<String, Any> {
        return try {
            // Placeholder: views, ratings, completion rate, etc.
            mapOf(
                "view_count" to 0,
                "average_rating" to 0.0,
                "completion_rate" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE ENGAGEMENT DA LESSON $lessonId: ${e.message}")
            emptyMap()
        }
    }
}

class LessonProgressDataSourceImpl : LessonProgressDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_lesson_progress"

    override suspend fun createLessonProgress(progress: LessonProgressModel): LessonProgressModel {
        return try {


            client.postgrest[schema, table]
                .insert(progress) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_lesson!inner(*), 
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR PROGRESSO DE LESSON: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonProgressById(id: Long): LessonProgressModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<LessonProgressModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PROGRESSO POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonProgressByLessonAndStudent(lessonId: Long, studentId: Long): LessonProgressModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("lesson_id", lessonId)
                        eq("student_id", studentId)
                    }
                }
                .decodeList<LessonProgressModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PROGRESSO POR LESSON $lessonId E STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getProgressByStudent(
        studentId: Long,
        completed: Boolean?,
        page: Int,
        pageSize: Int
    ): List<LessonProgressModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                        completed?.let { eq("completed", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PROGRESSO POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getProgressByLesson(
        lessonId: Long,
        completed: Boolean?,
        page: Int,
        pageSize: Int
    ): List<LessonProgressModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("lesson_id", lessonId)
                        completed?.let { eq("completed", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PROGRESSO POR LESSON $lessonId: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentProgress(studentId: Long, limit: Int): List<LessonProgressModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    limit(limit.toLong())
                    order("completed_at", Order.DESCENDING)
                }
                .decodeList<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PROGRESSO RECENTE DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getBookmarkedLessons(studentId: Long, page: Int, pageSize: Int): List<LessonProgressModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                        eq("bookmarked", true)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("updated_at", Order.DESCENDING)
                }
                .decodeList<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS BOOKMARKED DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getInProgressLessons(studentId: Long): List<LessonProgressModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_lesson!inner(*), 
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                        gt("progress_percentage", 0)
                        lt("progress_percentage", 100)
                    }
                    order("updated_at", Order.DESCENDING)
                }
                .decodeList<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LESSONS EM PROGRESSO DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun updateLessonProgress(progress: LessonProgressModel): LessonProgressModel {
        return try {
            client.postgrest[schema, table]
                .update(progress) {
                    filter { eq("id", progress.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_lesson!inner(*), 
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<LessonProgressModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PROGRESSO ${progress.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateProgressPercentage(id: Long, percentage: Int, lastPosition: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "progress_percentage" to percentage,
                        "last_position" to lastPosition,
                        "updated_at" to Clock.System.now().toString()
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()

        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PORCENTAGEM DO PROGRESSO $id: ${e.message}")
            false
        }
    }

    override suspend fun markAsCompleted(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "completed" to true,
                        "progress_percentage" to 100,
                        "completed_at" to Clock.System.now().toString()
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR LESSON COMO COMPLETA $id: ${e.message}")
            false
        }
    }

    override suspend fun toggleBookmark(id: Long, bookmarked: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("bookmarked" to bookmarked)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO TOGGLEAR BOOKMARK DO PROGRESSO $id: ${e.message}")
            false
        }
    }

    override suspend fun addNote(id: Long, note: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("notes" to note)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ADICIONAR NOTA NO PROGRESSO $id: ${e.message}")
            false
        }
    }

    override suspend fun rateLesson(id: Long, rating: Int, feedback: String?): Boolean {
        return try {
            val updates = buildMap {
                put("rating", rating)
                feedback?.let { put("feedback", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO AVALIAR LESSON $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteLessonProgress(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR PROGRESSO $id: ${e.message}")
            false
        }
    }

    override suspend fun countCompletedLessons(studentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("student_id", studentId)
                        eq("completed", true)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LESSONS COMPLETAS DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageCompletionTime(studentId: Long): Double {
        return try {
            // Placeholder: média de tempo gasto (precisa de coluna time_spent ou cálculo)
            client.postgrest.rpc("get_average_completion_time", mapOf("student_id" to studentId))
                .decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TEMPO MÉDIO DE COMPLETION: ${e.message}")
            0.0
        }
    }

    override suspend fun getStudentLearningTrend(studentId: Long, days: Int): Map<String, Int> {
        return try {
            // Placeholder: progresso diário ou semanal nos últimos days
            mapOf(
                "completed_lessons" to 0,
                "total_time_minutes" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER TENDÊNCIA DE APRENDIZAGEM DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getLessonCompletionRate(lessonId: Long): Double {
        return try {
            // Placeholder: % de alunos que completaram a lesson
            0.0
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TAXA DE COMPLETION DA LESSON $lessonId: ${e.message}")
            0.0
        }
    }
}

class MessageDataSourceImpl : MessageDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_message"

    override suspend fun createMessage(message: MessageModel): MessageModel {
        return try {

            client.postgrest[schema, table]
                .insert(message) {
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_sender!inner(*), 
                            tb_receiver!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR MESSAGE: ${e.message}")
            throw e
        }
    }

    override suspend fun getMessageById(id: Long): MessageModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<MessageModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR MESSAGE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getMessagesBySender(senderId: Long, page: Int, pageSize: Int): List<MessageModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("sender_id", senderId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR MESSAGES POR SENDER $senderId: ${e.message}")
            throw e
        }
    }

    override suspend fun getMessagesByReceiver(receiverId: Long, page: Int, pageSize: Int): List<MessageModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("receiver_id", receiverId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR MESSAGES POR RECEIVER $receiverId: ${e.message}")
            throw e
        }
    }

    override suspend fun getConversation(user1Id: Long, user2Id: Long, page: Int, pageSize: Int): List<MessageModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            and {
                                eq("sender_id", user1Id)
                                eq("receiver_id", user2Id)
                            }
                            and {
                                eq("sender_id", user2Id)
                                eq("receiver_id", user1Id)
                            }
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.ASCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CONVERSA ENTRE $user1Id E $user2Id: ${e.message}")
            throw e
        }
    }

    override suspend fun getUnreadMessages(userId: Long): List<MessageModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("receiver_id", userId)
                        eq("is_read", false)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR MESSAGES NÃO LIDAS PARA $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentConversations(userId: Long, limit: Int): List<MessageModel> {
        return try {
            // Placeholder: últimas mensagens por conversa (pode precisar de distinct ou RPC)
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            eq("sender_id", userId)
                            eq("receiver_id", userId)
                        }
                    }
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CONVERSAS RECENTES DE $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchMessages(
        query: String,
        userId: Long?,
        page: Int,
        pageSize: Int
    ): List<MessageModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *, 
                        tb_sender!inner(*), 
                        tb_receiver!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        ilike("content", "%$query%")
                        userId?.let {
                            or {
                                eq("sender_id", it)
                                eq("receiver_id", it)
                            }
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR MESSAGES '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateMessage(message: MessageModel): MessageModel {
        return try {
            client.postgrest[schema, table]
                .update(message) {
                    filter { eq("id", message.id) }
                    select(
                        columns = Columns.raw("""
                            *, 
                            tb_sender!inner(*), 
                            tb_receiver!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<MessageModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR MESSAGE ${message.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun markAsRead(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "is_read" to true,
                        "read_at" to Clock.System.now().toString()
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR MESSAGE $id COMO LIDA: ${e.message}")
            false
        }
    }

    override suspend fun markMultipleAsRead(messageIds: List<Long>): Boolean {
        return try {
            // Supabase suporta update com in filter
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "is_read" to true,
                        "read_at" to Clock.System.now().toString()
                    )
                ) {
                  //  filter { `in`("id", messageIds) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR MÚLTIPLAS MESSAGES COMO LIDAS: ${e.message}")
            false
        }
    }

    override suspend fun deleteMessage(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR MESSAGE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteConversation(user1Id: Long, user2Id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter {
                        or {
                            and {
                                eq("sender_id", user1Id)
                                eq("receiver_id", user2Id)
                            }
                            and {
                                eq("sender_id", user2Id)
                                eq("receiver_id", user1Id)
                            }
                        }
                    }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR CONVERSA ENTRE $user1Id E $user2Id: ${e.message}")
            false
        }
    }

    override suspend fun countUnreadMessages(userId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("receiver_id", userId)
                        eq("is_read", false)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR MESSAGES NÃO LIDAS DE $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun countMessagesByType(userId: Long): Map<MessageType, Int> {
        return try {
            // Placeholder: RPC ou view com group by message_type
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR MESSAGES POR TYPE DE $userId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getMessageActivity(userId: Long, days: Int): Map<String, Int> {
        return try {
            // Placeholder: mensagens por dia nos últimos days
            mapOf("total" to 0, "sent" to 0, "received" to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER ATIVIDADE DE MESSAGES DE $userId: ${e.message}")
            emptyMap()
        }
    }
}


class NotificationDataSourceImpl : NotificationDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_notification"

    override suspend fun createNotification(notification: NotificationModel): NotificationModel {
        return try {

            client.postgrest[schema, table]
                .insert(notification) {
                    select(Columns.ALL)
                }
                .decodeSingle<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR NOTIFICATION: ${e.message}")
            throw e
        }
    }

    override suspend fun createBulkNotifications(notifications: List<NotificationModel>): List<NotificationModel> {
        return try {

            client.postgrest[schema, table]
                .insert(notifications) {
                    select(Columns.ALL)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR BULK NOTIFICATIONS: ${e.message}")
            throw e
        }
    }

    override suspend fun getNotificationById(id: Long): NotificationModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<NotificationModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATION POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getNotificationsByUser(
        userId: Long,
        read: Boolean?,
        page: Int,
        pageSize: Int
    ): List<NotificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        read?.let { eq("is_read", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATIONS POR USER $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getNotificationsByType(
        notificationType: NotificationType,
        userId: Long?,
        page: Int,
        pageSize: Int
    ): List<NotificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("notification_type", notificationType.name)
                        userId?.let { eq("user_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATIONS POR TYPE ${notificationType.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getUnreadNotifications(userId: Long, limit: Int): List<NotificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("is_read", false)
                    }
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATIONS NÃO LIDAS DE $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentNotifications(userId: Long, limit: Int): List<NotificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("user_id", userId) }
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATIONS RECENTES DE $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getNotificationsByRelated(
        relatedId: Long,
        relatedType: String,
        page: Int,
        pageSize: Int
    ): List<NotificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("related_id", relatedId)
                        eq("related_type", relatedType)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR NOTIFICATIONS POR RELATED $relatedId ($relatedType): ${e.message}")
            throw e
        }
    }

    override suspend fun updateNotification(notification: NotificationModel): NotificationModel {
        return try {
            client.postgrest[schema, table]
                .update(notification) {
                    filter { eq("id", notification.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<NotificationModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR NOTIFICATION ${notification.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun markAsRead(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "is_read" to true,
                        "read_at" to Clock.System.now().toString()
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR NOTIFICATION $id COMO LIDA: ${e.message}")
            false
        }
    }

    override suspend fun markAllAsRead(userId: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "is_read" to true,
                        "read_at" to Clock.System.now().toString()
                    )
                ) {
                    filter {
                        eq("user_id", userId)
                        eq("is_read", false)
                    }
                    //eq("is_read", false)
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR TODAS AS NOTIFICATIONS COMO LIDAS PARA $userId: ${e.message}")
            false
        }
    }

    override suspend fun deleteNotification(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR NOTIFICATION $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteExpiredNotifications(): Int {
        return try {
            // Placeholder: deleta notificações antigas ou expiradas (usa RPC ou filter)
            0
        } catch (e: Exception) {
            println("ERRO AO DELETAR NOTIFICATIONS EXPIRADAS: ${e.message}")
            0
        }
    }

    override suspend fun deleteOldNotifications(days: Int): Int {
        return try {
            //val since = Clock.System.now().minus(DatePeriod(days = days)).toLocalDateTime(TimeZone.currentSystemDefault()).toString()
           // val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val future = Clock.System.todayIn(TimeZone.currentSystemDefault())
                .plus(DatePeriod(days = days))
                .toString()
            val response = client.postgrest[schema, table]
                .delete {
                    filter { lt("created_at", future) }
                }

            // Supabase delete não retorna count nativamente, usa RPC se precisar
            0 // Ajusta com RPC se quiser contar deletados
        } catch (e: Exception) {
            println("ERRO AO DELETAR NOTIFICATIONS ANTIGAS: ${e.message}")
            0
        }
    }

    override suspend fun countUnreadNotifications(userId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("user_id", userId)
                        eq("is_read", false)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR NOTIFICATIONS NÃO LIDAS DE $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun countNotificationsByType(userId: Long): Map<NotificationType, Int> {
        return try {
            // Placeholder: RPC com group by
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR NOTIFICATIONS POR TYPE DE $userId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getNotificationDeliveryStats(days: Int): Map<String, Any> {
        return try {
            // Placeholder: entregues, lidas, falhas nos últimos days
            mapOf("delivered" to 0, "read" to 0, "failed" to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE ENTREGA DE NOTIFICATIONS: ${e.message}")
            emptyMap()
        }
    }
}

class PaymentDataSourceImpl : PaymentDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_payment"

    override suspend fun createPayment(payment: PaymentModel): PaymentModel {
        return try {

            client.postgrest[schema, table]
                .insert(payment) {
                    select(Columns.ALL)
                }
                .decodeSingle<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR PAYMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getPaymentById(id: Long): PaymentModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<PaymentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getPaymentByTransactionId(transactionId: String): PaymentModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("transaction_id", transactionId) }
                }
                .decodeList<PaymentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENT POR TRANSACTION ID $transactionId: ${e.message}")
            throw e
        }
    }

    override suspend fun getPaymentsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<PaymentModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENTS POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun getPaymentsByStatus(status: String, page: Int, pageSize: Int): List<PaymentModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", status) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENTS POR STATUS $status: ${e.message}")
            throw e
        }
    }

    override suspend fun getPaymentsByDateRange(
        startDate: String,
        endDate: String,
        userId: Long?,
        page: Int,
        pageSize: Int
    ): List<PaymentModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        gte("payment_date", startDate)
                        lte("payment_date", endDate)
                        userId?.let { eq("user_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENTS POR DATA RANGE $startDate - $endDate: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentPayments(limit: Int): List<PaymentModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    limit(limit.toLong())
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PAYMENTS RECENTES: ${e.message}")
            throw e
        }
    }

    override suspend fun searchPayments(query: String, page: Int, pageSize: Int): List<PaymentModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("transaction_id", "%$query%")
                            ilike("reference", "%$query%")
                            ilike("status", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR PAYMENTS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updatePayment(payment: PaymentModel): PaymentModel {
        return try {
            client.postgrest[schema, table]
                .update(payment) {
                    filter { eq("id", payment.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<PaymentModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PAYMENT ${payment.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updatePaymentStatus(id: Long, status: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO PAYMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun deletePayment(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR PAYMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun countPaymentsByStatus(): Map<String, Int> {
        return try {
            // Placeholder: RPC com group by status
            mapOf("PENDING" to 0, "COMPLETED" to 0, "FAILED" to 0)
        } catch (e: Exception) {
            println("ERRO AO CONTAR PAYMENTS POR STATUS: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalRevenue(startDate: String, endDate: String): Double {
        return try {
            // Placeholder: RPC que soma amount onde status = COMPLETED
            client.postgrest.rpc("get_total_revenue", mapOf(
                "start_date" to startDate,
                "end_date" to endDate
            )).decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR RECEITA TOTAL: ${e.message}")
            0.0
        }
    }

    override suspend fun getAveragePaymentAmount(): Double {
        return try {
            // Placeholder: RPC para média de amount
            client.postgrest.rpc("get_average_payment_amount").decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DE PAGAMENTOS: ${e.message}")
            0.0
        }
    }

    override suspend fun getPaymentMethodDistribution(): Map<String, Int> {
        return try {
            // Placeholder: RPC com group by payment_method
            mapOf("CREDIT_CARD" to 0, "MOBILE_MONEY" to 0, "BANK_TRANSFER" to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER DISTRIBUIÇÃO DE MÉTODOS DE PAGAMENTO: ${e.message}")
            emptyMap()
        }
    }
}

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

    override suspend fun countSchoolsByType(): Map<Long, Int> {
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

class SchoolDirectorDataSourceImpl : SchoolDirectorDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_school_director"

    override suspend fun createDirector(director: SchoolDirectorModel): SchoolDirectorModel {
        return try {


            client.postgrest[schema, table]
                .insert(director) {
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolDirectorModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SCHOOL DIRECTOR: ${e.message}")
            throw e
        }
    }

    override suspend fun getDirectorById(id: Long): SchoolDirectorModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SchoolDirectorModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR DIRECTOR POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getDirectorByEmail(email: String): SchoolDirectorModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("email", email) }
                }
                .decodeList<SchoolDirectorModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR DIRECTOR POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getDirectorBySchool(schoolId: Long): SchoolDirectorModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<SchoolDirectorModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR DIRECTOR POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllDirectors(active: Boolean?, page: Int, pageSize: Int): List<SchoolDirectorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SchoolDirectorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS DIRECTORS: ${e.message}")
            throw e
        }
    }

    override suspend fun searchDirectors(query: String, page: Int, pageSize: Int): List<SchoolDirectorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("email", "%$query%")
                            ilike("phone", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SchoolDirectorModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR DIRECTORS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getDirectorsWithExpiringLicense(days: Int): List<SchoolDirectorModel> {
        return try {

            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val sinceDate = today.minus(DatePeriod(days = days))
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { lte("license_expiration_date", sinceDate) }
                    order("license_expiration_date", Order.ASCENDING)
                }
                .decodeList<SchoolDirectorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR DIRECTORS COM LICENÇA A EXPIRAR EM $days dias: ${e.message}")
            throw e
        }
    }

    override suspend fun updateDirector(director: SchoolDirectorModel): SchoolDirectorModel {
        return try {
            client.postgrest[schema, table]
                .update(director) {
                    filter { eq("id", director.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolDirectorModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR DIRECTOR ${director.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateDirectorStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO DIRECTOR $id: ${e.message}")
            false
        }
    }

    override suspend fun updateLicenseStatus(id: Long, active: Boolean, expirationDate: String?): Boolean {
        return try {
            val updates = buildMap {
                put("license_active", active)
                expirationDate?.let { put("license_expiration", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DA LICENÇA DO DIRECTOR $id: ${e.message}")
            false
        }
    }

    override suspend fun updateDirectorPermissions(
        id: Long,
        canManageTeachers: Boolean?,
        canManageStudents: Boolean?,
        canViewReports: Boolean?,
        canApproveContent: Boolean?
    ): Boolean {
        return try {
            val updates = buildMap {
                canManageTeachers?.let { put("can_manage_teachers", it) }
                canManageStudents?.let { put("can_manage_students", it) }
                canViewReports?.let { put("can_view_reports", it) }
                canApproveContent?.let { put("can_approve_content", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PERMISSÕES DO DIRECTOR $id: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteDirector(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE DIRECTOR $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteDirector(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR DIRECTOR $id: ${e.message}")
            false
        }
    }

    override suspend fun countDirectorsBySchool(): Map<Long, Int> {
        return try {
            // Placeholder: RPC com group by school_id
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR DIRECTORS POR SCHOOL: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalDirectorsCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE DIRECTORS: ${e.message}")
            throw e
        }
    }

    override suspend fun getDirectorLicenseStats(): Map<String, Any> {
        return try {
            // Placeholder: licenças ativas, expiradas, próximas a expirar
            mapOf(
                "active_licenses" to 0,
                "expiring_soon" to 0,
                "expired" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE LICENÇAS DOS DIRECTORS: ${e.message}")
            emptyMap()
        }
    }
}

class SchoolTypeDataSourceImpl : SchoolTypeDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_school_type"

    override suspend fun createSchoolType(schoolType: SchoolTypeModel): SchoolTypeModel {
        return try {
            val data = buildJsonObject {
                put("name", schoolType.name)
                put("code", schoolType.code)
                put("description", schoolType.description)
                put("is_active", schoolType.isActive)
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolTypeModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SCHOOL TYPE: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolTypeById(id: Long): SchoolTypeModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SchoolTypeModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOL TYPE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSchoolTypeByCode(code: String): SchoolTypeModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("code", code) }
                }
                .decodeList<SchoolTypeModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SCHOOL TYPE POR CODE $code: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllSchoolTypes(active: Boolean?, page: Int, pageSize: Int): List<SchoolTypeModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { active?.let { eq("is_active", it) } }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolTypeModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS SCHOOL TYPES: ${e.message}")
            throw e
        }
    }

    override suspend fun searchSchoolTypes(query: String, page: Int, pageSize: Int): List<SchoolTypeModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("name", "%$query%")
                            ilike("code", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SchoolTypeModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR SCHOOL TYPES '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateSchoolType(schoolType: SchoolTypeModel): SchoolTypeModel {
        return try {
            client.postgrest[schema, table]
                .update(schoolType) {
                    filter { eq("id", schoolType.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SchoolTypeModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SCHOOL TYPE ${schoolType.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSchoolTypeActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DO SCHOOL TYPE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteSchoolType(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SCHOOL TYPE $id: ${e.message}")
            false
        }
    }

    override suspend fun countSchoolsByType(): Map<Long, Int> {
        return try {
            // Placeholder: RPC com group by school_type_id
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SCHOOLS POR TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalSchoolTypesCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE SCHOOL TYPES: ${e.message}")
            throw e
        }
    }
}

class StateManagerDataSourceImpl : StateManagerDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_state_manager"

    override suspend fun createStateManager(manager: StateManagerModel): StateManagerModel {
        return try {

            client.postgrest[schema, table]
                .insert(manager) {
                    select(Columns.ALL)
                }
                .decodeSingle<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR STATE MANAGER: ${e.message}")
            throw e
        }
    }

    override suspend fun getStateManagerById(id: Long): StateManagerModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<StateManagerModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STATE MANAGER POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getStateManagerByEmail(email: String): StateManagerModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("email", email) }
                }
                .decodeList<StateManagerModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STATE MANAGER POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllStateManagers(
        active: Boolean?,
        accessLevel: ManagerAccessLevel?,
        page: Int,
        pageSize: Int
    ): List<StateManagerModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        accessLevel?.let { eq("access_level", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS STATE MANAGERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getStateManagersByProvince(provinceId: Long, page: Int, pageSize: Int): List<StateManagerModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("province_id", provinceId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STATE MANAGERS POR PROVINCE $provinceId: ${e.message}")
            throw e
        }
    }

    override suspend fun getStateManagersByAccessLevel(level: ManagerAccessLevel, page: Int, pageSize: Int): List<StateManagerModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("access_level", level.name) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STATE MANAGERS POR ACCESS LEVEL ${level.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun searchStateManagers(query: String, page: Int, pageSize: Int): List<StateManagerModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("email", "%$query%")
                            ilike("phone", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR STATE MANAGERS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateStateManager(manager: StateManagerModel): StateManagerModel {
        return try {
            client.postgrest[schema, table]
                .update(manager) {
                    filter { eq("id", manager.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<StateManagerModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATE MANAGER ${manager.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateStateManagerStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStateManagerAccessLevel(id: Long, accessLevel: ManagerAccessLevel): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("access_level" to accessLevel.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ACCESS LEVEL DO STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStateManagerPermissions(
        id: Long,
        canExportData: Boolean?,
        canViewNationalReports: Boolean?,
        canApproveContent: Boolean?
    ): Boolean {
        return try {
            val updates = buildMap {
                canExportData?.let { put("can_export_data", it) }
                canViewNationalReports?.let { put("can_view_national_reports", it) }
                canApproveContent?.let { put("can_approve_content", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PERMISSÕES DO STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun incrementReportsGenerated(id: Long): Boolean {
        return try {
            client.postgrest.rpc(
                function = "increment_reports_generated",
                parameters = mapOf("manager_id" to id)
            ).decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR REPORTS GENERATED DO STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteStateManager(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteStateManager(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR STATE MANAGER $id: ${e.message}")
            false
        }
    }

    override suspend fun countStateManagersByAccessLevel(): Map<ManagerAccessLevel, Int> {
        return try {
            // Placeholder: RPC com group by access_level
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR STATE MANAGERS POR ACCESS LEVEL: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun countStateManagersByProvince(provinceId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("province_id", provinceId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR STATE MANAGERS POR PROVINCE $provinceId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTotalStateManagersCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE STATE MANAGERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getManagerActivityStats(managerId: Long): Map<String, Any> {
        return try {
            // Placeholder: relatórios gerados, ações, etc.
            mapOf(
                "reports_generated" to 0,
                "last_activity" to ""
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DO STATE MANAGER $managerId: ${e.message}")
            emptyMap()
        }
    }
}

class StudentDataSourceImpl : StudentDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_student"

    override suspend fun createStudent(student: StudentModel): StudentModel {
        return try {
            val data = buildJsonObject {
                put("full_name", student.fullName)
                put("student_number", student.studentNumber)
                put("email", student.email)
                put("phone", student.phone)
                put("birth_date", student.birthDate)
                put("gender", student.gender.name)
                put("school_id", student.schoolId)
                put("classe_id", student.classeId)
                put("grade_id", student.gradeId)
                put("guardian_id", student.guardianId)
                put("account_type", student.accountType.name)
                put("status", student.status.name)
                put("points", 0)
                put("level", 1)
                put("current_streak", 0)
                put("longest_streak", 0)
                put("last_login", null)
                // Adiciona outros campos obrigatórios se existirem
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_school!inner(*),
                            tb_classe!inner(*),
                            tb_grade!inner(*),
                            tb_guardian!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR STUDENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentById(id: Long): StudentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<StudentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentByNumber(studentNumber: String): StudentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_number", studentNumber) }
                }
                .decodeList<StudentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENT POR NÚMERO $studentNumber: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentByEmail(email: String): StudentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("email", email) }
                }
                .decodeList<StudentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENT POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllStudents(
        active: Boolean?,
        accountType: StudentAccountType?,
        page: Int,
        pageSize: Int
    ): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        accountType?.let { eq("account_type", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS STUDENTS: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentsBySchool(
        schoolId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("school_id", schoolId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENTS POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentsByClasse(
        classeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentsByGrade(gradeId: Long, page: Int, pageSize: Int): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("grade_id", gradeId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENTS POR GRADE $gradeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getStudentsByGuardian(guardianId: Long, page: Int, pageSize: Int): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("guardian_id", guardianId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENTS POR GUARDIAN $guardianId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchStudents(
        query: String,
        schoolId: Long?,
        page: Int,
        pageSize: Int
    ): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("student_number", "%$query%")
                            ilike("email", "%$query%")
                        }
                        schoolId?.let { eq("school_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR STUDENTS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getTopStudentsByPoints(limit: Int): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    order("points", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TOP STUDENTS POR PONTOS: ${e.message}")
            throw e
        }
    }

    override suspend fun getActiveStudentsWithStreak(minStreak: Int): List<StudentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*),
                        tb_classe!inner(*),
                        tb_grade!inner(*),
                        tb_guardian!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("status", "ACTIVE")
                        gte("current_streak", minStreak)
                    }
                    order("current_streak", Order.DESCENDING)
                }
                .decodeList<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR STUDENTS ATIVOS COM STREAK >= $minStreak: ${e.message}")
            throw e
        }
    }

    override suspend fun updateStudent(student: StudentModel): StudentModel {
        return try {
            client.postgrest[schema, table]
                .update(student) {
                    filter { eq("id", student.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_school!inner(*),
                            tb_classe!inner(*),
                            tb_grade!inner(*),
                            tb_guardian!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<StudentModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STUDENT ${student.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateStudentStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentAccountType(id: Long, accountType: StudentAccountType): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("account_type" to accountType.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ACCOUNT TYPE DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentPoints(id: Long, points: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("points" to points)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PONTOS DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentLevel(id: Long, level: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("level" to level)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LEVEL DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentStreak(id: Long, streakDays: Int, currentStreak: Int, longestStreak: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "streak_days" to streakDays,
                        "current_streak" to currentStreak,
                        "longest_streak" to longestStreak
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STREAK DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateStudentStatistics(id: Long): Boolean {
        return try {
            // Placeholder: atualiza médias, contagens, etc. (pode usar RPC)
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateLastLogin(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("last_login" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LAST LOGIN DO STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun assignGuardian(studentId: Long, guardianId: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("guardian_id" to guardianId)) {
                    filter { eq("id", studentId) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ASSOCIAR GUARDIAN $guardianId AO STUDENT $studentId: ${e.message}")
            false
        }
    }

    override suspend fun assignToClasse(studentId: Long, classeId: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("classe_id" to classeId)) {
                    filter { eq("id", studentId) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ASSOCIAR CLASSE $classeId AO STUDENT $studentId: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteStudent(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteStudent(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR STUDENT $id: ${e.message}")
            false
        }
    }

    override suspend fun countStudentsBySchool(schoolId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR STUDENTS POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun countStudentsByGrade(gradeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("grade_id", gradeId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR STUDENTS POR GRADE $gradeId: ${e.message}")
            throw e
        }
    }

    override suspend fun countStudentsByClasse(classeId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("classe_id", classeId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR STUDENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun countStudentsByAccountType(): Map<StudentAccountType, Int> {
        return try {
            // Placeholder: ideal com RPC ou view com group by account_type
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR STUDENTS POR ACCOUNT TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalStudentsCount(schoolId: Long?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    schoolId?.let { filter { eq("school_id", it) } }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE STUDENTS: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageStudentScore(schoolId: Long?): Double {
        return try {
            // Placeholder: RPC ou subquery para média de notas (exams/results)
            client.postgrest.rpc("get_average_student_score", mapOf("school_id" to schoolId))
                .decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DE NOTAS DOS STUDENTS: ${e.message}")
            0.0
        }
    }

    override suspend fun getStudentProgressStatistics(studentId: Long): Map<String, Any> {
        return try {
            // Placeholder: aulas completas, exercícios feitos, taxa de sucesso, etc.
            mapOf(
                "completed_lessons" to 0,
                "success_rate_exercises" to 0.0,
                "total_points" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE PROGRESSO DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }
}
class StudentAchievementDataSourceImpl : StudentAchievementDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_student_achievement"

    override suspend fun createAchievement(achievement: StudentAchievementModel): StudentAchievementModel {
        return try {

            client.postgrest[schema, table]
                .insert(achievement) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR ACHIEVEMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getAchievementById(id: Long): StudentAchievementModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<StudentAchievementModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACHIEVEMENT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getAchievementsByStudent(studentId: Long, page: Int, pageSize: Int): List<StudentAchievementModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("achieved_at", Order.DESCENDING)
                }
                .decodeList<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACHIEVEMENTS POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAchievementsByType(type: String, studentId: Long?, page: Int, pageSize: Int): List<StudentAchievementModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("type", type)
                        studentId?.let { eq("student_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("achieved_at", Order.DESCENDING)
                }
                .decodeList<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACHIEVEMENTS POR TYPE $type: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentAchievements(studentId: Long, limit: Int): List<StudentAchievementModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("student_id", studentId) }
                    limit(limit.toLong())
                    order("achieved_at", Order.DESCENDING)
                }
                .decodeList<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACHIEVEMENTS RECENTES DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAchievementsWithBadges(studentId: Long, page: Int, pageSize: Int): List<StudentAchievementModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*),
                        tb_badge!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("student_id", studentId)
                       // isNotNull("badge_id")
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("achieved_at", Order.DESCENDING)
                }
                .decodeList<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACHIEVEMENTS COM BADGES DO STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun searchAchievements(query: String, studentId: Long?, page: Int, pageSize: Int): List<StudentAchievementModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_student!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("title", "%$query%")
                            ilike("description", "%$query%")
                            ilike("type", "%$query%")
                        }
                        studentId?.let { eq("student_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("achieved_at", Order.DESCENDING)
                }
                .decodeList<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR ACHIEVEMENTS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateAchievement(achievement: StudentAchievementModel): StudentAchievementModel {
        return try {
            client.postgrest[schema, table]
                .update(achievement) {
                    filter { eq("id", achievement.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_student!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<StudentAchievementModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ACHIEVEMENT ${achievement.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteAchievement(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR ACHIEVEMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun countAchievementsByStudent(studentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("student_id", studentId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ACHIEVEMENTS POR STUDENT $studentId: ${e.message}")
            throw e
        }
    }

    override suspend fun countAchievementsByType(studentId: Long): Map<String, Int> {
        return try {
            // Placeholder - ideal com RPC ou view com group by type
            mapOf("badge" to 0, "streak" to 0, "level_up" to 0)
        } catch (e: Exception) {
            println("ERRO AO CONTAR ACHIEVEMENTS POR TYPE DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalPointsEarned(studentId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("sum(points_earned)")) {
                    filter { eq("student_id", studentId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("sum")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CALCULAR TOTAL DE PONTOS DO STUDENT $studentId: ${e.message}")
            0
        }
    }

    override suspend fun getAchievementFrequency(studentId: Long, days: Int): Map<String, Int> {
        return try {
            //val startDate = Clock.System.now().minus(DatePeriod(days = days)).toLocalDateTime(TimeZone.currentSystemDefault()).toString()
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            // Placeholder - ideal com RPC que agrupa por data
            mapOf("daily" to 0, "weekly" to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER FREQUÊNCIA DE ACHIEVEMENTS DO STUDENT $studentId: ${e.message}")
            emptyMap()
        }
    }
}
class SubjectDataSourceImpl : SubjectDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_subject"

    override suspend fun createSubject(subject: SubjectModel): SubjectModel {
        return try {


            client.postgrest[schema, table]
                .insert(subject) {
                    select(Columns.ALL)
                }
                .decodeSingle<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SUBJECT: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubjectById(id: Long): SubjectModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SubjectModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubjectByCode(code: String): SubjectModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("code", code) }
                }
                .decodeList<SubjectModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECT POR CODE $code: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllSubjects(
        active: Boolean?,
        isCore: Boolean?,
        page: Int,
        pageSize: Int
    ): List<SubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("is_active", it) }
                        isCore?.let { eq("is_core", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS SUBJECTS: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubjectsByGradeLevel(level: GradeLevel, page: Int, pageSize: Int): List<SubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("grade_level", level.name) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECTS POR GRADE LEVEL ${level.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getCoreSubjects(page: Int, pageSize: Int): List<SubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("is_core", true) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CORE SUBJECTS: ${e.message}")
            throw e
        }
    }

    override suspend fun searchSubjects(query: String, page: Int, pageSize: Int): List<SubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("name", "%$query%")
                            ilike("code", "%$query%")
                            ilike("description", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("name", Order.ASCENDING)
                }
                .decodeList<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR SUBJECTS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateSubject(subject: SubjectModel): SubjectModel {
        return try {
            client.postgrest[schema, table]
                .update(subject) {
                    filter { eq("id", subject.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SUBJECT ${subject.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSubjectActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DO SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSubjectStatistics(id: Long): Boolean {
        return try {
            // Placeholder - atualiza contagens, popularidade, etc.
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteSubject(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun countSubjectsByGradeLevel(): Map<GradeLevel, Int> {
        return try {
            // Placeholder - ideal com RPC group by grade_level
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBJECTS POR GRADE LEVEL: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalSubjectsCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE SUBJECTS: ${e.message}")
            throw e
        }
    }

    override suspend fun getMostPopularSubjects(limit: Int): List<SubjectModel> {
        return try {
            // Placeholder - order by popularity (ex: uso em lessons)
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    order("usage_count", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<SubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECTS MAIS POPULARES: ${e.message}")
            throw e
        }
    }
}
class SubscriptionDataSourceImpl : SubscriptionDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_subscription"

    override suspend fun createSubscription(subscription: SubscriptionModel): SubscriptionModel {
        return try {


            client.postgrest[schema, table]
                .insert(subscription) {
                    select(Columns.ALL)
                }
                .decodeSingle<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SUBSCRIPTION: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubscriptionById(id: Long): SubscriptionModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SubscriptionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTION POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubscriptionByUser(userId: Long, userType: String, active: Boolean?): SubscriptionModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                        active?.let { eq("status", if (it) "ACTIVE" else "EXPIRED") }
                    }
                }
                .decodeList<SubscriptionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTION ATIVA POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun getSubscriptionsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("start_date", Order.DESCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun getSubscriptionsByPlan(planName: String, page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("plan_name", planName) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("start_date", Order.DESCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS POR PLAN $planName: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubscriptionsByStatus(status: String, page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", status) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("start_date", Order.DESCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS POR STATUS $status: ${e.message}")
            throw e
        }
    }

    override suspend fun getActiveSubscriptions(page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", "ACTIVE") }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("end_date", Order.ASCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS ATIVAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getExpiringSubscriptions(days: Int, page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {

            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("status", "ACTIVE")
                        lte("end_date", today)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("end_date", Order.ASCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS A EXPIRAR EM $days dias: ${e.message}")
            throw e
        }
    }

    override suspend fun getCanceledSubscriptions(page: Int, pageSize: Int): List<SubscriptionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", "CANCELED") }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("start_date", Order.DESCENDING)
                }
                .decodeList<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBSCRIPTIONS CANCELADAS: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSubscription(subscription: SubscriptionModel): SubscriptionModel {
        return try {
            client.postgrest[schema, table]
                .update(subscription) {
                    filter { eq("id", subscription.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SubscriptionModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SUBSCRIPTION ${subscription.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun cancelSubscription(id: Long, reason: String?): Boolean {
        return try {
            val updates = buildMap {
                put("status", "CANCELED")
                reason?.let { put("cancel_reason", it) }
                put("canceled_at", Clock.System.now().toString())
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO CANCELAR SUBSCRIPTION $id: ${e.message}")
            false
        }
    }

    override suspend fun renewSubscription(id: Long, endDate: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "end_date" to endDate,
                        "status" to "ACTIVE"
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO RENOVAR SUBSCRIPTION $id: ${e.message}")
            false
        }
    }

    override suspend fun updateAutoRenew(id: Long, autoRenew: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("auto_renew" to autoRenew)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR AUTO-RENEW DA SUBSCRIPTION $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSubscriptionStatus(id: Long, status: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DA SUBSCRIPTION $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteSubscription(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SUBSCRIPTION $id: ${e.message}")
            false
        }
    }

    override suspend fun countSubscriptionsByPlan(): Map<String, Int> {
        return try {
            // Placeholder - RPC com group by plan_name
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBSCRIPTIONS POR PLAN: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun countSubscriptionsByStatus(): Map<String, Int> {
        return try {
            // Placeholder - RPC com group by status
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBSCRIPTIONS POR STATUS: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalActiveSubscriptions(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("status", "ACTIVE") }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE SUBSCRIPTIONS ATIVAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getMonthlyRecurringRevenue(): Double {
        return try {
            // Placeholder - RPC que soma valores de planos ativos mensais
            client.postgrest.rpc("get_monthly_recurring_revenue").decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR RECEITA RECORRENTE MENSAL: ${e.message}")
            0.0
        }
    }

    override suspend fun getChurnRate(startDate: String, endDate: String): Double {
        return try {
            // Placeholder - RPC que calcula (cancelados / total no período) * 100
            client.postgrest.rpc("get_churn_rate", mapOf(
                "start_date" to startDate,
                "end_date" to endDate
            )).decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR CHURN RATE: ${e.message}")
            0.0
        }
    }
}

class SystemAdministratorDataSourceImpl : SystemAdministratorDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_system_administrator"

    override suspend fun createAdmin(admin: SystemAdministratorModel): SystemAdministratorModel {
        return try {


            client.postgrest[schema, table]
                .insert(admin) {
                    select(Columns.ALL)
                }
                .decodeSingle<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR SYSTEM ADMINISTRATOR: ${e.message}")
            throw e
        }
    }

    override suspend fun getAdminById(id: Long): SystemAdministratorModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<SystemAdministratorModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ADMIN POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getAdminByEmail(email: String): SystemAdministratorModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("email", email) }
                }
                .decodeList<SystemAdministratorModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ADMIN POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllAdmins(
        active: Boolean?,
        permissionLevel: AdminPermissionLevel?,
        page: Int,
        pageSize: Int
    ): List<SystemAdministratorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        permissionLevel?.let { eq("permission_level", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS ADMINS: ${e.message}")
            throw e
        }
    }

    override suspend fun getAdminsByPermissionLevel(level: AdminPermissionLevel, page: Int, pageSize: Int): List<SystemAdministratorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("permission_level", level.name) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ADMINS POR PERMISSION LEVEL ${level.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun searchAdmins(query: String, page: Int, pageSize: Int): List<SystemAdministratorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("email", "%$query%")
                            ilike("phone", "%$query%")
                        }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR ADMINS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getAdminsWithTwoFactorAuth(page: Int, pageSize: Int): List<SystemAdministratorModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("two_factor_enabled", true) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ADMINS COM 2FA: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAdmin(admin: SystemAdministratorModel): SystemAdministratorModel {
        return try {
            client.postgrest[schema, table]
                .update(admin) {
                    filter { eq("id", admin.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ADMIN ${admin.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAdminStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateAdminPermissionLevel(id: Long, permissionLevel: AdminPermissionLevel): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("permission_level" to permissionLevel.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PERMISSION LEVEL DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateAdminPermissions(
        id: Long,
        canManageUsers: Boolean?,
        canManageContent: Boolean?,
        canManagePayments: Boolean?
    ): Boolean {
        return try {
            val updates = buildMap {
                canManageUsers?.let { put("can_manage_users", it) }
                canManageContent?.let { put("can_manage_content", it) }
                canManagePayments?.let { put("can_manage_payments", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PERMISSÕES DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateTwoFactorAuth(id: Long, enabled: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("two_factor_enabled" to enabled)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR 2FA DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun incrementActionsPerformed(id: Long): Boolean {
        return try {
            client.postgrest.rpc("increment_admin_actions", mapOf("admin_id" to id))
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR ACTIONS DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun updateLastLogin(id: Long, ipAddress: String?): Boolean {
        return try {
            val updates = buildMap {
                put("last_login", Clock.System.now().toString())
                ipAddress?.let { put("last_ip_address", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LAST LOGIN DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun resetFailedLoginAttempts(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("failed_login_attempts" to 0)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO RESETAR FAILED LOGIN ATTEMPTS DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun incrementFailedLoginAttempts(id: Long): Boolean {
        return try {
            client.postgrest.rpc("increment_failed_login_attempts", mapOf("admin_id" to id))
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR FAILED LOGIN ATTEMPTS DO ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteAdmin(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteAdmin(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR ADMIN $id: ${e.message}")
            false
        }
    }

    override suspend fun countAdminsByPermissionLevel(): Map<AdminPermissionLevel, Int> {
        return try {
            // Placeholder - RPC com group by permission_level
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR ADMINS POR PERMISSION LEVEL: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTotalAdminsCount(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)"))
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE ADMINS: ${e.message}")
            throw e
        }
    }

    override suspend fun getAdminActivityStats(days: Int): Map<String, Any> {
        return try {
            // Placeholder - ações, logins, etc. nos últimos days
            mapOf(
                "actions_performed" to 0,
                "login_count" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE ADMIN: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getMostActiveAdmins(limit: Int): List<SystemAdministratorModel> {
        return try {
            // Placeholder - order by actions_performed desc
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    order("actions_performed", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<SystemAdministratorModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ADMINS MAIS ATIVOS: ${e.message}")
            throw e
        }
    }
}

class TeacherDataSourceImpl : TeacherDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_teacher"

    override suspend fun createTeacher(teacher: TeacherModel): TeacherModel {
        return try {


            client.postgrest[schema, table]
                .insert(teacher) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_school!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR TEACHER: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherById(id: Long): TeacherModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<TeacherModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHER POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherByNumber(teacherNumber: String): TeacherModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("teacher_number", teacherNumber) }
                }
                .decodeList<TeacherModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHER POR NÚMERO $teacherNumber: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherByEmail(email: String): TeacherModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("email", email) }
                }
                .decodeList<TeacherModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHER POR EMAIL $email: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllTeachers(
        active: Boolean?,
        verified: Boolean?,
        type: TeacherType?,
        page: Int,
        pageSize: Int
    ): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                        verified?.let { eq("verified", it) }
                        type?.let { eq("type", it.name) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODOS OS TEACHERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeachersBySchool(schoolId: Long, active: Boolean?, page: Int, pageSize: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("school_id", schoolId)
                        active?.let { eq("status", if (it) "ACTIVE" else "INACTIVE") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeachersByType(type: TeacherType, page: Int, pageSize: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("type", type.name) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS POR TYPE ${type.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getVerifiedTeachers(page: Int, pageSize: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("verified", true) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS VERIFICADOS: ${e.message}")
            throw e
        }
    }

    override suspend fun getPendingApprovalTeachers(page: Int, pageSize: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("verified", false) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS PENDENTES DE APROVAÇÃO: ${e.message}")
            throw e
        }
    }

    override suspend fun getFeaturedTeachers(page: Int, pageSize: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("is_featured", true) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS FEATURED: ${e.message}")
            throw e
        }
    }

    override suspend fun searchTeachers(
        query: String,
        schoolId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("full_name", "%$query%")
                            ilike("email", "%$query%")
                            ilike("phone", "%$query%")
                        }
                        schoolId?.let { eq("school_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("full_name", Order.ASCENDING)
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR TEACHERS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun getTopRatedTeachers(limit: Int): List<TeacherModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_school!inner(*)
                    """.trimIndent())
                ) {
                    order("average_rating", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TOP RATED TEACHERS: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTeacher(teacher: TeacherModel): TeacherModel {
        return try {
            client.postgrest[schema, table]
                .update(teacher) {
                    filter { eq("id", teacher.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_school!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR TEACHER ${teacher.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTeacherStatus(id: Long, status: UserStatus): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to status.name)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS DO TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun verifyTeacher(id: Long, verifiedBy: Long, verifiedAt: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "verified" to true,
                        "verified_by" to verifiedBy,
                        "verified_at" to verifiedAt
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO VERIFICAR TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun rejectTeacher(id: Long, rejectedBy: Long, reason: String?): Boolean {
        return try {
            val updates = buildMap {
                put("verified", false)
                put("rejected_by", rejectedBy)
                reason?.let { put("rejection_reason", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO REJEITAR TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateTeacherRating(id: Long, averageRating: Double, totalRatings: Int): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "average_rating" to averageRating,
                        "total_ratings" to totalRatings
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RATING DO TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateTeacherStatistics(id: Long): Boolean {
        return try {
            // Placeholder - recalcula médias, etc.
            client.postgrest[schema, table]
                .update(mapOf("updated_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR ESTATÍSTICAS DO TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateMarketplaceStatus(id: Long, enabled: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("marketplace_enabled" to enabled)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR MARKETPLACE STATUS DO TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun updateMarketplaceBalance(id: Long, balance: Double): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("marketplace_balance" to balance)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR MARKETPLACE BALANCE DO TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun markAsFeatured(id: Long, featured: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_featured" to featured)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO MARCAR TEACHER $id COMO FEATURED: ${e.message}")
            false
        }
    }

    override suspend fun assignToSchool(teacherId: Long, schoolId: Long?): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("school_id" to schoolId)) {
                    filter { eq("id", teacherId) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ASSOCIAR TEACHER $teacherId À SCHOOL $schoolId: ${e.message}")
            false
        }
    }

    override suspend fun softDeleteTeacher(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to "DELETED", "deleted_at" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO SOFT DELETE TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteTeacher(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR TEACHER $id: ${e.message}")
            false
        }
    }

    override suspend fun countTeachersBySchool(schoolId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TEACHERS POR SCHOOL $schoolId: ${e.message}")
            throw e
        }
    }

    override suspend fun countTeachersByType(): Map<TeacherType, Int> {
        return try {
            // Placeholder - RPC com group by type
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR TEACHERS POR TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun countVerifiedTeachers(): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("verified", true) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TEACHERS VERIFICADOS: ${e.message}")
            throw e
        }
    }

    override suspend fun getTotalTeachersCount(schoolId: Long?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    schoolId?.let { filter { eq("school_id", it) } }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TOTAL DE TEACHERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageTeacherRating(schoolId: Long?): Double {
        return try {
            // Placeholder - RPC para média de average_rating
            client.postgrest.rpc("get_average_teacher_rating", mapOf("school_id" to schoolId))
                .decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DE RATING DOS TEACHERS: ${e.message}")
            0.0
        }
    }

    override suspend fun getTeacherPerformanceStatistics(teacherId: Long): Map<String, Any> {
        return try {
            // Placeholder - aulas dadas, rating médio, alunos impactados, etc.
            mapOf(
                "lessons_given" to 0,
                "average_rating" to 0.0,
                "student_impact" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE PERFORMANCE DO TEACHER $teacherId: ${e.message}")
            emptyMap()
        }
    }
}

class TeacherAssignmentDataSourceImpl : TeacherAssignmentDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_teacher_assignment"

    override suspend fun createTeacherAssignment(assignment: TeacherAssignmentModel): TeacherAssignmentModel {
        return try {


            client.postgrest[schema, table]
                .insert(assignment) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*),
                            tb_classe!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR TEACHER ASSIGNMENT: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherAssignmentById(id: Long): TeacherAssignmentModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<TeacherAssignmentModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHER ASSIGNMENT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<TeacherAssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("school_year", Order.DESCENDING)
                }
                .decodeList<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsByClasse(
        classeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<TeacherAssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("school_year", Order.DESCENDING)
                }
                .decodeList<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAssignmentsBySubject(
        subjectId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<TeacherAssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("subject_id", subjectId)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("school_year", Order.DESCENDING)
                }
                .decodeList<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getHomeroomTeachers(
        classeId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherAssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("is_homeroom_teacher", true)
                        classeId?.let { eq("classe_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("school_year", Order.DESCENDING)
                }
                .decodeList<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR HOMEROOM TEACHERS: ${e.message}")
            throw e
        }
    }

    override suspend fun getActiveAssignmentsBySchoolYear(
        schoolYear: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherAssignmentModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_classe!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("school_year", schoolYear)
                        eq("is_active", true)
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("classe_id", Order.ASCENDING)
                }
                .decodeList<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ASSIGNMENTS ATIVOS NO ANO $schoolYear: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherWorkload(teacherId: Long, schoolYear: String): Map<String, Any> {
        return try {
            // Placeholder - horas totais, disciplinas, turmas, etc.
            // Ideal: RPC que soma hours_per_week e agrupa por subject/classe
            mapOf(
                "total_hours" to 0,
                "subjects_count" to 0,
                "classes_count" to 0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER WORKLOAD DO TEACHER $teacherId NO ANO $schoolYear: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun updateTeacherAssignment(assignment: TeacherAssignmentModel): TeacherAssignmentModel {
        return try {
            client.postgrest[schema, table]
                .update(assignment) {
                    filter { eq("id", assignment.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*),
                            tb_classe!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherAssignmentModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR TEACHER ASSIGNMENT ${assignment.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAssignmentActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DO ASSIGNMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateHomeroomStatus(id: Long, isHomeroomTeacher: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_homeroom_teacher" to isHomeroomTeacher)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR HOMEROOM STATUS DO ASSIGNMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteTeacherAssignment(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR TEACHER ASSIGNMENT $id: ${e.message}")
            false
        }
    }

    override suspend fun countAssignmentsByTeacher(teacherId: Long, schoolYear: String?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("teacher_id", teacherId)
                        schoolYear?.let { eq("school_year", it) }
                    }

                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ASSIGNMENTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countAssignmentsByClasse(classeId: Long, schoolYear: String?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("classe_id", classeId)
                        schoolYear?.let { eq("school_year", it) }
                    }

                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR ASSIGNMENTS POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTotalWeeklyHoursByTeacher(teacherId: Long, schoolYear: String): Int {
        return try {
            // Placeholder - soma hours_per_week
            client.postgrest.rpc("get_total_weekly_hours_by_teacher", mapOf(
                "teacher_id" to teacherId,
                "school_year" to schoolYear
            )).decodeSingle<Int>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR HORAS SEMANAIS DO TEACHER $teacherId NO ANO $schoolYear: ${e.message}")
            0
        }
    }

    override suspend fun getTeacherSubjectDistribution(teacherId: Long): Map<Long, Int> {
        return try {
            // Placeholder - contagem de assignments por subject_id
            mapOf(1L to 0, 2L to 0) // subject_id -> count
        } catch (e: Exception) {
            println("ERRO AO OBTER DISTRIBUIÇÃO DE SUBJECTS DO TEACHER $teacherId: ${e.message}")
            emptyMap()
        }
    }
}

class TeacherCertificationDataSourceImpl : TeacherCertificationDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_teacher_certification"

    override suspend fun createCertification(certification: TeacherCertificationModel): TeacherCertificationModel {
        return try {


            client.postgrest[schema, table]
                .insert(certification) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR CERTIFICATION: ${e.message}")
            throw e
        }
    }

    override suspend fun getCertificationById(id: Long): TeacherCertificationModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<TeacherCertificationModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CERTIFICATION POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getCertificationsByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): List<TeacherCertificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("status", if (it) "ACTIVE" else "EXPIRED") }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("issue_date", Order.DESCENDING)
                }
                .decodeList<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CERTIFICATIONS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getCertificationsByStatus(
        status: VerificationStatus,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherCertificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("status", status.name)
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("issue_date", Order.DESCENDING)
                }
                .decodeList<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CERTIFICATIONS POR STATUS ${status.name}: ${e.message}")
            throw e
        }
    }

    override suspend fun getVerifiedCertifications(
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherCertificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("verified", true)
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("verified_at", Order.DESCENDING)
                }
                .decodeList<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CERTIFICATIONS VERIFICADAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getExpiringCertifications(days: Int, page: Int, pageSize: Int): List<TeacherCertificationModel> {
        return try {
           // val threshold = Clock.System.now().plus(DatePeriod(days = days)).toLocalDateTime(TimeZone.currentSystemDefault()).toString()
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        gte("expiration_date", Clock.System.now().toString())
                        lte("expiration_date", today)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("expiration_date", Order.ASCENDING)
                }
                .decodeList<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CERTIFICATIONS A EXPIRAR EM $days dias: ${e.message}")
            throw e
        }
    }

    override suspend fun searchCertifications(
        query: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): List<TeacherCertificationModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        or {
                            ilike("title", "%$query%")
                            ilike("issuing_institution", "%$query%")
                        }
                        teacherId?.let { eq("teacher_id", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("issue_date", Order.DESCENDING)
                }
                .decodeList<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO PESQUISAR CERTIFICATIONS '$query': ${e.message}")
            throw e
        }
    }

    override suspend fun updateCertification(certification: TeacherCertificationModel): TeacherCertificationModel {
        return try {
            client.postgrest[schema, table]
                .update(certification) {
                    filter { eq("id", certification.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherCertificationModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR CERTIFICATION ${certification.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun verifyCertification(id: Long, verifiedBy: Long, verifiedAt: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(
                    mapOf(
                        "verified" to true,
                        "verified_by" to verifiedBy,
                        "verified_at" to verifiedAt,
                        "status" to "VERIFIED"
                    )
                ) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO VERIFICAR CERTIFICATION $id: ${e.message}")
            false
        }
    }

    override suspend fun rejectCertification(id: Long, rejectionReason: String?): Boolean {
        return try {
            val updates = buildMap {
                put("verified", false)
                put("status", "REJECTED")
                rejectionReason?.let { put("rejection_reason", it) }
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO REJEITAR CERTIFICATION $id: ${e.message}")
            false
        }
    }

    override suspend fun updateCertificationActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("status" to if (isActive) "ACTIVE" else "INACTIVE")) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DA CERTIFICATION $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteCertification(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR CERTIFICATION $id: ${e.message}")
            false
        }
    }

    override suspend fun countCertificationsByTeacher(teacherId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR CERTIFICATIONS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countCertificationsByStatus(teacherId: Long?): Map<VerificationStatus, Int> {
        return try {
            // Placeholder - RPC com group by status
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR CERTIFICATIONS POR STATUS: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getExpiredCertificationsCount(): Int {
        return try {
            val today = Clock.System.now().toString()

            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { lt("expiration_date", today) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR CERTIFICATIONS EXPIRADAS: ${e.message}")
            throw e
        }
    }
}

class TeacherSubjectDataSourceImpl : TeacherSubjectDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_teacher_subject"

    override suspend fun createTeacherSubject(teacherSubject: TeacherSubjectModel): TeacherSubjectModel {
        return try {

            client.postgrest[schema, table]
                .insert(teacherSubject) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR TEACHER SUBJECT: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherSubjectById(id: Long): TeacherSubjectModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<TeacherSubjectModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHER SUBJECT POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherSubjectsByTeacher(
        teacherId: Long,
        primary: Boolean?,
        page: Int,
        pageSize: Int
    ): List<TeacherSubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        primary?.let { eq("is_primary", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("is_primary", Order.DESCENDING)
                }
                .decodeList<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherSubjectsBySubject(subjectId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("subject_id", subjectId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("years_experience", Order.DESCENDING)
                }
                .decodeList<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getPrimarySubjectsByTeacher(teacherId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        eq("is_primary", true)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("years_experience", Order.DESCENDING)
                }
                .decodeList<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR PRIMARY SUBJECTS DO TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeachersBySubject(subjectId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("subject_id", subjectId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("years_experience", Order.DESCENDING)
                }
                .decodeList<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TEACHERS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSubjectsByTeacherWithExperience(
        teacherId: Long,
        minYears: Int,
        page: Int,
        pageSize: Int
    ): List<TeacherSubjectModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        gte("years_experience", minYears)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("years_experience", Order.DESCENDING)
                }
                .decodeList<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SUBJECTS COM EXPERIÊNCIA >= $minYears DO TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTeacherSubject(teacherSubject: TeacherSubjectModel): TeacherSubjectModel {
        return try {
            client.postgrest[schema, table]
                .update(teacherSubject) {
                    filter { eq("id", teacherSubject.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_teacher!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TeacherSubjectModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR TEACHER SUBJECT ${teacherSubject.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updatePrimaryStatus(id: Long, isPrimary: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_primary" to isPrimary)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR PRIMARY STATUS DO TEACHER SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun updateSubjectRating(id: Long, rating: Double): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("rating" to rating)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR RATING DO TEACHER SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun incrementLessonsInSubject(id: Long): Boolean {
        return try {
            client.postgrest.rpc("increment_lessons_in_subject", mapOf("ts_id" to id))
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR LESSONS NO TEACHER SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteTeacherSubject(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR TEACHER SUBJECT $id: ${e.message}")
            false
        }
    }

    override suspend fun countSubjectsByTeacher(teacherId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("teacher_id", teacherId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SUBJECTS POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun countTeachersBySubject(subjectId: Long): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("subject_id", subjectId) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR TEACHERS POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAverageExperienceBySubject(subjectId: Long): Double {
        return try {
            client.postgrest.rpc("get_average_experience_by_subject", mapOf("subject_id" to subjectId))
                .decodeSingle<Double>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR MÉDIA DE EXPERIÊNCIA POR SUBJECT $subjectId: ${e.message}")
            0.0
        }
    }

    override suspend fun getTeacherSubjectExpertise(teacherId: Long): Map<String, Any> {
        return try {
            // Placeholder - expertise por subject (anos, rating, lessons)
            mapOf(
                "total_subjects" to 0,
                "primary_subjects" to 0,
                "average_rating" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER EXPERTISE DO TEACHER $teacherId: ${e.message}")
            emptyMap()
        }
    }
}

class TimetableDataSourceImpl : TimetableDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_timetable"

    override suspend fun createTimetable(timetable: TimetableModel): TimetableModel {
        return try {
            val data = buildJsonObject {
                put("classe_id", timetable.classeId)
                put("teacher_id", timetable.teacherId)
                put("subject_id", timetable.subjectId)
                put("day_of_week", timetable.dayOfWeek)
                put("start_time", timetable.startTime)
                put("end_time", timetable.endTime)
                put("room", timetable.room)
                put("school_year", timetable.schoolYear)
                put("is_active", timetable.isActive)
            }

            client.postgrest[schema, table]
                .insert(data) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_classe!inner(*),
                            tb_teacher!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR TIMETABLE: ${e.message}")
            throw e
        }
    }

    override suspend fun createBulkTimetable(timetables: List<TimetableModel>): List<TimetableModel> {
        return try {
            val dataList = timetables.map { tt ->
                buildJsonObject {
                    put("classe_id", tt.classeId)
                    put("teacher_id", tt.teacherId)
                    put("subject_id", tt.subjectId)
                    put("day_of_week", tt.dayOfWeek)
                    put("start_time", tt.startTime)
                    put("end_time", tt.endTime)
                    put("room", tt.room)
                    put("school_year", tt.schoolYear)
                    put("is_active", tt.isActive)
                }
            }

            client.postgrest[schema, table]
                .insert(dataList) {
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_classe!inner(*),
                            tb_teacher!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR BULK TIMETABLE: ${e.message}")
            throw e
        }
    }

    override suspend fun getTimetableById(id: Long): TimetableModel? {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter { eq("id", id) }
                }
                .decodeList<TimetableModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getTimetableByClasse(
        classeId: Long,
        active: Boolean?,
        schoolYear: String?,
        page: Int,
        pageSize: Int
    ): List<TimetableModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        active?.let { eq("is_active", it) }
                        schoolYear?.let { eq("school_year", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("day_of_week", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE POR CLASSE $classeId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTimetableByTeacher(
        teacherId: Long,
        active: Boolean?,
        schoolYear: String?,
        page: Int,
        pageSize: Int
    ): List<TimetableModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        active?.let { eq("is_active", it) }
                        schoolYear?.let { eq("school_year", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("day_of_week", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE POR TEACHER $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getTimetableBySubject(
        subjectId: Long,
        active: Boolean?,
        schoolYear: String?,
        page: Int,
        pageSize: Int
    ): List<TimetableModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("subject_id", subjectId)
                        active?.let { eq("is_active", it) }
                        schoolYear?.let { eq("school_year", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("day_of_week", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE POR SUBJECT $subjectId: ${e.message}")
            throw e
        }
    }

    override suspend fun getDailyTimetable(classeId: Long, dayOfWeek: Int, schoolYear: String): List<TimetableModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        eq("day_of_week", dayOfWeek)
                        eq("school_year", schoolYear)
                        eq("is_active", true)
                    }
                    order("start_time", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE DIÁRIO DA CLASSE $classeId NO DIA $dayOfWeek: ${e.message}")
            throw e
        }
    }

    override suspend fun getWeeklyTimetable(classeId: Long, schoolYear: String): Map<Int, List<TimetableModel>> {
        return try {
            val entries = client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("classe_id", classeId)
                        eq("school_year", schoolYear)
                        eq("is_active", true)
                    }
                    order("day_of_week", Order.ASCENDING)
                    order("start_time", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()

            entries.groupBy { it.dayOfWeek }
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TIMETABLE SEMANAL DA CLASSE $classeId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getTimetableConflicts(
        teacherId: Long,
        dayOfWeek: Int,
        startTime: String,
        endTime: String,
        schoolYear: String
    ): List<TimetableModel> {
        return try {
            // Placeholder - busca slots que se sobrepõem no horário
            // Ideal: RPC com filtro de intervalo de tempo
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("teacher_id", teacherId)
                        eq("day_of_week", dayOfWeek)
                        eq("school_year", schoolYear)
                        eq("is_active", true)
                        // Sobreposição: (start < endTime AND end > startTime)
                    }
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR CONFLITOS DE TIMETABLE PARA TEACHER $teacherId: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getRoomSchedule(
        room: String,
        dayOfWeek: Int?,
        schoolYear: String?,
        page: Int,
        pageSize: Int
    ): List<TimetableModel> {
        return try {
            client.postgrest[schema, table]
                .select(
                    columns = Columns.raw("""
                        *,
                        tb_classe!inner(*),
                        tb_teacher!inner(*),
                        tb_subject!inner(*)
                    """.trimIndent())
                ) {
                    filter {
                        eq("room", room)
                        dayOfWeek?.let { eq("day_of_week", it) }
                        schoolYear?.let { eq("school_year", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("start_time", Order.ASCENDING)
                }
                .decodeList<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR AGENDA DA SALA $room: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTimetable(timetable: TimetableModel): TimetableModel {
        return try {
            client.postgrest[schema, table]
                .update(timetable) {
                    filter { eq("id", timetable.id) }
                    select(
                        columns = Columns.raw("""
                            *,
                            tb_classe!inner(*),
                            tb_teacher!inner(*),
                            tb_subject!inner(*)
                        """.trimIndent())
                    )
                }
                .decodeSingle<TimetableModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR TIMETABLE ${timetable.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateTimetableActiveStatus(id: Long, isActive: Boolean): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to isActive)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR STATUS ATIVO DO TIMETABLE $id: ${e.message}")
            false
        }
    }

    override suspend fun updateTimetableSlot(
        id: Long,
        dayOfWeek: Int?,
        startTime: String?,
        endTime: String?
    ): Boolean {
        return try {
            val updates = buildMap {
                dayOfWeek?.let { put("day_of_week", it) }
                startTime?.let { put("start_time", it) }
                endTime?.let { put("end_time", it) }
            }

            if (updates.isEmpty()) return true

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SLOT DO TIMETABLE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteTimetable(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR TIMETABLE $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteTimetableByClasse(classeId: Long, schoolYear: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter {
                        eq("classe_id", classeId)
                        eq("school_year", schoolYear)
                    }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR TIMETABLE DA CLASSE $classeId NO ANO $schoolYear: ${e.message}")
            false
        }
    }

    override suspend fun countTimetableSlotsByTeacher(teacherId: Long, schoolYear: String): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("teacher_id", teacherId)
                        eq("school_year", schoolYear)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SLOTS POR TEACHER $teacherId NO ANO $schoolYear: ${e.message}")
            throw e
        }
    }

    override suspend fun countTimetableSlotsByClasse(classeId: Long, schoolYear: String): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("classe_id", classeId)
                        eq("school_year", schoolYear)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SLOTS POR CLASSE $classeId NO ANO $schoolYear: ${e.message}")
            throw e
        }
    }

    override suspend fun getTeacherWeeklySchedule(teacherId: Long, schoolYear: String): Map<Int, Int> {
        return try {
            // Placeholder - horas por dia da semana
            mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER AGENDA SEMANAL DO TEACHER $teacherId: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getClassWeeklyHours(classeId: Long, schoolYear: String): Int {
        return try {
            // Placeholder - soma total de horas na semana
            client.postgrest.rpc("get_class_weekly_hours", mapOf(
                "classe_id" to classeId,
                "school_year" to schoolYear
            )).decodeSingle<Int>()
        } catch (e: Exception) {
            println("ERRO AO CALCULAR HORAS SEMANAIS DA CLASSE $classeId: ${e.message}")
            0
        }
    }
}

class UserActivityLogDataSourceImpl : UserActivityLogDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_user_activity_log"

    override suspend fun createActivityLog(log: UserActivityLogModel): UserActivityLogModel {
        return try {


            client.postgrest[schema, table]
                .insert(log) {
                    select(Columns.ALL)
                }
                .decodeSingle<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR ACTIVITY LOG: ${e.message}")
            throw e
        }
    }

    override suspend fun getActivityLogById(id: Long): UserActivityLogModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<UserActivityLogModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR ACTIVITY LOG POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getActivityLogsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<UserActivityLogModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LOGS POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun getActivityLogsByActivityType(activityType: String, page: Int, pageSize: Int): List<UserActivityLogModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("activity_type", activityType) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LOGS POR ACTIVITY TYPE $activityType: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentActivityLogs(limit: Int): List<UserActivityLogModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LOGS RECENTES: ${e.message}")
            throw e
        }
    }

    override suspend fun getActivityLogsByDateRange(
        startDate: String,
        endDate: String,
        userId: Long?,
        userType: String?,
        page: Int,
        pageSize: Int
    ): List<UserActivityLogModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        gte("created_at", startDate)
                        lte("created_at", endDate)
                        userId?.let { eq("user_id", it) }
                        userType?.let { eq("user_type", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LOGS POR DATA RANGE $startDate - $endDate: ${e.message}")
            throw e
        }
    }

    override suspend fun getActivityLogsByIpAddress(ipAddress: String, page: Int, pageSize: Int): List<UserActivityLogModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("ip_address", ipAddress) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<UserActivityLogModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LOGS POR IP $ipAddress: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteActivityLog(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR ACTIVITY LOG $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteOldActivityLogs(days: Int): Int {
        return try {
           // val threshold = Clock.System.now().minus(DatePeriod(days = days)).toString()
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            client.postgrest[schema, table]
                .delete {
                    filter { lt("created_at", today) }
                }
                .decodeSingle<Boolean>()
            // Nota: Supabase delete não retorna count nativo, usa RPC se quiseres contar
            0
        } catch (e: Exception) {
            println("ERRO AO DELETAR ACTIVITY LOGS ANTIGOS: ${e.message}")
            0
        }
    }

    override suspend fun countActivityLogsByUser(userId: Long, userType: String, days: Int): Int {
        return try {
            //val threshold = Clock.System.now().minus(DatePeriod(days = days)).toString()
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                        gte("created_at", today)
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LOGS POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun countActivityLogsByActivityType(days: Int): Map<String, Int> {
        return try {
            // Placeholder - RPC com group by activity_type nos últimos days
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR LOGS POR ACTIVITY TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getMostActiveUsers(userType: String, days: Int, limit: Int): List<Map<String, Any>> {
        return try {
            // Placeholder - RPC que retorna top users por count de logs
            emptyList()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR USERS MAIS ATIVOS: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getActivityFrequency(userId: Long, userType: String, days: Int): Map<String, Int> {
        return try {
            // Placeholder - logs por dia/semana
            mapOf("monday" to 0, "tuesday" to 0)
        } catch (e: Exception) {
            println("ERRO AO OBTER FREQUÊNCIA DE ATIVIDADE DO USER $userId: ${e.message}")
            emptyMap()
        }
    }
}

class UserSessionDataSourceImpl : UserSessionDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_user_session"

    override suspend fun createSession(session: UserSessionModel): UserSessionModel {
        return try {


            client.postgrest[schema, table]
                .insert(session) {
                    select(Columns.ALL)
                }
                .decodeSingle<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO CADASTRAR USER SESSION: ${e.message}")
            throw e
        }
    }

    override suspend fun getSessionById(id: Long): UserSessionModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeList<UserSessionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSION POR ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getSessionByToken(token: String): UserSessionModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("token", token) }
                }
                .decodeList<UserSessionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSION POR TOKEN: ${e.message}")
            throw e
        }
    }

    override suspend fun getSessionsByUser(userId: Long, userType: String, active: Boolean?, page: Int, pageSize: Int): List<UserSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                        active?.let { eq("is_active", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("last_activity", Order.DESCENDING)
                }
                .decodeList<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSIONS POR USER $userId ($userType): ${e.message}")
            throw e
        }
    }

    override suspend fun getActiveSessions(userId: Long, userType: String): List<UserSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                        eq("is_active", true)
                    }
                    order("last_activity", Order.DESCENDING)
                }
                .decodeList<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSIONS ATIVAS DO USER $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSessionsByDevice(deviceId: String, userId: Long?, userType: String?, page: Int, pageSize: Int): List<UserSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                        userId?.let { eq("user_id", it) }
                        userType?.let { eq("user_type", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("last_activity", Order.DESCENDING)
                }
                .decodeList<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSIONS POR DEVICE $deviceId: ${e.message}")
            throw e
        }
    }

    override suspend fun getSessionsByIpAddress(ipAddress: String, page: Int, pageSize: Int): List<UserSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("ip_address", ipAddress) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("last_activity", Order.DESCENDING)
                }
                .decodeList<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSIONS POR IP $ipAddress: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentSessions(userId: Long, userType: String, limit: Int): List<UserSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                    }
                    limit(limit.toLong())
                    order("last_activity", Order.DESCENDING)
                }
                .decodeList<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR SESSIONS RECENTES DO USER $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun updateSession(session: UserSessionModel): UserSessionModel {
        return try {
            client.postgrest[schema, table]
                .update(session) {
                    filter { eq("id", session.id) }
                    select(Columns.ALL)
                }
                .decodeSingle<UserSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR SESSION ${session.id}: ${e.message}")
            throw e
        }
    }

    override suspend fun updateLastActivity(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("last_activity" to Clock.System.now().toString())) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LAST ACTIVITY DA SESSION $id: ${e.message}")
            false
        }
    }

    override suspend fun logoutSession(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to false)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO FAZER LOGOUT DA SESSION $id: ${e.message}")
            false
        }
    }

    override suspend fun logoutAllSessions(userId: Long, userType: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to false)) {
                    filter {
                        eq("user_id", userId)
                        eq("user_type", userType)
                        eq("is_active", true)
                    }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO FAZER LOGOUT DE TODAS AS SESSIONS DO USER $userId: ${e.message}")
            false
        }
    }

    override suspend fun expireSession(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .update(mapOf("is_active" to false)) {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO EXPIRAR SESSION $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteSession(id: Long): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", id) }
                }
                .decodeSingle<Boolean>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SESSION $id: ${e.message}")
            false
        }
    }

    override suspend fun deleteExpiredSessions(): Int {
        return try {
            // Placeholder - deleta sessions expiradas (usa RPC ou filter)
            client.postgrest.rpc("delete_expired_sessions").decodeSingle<Int>()
        } catch (e: Exception) {
            println("ERRO AO DELETAR SESSIONS EXPIRADAS: ${e.message}")
            0
        }
    }

    override suspend fun countActiveSessions(userId: Long?, userType: String?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("is_active", true)
                        userId?.let { eq("user_id", it) }
                        userType?.let { eq("user_type", it) }
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR SESSIONS ATIVAS: ${e.message}")
            throw e
        }
    }

    override suspend fun countSessionsByDeviceType(userId: Long, userType: String): Map<String, Int> {
        return try {
            // Placeholder - RPC com group by device_type (precisa extrair de device_info)
            emptyMap()
        } catch (e: Exception) {
            println("ERRO AO CONTAR SESSIONS POR DEVICE TYPE: ${e.message}")
            emptyMap()
        }
    }

    override suspend fun getAverageSessionDuration(userId: Long, userType: String, days: Int): Double {
        return try {
            // Placeholder - RPC que calcula média de duração (last_activity - created_at)
            0.0
        } catch (e: Exception) {
            println("ERRO AO CALCULAR DURAÇÃO MÉDIA DE SESSIONS: ${e.message}")
            0.0
        }
    }

    override suspend fun getSessionActivity(userId: Long, userType: String, days: Int): Map<String, Any> {
        return try {
            // Placeholder - logins, ações, etc. nos últimos days
            mapOf(
                "session_count" to 0,
                "average_duration_minutes" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ATIVIDADE DE SESSIONS DO USER $userId: ${e.message}")
            emptyMap()
        }
    }
}



//==============================================================================================
//===================== LIVE SESSION DATASOURCE IMPLEMENTATION ===============================
//==============================================================================================

class LiveSessionDataSourceImpl : LiveSessionDataSource {

    private val client = TshikasiAutoSchool.supabase
    private val schema = "db_auto_school"
    private val table = "tb_live_sessions"

    // ==================== CREATE ====================

    override suspend fun createLiveSession(request: CreateLiveSessionRequest): LiveSessionModel {
        return try {
            val session = LiveSessionModel(
                id = UUID.randomUUID().toString(),
                title = request.title,
                description = request.description,
                subject = request.subject,
                scheduledStart = request.scheduledStart,
                durationMinutes = request.durationMinutes,
                maxParticipants = request.maxParticipants,
                isPublic = request.isPublic,
                requiresApproval = request.requiresApproval,
                price = request.price,
                status = LiveStatus.SCHEDULED,
                createdAt = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .insert(session) {
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO CRIAR LIVE SESSION: ${e.message}")
            throw e
        }
    }

    // ==================== READ ====================

    override suspend fun getLiveSessionById(sessionId: String): LiveSessionModel? {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("id", sessionId) }
                }
                .decodeList<LiveSessionModel>()
                .firstOrNull()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVE SESSION POR ID $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun getAllLiveSessions(
        filters: LiveSessionFilters?,
        page: Int,
        pageSize: Int
    ): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        filters?.status?.let { eq("status", it.name.lowercase()) }
                        filters?.subject?.let { eq("subject", it) }
                        filters?.teacherId?.let { eq("teacher_id", it) }
                        filters?.isPublic?.let { eq("is_public", it) }
                        filters?.minPrice?.let { gte("price", it) }
                        filters?.maxPrice?.let { lte("price", it) }
                       // filters?.searchQuery?.let { or("title.ilike.%$it%,description.ilike.%$it%")  }
                        filters?.dateFrom?.let { gte("scheduled_start", it) }
                        filters?.dateTo?.let { lte("scheduled_start", it) }
                        filters?.gradeLevel?.let { eq("grade_level", it) }
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order(filters?.sortBy ?: "scheduled_start",
                        if (filters?.sortOrder == "asc") Order.ASCENDING else Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR TODAS AS LIVE SESSIONS: ${e.message}")
            throw e
        }
    }

    override suspend fun getLiveSessionsByTeacher(
        teacherId: String,
        page: Int,
        pageSize: Int
    ): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("teacher_id", teacherId) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("scheduled_start", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES DO PROFESSOR $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getUpcomingLiveSessions(page: Int, pageSize: Int): List<LiveSessionModel> {
        return try {
            val now = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        eq("status", "scheduled")
                        gte("scheduled_start", now)
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("scheduled_start", Order.ASCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES AGENDADAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getLiveNowSessions(): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", "live") }
                    order("actual_start", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES AO VIVO: ${e.message}")
            throw e
        }
    }

    override suspend fun getPastLiveSessions(page: Int, pageSize: Int): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", "ended") }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("actual_end", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES PASSADAS: ${e.message}")
            throw e
        }
    }

    override suspend fun getUserLiveSessions(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<LiveSessionModel> {
        return try {
            // Query lives where user is a participant
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter {
                        // Usando inner join com live_participants
                        //or("teacher_id.eq.$userId")
                    }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("scheduled_start", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES DO USUÁRIO $userId: ${e.message}")
            throw e
        }
    }

    override suspend fun getLiveSessionsByStatus(
        status: LiveStatus,
        page: Int,
        pageSize: Int
    ): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("status", status.name.lowercase()) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("scheduled_start", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES POR STATUS $status: ${e.message}")
            throw e
        }
    }

    override suspend fun getLiveSessionsBySubject(
        subject: String,
        page: Int,
        pageSize: Int
    ): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    filter { eq("subject", subject) }
                    range((page * pageSize).toLong(), ((page + 1) * pageSize - 1).toLong())
                    order("scheduled_start", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES POR DISCIPLINA $subject: ${e.message}")
            throw e
        }
    }

    override suspend fun getRecentLiveSessions(limit: Int): List<LiveSessionModel> {
        return try {
            client.postgrest[schema, table]
                .select(Columns.ALL) {
                    limit(limit.toLong())
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO BUSCAR LIVES RECENTES: ${e.message}")
            throw e
        }
    }

    // ==================== UPDATE ====================

    override suspend fun updateLiveSession(
        sessionId: String,
        request: UpdateLiveSessionRequest
    ): LiveSessionModel {
        return try {
            val updates = buildMap {
                request.title?.let { put("title", it) }
                request.description?.let { put("description", it) }
                request.subject?.let { put("subject", it) }
                request.scheduledStart?.let { put("scheduled_start", it) }
                request.durationMinutes?.let { put("duration_minutes", it) }
                request.maxParticipants?.let { put("max_participants", it) }
                request.isPublic?.let { put("is_public", it) }
                request.status?.let { put("status", it.name.lowercase()) }
                put("updated_at", Clock.System.todayIn(TimeZone.currentSystemDefault()).toString())
            }

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun startLiveSession(sessionId: String): LiveSessionModel {
        return try {
            val updates = mapOf(
                "status" to "live",
                "actual_start" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString(),
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO INICIAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun pauseLiveSession(sessionId: String): LiveSessionModel {
        return try {
            val updates = mapOf(
                "status" to "paused",
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO PAUSAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun resumeLiveSession(sessionId: String): LiveSessionModel {
        return try {
            val updates = mapOf(
                "status" to "live",
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO RETOMAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun endLiveSession(sessionId: String): LiveSessionModel {
        return try {
            val updates = mapOf(
                "status" to "ended",
                "actual_end" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString(),
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO FINALIZAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun cancelLiveSession(sessionId: String): LiveSessionModel {
        return try {
            val updates = mapOf(
                "status" to "cancelled",
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                    select(Columns.ALL)
                }
                .decodeSingle<LiveSessionModel>()
        } catch (e: Exception) {
            println("ERRO AO CANCELAR LIVE SESSION $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun updateLiveMetrics(
        sessionId: String,
        participantCount: Int,
        viewCount: Int
    ): Boolean {
        return try {
            val updates = mapOf(
                "participant_count" to participantCount,
                "view_count" to viewCount,
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                }
            true
        } catch (e: Exception) {
            println("ERRO AO ATUALIZAR MÉTRICAS DA LIVE $sessionId: ${e.message}")
            false
        }
    }

    override suspend fun incrementParticipantCount(sessionId: String): Boolean {
        return try {
            // RPC call or fetch + increment + update
            val current = getLiveSessionById(sessionId)
            current?.let {
                val updates = mapOf(
                    "participant_count" to (it.participantCount + 1),
                    "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
                )
                client.postgrest[schema, table]
                    .update(updates) {
                        filter { eq("id", sessionId) }
                    }
                true
            } ?: false
        } catch (e: Exception) {
            println("ERRO AO INCREMENTAR PARTICIPANTES DA LIVE $sessionId: ${e.message}")
            false
        }
    }

    override suspend fun decrementParticipantCount(sessionId: String): Boolean {
        return try {
            val current = getLiveSessionById(sessionId)
            current?.let {
                val newCount = maxOf(0, it.participantCount - 1)
                val updates = mapOf(
                    "participant_count" to newCount,
                    "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
                )
                client.postgrest[schema, table]
                    .update(updates) {
                        filter { eq("id", sessionId) }
                    }
                true
            } ?: false
        } catch (e: Exception) {
            println("ERRO AO DECREMENTAR PARTICIPANTES DA LIVE $sessionId: ${e.message}")
            false
        }
    }

    // ==================== DELETE ====================

    override suspend fun deleteLiveSession(sessionId: String): Boolean {
        return try {
            client.postgrest[schema, table]
                .delete {
                    filter { eq("id", sessionId) }
                }
            true
        } catch (e: Exception) {
            println("ERRO AO DELETAR LIVE SESSION $sessionId: ${e.message}")
            false
        }
    }

    override suspend fun deleteCancelledSessions(olderThanDays: Int): Int {
        return try {
            val threshold = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            client.postgrest[schema, table]
                .delete {
                    filter {
                        eq("status", "cancelled")
                        lt("created_at", threshold)
                    }
                }
            // Supabase delete não retorna count, usar RPC se precisar
            0
        } catch (e: Exception) {
            println("ERRO AO DELETAR LIVES CANCELADAS ANTIGAS: ${e.message}")
            0
        }
    }

    // ==================== STREAMING ====================

    override suspend fun getStreamInfo(sessionId: String): StreamInfoResponse {
        return try {
            val session = getLiveSessionById(sessionId)
            session?.let {
                StreamInfoResponse(
                    success = true,
                    liveSession = it,
                    streamInfo = StreamInfo(
                        rtmpUrl = it.rtmpUrl ?: "",
                        streamKey = it.streamKey ?: "",
                        playbackUrl = it.playbackUrl ?: ""
                    )
                )
            } ?: StreamInfoResponse(
                success = false,
                liveSession = LiveSessionModel(),
                error = "Session not found"
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER STREAM INFO $sessionId: ${e.message}")
            throw e
        }
    }

    override suspend fun generateStreamKey(sessionId: String): String {
        return try {
            val streamKey = UUID.randomUUID().toString()
            val updates = mapOf(
                "stream_key" to streamKey,
                "updated_at" to Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            )

            client.postgrest[schema, table]
                .update(updates) {
                    filter { eq("id", sessionId) }
                }
            streamKey
        } catch (e: Exception) {
            println("ERRO AO GERAR STREAM KEY $sessionId: ${e.message}")
            throw e
        }
    }

    // ==================== STATISTICS ====================

    override suspend fun countLivesByStatus(status: LiveStatus): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter { eq("status", status.name.lowercase()) }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LIVES POR STATUS $status: ${e.message}")
            throw e
        }
    }

    override suspend fun countTeacherLives(teacherId: String, status: LiveStatus?): Int {
        return try {
            val response = client.postgrest[schema, table]
                .select(columns = Columns.raw("count(*)")) {
                    filter {
                        eq("teacher_id", teacherId)
                        status?.let { eq("status", it.name.lowercase()) }
                    }
                }
                .decodeList<Map<String, Long>>()

            response.firstOrNull()?.get("count")?.toInt() ?: 0
        } catch (e: Exception) {
            println("ERRO AO CONTAR LIVES DO PROFESSOR $teacherId: ${e.message}")
            throw e
        }
    }

    override suspend fun getLiveStatistics(teacherId: String?, days: Int): Map<String, Any> {
        return try {
            // Placeholder - usar RPC para estatísticas complexas
            mapOf(
                "total_lives" to 0,
                "total_viewers" to 0,
                "average_viewers" to 0.0
            )
        } catch (e: Exception) {
            println("ERRO AO OBTER ESTATÍSTICAS DE LIVES: ${e.message}")
            emptyMap()
        }
    }

    // ==================== REALTIME ====================
    override fun observeLiveSession(sessionId: String): Flow<LiveSessionModel> {
        return client.channel("live_session_$sessionId")
            .postgresChangeFlow<PostgresAction>(schema = schema) {
                table = this@LiveSessionDataSourceImpl.table
                filter = "id=eq.$sessionId"
            }
            .mapNotNull { action ->
                when (action) {
                    is PostgresAction.Update -> action.decodeRecord<LiveSessionModel>()
                    is PostgresAction.Insert -> action.decodeRecord<LiveSessionModel>()
                    else -> null
                }
            }
    }

    override fun observeLiveNowSessions(): Flow<List<LiveSessionModel>> {
        return client.channel("live_now_sessions")
            .postgresChangeFlow<PostgresAction>(schema = schema) {
                table = this@LiveSessionDataSourceImpl.table
                filter = "status=eq.live"
            }
            .mapLatest {
                // Fetch all live sessions on change
                getLiveNowSessions()
            }
    }
}