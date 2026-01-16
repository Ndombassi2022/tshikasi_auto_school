package com.tshikasi.tshikasi_auto_school.domain.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable




class Models {
    // Classe wrapper vazia para organização do package
}

// ============================================
// ENUMS
// ============================================

@Serializable
enum class MessageType {
    @SerialName("DIRECT") DIRECT, // Mensagem direta 1-para-1
    @SerialName("GROUP") GROUP, // Mensagem em grupo
    @SerialName("ANNOUNCEMENT") ANNOUNCEMENT, // Anúncio (broadcast)
    @SerialName("SYSTEM") SYSTEM // Mensagem do sistema
}

/**
 * MessagePriority - Prioridade da mensagem
 */
@Serializable
enum class MessagePriority {
    @SerialName("LOW") LOW,
    @SerialName("NORMAL") NORMAL,
    @SerialName("HIGH") HIGH,
    @SerialName("URGENT") URGENT
}

@Serializable
enum class UserType {
    @SerialName("STUDENT") STUDENT,
    @SerialName("TEACHER") TEACHER,
    @SerialName("GUARDIAN") GUARDIAN,
    @SerialName("SCHOOL_DIRECTOR") SCHOOL_DIRECTOR,
    @SerialName("SYSTEM_ADMIN") SYSTEM_ADMIN,
    @SerialName("STATE_MANAGER") STATE_MANAGER,
    @SerialName("GUEST") GUEST
}
@Serializable
enum class Gender {
    @SerialName("MALE") MALE,
    @SerialName("FEMALE") FEMALE,
    @SerialName("OTHER") OTHER
}

@Serializable
enum class StudentAccountType {
    @SerialName("FREE") FREE,
    @SerialName("PREMIUM") PREMIUM,
    @SerialName("SCHOOL") SCHOOL
}

@Serializable
enum class TeacherType {
    @SerialName("SCHOOL") SCHOOL,
    @SerialName("INDEPENDENT") INDEPENDENT,
    @SerialName("GUEST") GUEST,
    @SerialName("TUTOR") TUTOR
}

@Serializable
enum class GuardianRelation {
    @SerialName("FATHER") FATHER,
    @SerialName("MOTHER") MOTHER,
    @SerialName("GUARDIAN") GUARDIAN,
    @SerialName("GRANDPARENT") GRANDPARENT,
    @SerialName("SIBLING") SIBLING,
    @SerialName("OTHER") OTHER
}

@Serializable
enum class ReportFrequency {
    @SerialName("DAILY") DAILY,
    @SerialName("WEEKLY") WEEKLY,
    @SerialName("MONTHLY") MONTHLY,
    @SerialName("QUARTERLY") QUARTERLY,
    @SerialName("NEVER") NEVER
}

@Serializable
enum class ManagerAccessLevel {
    @SerialName("PROVINCIAL") PROVINCIAL,
    @SerialName("NATIONAL") NATIONAL,
    @SerialName("MINISTERIAL") MINISTERIAL,
    @SerialName("REGIONAL") REGIONAL
}



@Serializable
enum class AdminPermissionLevel {
    @SerialName("SUPER_ADMIN") SUPER_ADMIN,
    @SerialName("CONTENT_ADMIN") CONTENT_ADMIN,
    @SerialName("FINANCIAL_ADMIN") FINANCIAL_ADMIN,
    @SerialName("SUPPORT_ADMIN") SUPPORT_ADMIN,
    @SerialName("TEACHER_ADMIN") TEACHER_ADMIN,
    @SerialName("SCHOOL_ADMIN") SCHOOL_ADMIN
}

@Serializable
enum class GradeLevel {
    @SerialName("PRIMARY") PRIMARY, // 1ª a 6ª Classe
    @SerialName("SECONDARY_I") SECONDARY_I, // 7ª a 9ª Classe
    @SerialName("SECONDARY_II") SECONDARY_II, // 10ª a 12ª Classe
    @SerialName("VOCATIONAL") VOCATIONAL, // Técnico-Profissional
    @SerialName("PRE_UNIVERSITY") PRE_UNIVERSITY, // Pré-Universitário
    @SerialName("UNIVERSITY") UNIVERSITY,
    @SerialName("ADULT_EDUCATION") ADULT_EDUCATION
}

@Serializable
enum class UserStatus {
    @SerialName("ACTIVE") ACTIVE,
    @SerialName("INACTIVE") INACTIVE,
    @SerialName("PENDING") PENDING,
    @SerialName("PENDING_APPROVAL") PENDING_APPROVAL,
    @SerialName("APPROVED") APPROVED,
    @SerialName("REJECTED") REJECTED,
    @SerialName("SUSPENDED") SUSPENDED,
    @SerialName("BLOCKED") BLOCKED,
    @SerialName("GRADUATED") GRADUATED,
    @SerialName("TRANSFERRED") TRANSFERRED,
    @SerialName("ON_LEAVE") ON_LEAVE,
    @SerialName("DELETED") DELETED
}

@Serializable
enum class ContentType {
    @SerialName("VIDEO") VIDEO,
    @SerialName("AUDIO") AUDIO,
    @SerialName("PDF") PDF,
    @SerialName("DOCUMENT") DOCUMENT,
    @SerialName("PRESENTATION") PRESENTATION,
    @SerialName("INTERACTIVE") INTERACTIVE,
    @SerialName("QUIZ") QUIZ,
    @SerialName("EXERCISE") EXERCISE
}

@Serializable
enum class DifficultyLevel {
    @SerialName("EASY") EASY,
    @SerialName("MEDIUM") MEDIUM,
    @SerialName("HARD") HARD,
    @SerialName("ADVANCED") ADVANCED
}

@Serializable
enum class AssignmentStatus {
    @SerialName("PENDING") PENDING,
    @SerialName("IN_PROGRESS") IN_PROGRESS,
    @SerialName("SUBMITTED") SUBMITTED,
    @SerialName("GRADED") GRADED,
    @SerialName("OVERDUE") OVERDUE,
    @SerialName("CANCELLED") CANCELLED
}

@Serializable
enum class AttendanceStatus {
    @SerialName("PRESENT") PRESENT,
    @SerialName("ABSENT") ABSENT,
    @SerialName("LATE") LATE,
    @SerialName("EXCUSED") EXCUSED,
    @SerialName("HALF_DAY") HALF_DAY
}

@Serializable
enum class NotificationType {
    @SerialName("SYSTEM") SYSTEM,
    @SerialName("LESSON") LESSON,
    @SerialName("ASSIGNMENT") ASSIGNMENT,
    @SerialName("EXAM") EXAM,
    @SerialName("GRADE") GRADE,
    @SerialName("ATTENDANCE") ATTENDANCE,
    @SerialName("ANNOUNCEMENT") ANNOUNCEMENT,
    @SerialName("MESSAGE") MESSAGE,
    @SerialName("REPORT") REPORT,
    @SerialName("live_starting")LIVE_STARTING,      // 5 min antes
    @SerialName("live_started") LIVE_STARTED,       // Live começou
    @SerialName("live_ended") LIVE_ENDED,         // Live terminou
    @SerialName("mentioned")MENTIONED,          // Mencionado no chat
    @SerialName("question_answered")QUESTION_ANSWERED   // Pergunta respondida
}

@Serializable
enum class VerificationStatus {
    @SerialName("PENDING") PENDING,
    @SerialName("VERIFIED") VERIFIED,
    @SerialName("REJECTED") REJECTED,
    @SerialName("EXPIRED") EXPIRED
}

