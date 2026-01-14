package com.tshikasi.tshikasi_auto_school.data.datasource

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
            /*val dataList = notifications.map { n ->
                buildJsonObject {
                    put("user_id", n.userId)
                    put("title", n.title)
                    put("message", n.message)
                    put("notification_type", n.notificationType.name)
                    put("related_id", n.relatedId)
                    put("related_type", n.relatedType)
                    put("data", n.data)
                }
            }*/

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