package com.tshikasi.tshikasi_auto_school.data.repository

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.AssignmentDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.AssignmentSubmissionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.AttendanceDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ClasseDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExamDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExamResultDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExerciseDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.ExerciseResultDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.GradeDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.GuardianDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LessonDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LessonProgressDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveChatDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveNotificationDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveParticipantDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LivePollDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveReactionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveRecordingDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveSessionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveStatsDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.MessageDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.NotificationDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.PaymentDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.SchoolDirectorDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.SchoolTypeDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.StateManagerDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.StudentAchievementDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.StudentDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.SubjectDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.SubscriptionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.SystemAdministratorDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.TeacherAssignmentDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.TeacherCertificationDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.TeacherDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.TeacherSubjectDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.TimetableDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.UserActivityLogDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.UserDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.UserSessionDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.AdminPermissionLevel
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentModel
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentStatus
import com.tshikasi.tshikasi_auto_school.domain.model.AssignmentSubmissionModel
import com.tshikasi.tshikasi_auto_school.domain.model.AttendanceModel
import com.tshikasi.tshikasi_auto_school.domain.model.AttendanceStatus
import com.tshikasi.tshikasi_auto_school.domain.model.ChatMessageType
import com.tshikasi.tshikasi_auto_school.domain.model.ClasseModel
import com.tshikasi.tshikasi_auto_school.domain.model.CreateLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.model.DifficultyLevel
import com.tshikasi.tshikasi_auto_school.domain.model.ExamModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExamResultModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExerciseModel
import com.tshikasi.tshikasi_auto_school.domain.model.ExerciseResultModel
import com.tshikasi.tshikasi_auto_school.domain.model.GradeLevel
import com.tshikasi.tshikasi_auto_school.domain.model.GradeModel
import com.tshikasi.tshikasi_auto_school.domain.model.GuardianModel
import com.tshikasi.tshikasi_auto_school.domain.model.GuardianRelation
import com.tshikasi.tshikasi_auto_school.domain.model.JoinLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.model.LessonModel
import com.tshikasi.tshikasi_auto_school.domain.model.LessonProgressModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveChatMessageModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveNotificationModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveParticipantModel
import com.tshikasi.tshikasi_auto_school.domain.model.LivePollModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveReactionModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveRecordingModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionFilters
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStatus
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStreamStats
import com.tshikasi.tshikasi_auto_school.domain.model.ManagerAccessLevel
import com.tshikasi.tshikasi_auto_school.domain.model.MessageModel
import com.tshikasi.tshikasi_auto_school.domain.model.MessageType
import com.tshikasi.tshikasi_auto_school.domain.model.NotificationModel
import com.tshikasi.tshikasi_auto_school.domain.model.NotificationPreferences
import com.tshikasi.tshikasi_auto_school.domain.model.NotificationType
import com.tshikasi.tshikasi_auto_school.domain.model.ParticipantRole
import com.tshikasi.tshikasi_auto_school.domain.model.PaymentModel
import com.tshikasi.tshikasi_auto_school.domain.model.PollVoteModel
import com.tshikasi.tshikasi_auto_school.domain.model.ReportFrequency
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolDirectorModel
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolTypeModel
import com.tshikasi.tshikasi_auto_school.domain.model.SendChatMessageRequest
import com.tshikasi.tshikasi_auto_school.domain.model.StateManagerModel
import com.tshikasi.tshikasi_auto_school.domain.model.StreamInfoResponse
import com.tshikasi.tshikasi_auto_school.domain.model.StudentAccountType
import com.tshikasi.tshikasi_auto_school.domain.model.StudentAchievementModel
import com.tshikasi.tshikasi_auto_school.domain.model.StudentModel
import com.tshikasi.tshikasi_auto_school.domain.model.SubjectModel
import com.tshikasi.tshikasi_auto_school.domain.model.SubscriptionModel
import com.tshikasi.tshikasi_auto_school.domain.model.SystemAdministratorModel
import com.tshikasi.tshikasi_auto_school.domain.model.TeacherAssignmentModel
import com.tshikasi.tshikasi_auto_school.domain.model.TeacherCertificationModel
import com.tshikasi.tshikasi_auto_school.domain.model.TeacherModel
import com.tshikasi.tshikasi_auto_school.domain.model.TeacherSubjectModel
import com.tshikasi.tshikasi_auto_school.domain.model.TeacherType
import com.tshikasi.tshikasi_auto_school.domain.model.TimetableModel
import com.tshikasi.tshikasi_auto_school.domain.model.UpdateLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.model.UserActivityLogModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserSessionModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserStatus
import com.tshikasi.tshikasi_auto_school.domain.model.UserType
import com.tshikasi.tshikasi_auto_school.domain.model.VerificationStatus
import com.tshikasi.tshikasi_auto_school.domain.repository.AssignmentRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.AssignmentSubmissionRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.AttendanceRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.ClasseRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.ExamRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.ExamResultRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.ExerciseRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.ExerciseResultRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.GradeRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.GuardianRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LessonProgressRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LessonRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveChatRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveNotificationRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveParticipantRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LivePollRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveReactionRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveRecordingRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveSessionRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveStatsRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.MessageRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.NotificationRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.PaymentRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SchoolDirectorRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SchoolTypeRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.StateManagerRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.StudentAchievementRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.StudentRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SubjectRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SubscriptionRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SystemAdministratorRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherAssignmentRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherCertificationRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherLiveStats
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherSubjectRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TimetableRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.UserActivityLogRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.UserRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.UserSessionRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// AssignmentRepositoryImpl
class AssignmentRepositoryImpl(
    private val dataSource: AssignmentDataSource
) : AssignmentRepository {

    override suspend fun createAssignment(assignment: AssignmentModel): Either<NetworkError, AssignmentModel> {
        return Either.catch { dataSource.createAssignment(assignment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentById(id: Long): Either<NetworkError, AssignmentModel?> {
        return Either.catch { dataSource.getAssignmentById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsByTeacher(
        teacherId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsByTeacher(teacherId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsByClasse(
        classeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsByClasse(classeId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsBySubject(
        subjectId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsBySubject(subjectId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsByGrade(
        gradeId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsByGrade(gradeId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUpcomingAssignments(studentId: Long, days: Int): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getUpcomingAssignments(studentId, days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getOverdueAssignments(studentId: Long): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.getOverdueAssignments(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchAssignments(
        query: String,
        teacherId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AssignmentModel>> {
        return Either.catch { dataSource.searchAssignments(query, teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAssignment(assignment: AssignmentModel): Either<NetworkError, AssignmentModel> {
        return Either.catch { dataSource.updateAssignment(assignment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAssignmentStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAssignmentStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAssignmentStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAssignmentStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTotalSubmissions(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTotalSubmissions(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteAssignment(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteAssignment(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAssignmentsByTeacher(teacherId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countAssignmentsByTeacher(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAssignmentsByClasse(classeId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countAssignmentsByClasse(classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageScoreByAssignment(assignmentId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageScoreByAssignment(assignmentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentCompletionRate(assignmentId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAssignmentCompletionRate(assignmentId) }.mapLeft { it.toNetworkError() }
    }
}

// UserRepositoryImpl
class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun createUser(user: UserModel): Either<NetworkError, UserModel> {
        return Either.catch { dataSource.createUser(user) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserById(id: Long): Either<NetworkError, UserModel?> {
        return Either.catch { dataSource.getUserById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserByEmail(email: String): Either<NetworkError, UserModel?> {
        return Either.catch { dataSource.getUserByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUsersByType(
        userType: UserType,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<UserModel>> {
        return Either.catch { dataSource.getUsersByType(userType, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchUsers(
        query: String,
        userType: UserType?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<UserModel>> {
        return Either.catch { dataSource.searchUsers(query, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUsersByStatus(
        status: UserStatus,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<UserModel>> {
        return Either.catch { dataSource.getUsersByStatus(status, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentlyActiveUsers(days: Int, limit: Int): Either<NetworkError, List<UserModel>> {
        return Either.catch { dataSource.getRecentlyActiveUsers(days, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateUser(user: UserModel): Either<NetworkError, UserModel> {
        return Either.catch { dataSource.updateUser(user) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateUserStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateUserStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateUserProfile(
        id: Long,
        fullName: String?,
        phone: String?,
        profilePhoto: String?
    ): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateUserProfile(id, fullName, phone, profilePhoto) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateUserPreferences(id: Long, preferences: NotificationPreferences): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateUserPreferences(id, preferences) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLastLogin(id: Long, ipAddress: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLastLogin(id, ipAddress) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementFailedLoginAttempts(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementFailedLoginAttempts(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun resetFailedLoginAttempts(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.resetFailedLoginAttempts(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun lockUserAccount(id: Long, until: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.lockUserAccount(id, until) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun unlockUserAccount(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.unlockUserAccount(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteUser(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteUser(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteUser(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteUser(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUsersByType(): Either<NetworkError, Map<UserType, Int>> {
        return Either.catch { dataSource.countUsersByType() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUsersByStatus(): Either<NetworkError, Map<UserStatus, Int>> {
        return Either.catch { dataSource.countUsersByStatus() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalUsersCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalUsersCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNewUsersCount(days: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.getNewUsersCount(days) }.mapLeft { it.toNetworkError() }
    }
}

// AssignmentSubmissionRepositoryImpl
class AssignmentSubmissionRepositoryImpl(
    private val dataSource: AssignmentSubmissionDataSource
) : AssignmentSubmissionRepository {

    override suspend fun createSubmission(submission: AssignmentSubmissionModel): Either<NetworkError, AssignmentSubmissionModel> {
        return Either.catch { dataSource.createSubmission(submission) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubmissionById(id: Long): Either<NetworkError, AssignmentSubmissionModel?> {
        return Either.catch { dataSource.getSubmissionById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubmissionsByAssignment(assignmentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>> {
        return Either.catch { dataSource.getSubmissionsByAssignment(assignmentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubmissionsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>> {
        return Either.catch { dataSource.getSubmissionsByStudent(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubmissionByAssignmentAndStudent(assignmentId: Long, studentId: Long): Either<NetworkError, AssignmentSubmissionModel?> {
        return Either.catch { dataSource.getSubmissionByAssignmentAndStudent(assignmentId, studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLateSubmissions(assignmentId: Long?): Either<NetworkError, List<AssignmentSubmissionModel>> {
        return Either.catch { dataSource.getLateSubmissions(assignmentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUngradedSubmissions(teacherId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>> {
        return Either.catch { dataSource.getUngradedSubmissions(teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchSubmissions(query: String, assignmentId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>> {
        return Either.catch { dataSource.searchSubmissions(query, assignmentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubmission(submission: AssignmentSubmissionModel): Either<NetworkError, AssignmentSubmissionModel> {
        return Either.catch { dataSource.updateSubmission(submission) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun gradeSubmission(id: Long, grade: Double, feedback: String?, gradedBy: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.gradeSubmission(id, grade, feedback, gradedBy) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubmissionStatus(id: Long, status: AssignmentStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubmissionStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSubmission(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteSubmission(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubmissionsByAssignment(assignmentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countSubmissionsByAssignment(assignmentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubmissionsByStudent(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countSubmissionsByStudent(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageGradeByAssignment(assignmentId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageGradeByAssignment(assignmentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubmissionTimeliness(studentId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getSubmissionTimeliness(studentId) }.mapLeft { it.toNetworkError() }
    }
}

// AttendanceRepositoryImpl
class AttendanceRepositoryImpl(
    private val dataSource: AttendanceDataSource
) : AttendanceRepository {

    override suspend fun createAttendance(attendance: AttendanceModel): Either<NetworkError, AttendanceModel> {
        return Either.catch { dataSource.createAttendance(attendance) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun createBulkAttendance(attendances: List<AttendanceModel>): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.createBulkAttendance(attendances) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAttendanceById(id: Long): Either<NetworkError, AttendanceModel?> {
        return Either.catch { dataSource.getAttendanceById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAttendanceByStudent(
        studentId: Long,
        startDate: String?,
        endDate: String?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.getAttendanceByStudent(studentId, startDate, endDate, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAttendanceByClasse(
        classeId: Long,
        date: String?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.getAttendanceByClasse(classeId, date, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAttendanceByTeacher(
        teacherId: Long,
        date: String?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.getAttendanceByTeacher(teacherId, date, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDailyAttendance(classeId: Long, date: String): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.getDailyAttendance(classeId, date) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAttendanceSummary(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getAttendanceSummary(studentId, startDate, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAbsentStudents(classeId: Long, date: String): Either<NetworkError, List<AttendanceModel>> {
        return Either.catch { dataSource.getAbsentStudents(classeId, date) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAttendance(attendance: AttendanceModel): Either<NetworkError, AttendanceModel> {
        return Either.catch { dataSource.updateAttendance(attendance) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAttendanceStatus(id: Long, status: AttendanceStatus, notes: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAttendanceStatus(id, status, notes) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteAttendance(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteAttendance(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAttendanceByStatus(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Map<AttendanceStatus, Int>> {
        return Either.catch { dataSource.countAttendanceByStatus(studentId, startDate, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun calculateAttendanceRate(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.calculateAttendanceRate(studentId, startDate, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassAttendanceStats(classeId: Long, date: String): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getClassAttendanceStats(classeId, date) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentAttendanceTrend(studentId: Long, days: Int): Either<NetworkError, Map<String, Double>> {
        return Either.catch { dataSource.getStudentAttendanceTrend(studentId, days) }.mapLeft { it.toNetworkError() }
    }
}

// ClasseRepositoryImpl
class ClasseRepositoryImpl(
    private val dataSource: ClasseDataSource
) : ClasseRepository {

    override suspend fun createClasse(classe: ClasseModel): Either<NetworkError, ClasseModel> {
        return Either.catch { dataSource.createClasse(classe) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClasseById(id: Long): Either<NetworkError, ClasseModel?> {
        return Either.catch { dataSource.getClasseById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassesBySchool(schoolId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>> {
        return Either.catch { dataSource.getClassesBySchool(schoolId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassesByGrade(gradeId: Long, schoolId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>> {
        return Either.catch { dataSource.getClassesByGrade(gradeId, schoolId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassesByTeacher(teacherId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>> {
        return Either.catch { dataSource.getClassesByTeacher(teacherId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchClasses(query: String, schoolId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>> {
        return Either.catch { dataSource.searchClasses(query, schoolId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassSchedule(classeId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getClassSchedule(classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateClasse(classe: ClasseModel): Either<NetworkError, ClasseModel> {
        return Either.catch { dataSource.updateClasse(classe) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateClasseStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateClasseStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateClasseTeacher(id: Long, teacherId: Long?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateClasseTeacher(id, teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentCount(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentCount(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteClasse(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteClasse(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countClassesBySchool(schoolId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countClassesBySchool(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countClassesByGrade(schoolId: Long): Either<NetworkError, Map<Long, Int>> {
        return Either.catch { dataSource.countClassesByGrade(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassCapacityUtilization(classeId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getClassCapacityUtilization(classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolClassDistribution(schoolId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getSchoolClassDistribution(schoolId) }.mapLeft { it.toNetworkError() }
    }
}

// ExamRepositoryImpl
class ExamRepositoryImpl(
    private val dataSource: ExamDataSource
) : ExamRepository {

    override suspend fun createExam(exam: ExamModel): Either<NetworkError, ExamModel> {
        return Either.catch { dataSource.createExam(exam) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamById(id: Long): Either<NetworkError, ExamModel?> {
        return Either.catch { dataSource.getExamById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamsByTeacher(teacherId: Long, published: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.getExamsByTeacher(teacherId, published, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamsByClasse(classeId: Long, published: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.getExamsByClasse(classeId, published, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamsBySubject(subjectId: Long, published: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.getExamsBySubject(subjectId, published, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUpcomingExams(studentId: Long, days: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.getUpcomingExams(studentId, days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPublishedExams(classeId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.getPublishedExams(classeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchExams(query: String, teacherId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>> {
        return Either.catch { dataSource.searchExams(query, teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExam(exam: ExamModel): Either<NetworkError, ExamModel> {
        return Either.catch { dataSource.updateExam(exam) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun publishExam(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.publishExam(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun unpublishExam(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.unpublishExam(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExamStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateExamStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExam(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteExam(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExamsByTeacher(teacherId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countExamsByTeacher(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExamsByClasse(classeId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countExamsByClasse(classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamPerformanceStatistics(examId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getExamPerformanceStatistics(examId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUpcomingExamsCount(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.getUpcomingExamsCount(studentId) }.mapLeft { it.toNetworkError() }
    }
}

// ExamResultRepositoryImpl
class ExamResultRepositoryImpl(
    private val dataSource: ExamResultDataSource
) : ExamResultRepository {

    override suspend fun createExamResult(result: ExamResultModel): Either<NetworkError, ExamResultModel> {
        return Either.catch { dataSource.createExamResult(result) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamResultById(id: Long): Either<NetworkError, ExamResultModel?> {
        return Either.catch { dataSource.getExamResultById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamResultsByExam(examId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.getExamResultsByExam(examId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamResultsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.getExamResultsByStudent(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamResultByExamAndStudent(examId: Long, studentId: Long): Either<NetworkError, ExamResultModel?> {
        return Either.catch { dataSource.getExamResultByExamAndStudent(examId, studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTopPerformers(examId: Long, limit: Int): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.getTopPerformers(examId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getFailedStudents(examId: Long): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.getFailedStudents(examId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchExamResults(query: String, examId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.searchExamResults(query, examId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExamResult(result: ExamResultModel): Either<NetworkError, ExamResultModel> {
        return Either.catch { dataSource.updateExamResult(result) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun gradeExamResult(id: Long, score: Double, feedback: String?, gradedBy: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.gradeExamResult(id, score, feedback, gradedBy) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateRankInClass(examId: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateRankInClass(examId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExamResult(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteExamResult(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExamResultsByExam(examId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countExamResultsByExam(examId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExamAverageScore(examId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getExamAverageScore(examId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentExamPerformance(studentId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getStudentExamPerformance(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassExamRanking(examId: Long): Either<NetworkError, List<ExamResultModel>> {
        return Either.catch { dataSource.getClassExamRanking(examId) }.mapLeft { it.toNetworkError() }
    }
}

// ExerciseRepositoryImpl
class ExerciseRepositoryImpl(
    private val dataSource: ExerciseDataSource
) : ExerciseRepository {

    override suspend fun createExercise(exercise: ExerciseModel): Either<NetworkError, ExerciseModel> {
        return Either.catch { dataSource.createExercise(exercise) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun createBulkExercises(exercises: List<ExerciseModel>): Either<NetworkError, List<ExerciseModel>> {
        return Either.catch { dataSource.createBulkExercises(exercises) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseById(id: Long): Either<NetworkError, ExerciseModel?> {
        return Either.catch { dataSource.getExerciseById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExercisesByLesson(
        lessonId: Long,
        active: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<ExerciseModel>> {
        return Either.catch { dataSource.getExercisesByLesson(lessonId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExercisesByType(
        questionType: String,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<ExerciseModel>> {
        return Either.catch { dataSource.getExercisesByType(questionType, lessonId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExercisesByDifficulty(
        difficulty: DifficultyLevel,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<ExerciseModel>> {
        return Either.catch { dataSource.getExercisesByDifficulty(difficulty, lessonId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchExercises(
        query: String,
        lessonId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<ExerciseModel>> {
        return Either.catch { dataSource.searchExercises(query, lessonId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExercise(exercise: ExerciseModel): Either<NetworkError, ExerciseModel> {
        return Either.catch { dataSource.updateExercise(exercise) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExerciseActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateExerciseActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun reorderExercises(lessonId: Long, exerciseOrder: Map<Long, Int>): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.reorderExercises(lessonId, exerciseOrder) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExercise(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteExercise(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExercisesByLesson(lessonId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countExercisesByLesson(lessonId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExercisesByType(lessonId: Long): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countExercisesByType(lessonId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseDifficultyDistribution(lessonId: Long): Either<NetworkError, Map<DifficultyLevel, Int>> {
        return Either.catch { dataSource.getExerciseDifficultyDistribution(lessonId) }.mapLeft { it.toNetworkError() }
    }
}

// ExerciseResultRepositoryImpl
class ExerciseResultRepositoryImpl(
    private val dataSource: ExerciseResultDataSource
) : ExerciseResultRepository {

    override suspend fun createExerciseResult(result: ExerciseResultModel): Either<NetworkError, ExerciseResultModel> {
        return Either.catch { dataSource.createExerciseResult(result) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseResultById(id: Long): Either<NetworkError, ExerciseResultModel?> {
        return Either.catch { dataSource.getExerciseResultById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseResultsByExercise(exerciseId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseResultModel>> {
        return Either.catch { dataSource.getExerciseResultsByExercise(exerciseId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseResultsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseResultModel>> {
        return Either.catch { dataSource.getExerciseResultsByStudent(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseResultByExerciseAndStudent(exerciseId: Long, studentId: Long): Either<NetworkError, ExerciseResultModel?> {
        return Either.catch { dataSource.getExerciseResultByExerciseAndStudent(exerciseId, studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentExerciseResults(studentId: Long, limit: Int): Either<NetworkError, List<ExerciseResultModel>> {
        return Either.catch { dataSource.getRecentExerciseResults(studentId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getIncorrectExercises(studentId: Long, lessonId: Long?): Either<NetworkError, List<ExerciseResultModel>> {
        return Either.catch { dataSource.getIncorrectExercises(studentId, lessonId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateExerciseResult(result: ExerciseResultModel): Either<NetworkError, ExerciseResultModel> {
        return Either.catch { dataSource.updateExerciseResult(result) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementAttempts(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementAttempts(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExerciseResult(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteExerciseResult(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countExerciseResultsByStudent(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countExerciseResultsByStudent(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExerciseSuccessRate(studentId: Long, lessonId: Long?): Either<NetworkError, Double> {
        return Either.catch { dataSource.getExerciseSuccessRate(studentId, lessonId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageAttemptsByExercise(exerciseId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageAttemptsByExercise(exerciseId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentExerciseProgress(studentId: Long, days: Int): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getStudentExerciseProgress(studentId, days) }.mapLeft { it.toNetworkError() }
    }
}

// GradeRepositoryImpl
class GradeRepositoryImpl(
    private val dataSource: GradeDataSource
) : GradeRepository {

    override suspend fun createGrade(grade: GradeModel): Either<NetworkError, GradeModel> {
        return Either.catch { dataSource.createGrade(grade) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGradeById(id: Long): Either<NetworkError, GradeModel?> {
        return Either.catch { dataSource.getGradeById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGradeByNumber(number: Int, level: GradeLevel): Either<NetworkError, GradeModel?> {
        return Either.catch { dataSource.getGradeByNumber(number, level) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllGrades(active: Boolean?, level: GradeLevel?, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>> {
        return Either.catch { dataSource.getAllGrades(active, level, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGradesByLevel(level: GradeLevel, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>> {
        return Either.catch { dataSource.getGradesByLevel(level, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchGrades(query: String, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>> {
        return Either.catch { dataSource.searchGrades(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateGrade(grade: GradeModel): Either<NetworkError, GradeModel> {
        return Either.catch { dataSource.updateGrade(grade) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateGradeActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateGradeActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubjectsCount(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubjectsCount(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteGrade(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteGrade(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countGradesByLevel(): Either<NetworkError, Map<GradeLevel, Int>> {
        return Either.catch { dataSource.countGradesByLevel() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalGradesCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalGradesCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGradeWithMostSubjects(): Either<NetworkError, GradeModel?> {
        return Either.catch { dataSource.getGradeWithMostSubjects() }.mapLeft { it.toNetworkError() }
    }
}

// GuardianRepositoryImpl
class GuardianRepositoryImpl(
    private val dataSource: GuardianDataSource
) : GuardianRepository {

    override suspend fun createGuardian(guardian: GuardianModel): Either<NetworkError, GuardianModel> {
        return Either.catch { dataSource.createGuardian(guardian) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardianById(id: Long): Either<NetworkError, GuardianModel?> {
        return Either.catch { dataSource.getGuardianById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardianByPhone(phone: String): Either<NetworkError, GuardianModel?> {
        return Either.catch { dataSource.getGuardianByPhone(phone) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardianByEmail(email: String): Either<NetworkError, GuardianModel?> {
        return Either.catch { dataSource.getGuardianByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllGuardians(active: Boolean?, communeId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>> {
        return Either.catch { dataSource.getAllGuardians(active, communeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardiansByCommune(communeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>> {
        return Either.catch { dataSource.getGuardiansByCommune(communeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchGuardians(query: String, communeId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>> {
        return Either.catch { dataSource.searchGuardians(query, communeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardiansWithMultipleStudents(minStudents: Int, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>> {
        return Either.catch { dataSource.getGuardiansWithMultipleStudents(minStudents, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateGuardian(guardian: GuardianModel): Either<NetworkError, GuardianModel> {
        return Either.catch { dataSource.updateGuardian(guardian) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateGuardianStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateGuardianStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateGuardianPreferences(id: Long, preferences: NotificationPreferences): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateGuardianPreferences(id, preferences) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateParentalControl(
        id: Long,
        active: Boolean,
        blockedCategories: List<String>?,
        timeLimit: Int?
    ): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateParentalControl(id, active, blockedCategories, timeLimit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateReportFrequency(id: Long, frequency: ReportFrequency): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateReportFrequency(id, frequency) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteGuardian(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteGuardian(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteGuardian(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteGuardian(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countGuardiansByCommune(communeId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countGuardiansByCommune(communeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countGuardiansByRelation(): Either<NetworkError, Map<GuardianRelation, Int>> {
        return Either.catch { dataSource.countGuardiansByRelation() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalGuardiansCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalGuardiansCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getGuardianStudentStats(guardianId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getGuardianStudentStats(guardianId) }.mapLeft { it.toNetworkError() }
    }
}

// LessonRepositoryImpl
class LessonRepositoryImpl(
    private val dataSource: LessonDataSource
) : LessonRepository {

    override suspend fun createLesson(lesson: LessonModel): Either<NetworkError, LessonModel> {
        return Either.catch { dataSource.createLesson(lesson) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonById(id: Long): Either<NetworkError, LessonModel?> {
        return Either.catch { dataSource.getLessonById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonsByGrade(
        gradeId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getLessonsByGrade(gradeId, approved, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonsBySubject(
        subjectId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getLessonsBySubject(subjectId, approved, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonsByTeacher(
        teacherId: Long,
        approved: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getLessonsByTeacher(teacherId, approved, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getFeaturedLessons(limit: Int): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getFeaturedLessons(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getFreeLessons(page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getFreeLessons(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPopularLessons(limit: Int): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.getPopularLessons(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchLessons(
        query: String,
        gradeId: Long?,
        subjectId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonModel>> {
        return Either.catch { dataSource.searchLessons(query, gradeId, subjectId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLesson(lesson: LessonModel): Either<NetworkError, LessonModel> {
        return Either.catch { dataSource.updateLesson(lesson) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun approveLesson(id: Long, approvedBy: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.approveLesson(id, approvedBy) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun rejectLesson(id: Long, reason: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.rejectLesson(id, reason) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLessonStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLessonStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementViewCount(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementViewCount(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsFeatured(id: Long, featured: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAsFeatured(id, featured) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLessonRating(id: Long, rating: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLessonRating(id, rating) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteLesson(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteLesson(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countLessonsByTeacher(teacherId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countLessonsByTeacher(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countLessonsBySubject(subjectId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countLessonsBySubject(subjectId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalLessonsCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalLessonsCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonEngagementStatistics(lessonId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getLessonEngagementStatistics(lessonId) }.mapLeft { it.toNetworkError() }
    }
}

// LessonProgressRepositoryImpl
class LessonProgressRepositoryImpl(
    private val dataSource: LessonProgressDataSource
) : LessonProgressRepository {

    override suspend fun createLessonProgress(progress: LessonProgressModel): Either<NetworkError, LessonProgressModel> {
        return Either.catch { dataSource.createLessonProgress(progress) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonProgressById(id: Long): Either<NetworkError, LessonProgressModel?> {
        return Either.catch { dataSource.getLessonProgressById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonProgressByLessonAndStudent(lessonId: Long, studentId: Long): Either<NetworkError, LessonProgressModel?> {
        return Either.catch { dataSource.getLessonProgressByLessonAndStudent(lessonId, studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getProgressByStudent(
        studentId: Long,
        completed: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonProgressModel>> {
        return Either.catch { dataSource.getProgressByStudent(studentId, completed, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getProgressByLesson(
        lessonId: Long,
        completed: Boolean?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LessonProgressModel>> {
        return Either.catch { dataSource.getProgressByLesson(lessonId, completed, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentProgress(studentId: Long, limit: Int): Either<NetworkError, List<LessonProgressModel>> {
        return Either.catch { dataSource.getRecentProgress(studentId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getBookmarkedLessons(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<LessonProgressModel>> {
        return Either.catch { dataSource.getBookmarkedLessons(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getInProgressLessons(studentId: Long): Either<NetworkError, List<LessonProgressModel>> {
        return Either.catch { dataSource.getInProgressLessons(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLessonProgress(progress: LessonProgressModel): Either<NetworkError, LessonProgressModel> {
        return Either.catch { dataSource.updateLessonProgress(progress) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateProgressPercentage(id: Long, percentage: Int, lastPosition: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateProgressPercentage(id, percentage, lastPosition) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsCompleted(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAsCompleted(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun toggleBookmark(id: Long, bookmarked: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.toggleBookmark(id, bookmarked) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun addNote(id: Long, note: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.addNote(id, note) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun rateLesson(id: Long, rating: Int, feedback: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.rateLesson(id, rating, feedback) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteLessonProgress(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteLessonProgress(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countCompletedLessons(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countCompletedLessons(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageCompletionTime(studentId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageCompletionTime(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentLearningTrend(studentId: Long, days: Int): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getStudentLearningTrend(studentId, days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLessonCompletionRate(lessonId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getLessonCompletionRate(lessonId) }.mapLeft { it.toNetworkError() }
    }
}

// MessageRepositoryImpl
class MessageRepositoryImpl(
    private val dataSource: MessageDataSource
) : MessageRepository {

    override suspend fun createMessage(message: MessageModel): Either<NetworkError, MessageModel> {
        return Either.catch { dataSource.createMessage(message) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMessageById(id: Long): Either<NetworkError, MessageModel?> {
        return Either.catch { dataSource.getMessageById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMessagesBySender(senderId: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.getMessagesBySender(senderId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMessagesByReceiver(receiverId: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.getMessagesByReceiver(receiverId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getConversation(user1Id: Long, user2Id: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.getConversation(user1Id, user2Id, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUnreadMessages(userId: Long): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.getUnreadMessages(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentConversations(userId: Long, limit: Int): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.getRecentConversations(userId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchMessages(query: String, userId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>> {
        return Either.catch { dataSource.searchMessages(query, userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateMessage(message: MessageModel): Either<NetworkError, MessageModel> {
        return Either.catch { dataSource.updateMessage(message) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsRead(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAsRead(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markMultipleAsRead(messageIds: List<Long>): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markMultipleAsRead(messageIds) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteMessage(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteMessage(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteConversation(user1Id: Long, user2Id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteConversation(user1Id, user2Id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUnreadMessages(userId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countUnreadMessages(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countMessagesByType(userId: Long): Either<NetworkError, Map<MessageType, Int>> {
        return Either.catch { dataSource.countMessagesByType(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMessageActivity(userId: Long, days: Int): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getMessageActivity(userId, days) }.mapLeft { it.toNetworkError() }
    }
}

// NotificationRepositoryImpl
class NotificationRepositoryImpl(
    private val dataSource: NotificationDataSource
) : NotificationRepository {

    override suspend fun createNotification(notification: NotificationModel): Either<NetworkError, NotificationModel> {
        return Either.catch { dataSource.createNotification(notification) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun createBulkNotifications(notifications: List<NotificationModel>): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.createBulkNotifications(notifications) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNotificationById(id: Long): Either<NetworkError, NotificationModel?> {
        return Either.catch { dataSource.getNotificationById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNotificationsByUser(userId: Long, read: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.getNotificationsByUser(userId, read, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNotificationsByType(notificationType: NotificationType, userId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.getNotificationsByType(notificationType, userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUnreadNotifications(userId: Long, limit: Int): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.getUnreadNotifications(userId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentNotifications(userId: Long, limit: Int): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.getRecentNotifications(userId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNotificationsByRelated(relatedId: Long, relatedType: String, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>> {
        return Either.catch { dataSource.getNotificationsByRelated(relatedId, relatedType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateNotification(notification: NotificationModel): Either<NetworkError, NotificationModel> {
        return Either.catch { dataSource.updateNotification(notification) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsRead(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAsRead(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAllAsRead(userId: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAllAsRead(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteNotification(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteNotification(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExpiredNotifications(): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteExpiredNotifications() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteOldNotifications(days: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteOldNotifications(days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUnreadNotifications(userId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countUnreadNotifications(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countNotificationsByType(userId: Long): Either<NetworkError, Map<NotificationType, Int>> {
        return Either.catch { dataSource.countNotificationsByType(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getNotificationDeliveryStats(days: Int): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getNotificationDeliveryStats(days) }.mapLeft { it.toNetworkError() }
    }
}

// PaymentRepositoryImpl
class PaymentRepositoryImpl(
    private val dataSource: PaymentDataSource
) : PaymentRepository {

    override suspend fun createPayment(payment: PaymentModel): Either<NetworkError, PaymentModel> {
        return Either.catch { dataSource.createPayment(payment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentById(id: Long): Either<NetworkError, PaymentModel?> {
        return Either.catch { dataSource.getPaymentById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentByTransactionId(transactionId: String): Either<NetworkError, PaymentModel?> {
        return Either.catch { dataSource.getPaymentByTransactionId(transactionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>> {
        return Either.catch { dataSource.getPaymentsByUser(userId, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentsByStatus(status: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>> {
        return Either.catch { dataSource.getPaymentsByStatus(status, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentsByDateRange(startDate: String, endDate: String, userId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>> {
        return Either.catch { dataSource.getPaymentsByDateRange(startDate, endDate, userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentPayments(limit: Int): Either<NetworkError, List<PaymentModel>> {
        return Either.catch { dataSource.getRecentPayments(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchPayments(query: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>> {
        return Either.catch { dataSource.searchPayments(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updatePayment(payment: PaymentModel): Either<NetworkError, PaymentModel> {
        return Either.catch { dataSource.updatePayment(payment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updatePaymentStatus(id: Long, status: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updatePaymentStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deletePayment(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deletePayment(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countPaymentsByStatus(): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countPaymentsByStatus() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalRevenue(startDate: String, endDate: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.getTotalRevenue(startDate, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAveragePaymentAmount(): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAveragePaymentAmount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPaymentMethodDistribution(): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getPaymentMethodDistribution() }.mapLeft { it.toNetworkError() }
    }
}

// SchoolDirectorRepositoryImpl
class SchoolDirectorRepositoryImpl(
    private val dataSource: SchoolDirectorDataSource
) : SchoolDirectorRepository {

    override suspend fun createDirector(director: SchoolDirectorModel): Either<NetworkError, SchoolDirectorModel> {
        return Either.catch { dataSource.createDirector(director) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDirectorById(id: Long): Either<NetworkError, SchoolDirectorModel?> {
        return Either.catch { dataSource.getDirectorById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDirectorByEmail(email: String): Either<NetworkError, SchoolDirectorModel?> {
        return Either.catch { dataSource.getDirectorByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDirectorBySchool(schoolId: Long): Either<NetworkError, SchoolDirectorModel?> {
        return Either.catch { dataSource.getDirectorBySchool(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllDirectors(active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<SchoolDirectorModel>> {
        return Either.catch { dataSource.getAllDirectors(active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchDirectors(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SchoolDirectorModel>> {
        return Either.catch { dataSource.searchDirectors(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDirectorsWithExpiringLicense(days: Int): Either<NetworkError, List<SchoolDirectorModel>> {
        return Either.catch { dataSource.getDirectorsWithExpiringLicense(days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateDirector(director: SchoolDirectorModel): Either<NetworkError, SchoolDirectorModel> {
        return Either.catch { dataSource.updateDirector(director) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateDirectorStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateDirectorStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLicenseStatus(id: Long, active: Boolean, expirationDate: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLicenseStatus(id, active, expirationDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateDirectorPermissions(
        id: Long,
        canManageTeachers: Boolean?,
        canManageStudents: Boolean?,
        canViewReports: Boolean?,
        canApproveContent: Boolean?
    ): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateDirectorPermissions(id, canManageTeachers, canManageStudents, canViewReports, canApproveContent) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteDirector(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteDirector(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteDirector(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteDirector(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countDirectorsBySchool(): Either<NetworkError, Map<Long, Int>> {
        return Either.catch { dataSource.countDirectorsBySchool() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalDirectorsCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalDirectorsCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDirectorLicenseStats(): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getDirectorLicenseStats() }.mapLeft { it.toNetworkError() }
    }
}

// SchoolTypeRepositoryImpl
class SchoolTypeRepositoryImpl(
    private val dataSource: SchoolTypeDataSource
) : SchoolTypeRepository {

    override suspend fun createSchoolType(schoolType: SchoolTypeModel): Either<NetworkError, SchoolTypeModel> {
        return Either.catch { dataSource.createSchoolType(schoolType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolTypeById(id: Long): Either<NetworkError, SchoolTypeModel?> {
        return Either.catch { dataSource.getSchoolTypeById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolTypeByCode(code: String): Either<NetworkError, SchoolTypeModel?> {
        return Either.catch { dataSource.getSchoolTypeByCode(code) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllSchoolTypes(active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<SchoolTypeModel>> {
        return Either.catch { dataSource.getAllSchoolTypes(active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchSchoolTypes(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SchoolTypeModel>> {
        return Either.catch { dataSource.searchSchoolTypes(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSchoolType(schoolType: SchoolTypeModel): Either<NetworkError, SchoolTypeModel> {
        return Either.catch { dataSource.updateSchoolType(schoolType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSchoolTypeActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSchoolTypeActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSchoolType(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteSchoolType(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSchoolsByType(): Either<NetworkError, Map<Long, Int>> {
        return Either.catch { dataSource.countSchoolsByType() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalSchoolTypesCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalSchoolTypesCount() }.mapLeft { it.toNetworkError() }
    }
}

// StateManagerRepositoryImpl
class StateManagerRepositoryImpl(
    private val dataSource: StateManagerDataSource
) : StateManagerRepository {

    override suspend fun createStateManager(manager: StateManagerModel): Either<NetworkError, StateManagerModel> {
        return Either.catch { dataSource.createStateManager(manager) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStateManagerById(id: Long): Either<NetworkError, StateManagerModel?> {
        return Either.catch { dataSource.getStateManagerById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStateManagerByEmail(email: String): Either<NetworkError, StateManagerModel?> {
        return Either.catch { dataSource.getStateManagerByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllStateManagers(active: Boolean?, accessLevel: ManagerAccessLevel?, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>> {
        return Either.catch { dataSource.getAllStateManagers(active, accessLevel, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStateManagersByProvince(provinceId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>> {
        return Either.catch { dataSource.getStateManagersByProvince(provinceId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStateManagersByAccessLevel(level: ManagerAccessLevel, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>> {
        return Either.catch { dataSource.getStateManagersByAccessLevel(level, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchStateManagers(query: String, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>> {
        return Either.catch { dataSource.searchStateManagers(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStateManager(manager: StateManagerModel): Either<NetworkError, StateManagerModel> {
        return Either.catch { dataSource.updateStateManager(manager) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStateManagerStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStateManagerStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStateManagerAccessLevel(id: Long, accessLevel: ManagerAccessLevel): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStateManagerAccessLevel(id, accessLevel) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStateManagerPermissions(
        id: Long,
        canExportData: Boolean?,
        canViewNationalReports: Boolean?,
        canApproveContent: Boolean?
    ): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStateManagerPermissions(id, canExportData, canViewNationalReports, canApproveContent) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementReportsGenerated(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementReportsGenerated(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteStateManager(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteStateManager(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteStateManager(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteStateManager(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStateManagersByAccessLevel(): Either<NetworkError, Map<ManagerAccessLevel, Int>> {
        return Either.catch { dataSource.countStateManagersByAccessLevel() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStateManagersByProvince(provinceId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countStateManagersByProvince(provinceId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalStateManagersCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalStateManagersCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getManagerActivityStats(managerId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getManagerActivityStats(managerId) }.mapLeft { it.toNetworkError() }
    }
}

// StudentRepositoryImpl
class StudentRepositoryImpl(
    private val dataSource: StudentDataSource
) : StudentRepository {

    override suspend fun createStudent(student: StudentModel): Either<NetworkError, StudentModel> {
        return Either.catch { dataSource.createStudent(student) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentById(id: Long): Either<NetworkError, StudentModel?> {
        return Either.catch { dataSource.getStudentById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentByNumber(studentNumber: String): Either<NetworkError, StudentModel?> {
        return Either.catch { dataSource.getStudentByNumber(studentNumber) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentByEmail(email: String): Either<NetworkError, StudentModel?> {
        return Either.catch { dataSource.getStudentByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllStudents(active: Boolean?, accountType: StudentAccountType?, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getAllStudents(active, accountType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentsBySchool(schoolId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getStudentsBySchool(schoolId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentsByClasse(classeId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getStudentsByClasse(classeId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentsByGrade(gradeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getStudentsByGrade(gradeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentsByGuardian(guardianId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getStudentsByGuardian(guardianId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchStudents(query: String, schoolId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.searchStudents(query, schoolId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTopStudentsByPoints(limit: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getTopStudentsByPoints(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActiveStudentsWithStreak(minStreak: Int): Either<NetworkError, List<StudentModel>> {
        return Either.catch { dataSource.getActiveStudentsWithStreak(minStreak) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudent(student: StudentModel): Either<NetworkError, StudentModel> {
        return Either.catch { dataSource.updateStudent(student) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentAccountType(id: Long, accountType: StudentAccountType): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentAccountType(id, accountType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentPoints(id: Long, points: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentPoints(id, points) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentLevel(id: Long, level: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentLevel(id, level) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentStreak(id: Long, streakDays: Int, currentStreak: Int, longestStreak: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentStreak(id, streakDays, currentStreak, longestStreak) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateStudentStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateStudentStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLastLogin(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLastLogin(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun assignGuardian(studentId: Long, guardianId: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.assignGuardian(studentId, guardianId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun assignToClasse(studentId: Long, classeId: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.assignToClasse(studentId, classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteStudent(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteStudent(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteStudent(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteStudent(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStudentsBySchool(schoolId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countStudentsBySchool(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStudentsByGrade(gradeId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countStudentsByGrade(gradeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStudentsByClasse(classeId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countStudentsByClasse(classeId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countStudentsByAccountType(): Either<NetworkError, Map<StudentAccountType, Int>> {
        return Either.catch { dataSource.countStudentsByAccountType() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalStudentsCount(schoolId: Long?): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalStudentsCount(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageStudentScore(schoolId: Long?): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageStudentScore(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStudentProgressStatistics(studentId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getStudentProgressStatistics(studentId) }.mapLeft { it.toNetworkError() }
    }
}

// StudentAchievementRepositoryImpl
class StudentAchievementRepositoryImpl(
    private val dataSource: StudentAchievementDataSource
) : StudentAchievementRepository {

    override suspend fun createAchievement(achievement: StudentAchievementModel): Either<NetworkError, StudentAchievementModel> {
        return Either.catch { dataSource.createAchievement(achievement) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAchievementById(id: Long): Either<NetworkError, StudentAchievementModel?> {
        return Either.catch { dataSource.getAchievementById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAchievementsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>> {
        return Either.catch { dataSource.getAchievementsByStudent(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAchievementsByType(type: String, studentId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>> {
        return Either.catch { dataSource.getAchievementsByType(type, studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentAchievements(studentId: Long, limit: Int): Either<NetworkError, List<StudentAchievementModel>> {
        return Either.catch { dataSource.getRecentAchievements(studentId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAchievementsWithBadges(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>> {
        return Either.catch { dataSource.getAchievementsWithBadges(studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchAchievements(query: String, studentId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>> {
        return Either.catch { dataSource.searchAchievements(query, studentId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAchievement(achievement: StudentAchievementModel): Either<NetworkError, StudentAchievementModel> {
        return Either.catch { dataSource.updateAchievement(achievement) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteAchievement(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteAchievement(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAchievementsByStudent(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countAchievementsByStudent(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAchievementsByType(studentId: Long): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countAchievementsByType(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalPointsEarned(studentId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalPointsEarned(studentId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAchievementFrequency(studentId: Long, days: Int): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getAchievementFrequency(studentId, days) }.mapLeft { it.toNetworkError() }
    }
}

// SubjectRepositoryImpl
class SubjectRepositoryImpl(
    private val dataSource: SubjectDataSource
) : SubjectRepository {

    override suspend fun createSubject(subject: SubjectModel): Either<NetworkError, SubjectModel> {
        return Either.catch { dataSource.createSubject(subject) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubjectById(id: Long): Either<NetworkError, SubjectModel?> {
        return Either.catch { dataSource.getSubjectById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubjectByCode(code: String): Either<NetworkError, SubjectModel?> {
        return Either.catch { dataSource.getSubjectByCode(code) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllSubjects(active: Boolean?, isCore: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>> {
        return Either.catch { dataSource.getAllSubjects(active, isCore, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubjectsByGradeLevel(level: GradeLevel, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>> {
        return Either.catch { dataSource.getSubjectsByGradeLevel(level, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCoreSubjects(page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>> {
        return Either.catch { dataSource.getCoreSubjects(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchSubjects(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>> {
        return Either.catch { dataSource.searchSubjects(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubject(subject: SubjectModel): Either<NetworkError, SubjectModel> {
        return Either.catch { dataSource.updateSubject(subject) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubjectActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubjectActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubjectStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubjectStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSubject(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteSubject(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubjectsByGradeLevel(): Either<NetworkError, Map<GradeLevel, Int>> {
        return Either.catch { dataSource.countSubjectsByGradeLevel() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalSubjectsCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalSubjectsCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMostPopularSubjects(limit: Int): Either<NetworkError, List<SubjectModel>> {
        return Either.catch { dataSource.getMostPopularSubjects(limit) }.mapLeft { it.toNetworkError() }
    }
}

// SubscriptionRepositoryImpl
class SubscriptionRepositoryImpl(
    private val dataSource: SubscriptionDataSource
) : SubscriptionRepository {

    override suspend fun createSubscription(subscription: SubscriptionModel): Either<NetworkError, SubscriptionModel> {
        return Either.catch { dataSource.createSubscription(subscription) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubscriptionById(id: Long): Either<NetworkError, SubscriptionModel?> {
        return Either.catch { dataSource.getSubscriptionById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubscriptionByUser(userId: Long, userType: String, active: Boolean?): Either<NetworkError, SubscriptionModel?> {
        return Either.catch { dataSource.getSubscriptionByUser(userId, userType, active) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubscriptionsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getSubscriptionsByUser(userId, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubscriptionsByPlan(planName: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getSubscriptionsByPlan(planName, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubscriptionsByStatus(status: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getSubscriptionsByStatus(status, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActiveSubscriptions(page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getActiveSubscriptions(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExpiringSubscriptions(days: Int, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getExpiringSubscriptions(days, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCanceledSubscriptions(page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>> {
        return Either.catch { dataSource.getCanceledSubscriptions(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubscription(subscription: SubscriptionModel): Either<NetworkError, SubscriptionModel> {
        return Either.catch { dataSource.updateSubscription(subscription) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun cancelSubscription(id: Long, reason: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.cancelSubscription(id, reason) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun renewSubscription(id: Long, endDate: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.renewSubscription(id, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAutoRenew(id: Long, autoRenew: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAutoRenew(id, autoRenew) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubscriptionStatus(id: Long, status: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubscriptionStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSubscription(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteSubscription(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubscriptionsByPlan(): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countSubscriptionsByPlan() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubscriptionsByStatus(): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countSubscriptionsByStatus() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalActiveSubscriptions(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalActiveSubscriptions() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMonthlyRecurringRevenue(): Either<NetworkError, Double> {
        return Either.catch { dataSource.getMonthlyRecurringRevenue() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getChurnRate(startDate: String, endDate: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.getChurnRate(startDate, endDate) }.mapLeft { it.toNetworkError() }
    }
}

// SystemAdministratorRepositoryImpl
class SystemAdministratorRepositoryImpl(
    private val dataSource: SystemAdministratorDataSource
) : SystemAdministratorRepository {

    override suspend fun createAdmin(admin: SystemAdministratorModel): Either<NetworkError, SystemAdministratorModel> {
        return Either.catch { dataSource.createAdmin(admin) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAdminById(id: Long): Either<NetworkError, SystemAdministratorModel?> {
        return Either.catch { dataSource.getAdminById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAdminByEmail(email: String): Either<NetworkError, SystemAdministratorModel?> {
        return Either.catch { dataSource.getAdminByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllAdmins(active: Boolean?, permissionLevel: AdminPermissionLevel?, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>> {
        return Either.catch { dataSource.getAllAdmins(active, permissionLevel, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAdminsByPermissionLevel(level: AdminPermissionLevel, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>> {
        return Either.catch { dataSource.getAdminsByPermissionLevel(level, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchAdmins(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>> {
        return Either.catch { dataSource.searchAdmins(query, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAdminsWithTwoFactorAuth(page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>> {
        return Either.catch { dataSource.getAdminsWithTwoFactorAuth(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAdmin(admin: SystemAdministratorModel): Either<NetworkError, SystemAdministratorModel> {
        return Either.catch { dataSource.updateAdmin(admin) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAdminStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAdminStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAdminPermissionLevel(id: Long, permissionLevel: AdminPermissionLevel): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAdminPermissionLevel(id, permissionLevel) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAdminPermissions(
        id: Long,
        canManageUsers: Boolean?,
        canManageContent: Boolean?,
        canManagePayments: Boolean?
    ): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAdminPermissions(id, canManageUsers, canManageContent, canManagePayments) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTwoFactorAuth(id: Long, enabled: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTwoFactorAuth(id, enabled) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementActionsPerformed(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementActionsPerformed(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLastLogin(id: Long, ipAddress: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLastLogin(id, ipAddress) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun resetFailedLoginAttempts(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.resetFailedLoginAttempts(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementFailedLoginAttempts(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementFailedLoginAttempts(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteAdmin(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteAdmin(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteAdmin(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteAdmin(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAdminsByPermissionLevel(): Either<NetworkError, Map<AdminPermissionLevel, Int>> {
        return Either.catch { dataSource.countAdminsByPermissionLevel() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalAdminsCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalAdminsCount() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAdminActivityStats(days: Int): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getAdminActivityStats(days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMostActiveAdmins(limit: Int): Either<NetworkError, List<SystemAdministratorModel>> {
        return Either.catch { dataSource.getMostActiveAdmins(limit) }.mapLeft { it.toNetworkError() }
    }
}

// TeacherRepositoryImpl
class TeacherRepositoryImpl(
    private val dataSource: TeacherDataSource
) : TeacherRepository {

    override suspend fun createTeacher(teacher: TeacherModel): Either<NetworkError, TeacherModel> {
        return Either.catch { dataSource.createTeacher(teacher) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherById(id: Long): Either<NetworkError, TeacherModel?> {
        return Either.catch { dataSource.getTeacherById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherByNumber(teacherNumber: String): Either<NetworkError, TeacherModel?> {
        return Either.catch { dataSource.getTeacherByNumber(teacherNumber) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherByEmail(email: String): Either<NetworkError, TeacherModel?> {
        return Either.catch { dataSource.getTeacherByEmail(email) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllTeachers(active: Boolean?, verified: Boolean?, type: TeacherType?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getAllTeachers(active, verified, type, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeachersBySchool(schoolId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getTeachersBySchool(schoolId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeachersByType(type: TeacherType, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getTeachersByType(type, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getVerifiedTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getVerifiedTeachers(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPendingApprovalTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getPendingApprovalTeachers(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getFeaturedTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getFeaturedTeachers(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchTeachers(query: String, schoolId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.searchTeachers(query, schoolId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTopRatedTeachers(limit: Int): Either<NetworkError, List<TeacherModel>> {
        return Either.catch { dataSource.getTopRatedTeachers(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacher(teacher: TeacherModel): Either<NetworkError, TeacherModel> {
        return Either.catch { dataSource.updateTeacher(teacher) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacherStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTeacherStatus(id, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun verifyTeacher(id: Long, verifiedBy: Long, verifiedAt: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.verifyTeacher(id, verifiedBy, verifiedAt) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun rejectTeacher(id: Long, rejectedBy: Long, reason: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.rejectTeacher(id, rejectedBy, reason) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacherRating(id: Long, averageRating: Double, totalRatings: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTeacherRating(id, averageRating, totalRatings) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacherStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTeacherStatistics(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateMarketplaceStatus(id: Long, enabled: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateMarketplaceStatus(id, enabled) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateMarketplaceBalance(id: Long, balance: Double): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateMarketplaceBalance(id, balance) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsFeatured(id: Long, featured: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.markAsFeatured(id, featured) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun assignToSchool(teacherId: Long, schoolId: Long?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.assignToSchool(teacherId, schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun softDeleteTeacher(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.softDeleteTeacher(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteTeacher(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteTeacher(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTeachersBySchool(schoolId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTeachersBySchool(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTeachersByType(): Either<NetworkError, Map<TeacherType, Int>> {
        return Either.catch { dataSource.countTeachersByType() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countVerifiedTeachers(): Either<NetworkError, Int> {
        return Either.catch { dataSource.countVerifiedTeachers() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalTeachersCount(schoolId: Long?): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalTeachersCount(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageTeacherRating(schoolId: Long?): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageTeacherRating(schoolId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherPerformanceStatistics(teacherId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getTeacherPerformanceStatistics(teacherId) }.mapLeft { it.toNetworkError() }
    }
}

// TeacherAssignmentRepositoryImpl
class TeacherAssignmentRepositoryImpl(
    private val dataSource: TeacherAssignmentDataSource
) : TeacherAssignmentRepository {

    override suspend fun createTeacherAssignment(assignment: TeacherAssignmentModel): Either<NetworkError, TeacherAssignmentModel> {
        return Either.catch { dataSource.createTeacherAssignment(assignment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherAssignmentById(id: Long): Either<NetworkError, TeacherAssignmentModel?> {
        return Either.catch { dataSource.getTeacherAssignmentById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsByTeacher(teacherId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsByTeacher(teacherId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsByClasse(classeId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsByClasse(classeId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAssignmentsBySubject(subjectId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>> {
        return Either.catch { dataSource.getAssignmentsBySubject(subjectId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getHomeroomTeachers(classeId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>> {
        return Either.catch { dataSource.getHomeroomTeachers(classeId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActiveAssignmentsBySchoolYear(schoolYear: String, teacherId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>> {
        return Either.catch { dataSource.getActiveAssignmentsBySchoolYear(schoolYear, teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherWorkload(teacherId: Long, schoolYear: String): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getTeacherWorkload(teacherId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacherAssignment(assignment: TeacherAssignmentModel): Either<NetworkError, TeacherAssignmentModel> {
        return Either.catch { dataSource.updateTeacherAssignment(assignment) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateAssignmentActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateAssignmentActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateHomeroomStatus(id: Long, isHomeroomTeacher: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateHomeroomStatus(id, isHomeroomTeacher) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteTeacherAssignment(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteTeacherAssignment(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAssignmentsByTeacher(teacherId: Long, schoolYear: String?): Either<NetworkError, Int> {
        return Either.catch { dataSource.countAssignmentsByTeacher(teacherId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countAssignmentsByClasse(classeId: Long, schoolYear: String?): Either<NetworkError, Int> {
        return Either.catch { dataSource.countAssignmentsByClasse(classeId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalWeeklyHoursByTeacher(teacherId: Long, schoolYear: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.getTotalWeeklyHoursByTeacher(teacherId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherSubjectDistribution(teacherId: Long): Either<NetworkError, Map<Long, Int>> {
        return Either.catch { dataSource.getTeacherSubjectDistribution(teacherId) }.mapLeft { it.toNetworkError() }
    }
}

// TeacherCertificationRepositoryImpl
class TeacherCertificationRepositoryImpl(
    private val dataSource: TeacherCertificationDataSource
) : TeacherCertificationRepository {

    override suspend fun createCertification(certification: TeacherCertificationModel): Either<NetworkError, TeacherCertificationModel> {
        return Either.catch { dataSource.createCertification(certification) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCertificationById(id: Long): Either<NetworkError, TeacherCertificationModel?> {
        return Either.catch { dataSource.getCertificationById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCertificationsByTeacher(teacherId: Long, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>> {
        return Either.catch { dataSource.getCertificationsByTeacher(teacherId, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getCertificationsByStatus(status: VerificationStatus, teacherId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>> {
        return Either.catch { dataSource.getCertificationsByStatus(status, teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getVerifiedCertifications(teacherId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>> {
        return Either.catch { dataSource.getVerifiedCertifications(teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExpiringCertifications(days: Int, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>> {
        return Either.catch { dataSource.getExpiringCertifications(days, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchCertifications(query: String, teacherId: Long?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>> {
        return Either.catch { dataSource.searchCertifications(query, teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateCertification(certification: TeacherCertificationModel): Either<NetworkError, TeacherCertificationModel> {
        return Either.catch { dataSource.updateCertification(certification) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun verifyCertification(id: Long, verifiedBy: Long, verifiedAt: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.verifyCertification(id, verifiedBy, verifiedAt) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun rejectCertification(id: Long, rejectionReason: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.rejectCertification(id, rejectionReason) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateCertificationActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateCertificationActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteCertification(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteCertification(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countCertificationsByTeacher(teacherId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countCertificationsByTeacher(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countCertificationsByStatus(teacherId: Long?): Either<NetworkError, Map<VerificationStatus, Int>> {
        return Either.catch { dataSource.countCertificationsByStatus(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getExpiredCertificationsCount(): Either<NetworkError, Int> {
        return Either.catch { dataSource.getExpiredCertificationsCount() }.mapLeft { it.toNetworkError() }
    }
}

// TeacherSubjectRepositoryImpl
class TeacherSubjectRepositoryImpl(
    private val dataSource: TeacherSubjectDataSource
) : TeacherSubjectRepository {

    override suspend fun createTeacherSubject(teacherSubject: TeacherSubjectModel): Either<NetworkError, TeacherSubjectModel> {
        return Either.catch { dataSource.createTeacherSubject(teacherSubject) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherSubjectById(id: Long): Either<NetworkError, TeacherSubjectModel?> {
        return Either.catch { dataSource.getTeacherSubjectById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherSubjectsByTeacher(teacherId: Long, primary: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>> {
        return Either.catch { dataSource.getTeacherSubjectsByTeacher(teacherId, primary, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherSubjectsBySubject(subjectId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>> {
        return Either.catch { dataSource.getTeacherSubjectsBySubject(subjectId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPrimarySubjectsByTeacher(teacherId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>> {
        return Either.catch { dataSource.getPrimarySubjectsByTeacher(teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeachersBySubject(subjectId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>> {
        return Either.catch { dataSource.getTeachersBySubject(subjectId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSubjectsByTeacherWithExperience(teacherId: Long, minYears: Int, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>> {
        return Either.catch { dataSource.getSubjectsByTeacherWithExperience(teacherId, minYears, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTeacherSubject(teacherSubject: TeacherSubjectModel): Either<NetworkError, TeacherSubjectModel> {
        return Either.catch { dataSource.updateTeacherSubject(teacherSubject) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updatePrimaryStatus(id: Long, isPrimary: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updatePrimaryStatus(id, isPrimary) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubjectRating(id: Long, rating: Double): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateSubjectRating(id, rating) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementLessonsInSubject(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementLessonsInSubject(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteTeacherSubject(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteTeacherSubject(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSubjectsByTeacher(teacherId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countSubjectsByTeacher(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTeachersBySubject(subjectId: Long): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTeachersBySubject(subjectId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageExperienceBySubject(subjectId: Long): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageExperienceBySubject(subjectId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherSubjectExpertise(teacherId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getTeacherSubjectExpertise(teacherId) }.mapLeft { it.toNetworkError() }
    }
}

// TimetableRepositoryImpl
class TimetableRepositoryImpl(
    private val dataSource: TimetableDataSource
) : TimetableRepository {

    override suspend fun createTimetable(timetable: TimetableModel): Either<NetworkError, TimetableModel> {
        return Either.catch { dataSource.createTimetable(timetable) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun createBulkTimetable(timetables: List<TimetableModel>): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.createBulkTimetable(timetables) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTimetableById(id: Long): Either<NetworkError, TimetableModel?> {
        return Either.catch { dataSource.getTimetableById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTimetableByClasse(classeId: Long, active: Boolean?, schoolYear: String?, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getTimetableByClasse(classeId, active, schoolYear, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTimetableByTeacher(teacherId: Long, active: Boolean?, schoolYear: String?, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getTimetableByTeacher(teacherId, active, schoolYear, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTimetableBySubject(subjectId: Long, active: Boolean?, schoolYear: String?, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getTimetableBySubject(subjectId, active, schoolYear, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getDailyTimetable(classeId: Long, dayOfWeek: Int, schoolYear: String): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getDailyTimetable(classeId, dayOfWeek, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getWeeklyTimetable(classeId: Long, schoolYear: String): Either<NetworkError, Map<Int, List<TimetableModel>>> {
        return Either.catch { dataSource.getWeeklyTimetable(classeId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTimetableConflicts(teacherId: Long, dayOfWeek: Int, startTime: String, endTime: String, schoolYear: String): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getTimetableConflicts(teacherId, dayOfWeek, startTime, endTime, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRoomSchedule(room: String, dayOfWeek: Int?, schoolYear: String?, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>> {
        return Either.catch { dataSource.getRoomSchedule(room, dayOfWeek, schoolYear, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTimetable(timetable: TimetableModel): Either<NetworkError, TimetableModel> {
        return Either.catch { dataSource.updateTimetable(timetable) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTimetableActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTimetableActiveStatus(id, isActive) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateTimetableSlot(id: Long, dayOfWeek: Int?, startTime: String?, endTime: String?): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateTimetableSlot(id, dayOfWeek, startTime, endTime) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteTimetable(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteTimetable(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteTimetableByClasse(classeId: Long, schoolYear: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteTimetableByClasse(classeId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTimetableSlotsByTeacher(teacherId: Long, schoolYear: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTimetableSlotsByTeacher(teacherId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTimetableSlotsByClasse(classeId: Long, schoolYear: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTimetableSlotsByClasse(classeId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherWeeklySchedule(teacherId: Long, schoolYear: String): Either<NetworkError, Map<Int, Int>> {
        return Either.catch { dataSource.getTeacherWeeklySchedule(teacherId, schoolYear) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getClassWeeklyHours(classeId: Long, schoolYear: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.getClassWeeklyHours(classeId, schoolYear) }.mapLeft { it.toNetworkError() }
    }
}

// UserActivityLogRepositoryImpl
class UserActivityLogRepositoryImpl(
    private val dataSource: UserActivityLogDataSource
) : UserActivityLogRepository {

    override suspend fun createActivityLog(log: UserActivityLogModel): Either<NetworkError, UserActivityLogModel> {
        return Either.catch { dataSource.createActivityLog(log) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityLogById(id: Long): Either<NetworkError, UserActivityLogModel?> {
        return Either.catch { dataSource.getActivityLogById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityLogsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>> {
        return Either.catch { dataSource.getActivityLogsByUser(userId, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityLogsByActivityType(activityType: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>> {
        return Either.catch { dataSource.getActivityLogsByActivityType(activityType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentActivityLogs(limit: Int): Either<NetworkError, List<UserActivityLogModel>> {
        return Either.catch { dataSource.getRecentActivityLogs(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityLogsByDateRange(startDate: String, endDate: String, userId: Long?, userType: String?, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>> {
        return Either.catch { dataSource.getActivityLogsByDateRange(startDate, endDate, userId, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityLogsByIpAddress(ipAddress: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>> {
        return Either.catch { dataSource.getActivityLogsByIpAddress(ipAddress, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteActivityLog(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteActivityLog(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteOldActivityLogs(days: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteOldActivityLogs(days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countActivityLogsByUser(userId: Long, userType: String, days: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.countActivityLogsByUser(userId, userType, days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countActivityLogsByActivityType(days: Int): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countActivityLogsByActivityType(days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMostActiveUsers(userType: String, days: Int, limit: Int): Either<NetworkError, List<Map<String, Any>>> {
        return Either.catch { dataSource.getMostActiveUsers(userType, days, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivityFrequency(userId: Long, userType: String, days: Int): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getActivityFrequency(userId, userType, days) }.mapLeft { it.toNetworkError() }
    }
}

// UserSessionRepositoryImpl
class UserSessionRepositoryImpl(
    private val dataSource: UserSessionDataSource
) : UserSessionRepository {

    override suspend fun createSession(session: UserSessionModel): Either<NetworkError, UserSessionModel> {
        return Either.catch { dataSource.createSession(session) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionById(id: Long): Either<NetworkError, UserSessionModel?> {
        return Either.catch { dataSource.getSessionById(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionByToken(token: String): Either<NetworkError, UserSessionModel?> {
        return Either.catch { dataSource.getSessionByToken(token) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionsByUser(userId: Long, userType: String, active: Boolean?, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>> {
        return Either.catch { dataSource.getSessionsByUser(userId, userType, active, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActiveSessions(userId: Long, userType: String): Either<NetworkError, List<UserSessionModel>> {
        return Either.catch { dataSource.getActiveSessions(userId, userType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionsByDevice(deviceId: String, userId: Long?, userType: String?, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>> {
        return Either.catch { dataSource.getSessionsByDevice(deviceId, userId, userType, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionsByIpAddress(ipAddress: String, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>> {
        return Either.catch { dataSource.getSessionsByIpAddress(ipAddress, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentSessions(userId: Long, userType: String, limit: Int): Either<NetworkError, List<UserSessionModel>> {
        return Either.catch { dataSource.getRecentSessions(userId, userType, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSession(session: UserSessionModel): Either<NetworkError, UserSessionModel> {
        return Either.catch { dataSource.updateSession(session) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLastActivity(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLastActivity(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun logoutSession(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.logoutSession(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun logoutAllSessions(userId: Long, userType: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.logoutAllSessions(userId, userType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun expireSession(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.expireSession(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSession(id: Long): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteSession(id) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteExpiredSessions(): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteExpiredSessions() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countActiveSessions(userId: Long?, userType: String?): Either<NetworkError, Int> {
        return Either.catch { dataSource.countActiveSessions(userId, userType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSessionsByDeviceType(userId: Long, userType: String): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countSessionsByDeviceType(userId, userType) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAverageSessionDuration(userId: Long, userType: String, days: Int): Either<NetworkError, Double> {
        return Either.catch { dataSource.getAverageSessionDuration(userId, userType, days) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSessionActivity(userId: Long, userType: String, days: Int): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getSessionActivity(userId, userType, days) }.mapLeft { it.toNetworkError() }
    }
}



//=========================================
//==================================================
// LIVE STREAM SECTION
//==================================================
// ==================== LIVE SESSION REPOSITORY IMPL ====================

class LiveSessionRepositoryImpl(
    private val dataSource: LiveSessionDataSource
) : LiveSessionRepository {

    // ==================== CREATE ====================

    override suspend fun createLiveSession(request: CreateLiveSessionRequest): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.createLiveSession(request) }.mapLeft { it.toNetworkError() }
    }

    // ==================== READ ====================

    override suspend fun getLiveSessionById(sessionId: String): Either<NetworkError, LiveSessionModel?> {
        return Either.catch { dataSource.getLiveSessionById(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllLiveSessions(filters: LiveSessionFilters?, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getAllLiveSessions(filters, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveSessionsByTeacher(teacherId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getLiveSessionsByTeacher(teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUpcomingLiveSessions(page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getUpcomingLiveSessions(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveNowSessions(): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getLiveNowSessions() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPastLiveSessions(page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getPastLiveSessions(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserLiveSessions(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getUserLiveSessions(userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveSessionsByStatus(status: LiveStatus, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getLiveSessionsByStatus(status, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveSessionsBySubject(subject: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getLiveSessionsBySubject(subject, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentLiveSessions(limit: Int): Either<NetworkError, List<LiveSessionModel>> {
        return Either.catch { dataSource.getRecentLiveSessions(limit) }.mapLeft { it.toNetworkError() }
    }

    // ==================== UPDATE ====================

    override suspend fun updateLiveSession(sessionId: String, request: UpdateLiveSessionRequest): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.updateLiveSession(sessionId, request) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun startLiveSession(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.startLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun pauseLiveSession(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.pauseLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun resumeLiveSession(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.resumeLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun endLiveSession(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.endLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun cancelLiveSession(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return Either.catch { dataSource.cancelLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLiveMetrics(sessionId: String, participantCount: Int, viewCount: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updateLiveMetrics(sessionId, participantCount, viewCount) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementParticipantCount(sessionId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementParticipantCount(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun decrementParticipantCount(sessionId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.decrementParticipantCount(sessionId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== DELETE ====================

    override suspend fun deleteLiveSession(sessionId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteLiveSession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteCancelledSessions(olderThanDays: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteCancelledSessions(olderThanDays) }.mapLeft { it.toNetworkError() }
    }

    // ==================== STREAMING ====================

    override suspend fun getStreamInfo(sessionId: String): Either<NetworkError, StreamInfoResponse> {
        return Either.catch { dataSource.getStreamInfo(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun generateStreamKey(sessionId: String): Either<NetworkError, String> {
        return Either.catch { dataSource.generateStreamKey(sessionId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== STATISTICS ====================

    override suspend fun countLivesByStatus(status: LiveStatus): Either<NetworkError, Int> {
        return Either.catch { dataSource.countLivesByStatus(status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTeacherLives(teacherId: String, status: LiveStatus?): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTeacherLives(teacherId, status) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveStatistics(teacherId: String?, days: Int): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getLiveStatistics(teacherId, days) }.mapLeft { it.toNetworkError() }
    }

    // ==================== REALTIME ====================

    override fun observeLiveSession(sessionId: String): Flow<Either<NetworkError, LiveSessionModel>> {
        return dataSource.observeLiveSession(sessionId).map { session ->
            Either.catch { session }.mapLeft { it.toNetworkError() }
        }
    }

    override fun observeLiveNowSessions(): Flow<Either<NetworkError, List<LiveSessionModel>>> {
        return dataSource.observeLiveNowSessions().map { sessions ->
            Either.catch { sessions }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE PARTICIPANT REPOSITORY IMPL ====================

class LiveParticipantRepositoryImpl(
    private val dataSource: LiveParticipantDataSource
) : LiveParticipantRepository {

    // ==================== CREATE ====================

    override suspend fun joinLiveSession(request: JoinLiveSessionRequest): Either<NetworkError, LiveParticipantModel> {
        return Either.catch { dataSource.joinLiveSession(request) }.mapLeft { it.toNetworkError() }
    }

    // ==================== READ ====================

    override suspend fun getParticipants(sessionId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveParticipantModel>> {
        return Either.catch { dataSource.getParticipants(sessionId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getParticipant(sessionId: String, userId: String): Either<NetworkError, LiveParticipantModel?> {
        return Either.catch { dataSource.getParticipant(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getParticipantsByRole(sessionId: String, role: ParticipantRole): Either<NetworkError, List<LiveParticipantModel>> {
        return Either.catch { dataSource.getParticipantsByRole(sessionId, role) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getOnlineParticipants(sessionId: String): Either<NetworkError, List<LiveParticipantModel>> {
        return Either.catch { dataSource.getOnlineParticipants(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPendingApprovals(sessionId: String): Either<NetworkError, List<LiveParticipantModel>> {
        return Either.catch { dataSource.getPendingApprovals(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserParticipationHistory(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveParticipantModel>> {
        return Either.catch { dataSource.getUserParticipationHistory(userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    // ==================== UPDATE ====================

    override suspend fun leaveLiveSession(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.leaveLiveSession(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateParticipantRole(participantId: String, newRole: ParticipantRole): Either<NetworkError, LiveParticipantModel> {
        return Either.catch { dataSource.updateParticipantRole(participantId, newRole) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun approveParticipant(participantId: String): Either<NetworkError, LiveParticipantModel> {
        return Either.catch { dataSource.approveParticipant(participantId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun rejectParticipant(participantId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.rejectParticipant(participantId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== DELETE ====================

    override suspend fun removeParticipant(participantId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.removeParticipant(participantId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun cleanupOfflineParticipants(sessionId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.cleanupOfflineParticipants(sessionId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== STATISTICS ====================

    override suspend fun countParticipants(sessionId: String, onlineOnly: Boolean): Either<NetworkError, Int> {
        return Either.catch { dataSource.countParticipants(sessionId, onlineOnly) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUserParticipations(userId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countUserParticipations(userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun isUserInLive(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.isUserInLive(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== REALTIME ====================

    override fun observeParticipants(sessionId: String): Flow<Either<NetworkError, List<LiveParticipantModel>>> {
        return dataSource.observeParticipants(sessionId).map { participants ->
            Either.catch { participants }.mapLeft { it.toNetworkError() }
        }
    }

    override fun observeOnlineCount(sessionId: String): Flow<Either<NetworkError, Int>> {
        return dataSource.observeOnlineCount(sessionId).map { count ->
            Either.catch { count }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE CHAT REPOSITORY IMPL ====================

class LiveChatRepositoryImpl(
    private val dataSource: LiveChatDataSource
) : LiveChatRepository {

    // ==================== CREATE ====================

    override suspend fun sendMessage(request: SendChatMessageRequest): Either<NetworkError, LiveChatMessageModel> {
        return Either.catch { dataSource.sendMessage(request) }.mapLeft { it.toNetworkError() }
    }

    // ==================== READ ====================

    override suspend fun getMessages(sessionId: String, limit: Int, offset: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getMessages(sessionId, limit, offset) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentMessages(sessionId: String, limit: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getRecentMessages(sessionId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getMessagesByType(sessionId: String, messageType: ChatMessageType, limit: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getMessagesByType(sessionId, messageType, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUnansweredQuestions(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getUnansweredQuestions(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserMessages(sessionId: String, userId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getUserMessages(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPendingMessages(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return Either.catch { dataSource.getPendingMessages(sessionId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== UPDATE ====================

    override suspend fun approveMessage(messageId: String): Either<NetworkError, LiveChatMessageModel> {
        return Either.catch { dataSource.approveMessage(messageId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun pinMessage(messageId: String): Either<NetworkError, LiveChatMessageModel> {
        return Either.catch { dataSource.pinMessage(messageId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun unpinMessage(messageId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.unpinMessage(messageId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== DELETE ====================

    override suspend fun deleteMessage(messageId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteMessage(messageId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteUserMessages(sessionId: String, userId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.deleteUserMessages(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun clearOldMessages(sessionId: String, olderThanMinutes: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.clearOldMessages(sessionId, olderThanMinutes) }.mapLeft { it.toNetworkError() }
    }

    // ==================== STATISTICS ====================

    override suspend fun countMessages(sessionId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countMessages(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countUserMessages(sessionId: String, userId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countUserMessages(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countMessagesByType(sessionId: String): Either<NetworkError, Map<ChatMessageType, Int>> {
        return Either.catch { dataSource.countMessagesByType(sessionId) }.mapLeft { it.toNetworkError() }
    }

    // ==================== REALTIME ====================

    override fun observeMessages(sessionId: String): Flow<Either<NetworkError, LiveChatMessageModel>> {
        return dataSource.observeMessages(sessionId).map { message ->
            Either.catch { message }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE REACTION REPOSITORY IMPL ====================

class LiveReactionRepositoryImpl(
    private val dataSource: LiveReactionDataSource
) : LiveReactionRepository {

    override suspend fun sendReaction(sessionId: String, userId: String, emoji: String): Either<NetworkError, LiveReactionModel> {
        return Either.catch { dataSource.sendReaction(sessionId, userId, emoji) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getReactions(sessionId: String, limit: Int): Either<NetworkError, List<LiveReactionModel>> {
        return Either.catch { dataSource.getReactions(sessionId, limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentReactions(sessionId: String, seconds: Int): Either<NetworkError, List<LiveReactionModel>> {
        return Either.catch { dataSource.getRecentReactions(sessionId, seconds) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun clearOldReactions(sessionId: String, olderThanSeconds: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.clearOldReactions(sessionId, olderThanSeconds) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countReactionsByType(sessionId: String): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.countReactionsByType(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTotalReactions(sessionId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTotalReactions(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override fun observeReactions(sessionId: String): Flow<Either<NetworkError, LiveReactionModel>> {
        return dataSource.observeReactions(sessionId).map { reaction ->
            Either.catch { reaction }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE POLL REPOSITORY IMPL ====================

class LivePollRepositoryImpl(
    private val dataSource: LivePollDataSource
) : LivePollRepository {

    override suspend fun createPoll(sessionId: String, question: String, options: List<String>, durationSeconds: Int?): Either<NetworkError, LivePollModel> {
        return Either.catch { dataSource.createPoll(sessionId, question, options, durationSeconds) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun votePoll(pollId: String, optionId: String, userId: String): Either<NetworkError, PollVoteModel> {
        return Either.catch { dataSource.votePoll(pollId, optionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPolls(sessionId: String): Either<NetworkError, List<LivePollModel>> {
        return Either.catch { dataSource.getPolls(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPollById(pollId: String): Either<NetworkError, LivePollModel?> {
        return Either.catch { dataSource.getPollById(pollId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getActivePolls(sessionId: String): Either<NetworkError, List<LivePollModel>> {
        return Either.catch { dataSource.getActivePolls(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPollVotes(pollId: String): Either<NetworkError, List<PollVoteModel>> {
        return Either.catch { dataSource.getPollVotes(pollId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun hasUserVoted(pollId: String, userId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.hasUserVoted(pollId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun closePoll(pollId: String): Either<NetworkError, LivePollModel> {
        return Either.catch { dataSource.closePoll(pollId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deletePoll(pollId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deletePoll(pollId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countPollVotes(pollId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countPollVotes(pollId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPollResults(pollId: String): Either<NetworkError, Map<String, Int>> {
        return Either.catch { dataSource.getPollResults(pollId) }.mapLeft { it.toNetworkError() }
    }

    override fun observePollResults(pollId: String): Flow<Either<NetworkError, LivePollModel>> {
        return dataSource.observePollResults(pollId).map { poll ->
            Either.catch { poll }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE STATS REPOSITORY IMPL ====================

class LiveStatsRepositoryImpl(
    private val dataSource: LiveStatsDataSource
) : LiveStatsRepository {

    override suspend fun createLiveStats(sessionId: String): Either<NetworkError, LiveStreamStats> {
        return Either.catch { dataSource.createLiveStats(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateLiveStats(stats: LiveStreamStats): Either<NetworkError, LiveStreamStats> {
        return Either.catch { dataSource.updateLiveStats(stats) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun recordView(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.recordView(sessionId, userId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun recordWatchTime(sessionId: String, userId: String, seconds: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.recordWatchTime(sessionId, userId, seconds) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementMessageCount(sessionId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementMessageCount(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun incrementReactionCount(sessionId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.incrementReactionCount(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updatePeakViewers(sessionId: String, viewers: Int): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.updatePeakViewers(sessionId, viewers) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getLiveStats(sessionId: String): Either<NetworkError, LiveStreamStats?> {
        return Either.catch { dataSource.getLiveStats(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTeacherStats(teacherId: String): Either<NetworkError, TeacherLiveStats> {
        return Either.catch { dataSource.getTeacherStats(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getStatsByPeriod(teacherId: String, startDate: String, endDate: String): Either<NetworkError, Map<String, Any>> {
        return Either.catch { dataSource.getStatsByPeriod(teacherId, startDate, endDate) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun calculateAverageViewers(sessionId: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.calculateAverageViewers(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun calculateRetentionRate(sessionId: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.calculateRetentionRate(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun calculateEngagementRate(sessionId: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.calculateEngagementRate(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override fun observeLiveStats(sessionId: String): Flow<Either<NetworkError, LiveStreamStats>> {
        return dataSource.observeLiveStats(sessionId).map { stats ->
            Either.catch { stats }.mapLeft { it.toNetworkError() }
        }
    }
}

// ==================== LIVE RECORDING REPOSITORY IMPL ====================

class LiveRecordingRepositoryImpl(
    private val dataSource: LiveRecordingDataSource
) : LiveRecordingRepository {

    override suspend fun startRecording(sessionId: String): Either<NetworkError, LiveRecordingModel> {
        return Either.catch { dataSource.startRecording(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecordingById(recordingId: String): Either<NetworkError, LiveRecordingModel?> {
        return Either.catch { dataSource.getRecordingById(recordingId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecordingsBySession(sessionId: String): Either<NetworkError, List<LiveRecordingModel>> {
        return Either.catch { dataSource.getRecordingsBySession(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecordingsByTeacher(teacherId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveRecordingModel>> {
        return Either.catch { dataSource.getRecordingsByTeacher(teacherId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPublicRecordings(page: Int, pageSize: Int): Either<NetworkError, List<LiveRecordingModel>> {
        return Either.catch { dataSource.getPublicRecordings(page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getRecentRecordings(limit: Int): Either<NetworkError, List<LiveRecordingModel>> {
        return Either.catch { dataSource.getRecentRecordings(limit) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun stopRecording(recordingId: String): Either<NetworkError, LiveRecordingModel> {
        return Either.catch { dataSource.stopRecording(recordingId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateRecordingVisibility(recordingId: String, isPublic: Boolean): Either<NetworkError, LiveRecordingModel> {
        return Either.catch { dataSource.updateRecordingVisibility(recordingId, isPublic) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateProcessingStatus(recordingId: String, isProcessing: Boolean): Either<NetworkError, LiveRecordingModel> {
        return Either.catch { dataSource.updateProcessingStatus(recordingId, isProcessing) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteRecording(recordingId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteRecording(recordingId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countTeacherRecordings(teacherId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.countTeacherRecordings(teacherId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalRecordingSize(teacherId: String): Either<NetworkError, Double> {
        return Either.catch { dataSource.getTotalRecordingSize(teacherId) }.mapLeft { it.toNetworkError() }
    }
}

// ==================== LIVE NOTIFICATION REPOSITORY IMPL ====================

class LiveNotificationRepositoryImpl(
    private val dataSource: LiveNotificationDataSource
) : LiveNotificationRepository {

    override suspend fun createNotification(sessionId: String, userId: String, type: NotificationType): Either<NetworkError, LiveNotificationModel> {
        return Either.catch { dataSource.createNotification(sessionId, userId, type) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun notifyLiveStartingSoon(sessionId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.notifyLiveStartingSoon(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun notifyLiveStarted(sessionId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.notifyLiveStarted(sessionId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun notifyRecordingAvailable(sessionId: String, recordingId: String): Either<NetworkError, Int> {
        return Either.catch { dataSource.notifyRecordingAvailable(sessionId, recordingId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getUserNotifications(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveNotificationModel>> {
        return Either.catch { dataSource.getUserNotifications(userId, page, pageSize) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getPendingNotifications(): Either<NetworkError, List<LiveNotificationModel>> {
        return Either.catch { dataSource.getPendingNotifications() }.mapLeft { it.toNetworkError() }
    }

    override suspend fun markAsSent(notificationId: String): Either<NetworkError, LiveNotificationModel> {
        return Either.catch { dataSource.markAsSent(notificationId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteNotification(notificationId: String): Either<NetworkError, Boolean> {
        return Either.catch { dataSource.deleteNotification(notificationId) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun clearOldNotifications(olderThanDays: Int): Either<NetworkError, Int> {
        return Either.catch { dataSource.clearOldNotifications(olderThanDays) }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countPendingNotifications(userId: String?): Either<NetworkError, Int> {
        return Either.catch { dataSource.countPendingNotifications(userId) }.mapLeft { it.toNetworkError() }
    }
}