// ============================================
// BASE MODELS
// ============================================



@Serializable
@Parcelize
data class BadgeModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("icon")
    val icon: String = "",

    @SerialName("color")
    val color: String = "",

    @SerialName("points_required")
    val pointsRequired: Int = 0,

    @SerialName("category")
    val category: String = "" // "academic", "attendance", "participation", "achievement"
) : Parcelable

@Serializable
@Parcelize
data class NotificationPreferences(
    @SerialName("email")
    val email: Boolean = true,

    @SerialName("push")
    val push: Boolean = true,

    @SerialName("sms")
    val sms: Boolean = false,

    @SerialName("whatsapp")
    val whatsapp: Boolean = false,

    @SerialName("lesson_updates")
    val lessonUpdates: Boolean = true,

    @SerialName("assignment_deadlines")
    val assignmentDeadlines: Boolean = true,

    @SerialName("grade_updates")
    val gradeUpdates: Boolean = true,

    @SerialName("attendance_alerts")
    val attendanceAlerts: Boolean = true,

    @SerialName("system_announcements")
    val systemAnnouncements: Boolean = true
) : Parcelable

// ============================================
// CORE MODELS (Alfabética)
// ============================================

/**
 * AssignmentModel - Tarefa/Atividade
 */
@Serializable
@Parcelize
data class AssignmentModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("classe_id")
    val classeId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("grade_id")
    val gradeId: Long = 0,

    @SerialName("content_url")
    val contentUrl: String? = null,

    @SerialName("content_type")
    val contentType: ContentType = ContentType.DOCUMENT,

    @SerialName("due_date")
    val dueDate: String = "",

    @SerialName("max_points")
    val maxPoints: Int = 100,

    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel = DifficultyLevel.MEDIUM,

    @SerialName("estimated_time")
    val estimatedTime: Int = 60, // minutos

    @SerialName("allow_late_submission")
    val allowLateSubmission: Boolean = false,

    @SerialName("late_submission_days")
    val lateSubmissionDays: Int = 0,

    @SerialName("requires_file_upload")
    val requiresFileUpload: Boolean = false,

    @SerialName("allowed_file_types")
    val allowedFileTypes: String = "", // JSON array

    @SerialName("max_file_size")
    val maxFileSize: Int = 10, // MB

    @SerialName("total_submissions")
    val totalSubmissions: Int = 0,

    @SerialName("average_score")
    val averageScore: Double = 0.0,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null,

    @SerialName("tb_grade")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grade: GradeModel? = null
) : Parcelable

/**
 * UserModel - Modelo CENTRAL de usuário
 * Todos os outros modelos (Student, Teacher, etc) ESTENDEM deste
 */
@Serializable
@Parcelize
data class UserModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("email")
    val email: String = "",

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("user_type")
    val userType: UserType = UserType.GUEST,

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("password_hash")
    val passwordHash: String = "", // Nunca retornar ao frontend

    @SerialName("two_factor_enabled")
    val twoFactorEnabled: Boolean = false,

    @SerialName("two_factor_secret")
    val twoFactorSecret: String? = null,

    @SerialName("email_verified")
    val emailVerified: Boolean = false,

    @SerialName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerialName("phone_verified")
    val phoneVerified: Boolean = false,

    @SerialName("phone_verified_at")
    val phoneVerifiedAt: String? = null,

    @SerialName("last_login")
    val lastLogin: String? = null,

    @SerialName("last_login_ip")
    val lastLoginIp: String? = null,

    @SerialName("failed_login_attempts")
    val failedLoginAttempts: Int = 0,

    @SerialName("locked_until")
    val lockedUntil: String? = null,

    @SerialName("preferred_language")
    val preferredLanguage: String = "pt",

    @SerialName("timezone")
    val timezone: String = "Africa/Luanda",

    @SerialName("notification_preferences")
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("deleted_at")
    val deletedAt: String? = null,

    // ==================== RELATIONS ====================

    // Perfil específico baseado no user_type
    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var studentProfile: StudentModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacherProfile: TeacherModel? = null,

    @SerialName("tb_guardian")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var guardianProfile: GuardianModel? = null,

    @SerialName("tb_school_director")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var directorProfile: SchoolDirectorModel? = null,

    @SerialName("tb_system_admin")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var adminProfile: SystemAdministratorModel? = null,

    @SerialName("tb_state_manager")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var managerProfile: StateManagerModel? = null,

    @SerialName("tb_sessions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var sessions: List<UserSessionModel>? = null,

    @SerialName("tb_activity_logs")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var activityLogs: List<UserActivityLogModel>? = null
) : Parcelable

/**
 * AssignmentSubmissionModel - Submissão de Tarefa
 */
@Serializable
@Parcelize
data class AssignmentSubmissionModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("assignment_id")
    val assignmentId: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("submission_text")
    val submissionText: String? = null,

    @SerialName("file_url")
    val fileUrl: String? = null,

    @SerialName("file_name")
    val fileName: String? = null,

    @SerialName("file_size")
    val fileSize: Long? = null,

    @SerialName("submitted_at")
    val submittedAt: String = "",

    @SerialName("submission_status")
    val submissionStatus: AssignmentStatus = AssignmentStatus.SUBMITTED,

    @SerialName("is_late")
    val isLate: Boolean = false,

    @SerialName("late_days")
    val lateDays: Int = 0,

    @SerialName("grade")
    val grade: Double? = null,

    @SerialName("graded_by")
    val gradedBy: Long? = null,

    @SerialName("graded_at")
    val gradedAt: String? = null,

    @SerialName("feedback")
    val feedback: String? = null,

    @SerialName("points_deducted_late")
    val pointsDeductedLate: Double = 0.0,

    @SerialName("final_score")
    val finalScore: Double? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_assignment")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var assignment: AssignmentModel? = null,

    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grader: TeacherModel? = null
) : Parcelable

/**
 * AttendanceModel - Registro de Presença
 */
@Serializable
@Parcelize
data class AttendanceModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("classe_id")
    val classeId: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("date")
    val date: String = "",

    @SerialName("status")
    val status: AttendanceStatus = AttendanceStatus.PRESENT,

    @SerialName("check_in_time")
    val checkInTime: String? = null,

    @SerialName("check_out_time")
    val checkOutTime: String? = null,

    @SerialName("notes")
    val notes: String? = null,

    @SerialName("excuse_reason")
    val excuseReason: String? = null,

    @SerialName("excuse_document_url")
    val excuseDocumentUrl: String? = null,

    @SerialName("late_minutes")
    val lateMinutes: Int = 0,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null,

    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null
) : Parcelable

/**
 * ClasseModel - Turma escolar
 */
@Serializable
@Parcelize
data class ClasseModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "", // "10ª Classe A"

    @SerialName("grade_id")
    val gradeId: Long = 0, // ✅ FK para GradeModel

    @SerialName("room")
    val room: String? = null,

    @SerialName("school_id")
    val schoolId: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long? = null,

    @SerialName("school_year")
    val schoolYear: String = "", // "2024/2025"

    @SerialName("shift")
    val shift: String = "", // "Morning", "Afternoon", "Evening"

    @SerialName("capacity")
    val capacity: Int = 0, // ✅ Capacidade máxima

    @SerialName("current_students")
    val currentStudents: Int = 0, // ✅ Total de alunos

    @SerialName("meeting_days")
    val meetingDays: String = "", // JSON: ["MON","WED","FRI"]

    @SerialName("meeting_time")
    val meetingTime: String = "", // "08:00-10:00"

    @SerialName("academic_year_start")
    val academicYearStart: String = "",

    @SerialName("academic_year_end")
    val academicYearEnd: String = "",

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_grade")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grade: GradeModel? = null,

    @SerialName("tb_school")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var school: SchoolModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_class_students")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var students: List<StudentModel>? = null
) : Parcelable

/**
 * ExamModel - Prova/Avaliação
 */
@Serializable
@Parcelize
data class ExamModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("classe_id")
    val classeId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("grade_id")
    val gradeId: Long = 0,

    @SerialName("exam_date")
    val examDate: String = "",

    @SerialName("start_time")
    val startTime: String = "",

    @SerialName("end_time")
    val endTime: String = "",

    @SerialName("duration_minutes")
    val durationMinutes: Int = 90,

    @SerialName("total_questions")
    val totalQuestions: Int = 0,

    @SerialName("max_score")
    val maxScore: Int = 100,

    @SerialName("passing_score")
    val passingScore: Int = 50,

    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel = DifficultyLevel.MEDIUM,

    @SerialName("room")
    val room: String? = null,

    @SerialName("instructions")
    val instructions: String = "",

    @SerialName("is_published")
    val isPublished: Boolean = false,

    @SerialName("published_at")
    val publishedAt: String? = null,

    @SerialName("average_score")
    val averageScore: Double? = null,

    @SerialName("highest_score")
    val highestScore: Double? = null,

    @SerialName("lowest_score")
    val lowestScore: Double? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null,

    @SerialName("tb_grade")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grade: GradeModel? = null
) : Parcelable

/**
 * ExamResultModel - Resultado de Prova
 */
@Serializable
@Parcelize
data class ExamResultModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("exam_id")
    val examId: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("score")
    val score: Double = 0.0,

    @SerialName("percentage")
    val percentage: Double = 0.0,

    @SerialName("grade")
    val grade: String? = null, // "A", "B", "C", etc.

    @SerialName("is_passing")
    val isPassing: Boolean = false,

    @SerialName("correct_answers")
    val correctAnswers: Int = 0,

    @SerialName("incorrect_answers")
    val incorrectAnswers: Int = 0,

    @SerialName("skipped_questions")
    val skippedQuestions: Int = 0,

    @SerialName("time_spent_minutes")
    val timeSpentMinutes: Int = 0,

    @SerialName("started_at")
    val startedAt: String? = null,

    @SerialName("completed_at")
    val completedAt: String? = null,

    @SerialName("graded_by")
    val gradedBy: Long? = null,

    @SerialName("graded_at")
    val gradedAt: String? = null,

    @SerialName("feedback")
    val feedback: String? = null,

    @SerialName("rank_in_class")
    val rankInClass: Int? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_exam")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var exam: ExamModel? = null,

    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grader: TeacherModel? = null
) : Parcelable

/**
 * ExerciseModel - Exercício/Questão
 */
@Serializable
@Parcelize
data class ExerciseModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("lesson_id")
    val lessonId: Long = 0,

    @SerialName("title")
    val title: String = "",

    @SerialName("question")
    val question: String = "",

    @SerialName("question_type")
    val questionType: String = "", // "multiple_choice", "true_false", "short_answer", "essay"

    @SerialName("options")
    val options: String = "", // JSON array para múltipla escolha

    @SerialName("correct_answer")
    val correctAnswer: String = "",

    @SerialName("explanation")
    val explanation: String? = null,

    @SerialName("points")
    val points: Int = 10,

    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel = DifficultyLevel.MEDIUM,

    @SerialName("estimated_time")
    val estimatedTime: Int = 5, // minutos

    @SerialName("hint")
    val hint: String? = null,

    @SerialName("has_image")
    val hasImage: Boolean = false,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("order")
    val order: Int = 0,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_lesson")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var lesson: LessonModel? = null
) : Parcelable

/**
 * ExerciseResultModel - Resultado de Exercício
 */
@Serializable
@Parcelize
data class ExerciseResultModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("exercise_id")
    val exerciseId: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("answer")
    val answer: String = "",

    @SerialName("is_correct")
    val isCorrect: Boolean = false,

    @SerialName("score")
    val score: Double = 0.0,

    @SerialName("time_spent_seconds")
    val timeSpentSeconds: Int = 0,

    @SerialName("attempts")
    val attempts: Int = 1,

    @SerialName("completed_at")
    val completedAt: String = "",

    @SerialName("feedback")
    val feedback: String? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_exercise")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var exercise: ExerciseModel? = null,

    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null
) : Parcelable

/**
 * GradeModel - Classe/Ano escolar
 */
@Serializable
@Parcelize
data class GradeModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("name_en")
    val nameEn: String = "",

    @SerialName("number")
    val number: Int = 1,

    @SerialName("level")
    val level: GradeLevel = GradeLevel.PRIMARY,

    @SerialName("details")
    val details: String = "",

    @SerialName("color")
    val color: String = "",

    @SerialName("age_range")
    val ageRange: String = "",

    @SerialName("order")
    val order: Int = 0,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("curriculum_id")
    val curriculumId: Long? = null,

    @SerialName("subjects_count")
    val subjectsCount: Int = 0
) : Parcelable

/**
 * GuardianModel - Encarregado de educação
 */
@Serializable
@Parcelize
data class GuardianModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("phone")
    val phone: String = "",

    @SerialName("email")
    val email: String? = null,

    @SerialName("relation")
    val relation: GuardianRelation = GuardianRelation.FATHER,

    @SerialName("commune_id")
    val communeId: Long? = null, // ✅ Usar db_eaonde

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("notification_preferences")
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),

    @SerialName("report_frequency")
    val reportFrequency: ReportFrequency = ReportFrequency.WEEKLY,

    @SerialName("parental_control_active")
    val parentalControlActive: Boolean = false,

    @SerialName("blocked_content_categories")
    val blockedContentCategories: List<String> = emptyList(),

    @SerialName("daily_time_limit")
    val dailyTimeLimit: Int = 120, // minutos

    @SerialName("can_purchase_content")
    val canPurchaseContent: Boolean = true,

    @SerialName("payment_methods")
    val paymentMethods: List<String> = emptyList(), // JSON array

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_commune")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var commune: CommuneModel? = null,

    @SerialName("tb_students")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var students: List<StudentModel>? = null
) : Parcelable

/**
 * LessonModel - Aula/Conteúdo
 */
@Serializable
@Parcelize
data class LessonModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("grade_id")
    val gradeId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("video_url")
    val videoUrl: String = "",

    @SerialName("audio_url")
    val audioUrl: String? = null,

    @SerialName("duration")
    val duration: Int = 0, // segundos

    @SerialName("thumbnail")
    val thumbnail: String = "",

    @SerialName("content_type")
    val contentType: ContentType = ContentType.VIDEO,

    @SerialName("difficulty_level")
    val difficultyLevel: DifficultyLevel = DifficultyLevel.MEDIUM,

    @SerialName("prerequisites")
    val prerequisites: String = "", // JSON array de lesson_ids

    @SerialName("tags")
    val tags: String = "", // JSON array

    @SerialName("view_count")
    val viewCount: Int = 0,

    @SerialName("completion_count")
    val completionCount: Int = 0,

    @SerialName("average_rating")
    val averageRating: Double = 0.0,

    @SerialName("total_ratings")
    val totalRatings: Int = 0,

    @SerialName("download_count")
    val downloadCount: Int = 0,

    @SerialName("is_free")
    val isFree: Boolean = true,

    @SerialName("price")
    val price: Double = 0.0,

    @SerialName("is_featured")
    val isFeatured: Boolean = false,

    @SerialName("is_approved")
    val isApproved: Boolean = true,

    @SerialName("approved_by")
    val approvedBy: Long? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_grade")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grade: GradeModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_exercises")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var exercises: List<ExerciseModel>? = null
) : Parcelable

/**
 * LessonProgressModel - Progresso na Aula
 */
@Serializable
@Parcelize
data class LessonProgressModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("lesson_id")
    val lessonId: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("progress_percentage")
    val progressPercentage: Int = 0,

    @SerialName("last_position")
    val lastPosition: Int = 0, // segundos

    @SerialName("completed")
    val completed: Boolean = false,

    @SerialName("completed_at")
    val completedAt: String? = null,

    @SerialName("time_spent_minutes")
    val timeSpentMinutes: Int = 0,

    @SerialName("watch_count")
    val watchCount: Int = 1,

    @SerialName("first_watched_at")
    val firstWatchedAt: String = "",

    @SerialName("last_watched_at")
    val lastWatchedAt: String = "",

    @SerialName("notes")
    val notes: String? = null,

    @SerialName("rating")
    val rating: Int? = null, // 1-5

    @SerialName("feedback")
    val feedback: String? = null,

    @SerialName("is_bookmarked")
    val isBookmarked: Boolean = false,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_lesson")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var lesson: LessonModel? = null,

    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null
) : Parcelable

/**
 * MessageModel - Mensagem/Conversa
 */
@Serializable
@Parcelize
data class MessageModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("sender_id")
    val senderId: Long = 0,

    // ✅ REMOVIDO: sender_type (está em sender.userType)

    @SerialName("receiver_id")
    val receiverId: Long = 0,

    // ✅ REMOVIDO: receiver_type (está em receiver.userType)

    @SerialName("subject")
    val subject: String? = null,

    @SerialName("content")
    val content: String = "",

    @SerialName("message_type")
    val messageType: MessageType = MessageType.DIRECT,

    @SerialName("priority")
    val priority: MessagePriority = MessagePriority.NORMAL,

    @SerialName("is_read")
    val isRead: Boolean = false,

    @SerialName("read_at")
    val readAt: String? = null,

    @SerialName("parent_message_id")
    val parentMessageId: Long? = null,

    @SerialName("has_attachments")
    val hasAttachments: Boolean = false,

    @SerialName("attachment_urls")
    val attachmentUrls: List<String> = emptyList(), // ✅ MUDOU: String JSON -> List

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // ==================== RELATIONS ====================

    // ✅ MUDOU: Any -> UserModel
    @SerialName("tb_sender")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var sender: UserModel? = null,

    @SerialName("tb_receiver")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var receiver: UserModel? = null,

    @SerialName("tb_parent_message")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var parentMessage: MessageModel? = null
) : Parcelable
/**
 * NotificationModel - Notificação do Sistema
 */
@Serializable
@Parcelize
data class NotificationModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("user_id")
    val userId: Long = 0,

    @SerialName("user_type")
    val userType: String = "", // "student", "teacher", "guardian", "director", "admin"

    @SerialName("title")
    val title: String = "",

    @SerialName("message")
    val message: String = "",

    @SerialName("notification_type")
    val notificationType: NotificationType = NotificationType.SYSTEM,

    @SerialName("related_id")
    val relatedId: Long? = null, // ID relacionado (lesson, assignment, etc.)

    @SerialName("related_type")
    val relatedType: String? = null, // Tipo do relacionamento

    @SerialName("is_read")
    val isRead: Boolean = false,

    @SerialName("read_at")
    val readAt: String? = null,

    @SerialName("action_url")
    val actionUrl: String? = null,

    @SerialName("action_text")
    val actionText: String? = null,

    @SerialName("priority")
    val priority: Int = 1, // 1=low, 2=medium, 3=high

    @SerialName("expires_at")
    val expiresAt: String? = null,

    @SerialName("sent_via")
    val sentVia: List<String> = emptyList(), // JSON array: ["push", "email", "sms"]

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null
) : Parcelable

/**
 * PaymentModel - Pagamento/Transação
 */
@Serializable
@Parcelize
data class PaymentModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("user_id")
    val userId: Long = 0,

    @SerialName("user_type")
    val userType: String = "", // "student", "guardian", "school"

    @SerialName("amount")
    val amount: Double = 0.0,

    @SerialName("currency")
    val currency: String = "USD",

    @SerialName("description")
    val description: String = "",

    @SerialName("payment_method")
    val paymentMethod: String = "", // "credit_card", "mobile_money", "bank_transfer"

    @SerialName("transaction_id")
    val transactionId: String = "",

    @SerialName("status")
    val status: String = "", // "pending", "completed", "failed", "refunded"

    @SerialName("payment_date")
    val paymentDate: String = "",

    @SerialName("subscription_id")
    val subscriptionId: Long? = null,

    @SerialName("invoice_url")
    val invoiceUrl: String? = null,

    @SerialName("receipt_url")
    val receiptUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null
) : Parcelable

/**
 * SchoolModel - Escola
 */
@Serializable
@Parcelize
data class SchoolModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("code")
    val code: String = "",

    @SerialName("commune_id")
    val communeId: Long = 0,

    @SerialName("address")
    val address: String = "",

    @SerialName("school_type_id")
    val schoolTypeId: Long = 0,

    @SerialName("school_director_id")
    val schoolDirectorId: Long? = null,

    @SerialName("logo_url")
    val logoUrl: String? = null,

    @SerialName("founded_date")
    val foundedDate: String? = null,

    @SerialName("contact_email")
    val contactEmail: String = "",

    @SerialName("contact_phone")
    val contactPhone: String = "",

    @SerialName("website")
    val website: String? = null,

    @SerialName("total_students")
    val totalStudents: Int = 0,

    @SerialName("total_teachers")
    val totalTeachers: Int = 0,

    @SerialName("total_classes")
    val totalClasses: Int = 0,

    @SerialName("academic_year")
    val academicYear: String = "", // "2024/2025"

    @SerialName("subscription_plan")
    val subscriptionPlan: String? = null,

    @SerialName("subscription_expires_at")
    val subscriptionExpiresAt: String? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_commune")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var commune: CommuneModel? = null,

    @SerialName("tb_school_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var schoolType: SchoolTypeModel? = null,

    @SerialName("tb_director")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var schoolDirector: SchoolDirectorModel? = null,

    @SerialName("tb_classes")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classes: List<ClasseModel>? = null
) : Parcelable

/**
 * SchoolDirectorModel - Diretor de escola
 */
@Serializable
@Parcelize
data class SchoolDirectorModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("phone")
    val phone: String = "",

    @SerialName("school_id")
    val schoolId: Long = 0,

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("license_active")
    val licenseActive: Boolean = false,

    @SerialName("license_expiration_date")
    val licenseExpirationDate: String? = null,

    @SerialName("contracted_plan")
    val contractedPlan: String? = null,

    @SerialName("years_experience")
    val yearsExperience: Int = 0,

    @SerialName("qualifications")
    val qualifications: String = "", // JSON array

    @SerialName("can_manage_teachers")
    val canManageTeachers: Boolean = true,

    @SerialName("can_manage_students")
    val canManageStudents: Boolean = true,

    @SerialName("can_view_reports")
    val canViewReports: Boolean = true,

    @SerialName("can_approve_content")
    val canApproveContent: Boolean = false,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_school")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var school: SchoolModel? = null
) : Parcelable

/**
 * SchoolTypeModel - Tipo de escola
 */
@Serializable
@Parcelize
data class SchoolTypeModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("name_en")
    val nameEn: String? = null,

    @SerialName("code")
    val code: String = "",

    @SerialName("description")
    val description: String? = null,

    @SerialName("icon")
    val icon: String? = null,

    @SerialName("color")
    val color: String? = null,

    @SerialName("requires_government_approval")
    val requiresGovernmentApproval: Boolean = true,

    @SerialName("can_charge_fees")
    val canChargeFees: Boolean = false,

    @SerialName("max_students")
    val maxStudents: Int? = null,

    @SerialName("min_teachers")
    val minTeachers: Int? = null,

    @SerialName("curriculum_type")
    val curriculumType: String? = null, // "national", "international", "religious"

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
) : Parcelable

/**
 * StateManagerModel - Gestor estadual (Ministério)
 */
@Serializable
@Parcelize
data class StateManagerModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("phone")
    val phone: String = "",

    @SerialName("position")
    val position: String = "",

    @SerialName("ministry")
    val ministry: String = "",

    @SerialName("access_level")
    val accessLevel: ManagerAccessLevel = ManagerAccessLevel.PROVINCIAL,

    @SerialName("province_id")
    val provinceId: Long? = null, // ✅ Usar db_eaonde

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("can_export_data")
    val canExportData: Boolean = true,

    @SerialName("can_view_national_reports")
    val canViewNationalReports: Boolean = false,

    @SerialName("can_approve_content")
    val canApproveContent: Boolean = false,

    @SerialName("can_manage_schools")
    val canManageSchools: Boolean = true,

    @SerialName("can_manage_teachers")
    val canManageTeachers: Boolean = false,

    @SerialName("assigned_regions")
    val assignedRegions: String = "", // JSON array

    @SerialName("reports_generated")
    val reportsGenerated: Int = 0,

    @SerialName("last_report_date")
    val lastReportDate: String? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_schools")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var schools: List<SchoolModel>? = null
) : Parcelable

/**
 * StudentModel - Estudante
 */
@Serializable
@Parcelize
data class StudentModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("birth_date")
    val birthDate: String = "",

    @SerialName("gender")
    val gender: Gender = Gender.MALE,

    @SerialName("student_number")
    val studentNumber: String = "",

    @SerialName("grade_id")
    val gradeId: Long = 0, // ✅ FK para GradeModel

    @SerialName("school_id")
    val schoolId: Long? = null,

    @SerialName("classe_id")
    val classeId: Long? = null,

    @SerialName("guardian_id")
    val guardianId: Long? = null, // ✅ Novo: FK direto

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("account_type")
    val accountType: StudentAccountType = StudentAccountType.FREE,

    // Gamification
    @SerialName("points")
    val points: Int = 0,

    @SerialName("level")
    val level: Int = 1,

    @SerialName("badges")
    val badges: List<BadgeModel> = emptyList(),

    @SerialName("streak_days")
    val streakDays: Int = 0,

    @SerialName("current_streak")
    val currentStreak: Int = 0,

    @SerialName("longest_streak")
    val longestStreak: Int = 0,

    // Progress
    @SerialName("total_lessons_watched")
    val totalLessonsWatched: Int = 0,

    @SerialName("total_exercises_done")
    val totalExercisesDone: Int = 0,

    @SerialName("total_study_time")
    val totalStudyTime: Long = 0, // minutos

    @SerialName("average_score")
    val averageScore: Double = 0.0,

    @SerialName("attendance_rate")
    val attendanceRate: Double = 0.0,

    @SerialName("last_login")
    val lastLogin: String? = null,

    @SerialName("preferred_language")
    val preferredLanguage: String = "pt",

    @SerialName("accessibility_settings")
    val accessibilitySettings: String = "{}", // JSON

    @SerialName("subscription_expires_at")
    val subscriptionExpiresAt: String? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_grade")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var grade: GradeModel? = null,

    @SerialName("tb_school")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var school: SchoolModel? = null,

    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_guardian")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var guardian: GuardianModel? = null,

    @SerialName("tb_lesson_progress")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var lessonProgress: List<LessonProgressModel>? = null,

    @SerialName("tb_assignments")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var assignments: List<AssignmentSubmissionModel>? = null,

    @SerialName("tb_exam_results")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var examResults: List<ExamResultModel>? = null,

    @SerialName("tb_attendance")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var attendance: List<AttendanceModel>? = null
) : Parcelable

/**
 * StudentAchievementModel - Conquista do Estudante
 */
@Serializable
@Parcelize
data class StudentAchievementModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("student_id")
    val studentId: Long = 0,

    @SerialName("achievement_type")
    val achievementType: String = "", // "academic", "attendance", "participation", "skill"

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("points_awarded")
    val pointsAwarded: Int = 0,

    @SerialName("badge_id")
    val badgeId: Long? = null,

    @SerialName("achieved_at")
    val achievedAt: String = "",

    @SerialName("related_id")
    val relatedId: Long? = null, // lesson, exam, etc.

    @SerialName("related_type")
    val relatedType: String? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_student")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var student: StudentModel? = null,

    @SerialName("tb_badge")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var badge: BadgeModel? = null
) : Parcelable

/**
 * SubjectModel - Disciplina
 */
@Serializable
@Parcelize
data class SubjectModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("name_en")
    val nameEn: String? = null,

    @SerialName("code")
    val code: String = "",

    @SerialName("description")
    val description: String? = null,

    @SerialName("icon")
    val icon: String? = null,

    @SerialName("color")
    val color: String? = null,

    @SerialName("order")
    val order: Int = 0,

    @SerialName("grade_levels")
    val gradeLevels: String = "", // JSON array de GradeLevel

    @SerialName("weekly_hours")
    val weeklyHours: Int = 0,

    @SerialName("is_core")
    val isCore: Boolean = true,

    @SerialName("curriculum_id")
    val curriculumId: Long? = null,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("total_lessons")
    val totalLessons: Int = 0,

    @SerialName("total_teachers")
    val totalTeachers: Int = 0
) : Parcelable

/**
 * SubscriptionModel - Assinatura/Plano
 */
@Serializable
@Parcelize
data class SubscriptionModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("user_id")
    val userId: Long = 0,

    @SerialName("user_type")
    val userType: String = "", // "student", "guardian", "school"

    @SerialName("plan_name")
    val planName: String = "",

    @SerialName("plan_type")
    val planType: String = "", // "monthly", "yearly", "lifetime"

    @SerialName("features")
    val features: String = "", // JSON array

    @SerialName("price")
    val price: Double = 0.0,

    @SerialName("currency")
    val currency: String = "USD",

    @SerialName("start_date")
    val startDate: String = "",

    @SerialName("end_date")
    val endDate: String? = null,

    @SerialName("auto_renew")
    val autoRenew: Boolean = true,

    @SerialName("status")
    val status: String = "", // "active", "canceled", "expired", "pending"

    @SerialName("canceled_at")
    val canceledAt: String? = null,

    @SerialName("cancel_reason")
    val cancelReason: String? = null,

    @SerialName("payment_method")
    val paymentMethod: String? = null,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null,

    @SerialName("tb_payments")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var payments: List<PaymentModel>? = null
) : Parcelable

/**
 * SystemAdministratorModel - Administrador do sistema
 */
@Serializable
@Parcelize
data class SystemAdministratorModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("phone")
    val phone: String = "",

    @SerialName("permission_level")
    val permissionLevel: AdminPermissionLevel = AdminPermissionLevel.CONTENT_ADMIN,

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("two_factor_auth")
    val twoFactorAuth: Boolean = false,

    @SerialName("actions_performed")
    val actionsPerformed: Int = 0,

    @SerialName("last_action")
    val lastAction: String? = null,

    @SerialName("last_action_date")
    val lastActionDate: String? = null,

    @SerialName("failed_login_attempts")
    val failedLoginAttempts: Int = 0,

    @SerialName("last_login")
    val lastLogin: String? = null,

    @SerialName("ip_address")
    val ipAddress: String? = null,

    @SerialName("can_manage_users")
    val canManageUsers: Boolean = true,

    @SerialName("can_manage_content")
    val canManageContent: Boolean = true,

    @SerialName("can_manage_payments")
    val canManagePayments: Boolean = false,

    @SerialName("can_view_logs")
    val canViewLogs: Boolean = true,

    @SerialName("assigned_modules")
    val assignedModules: String = "", // JSON array

    @SerialName("status")
    val status: UserStatus = UserStatus.ACTIVE,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null
) :Parcelable

/**
 * TeacherAssignmentModel - Atribuição de Professor à Classe
 */
@Serializable
@Parcelize
data class TeacherAssignmentModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("classe_id")
    val classeId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("school_year")
    val schoolYear: String = "",

    @SerialName("is_homeroom_teacher")
    val isHomeroomTeacher: Boolean = false,

    @SerialName("weekly_hours")
    val weeklyHours: Int = 0,

    @SerialName("meeting_days")
    val meetingDays: String = "", // JSON array

    @SerialName("meeting_time")
    val meetingTime: String = "",

    @SerialName("start_date")
    val startDate: String = "",

    @SerialName("end_date")
    val endDate: String? = null,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null
) : Parcelable

/**
 * TeacherModel - Professor
 */
@Serializable
@Parcelize
data class TeacherModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("full_name")
    val fullName: String = "",

    @SerialName("teacher_number")
    val teacherNumber: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("phone")
    val phone: String = "",

    @SerialName("school_id")
    val schoolId: Long? = null,

    @SerialName("type")
    val type: TeacherType = TeacherType.SCHOOL,

    @SerialName("profile_photo")
    val profilePhoto: String? = null,

    @SerialName("biography")
    val biography: String? = null,

    // Ratings
    @SerialName("average_rating")
    val averageRating: Double = 0.0,

    @SerialName("total_ratings")
    val totalRatings: Int = 0,

    // Statistics
    @SerialName("total_lessons_created")
    val totalLessonsCreated: Int = 0,

    @SerialName("total_students_impacted")
    val totalStudentsImpacted: Int = 0,

    @SerialName("years_experience")
    val yearsExperience: Int = 0,

    @SerialName("highest_qualification")
    val highestQualification: String? = null,

    // Verification
    @SerialName("verified")
    val verified: Boolean = false,

    @SerialName("verified_by_admin_id")
    val verifiedByAdminId: Long? = null,

    @SerialName("verified_at")
    val verifiedAt: String? = null,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null,

    @SerialName("rejected_by")
    val rejectedBy: Long? = null,

    @SerialName("rejected_at")
    val rejectedAt: String? = null,

    // Marketplace
    @SerialName("marketplace_enabled")
    val marketplaceEnabled: Boolean = false,

    @SerialName("marketplace_balance")
    val marketplaceBalance: Double = 0.0,

    @SerialName("total_sales")
    val totalSales: Int = 0,

    @SerialName("commission_rate")
    val commissionRate: Double = 0.0,

    @SerialName("is_featured")
    val isFeatured: Boolean = false,

    @SerialName("languages")
    val languages: String = "", // JSON array

    @SerialName("office_hours")
    val officeHours: String? = null,

    @SerialName("consultation_fee")
    val consultationFee: Double? = null,

    @SerialName("status")
    val status: UserStatus = UserStatus.PENDING_APPROVAL,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // ==================== RELATIONS ====================

    @SerialName("tb_school")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var school: SchoolModel? = null,

    @SerialName("tb_verified_by_admin")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var verifiedByAdmin: SystemAdministratorModel? = null,

    @SerialName("tb_teacher_subjects")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subjects: List<TeacherSubjectModel>? = null,

    @SerialName("tb_teacher_assignments")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var assignments: List<TeacherAssignmentModel>? = null,

    @SerialName("tb_teacher_certifications")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var certifications: List<TeacherCertificationModel>? = null,

    @SerialName("tb_lessons")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var lessons: List<LessonModel>? = null,

    @SerialName("tb_assignments")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var createdAssignments: List<AssignmentModel>? = null,

    @SerialName("tb_exams")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var exams: List<ExamModel>? = null
) : Parcelable

@Serializable
@Parcelize
data class TeacherSubjectModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("is_primary")
    val isPrimary: Boolean = false,

    @SerialName("years_experience")
    val yearsExperience: Int = 0,

    @SerialName("certification_level")
    val certificationLevel: String? = null,

    @SerialName("rating_in_subject")
    val ratingInSubject: Double = 0.0,

    @SerialName("total_lessons_in_subject")
    val totalLessonsInSubject: Int = 0,

    @SerialName("created_at")
    val createdAt: String? = null,

    // Relations
    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null
) : Parcelable

/**
 * TeacherCertificationModel - Certificações do professor
 */
@Serializable
@Parcelize
data class TeacherCertificationModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("institution")
    val institution: String = "",

    @SerialName("issue_date")
    val issueDate: String? = null,

    @SerialName("expiry_date")
    val expiryDate: String? = null,

    @SerialName("certificate_url")
    val certificateUrl: String? = null,

    @SerialName("certificate_number")
    val certificateNumber: String? = null,

    @SerialName("verification_status")
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,

    @SerialName("verified_by_admin_id")
    val verifiedByAdminId: Long? = null,

    @SerialName("verified_at")
    val verifiedAt: String? = null,

    @SerialName("rejection_reason")
    val rejectionReason: String? = null,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String? = null,

    // Relations
    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null,

    @SerialName("tb_verified_by_admin")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var verifiedByAdmin: SystemAdministratorModel? = null
) : Parcelable

/**
 * TimetableModel - Horário Escolar
 */
@Serializable
@Parcelize
data class TimetableModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("classe_id")
    val classeId: Long = 0,

    @SerialName("subject_id")
    val subjectId: Long = 0,

    @SerialName("teacher_id")
    val teacherId: Long = 0,

    @SerialName("day_of_week")
    val dayOfWeek: Int = 0, // 1=Segunda, 2=Terça, etc.

    @SerialName("start_time")
    val startTime: String = "", // "08:00"

    @SerialName("end_time")
    val endTime: String = "", // "09:00"

    @SerialName("room")
    val room: String? = null,

    @SerialName("is_recurring")
    val isRecurring: Boolean = true,

    @SerialName("recurrence_pattern")
    val recurrencePattern: String? = null, // JSON

    @SerialName("school_year")
    val schoolYear: String = "",

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_classe")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var classe: ClasseModel? = null,

    @SerialName("tb_subject")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var subject: SubjectModel? = null,

    @SerialName("tb_teacher")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: TeacherModel? = null
) : Parcelable

/**
 * UserActivityLogModel - Log de Atividade do Usuário
 */
@Serializable
@Parcelize
data class UserActivityLogModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("user_id")
    val userId: Long = 0,

    @SerialName("user_type")
    val userType: String = "", // "student", "teacher", "admin", etc.

    @SerialName("activity_type")
    val activityType: String = "", // "login", "logout", "view_lesson", "submit_assignment"

    @SerialName("description")
    val description: String = "",

    @SerialName("ip_address")
    val ipAddress: String? = null,

    @SerialName("device_info")
    val deviceInfo: String? = null,

    @SerialName("user_agent")
    val userAgent: String? = null,

    @SerialName("related_id")
    val relatedId: Long? = null,

    @SerialName("related_type")
    val relatedType: String? = null,

    @SerialName("metadata")
    val metadata: String? = null, // JSON adicional

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null
) : Parcelable

/**
 * UserSessionModel - Sessão do Usuário
 */
@Serializable
@Parcelize
data class UserSessionModel(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("user_id")
    val userId: Long = 0,

    @SerialName("user_type")
    val userType: String = "",

    @SerialName("session_token")
    val sessionToken: String = "",

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    @SerialName("ip_address")
    val ipAddress: String? = null,

    @SerialName("device_id")
    val deviceId: String? = null,

    @SerialName("device_name")
    val deviceName: String? = null,

    @SerialName("device_type")
    val deviceType: String? = null, // "mobile", "tablet", "desktop"

    @SerialName("os_version")
    val osVersion: String? = null,

    @SerialName("app_version")
    val appVersion: String? = null,

    @SerialName("login_at")
    val loginAt: String = "",

    @SerialName("last_activity_at")
    val lastActivityAt: String = "",

    @SerialName("logout_at")
    val logoutAt: String? = null,

    @SerialName("expires_at")
    val expiresAt: String = "",

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null
) : Parcelable



// ==================== ENUMS SERIALIZÁVEIS ====================

@Serializable
enum class LiveStatus {
    @SerialName("scheduled")
    SCHEDULED,

    @SerialName("live")
    LIVE,
    @SerialName("paused")
    PAUSED,
    @SerialName("ended")
    ENDED,

    @SerialName("cancelled")
    CANCELLED
}

@Serializable
enum class ParticipantRole {
    @SerialName("viewer")
    VIEWER,

    @SerialName("speaker")
    SPEAKER,

    @SerialName("moderator")
    MODERATOR,

    @SerialName("teacher")
    TEACHER
}

@Serializable
enum class ChatMessageType {
    @SerialName("text")
    TEXT,

    @SerialName("question")
    QUESTION,

    @SerialName("answer")
    ANSWER,

    @SerialName("system")
    SYSTEM
}


//=============================================================
//=============================================================
// ==================== MODELOS PRINCIPAIS ====================
/**
 * LiveSessionModel - Sessão de Aula ao Vivo
 */



@Serializable
@Parcelize
data class LiveSessionModel(
    // Identificação
    @SerialName("id")
    val id: String = "",

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("subject")
    val subject: String = "Matemática",

    // Professor
    @SerialName("teacher_id")
    val teacherId: String = "",

    @SerialName("teacher_name")
    val teacherName: String = "",

    @SerialName("teacher_avatar")
    val teacherAvatar: String? = null,

    // Agendamento
    @SerialName("scheduled_start")
    val scheduledStart: String = "", // ISO 8601 format

    @SerialName("actual_start")
    val actualStart: String? = null,

    @SerialName("actual_end")
    val actualEnd: String? = null,

    @SerialName("duration_minutes")
    val durationMinutes: Int = 60,

    // Status
    @SerialName("status")
    val status: LiveStatus = LiveStatus.SCHEDULED,

    // Streaming
    @SerialName("stream_id")
    val streamId: String? = null,

    @SerialName("playback_url")
    val playbackUrl: String? = null,

    @SerialName("rtmp_url")
    val rtmpUrl: String? = null,

    @SerialName("stream_key")
    val streamKey: String? = null,

    // Configurações
    @SerialName("max_participants")
    val maxParticipants: Int? = null,

    @SerialName("is_public")
    val isPublic: Boolean = true,

    @SerialName("requires_approval")
    val requiresApproval: Boolean = false,

    @SerialName("price")
    val price: Double = 0.0,

    // Métricas
    @SerialName("participant_count")
    val participantCount: Int = 0,

    @SerialName("view_count")
    val viewCount: Int = 0,

    // Timestamps
    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String? = null,

    // Relations (para queries JOIN)
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var teacher: UserModel? = null,

    @SerialName("current_user_participant")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var currentUserParticipant: LiveParticipantModel? = null

) : Parcelable {

    // ==================== PROPRIEDADES COMPUTADAS ====================

    /**
     * Verifica se a live é gratuita
     */
    val isFree: Boolean get() = price == 0.0

    /**
     * Verifica se está ao vivo agora
     */
    val isLiveNow: Boolean get() = status == LiveStatus.LIVE

    /**
     * Verifica se já terminou
     */
    val hasEnded: Boolean get() = status == LiveStatus.ENDED

    /**
     * Verifica se está agendada (futura)
     */
    val isUpcoming: Boolean get() = status == LiveStatus.SCHEDULED

    /**
     * Verifica se foi cancelada
     */
    val isCancelled: Boolean get() = status == LiveStatus.CANCELLED

    /**
     * Verifica se está cheia (max participants)
     */
    val isFull: Boolean get() = maxParticipants?.let { participantCount >= it } ?: false

    /**
     * Verifica se o usuário pode entrar
     */
    val canJoin: Boolean get() = isPublic && !isFull && !hasEnded && !isCancelled

    /**
     * Verifica se o usuário atual já entrou
     */
    val currentUserJoined: Boolean get() = currentUserParticipant != null

    /**
     * Papel do usuário atual na live
     */
    val currentUserRole: ParticipantRole? get() = currentUserParticipant?.role

    /**
     * Cor baseada na disciplina (para UI)
     */
    val subjectColor: Color
        get() = when (subject.lowercase()) {
            "matemática" -> Color(0xFFEF4444) // Red500
            "português" -> Color(0xFF3B82F6)  // Blue500
            "inglês" -> Color(0xFF8B5CF6)     // Purple500
            "biologia" -> Color(0xFF10B981)   // Green500
            "química" -> Color(0xFFF59E0B)    // Yellow500
            "física" -> Color(0xFFEC4899)     // Pink500
            "história" -> Color(0xFFF97316)   // Orange500
            "geografia" -> Color(0xFF059669)  // Emerald500
            else -> Color(0xFF6B7280)         // Gray500
        }
}
@Serializable
@Parcelize
data class LiveParticipantModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("user_name")
    val userName: String = "",

    @SerialName("user_avatar")
    val userAvatar: String? = null,

    @SerialName("role")
    val role: ParticipantRole = ParticipantRole.VIEWER,

    @SerialName("is_approved")
    val isApproved: Boolean = true,

    @SerialName("joined_at")
    val joinedAt: String = "",

    @SerialName("left_at")
    val leftAt: String? = null,

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null,

    @SerialName("live_session")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var liveSession: LiveSessionModel? = null

) : Parcelable
@Serializable
@Parcelize
data class LiveChatMessageModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("user_name")
    val userName: String = "",

    @SerialName("user_avatar")
    val userAvatar: String? = null,

    @SerialName("message")
    val message: String = "",

    @SerialName("message_type")
    val messageType: ChatMessageType = ChatMessageType.TEXT,

    @SerialName("is_approved")
    val isApproved: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    // Relations
    @SerialName("tb_user")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var user: UserModel? = null

) : Parcelable
// ==================== REQUEST/RESPONSE MODELS ====================

@Serializable
data class CreateLiveSessionRequest(
    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String = "",

    @SerialName("subject")
    val subject: String = "Matemática",

    @SerialName("scheduled_start")
    val scheduledStart: String,

    @SerialName("duration_minutes")
    val durationMinutes: Int = 60,

    @SerialName("max_participants")
    val maxParticipants: Int? = null,

    @SerialName("is_public")
    val isPublic: Boolean = true,

    @SerialName("requires_approval")
    val requiresApproval: Boolean = false,

    @SerialName("price")
    val price: Double = 0.0
)

@Serializable
data class UpdateLiveSessionRequest(
    @SerialName("title")
    val title: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("subject")
    val subject: String? = null,

    @SerialName("scheduled_start")
    val scheduledStart: String? = null,

    @SerialName("duration_minutes")
    val durationMinutes: Int? = null,

    @SerialName("max_participants")
    val maxParticipants: Int? = null,

    @SerialName("is_public")
    val isPublic: Boolean? = null,

    @SerialName("status")
    val status: LiveStatus? = null
)

@Serializable
data class JoinLiveSessionRequest(
    @SerialName("live_session_id")
    val liveSessionId: String,

    @SerialName("role")
    val role: ParticipantRole = ParticipantRole.VIEWER
)

@Serializable
data class SendChatMessageRequest(
    @SerialName("live_session_id")
    val liveSessionId: String,

    @SerialName("message")
    val message: String,

    @SerialName("message_type")
    val messageType: ChatMessageType = ChatMessageType.TEXT
)

@Serializable
data class StreamInfoResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("live_session")
    val liveSession: LiveSessionModel,

    @SerialName("stream_info")
    val streamInfo: StreamInfo? = null,

    @SerialName("error")
    val error: String? = null
)





@Serializable
@Parcelize
data class LiveReactionModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("emoji")
    val emoji: String = "👍", // 👍, ❤️, 😂, 😮, 👏

    @SerialName("created_at")
    val createdAt: String = ""
) : Parcelable

@Serializable
@Parcelize
data class LivePollModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("question")
    val question: String = "",

    @SerialName("options")
    val options: List<PollOption> = emptyList(),

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("ends_at")
    val endsAt: String? = null
) : Parcelable

@Serializable
@Parcelize
data class PollOption(
    @SerialName("id")
    val id: String = "",

    @SerialName("text")
    val text: String = "",

    @SerialName("vote_count")
    val voteCount: Int = 0
) : Parcelable

@Serializable
@Parcelize
data class PollVoteModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("poll_id")
    val pollId: String = "",

    @SerialName("option_id")
    val optionId: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("created_at")
    val createdAt: String = ""
) : Parcelable
@Serializable
@Parcelize
data class StreamQuality(
    @SerialName("quality")
    val quality: String = "auto", // "auto", "1080p", "720p", "480p", "360p"

    @SerialName("bitrate")
    val bitrate: Int = 2500, // kbps

    @SerialName("fps")
    val fps: Int = 30,

    @SerialName("latency")
    val latency: Int = 0 // ms
) : Parcelable

// Adicionar ao LiveSessionModel:
@SerialName("available_qualities")
val availableQualities: List<String> = listOf("auto", "720p", "480p", "360p")

@Serializable
data class StreamInfo(
    @SerialName("rtmp_url")
    val rtmpUrl: String,

    @SerialName("stream_key")
    val streamKey: String,

    @SerialName("playback_url")
    val playbackUrl: String,

    // ✅ ADICIONAR
    @SerialName("hls_url")
    val hlsUrl: String? = null,

    @SerialName("backup_url")
    val backupUrl: String? = null,

    @SerialName("chat_url")
    val chatUrl: String? = null,

    @SerialName("quality_options")
    val qualityOptions: List<StreamQuality> = emptyList(),

    @SerialName("server_region")
    val serverRegion: String = "eu-west-1"
)

@Serializable
@Parcelize
data class LiveStreamStats(
    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("current_viewers")
    val currentViewers: Int = 0,

    @SerialName("peak_viewers")
    val peakViewers: Int = 0,

    @SerialName("total_views")
    val totalViews: Int = 0,

    @SerialName("average_watch_time_seconds")
    val averageWatchTimeSeconds: Int = 0,

    @SerialName("chat_messages_count")
    val chatMessagesCount: Int = 0,

    @SerialName("reactions_count")
    val reactionsCount: Int = 0,

    @SerialName("bandwidth_mbps")
    val bandwidthMbps: Double = 0.0,

    @SerialName("buffering_ratio")
    val bufferingRatio: Double = 0.0,

    @SerialName("updated_at")
    val updatedAt: String = ""
) : Parcelable

@Serializable
@Parcelize
data class LiveNotificationModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("type")
    val type: NotificationType = NotificationType.LIVE_STARTING,

    @SerialName("is_sent")
    val isSent: Boolean = false,

    @SerialName("sent_at")
    val sentAt: String? = null,

    @SerialName("created_at")
    val createdAt: String = ""
) : Parcelable


@Serializable
data class LiveSessionFilters(
    @SerialName("status")
    val status: LiveStatus? = null,

    @SerialName("subject")
    val subject: String? = null,

    @SerialName("teacher_id")
    val teacherId: String? = null,

    @SerialName("is_public")
    val isPublic: Boolean? = null,

    @SerialName("min_price")
    val minPrice: Double? = null,

    @SerialName("max_price")
    val maxPrice: Double? = null,

    @SerialName("search_query")
    val searchQuery: String? = null,

    // ✅ ADICIONAR FILTROS
    @SerialName("date_from")
    val dateFrom: String? = null,  // ISO 8601

    @SerialName("date_to")
    val dateTo: String? = null,

    @SerialName("min_participants")
    val minParticipants: Int? = null,

    @SerialName("max_participants")
    val maxParticipants: Int? = null,

    @SerialName("has_available_spots")
    val hasAvailableSpots: Boolean? = null,

    @SerialName("grade_level")
    val gradeLevel: String? = null,  // "10ª Classe", "11ª Classe"

    @SerialName("tags")
    val tags: List<String>? = null,

    // Paginação
    @SerialName("page")
    val page: Int = 1,

    @SerialName("limit")
    val limit: Int = 20,

    @SerialName("sort_by")
    val sortBy: String = "scheduled_start",

    @SerialName("sort_order")
    val sortOrder: String = "desc"
)

@Serializable
@Parcelize
data class LiveRecordingModel(
    @SerialName("id")
    val id: String = "",

    @SerialName("live_session_id")
    val liveSessionId: String = "",

    @SerialName("video_url")
    val videoUrl: String = "",

    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,

    @SerialName("duration_seconds")
    val durationSeconds: Int = 0,

    @SerialName("file_size_mb")
    val fileSizeMb: Double = 0.0,

    @SerialName("is_public")
    val isPublic: Boolean = false,

    @SerialName("is_processing")
    val isProcessing: Boolean = true,

    @SerialName("created_at")
    val createdAt: String = ""
) : Parcelable {
    val formattedDuration: String
        get() {
            val hours = durationSeconds / 3600
            val minutes = (durationSeconds % 3600) / 60
            val seconds = durationSeconds % 60
            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%d:%02d", minutes, seconds)
            }
        }
}


@Serializable
data class LiveSessionUIModel(
    val session: LiveSessionModel,
    val formattedTime: String,
    val statusBadge: String,
    val statusColor: Color,
    val canJoin: Boolean,
    val isUserJoined: Boolean
)