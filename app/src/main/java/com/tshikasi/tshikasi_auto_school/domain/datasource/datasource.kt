package com.tshikasi.tshikasi_auto_school.domain.datasource

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.*
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherLiveStats
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow

// ============================================
// INTERFACE: AssignmentDataSource
// ============================================
interface AssignmentDataSource {
    // CREATE
    suspend fun createAssignment(assignment: AssignmentModel): AssignmentModel

    // READ
    suspend fun getAssignmentById(id: Long): AssignmentModel?
    suspend fun getAssignmentsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<AssignmentModel>
    suspend fun getAssignmentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<AssignmentModel>
    suspend fun getAssignmentsBySubject(subjectId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<AssignmentModel>
    suspend fun getAssignmentsByGrade(gradeId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<AssignmentModel>
    suspend fun getUpcomingAssignments(studentId: Long, days: Int = 7): List<AssignmentModel>
    suspend fun getOverdueAssignments(studentId: Long): List<AssignmentModel>
    suspend fun searchAssignments(query: String, teacherId: Long? = null, page: Int, pageSize: Int): List<AssignmentModel>

    // UPDATE
    suspend fun updateAssignment(assignment: AssignmentModel): AssignmentModel
    suspend fun updateAssignmentStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateAssignmentStatistics(id: Long): Boolean
    suspend fun updateTotalSubmissions(id: Long): Boolean

    // DELETE
    suspend fun deleteAssignment(id: Long): Boolean

    // STATISTICS
    suspend fun countAssignmentsByTeacher(teacherId: Long): Int
    suspend fun countAssignmentsByClasse(classeId: Long): Int
    suspend fun getAverageScoreByAssignment(assignmentId: Long): Double
    suspend fun getAssignmentCompletionRate(assignmentId: Long): Double
}

// ============================================
// INTERFACE: UserDataSource
// ============================================
interface UserDataSource {
    // CREATE
    suspend fun createUser(user: UserModel): UserModel

    // READ
    suspend fun getUserById(id: Long): UserModel?
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUsersByType(userType: UserType, active: Boolean? = null, page: Int, pageSize: Int): List<UserModel>
    suspend fun searchUsers(query: String, userType: UserType? = null, page: Int, pageSize: Int): List<UserModel>
    suspend fun getUsersByStatus(status: UserStatus, page: Int, pageSize: Int): List<UserModel>
    suspend fun getRecentlyActiveUsers(days: Int = 7, limit: Int = 50): List<UserModel>

    // UPDATE
    suspend fun updateUser(user: UserModel): UserModel
    suspend fun updateUserStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateUserProfile(id: Long, fullName: String? = null, phone: String? = null, profilePhoto: String? = null): Boolean
    suspend fun updateUserPreferences(id: Long, preferences: NotificationPreferences): Boolean
    suspend fun updateLastLogin(id: Long, ipAddress: String? = null): Boolean
    suspend fun incrementFailedLoginAttempts(id: Long): Boolean
    suspend fun resetFailedLoginAttempts(id: Long): Boolean
    suspend fun lockUserAccount(id: Long, until: String): Boolean
    suspend fun unlockUserAccount(id: Long): Boolean

    // DELETE
    suspend fun softDeleteUser(id: Long): Boolean
    suspend fun deleteUser(id: Long): Boolean

    // STATISTICS
    suspend fun countUsersByType(): Map<UserType, Int>
    suspend fun countUsersByStatus(): Map<UserStatus, Int>
    suspend fun getTotalUsersCount(): Int
    suspend fun getNewUsersCount(days: Int = 30): Int
}

// ============================================
// INTERFACE: AssignmentSubmissionDataSource
// ============================================
interface AssignmentSubmissionDataSource {
    // CREATE
    suspend fun createSubmission(submission: AssignmentSubmissionModel): AssignmentSubmissionModel

    // READ
    suspend fun getSubmissionById(id: Long): AssignmentSubmissionModel?
    suspend fun getSubmissionsByAssignment(assignmentId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel>
    suspend fun getSubmissionsByStudent(studentId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel>
    suspend fun getSubmissionByAssignmentAndStudent(assignmentId: Long, studentId: Long): AssignmentSubmissionModel?
    suspend fun getLateSubmissions(assignmentId: Long? = null): List<AssignmentSubmissionModel>
    suspend fun getUngradedSubmissions(teacherId: Long, page: Int, pageSize: Int): List<AssignmentSubmissionModel>
    suspend fun searchSubmissions(query: String, assignmentId: Long? = null, page: Int, pageSize: Int): List<AssignmentSubmissionModel>

    // UPDATE
    suspend fun updateSubmission(submission: AssignmentSubmissionModel): AssignmentSubmissionModel
    suspend fun gradeSubmission(id: Long, grade: Double, feedback: String? = null, gradedBy: Long): Boolean
    suspend fun updateSubmissionStatus(id: Long, status: AssignmentStatus): Boolean

    // DELETE
    suspend fun deleteSubmission(id: Long): Boolean

    // STATISTICS
    suspend fun countSubmissionsByAssignment(assignmentId: Long): Int
    suspend fun countSubmissionsByStudent(studentId: Long): Int
    suspend fun getAverageGradeByAssignment(assignmentId: Long): Double
    suspend fun getSubmissionTimeliness(studentId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: AttendanceDataSource
// ============================================
interface AttendanceDataSource {
    // CREATE
    suspend fun createAttendance(attendance: AttendanceModel): AttendanceModel
    suspend fun createBulkAttendance(attendances: List<AttendanceModel>): List<AttendanceModel>

    // READ
    suspend fun getAttendanceById(id: Long): AttendanceModel?
    suspend fun getAttendanceByStudent(studentId: Long, startDate: String? = null, endDate: String? = null, page: Int, pageSize: Int): List<AttendanceModel>
    suspend fun getAttendanceByClasse(classeId: Long, date: String? = null, page: Int, pageSize: Int): List<AttendanceModel>
    suspend fun getAttendanceByTeacher(teacherId: Long, date: String? = null, page: Int, pageSize: Int): List<AttendanceModel>
    suspend fun getDailyAttendance(classeId: Long, date: String): List<AttendanceModel>
    suspend fun getAttendanceSummary(studentId: Long, startDate: String, endDate: String): Map<String, Any>
    suspend fun getAbsentStudents(classeId: Long, date: String): List<AttendanceModel>

    // UPDATE
    suspend fun updateAttendance(attendance: AttendanceModel): AttendanceModel
    suspend fun updateAttendanceStatus(id: Long, status: AttendanceStatus, notes: String? = null): Boolean

    // DELETE
    suspend fun deleteAttendance(id: Long): Boolean

    // STATISTICS
    suspend fun countAttendanceByStatus(studentId: Long, startDate: String, endDate: String): Map<AttendanceStatus, Int>
    suspend fun calculateAttendanceRate(studentId: Long, startDate: String, endDate: String): Double
    suspend fun getClassAttendanceStats(classeId: Long, date: String): Map<String, Any>
    suspend fun getStudentAttendanceTrend(studentId: Long, days: Int = 30): Map<String, Double>
}

// ============================================
// INTERFACE: ClasseDataSource
// ============================================
interface ClasseDataSource {
    // CREATE
    suspend fun createClasse(classe: ClasseModel): ClasseModel

    // READ
    suspend fun getClasseById(id: Long): ClasseModel?
    suspend fun getClassesBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<ClasseModel>
    suspend fun getClassesByGrade(gradeId: Long, schoolId: Long? = null, page: Int, pageSize: Int): List<ClasseModel>
    suspend fun getClassesByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<ClasseModel>
    suspend fun searchClasses(query: String, schoolId: Long? = null, page: Int, pageSize: Int): List<ClasseModel>
    suspend fun getClassSchedule(classeId: Long): Map<String, Any>

    // UPDATE
    suspend fun updateClasse(classe: ClasseModel): ClasseModel
    suspend fun updateClasseStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateClasseTeacher(id: Long, teacherId: Long?): Boolean
    suspend fun updateStudentCount(id: Long): Boolean

    // DELETE
    suspend fun deleteClasse(id: Long): Boolean

    // STATISTICS
    suspend fun countClassesBySchool(schoolId: Long): Int
    suspend fun countClassesByGrade(schoolId: Long): Map<Long, Int>
    suspend fun getClassCapacityUtilization(classeId: Long): Double
    suspend fun getSchoolClassDistribution(schoolId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: ExamDataSource
// ============================================
interface ExamDataSource {
    // CREATE
    suspend fun createExam(exam: ExamModel): ExamModel

    // READ
    suspend fun getExamById(id: Long): ExamModel?
    suspend fun getExamsByTeacher(teacherId: Long, published: Boolean? = null, page: Int, pageSize: Int): List<ExamModel>
    suspend fun getExamsByClasse(classeId: Long, published: Boolean? = null, page: Int, pageSize: Int): List<ExamModel>
    suspend fun getExamsBySubject(subjectId: Long, published: Boolean? = null, page: Int, pageSize: Int): List<ExamModel>
    suspend fun getUpcomingExams(studentId: Long, days: Int = 30): List<ExamModel>
    suspend fun getPublishedExams(classeId: Long? = null, page: Int, pageSize: Int): List<ExamModel>
    suspend fun searchExams(query: String, teacherId: Long? = null, page: Int, pageSize: Int): List<ExamModel>

    // UPDATE
    suspend fun updateExam(exam: ExamModel): ExamModel
    suspend fun publishExam(id: Long): Boolean
    suspend fun unpublishExam(id: Long): Boolean
    suspend fun updateExamStatistics(id: Long): Boolean

    // DELETE
    suspend fun deleteExam(id: Long): Boolean

    // STATISTICS
    suspend fun countExamsByTeacher(teacherId: Long): Int
    suspend fun countExamsByClasse(classeId: Long): Int
    suspend fun getExamPerformanceStatistics(examId: Long): Map<String, Any>
    suspend fun getUpcomingExamsCount(studentId: Long): Int
}

// ============================================
// INTERFACE: ExamResultDataSource
// ============================================
interface ExamResultDataSource {
    // CREATE
    suspend fun createExamResult(result: ExamResultModel): ExamResultModel

    // READ
    suspend fun getExamResultById(id: Long): ExamResultModel?
    suspend fun getExamResultsByExam(examId: Long, page: Int, pageSize: Int): List<ExamResultModel>
    suspend fun getExamResultsByStudent(studentId: Long, page: Int, pageSize: Int): List<ExamResultModel>
    suspend fun getExamResultByExamAndStudent(examId: Long, studentId: Long): ExamResultModel?
    suspend fun getTopPerformers(examId: Long, limit: Int = 10): List<ExamResultModel>
    suspend fun getFailedStudents(examId: Long): List<ExamResultModel>
    suspend fun searchExamResults(query: String, examId: Long? = null, page: Int, pageSize: Int): List<ExamResultModel>

    // UPDATE
    suspend fun updateExamResult(result: ExamResultModel): ExamResultModel
    suspend fun gradeExamResult(id: Long, score: Double, feedback: String? = null, gradedBy: Long): Boolean
    suspend fun updateRankInClass(examId: Long): Boolean

    // DELETE
    suspend fun deleteExamResult(id: Long): Boolean

    // STATISTICS
    suspend fun countExamResultsByExam(examId: Long): Int
    suspend fun getExamAverageScore(examId: Long): Double
    suspend fun getStudentExamPerformance(studentId: Long): Map<String, Any>
    suspend fun getClassExamRanking(examId: Long): List<ExamResultModel>
}

// ============================================
// INTERFACE: ExerciseDataSource
// ============================================
interface ExerciseDataSource {
    // CREATE
    suspend fun createExercise(exercise: ExerciseModel): ExerciseModel
    suspend fun createBulkExercises(exercises: List<ExerciseModel>): List<ExerciseModel>

    // READ
    suspend fun getExerciseById(id: Long): ExerciseModel?
    suspend fun getExercisesByLesson(lessonId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<ExerciseModel>
    suspend fun getExercisesByType(questionType: String, lessonId: Long? = null, page: Int, pageSize: Int): List<ExerciseModel>
    suspend fun getExercisesByDifficulty(difficulty: DifficultyLevel, lessonId: Long? = null, page: Int, pageSize: Int): List<ExerciseModel>
    suspend fun searchExercises(query: String, lessonId: Long? = null, page: Int, pageSize: Int): List<ExerciseModel>

    // UPDATE
    suspend fun updateExercise(exercise: ExerciseModel): ExerciseModel
    suspend fun updateExerciseActiveStatus(id: Long, isActive: Boolean): Boolean
    suspend fun reorderExercises(lessonId: Long, exerciseOrder: Map<Long, Int>): Boolean

    // DELETE
    suspend fun deleteExercise(id: Long): Boolean

    // STATISTICS
    suspend fun countExercisesByLesson(lessonId: Long): Int
    suspend fun countExercisesByType(lessonId: Long): Map<String, Int>
    suspend fun getExerciseDifficultyDistribution(lessonId: Long): Map<DifficultyLevel, Int>
}

// ============================================
// INTERFACE: ExerciseResultDataSource
// ============================================
interface ExerciseResultDataSource {
    // CREATE
    suspend fun createExerciseResult(result: ExerciseResultModel): ExerciseResultModel

    // READ
    suspend fun getExerciseResultById(id: Long): ExerciseResultModel?
    suspend fun getExerciseResultsByExercise(exerciseId: Long, page: Int, pageSize: Int): List<ExerciseResultModel>
    suspend fun getExerciseResultsByStudent(studentId: Long, page: Int, pageSize: Int): List<ExerciseResultModel>
    suspend fun getExerciseResultByExerciseAndStudent(exerciseId: Long, studentId: Long): ExerciseResultModel?
    suspend fun getRecentExerciseResults(studentId: Long, limit: Int = 20): List<ExerciseResultModel>
    suspend fun getIncorrectExercises(studentId: Long, lessonId: Long? = null): List<ExerciseResultModel>

    // UPDATE
    suspend fun updateExerciseResult(result: ExerciseResultModel): ExerciseResultModel
    suspend fun incrementAttempts(id: Long): Boolean

    // DELETE
    suspend fun deleteExerciseResult(id: Long): Boolean

    // STATISTICS
    suspend fun countExerciseResultsByStudent(studentId: Long): Int
    suspend fun getExerciseSuccessRate(studentId: Long, lessonId: Long? = null): Double
    suspend fun getAverageAttemptsByExercise(exerciseId: Long): Double
    suspend fun getStudentExerciseProgress(studentId: Long, days: Int = 30): Map<String, Any>
}

// ============================================
// INTERFACE: GradeDataSource
// ============================================
interface GradeDataSource {
    // CREATE
    suspend fun createGrade(grade: GradeModel): GradeModel

    // READ
    suspend fun getGradeById(id: Long): GradeModel?
    suspend fun getGradeByNumber(number: Int, level: GradeLevel): GradeModel?
    suspend fun getAllGrades(active: Boolean? = null, level: GradeLevel? = null, page: Int, pageSize: Int): List<GradeModel>
    suspend fun getGradesByLevel(level: GradeLevel, active: Boolean? = null, page: Int, pageSize: Int): List<GradeModel>
    suspend fun searchGrades(query: String, page: Int, pageSize: Int): List<GradeModel>

    // UPDATE
    suspend fun updateGrade(grade: GradeModel): GradeModel
    suspend fun updateGradeActiveStatus(id: Long, isActive: Boolean): Boolean
    suspend fun updateSubjectsCount(id: Long): Boolean

    // DELETE
    suspend fun deleteGrade(id: Long): Boolean

    // STATISTICS
    suspend fun countGradesByLevel(): Map<GradeLevel, Int>
    suspend fun getTotalGradesCount(): Int
    suspend fun getGradeWithMostSubjects(): GradeModel?
}

// ============================================
// INTERFACE: GuardianDataSource
// ============================================
interface GuardianDataSource {
    // CREATE
    suspend fun createGuardian(guardian: GuardianModel): GuardianModel

    // READ
    suspend fun getGuardianById(id: Long): GuardianModel?
    suspend fun getGuardianByPhone(phone: String): GuardianModel?
    suspend fun getGuardianByEmail(email: String): GuardianModel?
    suspend fun getAllGuardians(active: Boolean? = null, communeId: Long? = null, page: Int, pageSize: Int): List<GuardianModel>
    suspend fun getGuardiansByCommune(communeId: Long, page: Int, pageSize: Int): List<GuardianModel>
    suspend fun searchGuardians(query: String, communeId: Long? = null, page: Int, pageSize: Int): List<GuardianModel>
    suspend fun getGuardiansWithMultipleStudents(minStudents: Int = 2, page: Int, pageSize: Int): List<GuardianModel>

    // UPDATE
    suspend fun updateGuardian(guardian: GuardianModel): GuardianModel
    suspend fun updateGuardianStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateGuardianPreferences(id: Long, preferences: NotificationPreferences): Boolean
    suspend fun updateParentalControl(id: Long, active: Boolean, blockedCategories: List<String>? = null, timeLimit: Int? = null): Boolean
    suspend fun updateReportFrequency(id: Long, frequency: ReportFrequency): Boolean

    // DELETE
    suspend fun softDeleteGuardian(id: Long): Boolean
    suspend fun deleteGuardian(id: Long): Boolean

    // STATISTICS
    suspend fun countGuardiansByCommune(communeId: Long): Int
    suspend fun countGuardiansByRelation(): Map<GuardianRelation, Int>
    suspend fun getTotalGuardiansCount(): Int
    suspend fun getGuardianStudentStats(guardianId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: LessonDataSource
// ============================================
interface LessonDataSource {
    // CREATE
    suspend fun createLesson(lesson: LessonModel): LessonModel

    // READ
    suspend fun getLessonById(id: Long): LessonModel?
    suspend fun getLessonsByGrade(gradeId: Long, approved: Boolean? = null, page: Int, pageSize: Int): List<LessonModel>
    suspend fun getLessonsBySubject(subjectId: Long, approved: Boolean? = null, page: Int, pageSize: Int): List<LessonModel>
    suspend fun getLessonsByTeacher(teacherId: Long, approved: Boolean? = null, page: Int, pageSize: Int): List<LessonModel>
    suspend fun getFeaturedLessons(limit: Int = 10): List<LessonModel>
    suspend fun getFreeLessons(page: Int, pageSize: Int): List<LessonModel>
    suspend fun getPopularLessons(limit: Int = 20): List<LessonModel>
    suspend fun searchLessons(query: String, gradeId: Long? = null, subjectId: Long? = null, page: Int, pageSize: Int): List<LessonModel>

    // UPDATE
    suspend fun updateLesson(lesson: LessonModel): LessonModel
    suspend fun approveLesson(id: Long, approvedBy: Long): Boolean
    suspend fun rejectLesson(id: Long, reason: String? = null): Boolean
    suspend fun updateLessonStatistics(id: Long): Boolean
    suspend fun incrementViewCount(id: Long): Boolean
    suspend fun markAsFeatured(id: Long, featured: Boolean): Boolean
    suspend fun updateLessonRating(id: Long, rating: Int): Boolean

    // DELETE
    suspend fun deleteLesson(id: Long): Boolean

    // STATISTICS
    suspend fun countLessonsByTeacher(teacherId: Long): Int
    suspend fun countLessonsBySubject(subjectId: Long): Int
    suspend fun getTotalLessonsCount(): Int
    suspend fun getLessonEngagementStatistics(lessonId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: LessonProgressDataSource
// ============================================
interface LessonProgressDataSource {
    // CREATE
    suspend fun createLessonProgress(progress: LessonProgressModel): LessonProgressModel

    // READ
    suspend fun getLessonProgressById(id: Long): LessonProgressModel?
    suspend fun getLessonProgressByLessonAndStudent(lessonId: Long, studentId: Long): LessonProgressModel?
    suspend fun getProgressByStudent(studentId: Long, completed: Boolean? = null, page: Int, pageSize: Int): List<LessonProgressModel>
    suspend fun getProgressByLesson(lessonId: Long, completed: Boolean? = null, page: Int, pageSize: Int): List<LessonProgressModel>
    suspend fun getRecentProgress(studentId: Long, limit: Int = 10): List<LessonProgressModel>
    suspend fun getBookmarkedLessons(studentId: Long, page: Int, pageSize: Int): List<LessonProgressModel>
    suspend fun getInProgressLessons(studentId: Long): List<LessonProgressModel>

    // UPDATE
    suspend fun updateLessonProgress(progress: LessonProgressModel): LessonProgressModel
    suspend fun updateProgressPercentage(id: Long, percentage: Int, lastPosition: Int): Boolean
    suspend fun markAsCompleted(id: Long): Boolean
    suspend fun toggleBookmark(id: Long, bookmarked: Boolean): Boolean
    suspend fun addNote(id: Long, note: String): Boolean
    suspend fun rateLesson(id: Long, rating: Int, feedback: String? = null): Boolean

    // DELETE
    suspend fun deleteLessonProgress(id: Long): Boolean

    // STATISTICS
    suspend fun countCompletedLessons(studentId: Long): Int
    suspend fun getAverageCompletionTime(studentId: Long): Double
    suspend fun getStudentLearningTrend(studentId: Long, days: Int = 30): Map<String, Int>
    suspend fun getLessonCompletionRate(lessonId: Long): Double
}

// ============================================
// INTERFACE: MessageDataSource
// ============================================
interface MessageDataSource {
    // CREATE
    suspend fun createMessage(message: MessageModel): MessageModel

    // READ
    suspend fun getMessageById(id: Long): MessageModel?
    suspend fun getMessagesBySender(senderId: Long, page: Int, pageSize: Int): List<MessageModel>
    suspend fun getMessagesByReceiver(receiverId: Long, page: Int, pageSize: Int): List<MessageModel>
    suspend fun getConversation(user1Id: Long, user2Id: Long, page: Int, pageSize: Int): List<MessageModel>
    suspend fun getUnreadMessages(userId: Long): List<MessageModel>
    suspend fun getRecentConversations(userId: Long, limit: Int = 10): List<MessageModel>
    suspend fun searchMessages(query: String, userId: Long? = null, page: Int, pageSize: Int): List<MessageModel>

    // UPDATE
    suspend fun updateMessage(message: MessageModel): MessageModel
    suspend fun markAsRead(id: Long): Boolean
    suspend fun markMultipleAsRead(messageIds: List<Long>): Boolean

    // DELETE
    suspend fun deleteMessage(id: Long): Boolean
    suspend fun deleteConversation(user1Id: Long, user2Id: Long): Boolean

    // STATISTICS
    suspend fun countUnreadMessages(userId: Long): Int
    suspend fun countMessagesByType(userId: Long): Map<MessageType, Int>
    suspend fun getMessageActivity(userId: Long, days: Int = 30): Map<String, Int>
}

// ============================================
// INTERFACE: NotificationDataSource
// ============================================
interface NotificationDataSource {
    // CREATE
    suspend fun createNotification(notification: NotificationModel): NotificationModel
    suspend fun createBulkNotifications(notifications: List<NotificationModel>): List<NotificationModel>

    // READ
    suspend fun getNotificationById(id: Long): NotificationModel?
    suspend fun getNotificationsByUser(userId: Long, read: Boolean? = null, page: Int, pageSize: Int): List<NotificationModel>
    suspend fun getNotificationsByType(notificationType: NotificationType, userId: Long? = null, page: Int, pageSize: Int): List<NotificationModel>
    suspend fun getUnreadNotifications(userId: Long, limit: Int = 20): List<NotificationModel>
    suspend fun getRecentNotifications(userId: Long, limit: Int = 10): List<NotificationModel>
    suspend fun getNotificationsByRelated(relatedId: Long, relatedType: String, page: Int, pageSize: Int): List<NotificationModel>

    // UPDATE
    suspend fun updateNotification(notification: NotificationModel): NotificationModel
    suspend fun markAsRead(id: Long): Boolean
    suspend fun markAllAsRead(userId: Long): Boolean

    // DELETE
    suspend fun deleteNotification(id: Long): Boolean
    suspend fun deleteExpiredNotifications(): Int
    suspend fun deleteOldNotifications(days: Int = 90): Int

    // STATISTICS
    suspend fun countUnreadNotifications(userId: Long): Int
    suspend fun countNotificationsByType(userId: Long): Map<NotificationType, Int>
    suspend fun getNotificationDeliveryStats(days: Int = 30): Map<String, Any>
}

// ============================================
// INTERFACE: PaymentDataSource
// ============================================
interface PaymentDataSource {
    // CREATE
    suspend fun createPayment(payment: PaymentModel): PaymentModel

    // READ
    suspend fun getPaymentById(id: Long): PaymentModel?
    suspend fun getPaymentByTransactionId(transactionId: String): PaymentModel?
    suspend fun getPaymentsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<PaymentModel>
    suspend fun getPaymentsByStatus(status: String, page: Int, pageSize: Int): List<PaymentModel>
    suspend fun getPaymentsByDateRange(startDate: String, endDate: String, userId: Long? = null, page: Int, pageSize: Int): List<PaymentModel>
    suspend fun getRecentPayments(limit: Int = 50): List<PaymentModel>
    suspend fun searchPayments(query: String, page: Int, pageSize: Int): List<PaymentModel>

    // UPDATE
    suspend fun updatePayment(payment: PaymentModel): PaymentModel
    suspend fun updatePaymentStatus(id: Long, status: String): Boolean

    // DELETE
    suspend fun deletePayment(id: Long): Boolean

    // STATISTICS
    suspend fun countPaymentsByStatus(): Map<String, Int>
    suspend fun getTotalRevenue(startDate: String, endDate: String): Double
    suspend fun getAveragePaymentAmount(): Double
    suspend fun getPaymentMethodDistribution(): Map<String, Int>
}



// ============================================
// INTERFACE: SchoolDirectorDataSource
// ============================================
interface SchoolDirectorDataSource {
    // CREATE
    suspend fun createDirector(director: SchoolDirectorModel): SchoolDirectorModel

    // READ
    suspend fun getDirectorById(id: Long): SchoolDirectorModel?
    suspend fun getDirectorByEmail(email: String): SchoolDirectorModel?
    suspend fun getDirectorBySchool(schoolId: Long): SchoolDirectorModel?
    suspend fun getAllDirectors(active: Boolean? = null, page: Int, pageSize: Int): List<SchoolDirectorModel>
    suspend fun searchDirectors(query: String, page: Int, pageSize: Int): List<SchoolDirectorModel>
    suspend fun getDirectorsWithExpiringLicense(days: Int = 30): List<SchoolDirectorModel>

    // UPDATE
    suspend fun updateDirector(director: SchoolDirectorModel): SchoolDirectorModel
    suspend fun updateDirectorStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateLicenseStatus(id: Long, active: Boolean, expirationDate: String? = null): Boolean
    suspend fun updateDirectorPermissions(id: Long, canManageTeachers: Boolean? = null, canManageStudents: Boolean? = null, canViewReports: Boolean? = null, canApproveContent: Boolean? = null): Boolean

    // DELETE
    suspend fun softDeleteDirector(id: Long): Boolean
    suspend fun deleteDirector(id: Long): Boolean

    // STATISTICS
    suspend fun countDirectorsBySchool(): Map<Long, Int>
    suspend fun getTotalDirectorsCount(): Int
    suspend fun getDirectorLicenseStats(): Map<String, Any>
}

// ============================================
// INTERFACE: SchoolTypeDataSource
// ============================================
interface SchoolTypeDataSource {
    // CREATE
    suspend fun createSchoolType(schoolType: SchoolTypeModel): SchoolTypeModel

    // READ
    suspend fun getSchoolTypeById(id: Long): SchoolTypeModel?
    suspend fun getSchoolTypeByCode(code: String): SchoolTypeModel?
    suspend fun getAllSchoolTypes(active: Boolean? = null, page: Int, pageSize: Int): List<SchoolTypeModel>
    suspend fun searchSchoolTypes(query: String, page: Int, pageSize: Int): List<SchoolTypeModel>

    // UPDATE
    suspend fun updateSchoolType(schoolType: SchoolTypeModel): SchoolTypeModel
    suspend fun updateSchoolTypeActiveStatus(id: Long, isActive: Boolean): Boolean

    // DELETE
    suspend fun deleteSchoolType(id: Long): Boolean

    // STATISTICS
    suspend fun countSchoolsByType(): Map<Long, Int>
    suspend fun getTotalSchoolTypesCount(): Int
}

// ============================================
// INTERFACE: StateManagerDataSource
// ============================================
interface StateManagerDataSource {
    // CREATE
    suspend fun createStateManager(manager: StateManagerModel): StateManagerModel

    // READ
    suspend fun getStateManagerById(id: Long): StateManagerModel?
    suspend fun getStateManagerByEmail(email: String): StateManagerModel?
    suspend fun getAllStateManagers(active: Boolean? = null, accessLevel: ManagerAccessLevel? = null, page: Int, pageSize: Int): List<StateManagerModel>
    suspend fun getStateManagersByProvince(provinceId: Long, page: Int, pageSize: Int): List<StateManagerModel>
    suspend fun getStateManagersByAccessLevel(level: ManagerAccessLevel, page: Int, pageSize: Int): List<StateManagerModel>
    suspend fun searchStateManagers(query: String, page: Int, pageSize: Int): List<StateManagerModel>

    // UPDATE
    suspend fun updateStateManager(manager: StateManagerModel): StateManagerModel
    suspend fun updateStateManagerStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateStateManagerAccessLevel(id: Long, accessLevel: ManagerAccessLevel): Boolean
    suspend fun updateStateManagerPermissions(id: Long, canExportData: Boolean? = null, canViewNationalReports: Boolean? = null, canApproveContent: Boolean? = null): Boolean
    suspend fun incrementReportsGenerated(id: Long): Boolean

    // DELETE
    suspend fun softDeleteStateManager(id: Long): Boolean
    suspend fun deleteStateManager(id: Long): Boolean

    // STATISTICS
    suspend fun countStateManagersByAccessLevel(): Map<ManagerAccessLevel, Int>
    suspend fun countStateManagersByProvince(provinceId: Long): Int
    suspend fun getTotalStateManagersCount(): Int
    suspend fun getManagerActivityStats(managerId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: StudentDataSource
// ============================================

interface StudentDataSource {
    // CREATE
    suspend fun createStudent(student: StudentModel): StudentModel

    // READ
    suspend fun getStudentById(id: Long): StudentModel?
    suspend fun getStudentByNumber(studentNumber: String): StudentModel?
    suspend fun getStudentByEmail(email: String): StudentModel?
    suspend fun getAllStudents(active: Boolean? = null, accountType: StudentAccountType? = null, page: Int, pageSize: Int): List<StudentModel>
    suspend fun getStudentsBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<StudentModel>
    suspend fun getStudentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<StudentModel>
    suspend fun getStudentsByGrade(gradeId: Long, page: Int, pageSize: Int): List<StudentModel>
    suspend fun getStudentsByGuardian(guardianId: Long, page: Int, pageSize: Int): List<StudentModel>
    suspend fun searchStudents(query: String, schoolId: Long? = null, page: Int, pageSize: Int): List<StudentModel>
    suspend fun getTopStudentsByPoints(limit: Int = 10): List<StudentModel>
    suspend fun getActiveStudentsWithStreak(minStreak: Int = 7): List<StudentModel>

    // UPDATE
    suspend fun updateStudent(student: StudentModel): StudentModel
    suspend fun updateStudentStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateStudentAccountType(id: Long, accountType: StudentAccountType): Boolean
    suspend fun updateStudentPoints(id: Long, points: Int): Boolean
    suspend fun updateStudentLevel(id: Long, level: Int): Boolean
    suspend fun updateStudentStreak(id: Long, streakDays: Int, currentStreak: Int, longestStreak: Int): Boolean
    suspend fun updateStudentStatistics(id: Long): Boolean
    suspend fun updateLastLogin(id: Long): Boolean
    suspend fun assignGuardian(studentId: Long, guardianId: Long): Boolean
    suspend fun assignToClasse(studentId: Long, classeId: Long): Boolean

    // DELETE
    suspend fun softDeleteStudent(id: Long): Boolean
    suspend fun deleteStudent(id: Long): Boolean

    // STATISTICS
    suspend fun countStudentsBySchool(schoolId: Long): Int
    suspend fun countStudentsByGrade(gradeId: Long): Int
    suspend fun countStudentsByClasse(classeId: Long): Int
    suspend fun countStudentsByAccountType(): Map<StudentAccountType, Int>
    suspend fun getTotalStudentsCount(schoolId: Long? = null): Int
    suspend fun getAverageStudentScore(schoolId: Long? = null): Double
    suspend fun getStudentProgressStatistics(studentId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: StudentAchievementDataSource
interface StudentAchievementDataSource {
    // CREATE
    suspend fun createAchievement(achievement: StudentAchievementModel): StudentAchievementModel

    // READ
    suspend fun getAchievementById(id: Long): StudentAchievementModel?
    suspend fun getAchievementsByStudent(studentId: Long, page: Int, pageSize: Int): List<StudentAchievementModel>
    suspend fun getAchievementsByType(type: String, studentId: Long? = null, page: Int, pageSize: Int): List<StudentAchievementModel>
    suspend fun getRecentAchievements(studentId: Long, limit: Int = 10): List<StudentAchievementModel>
    suspend fun getAchievementsWithBadges(studentId: Long, page: Int, pageSize: Int): List<StudentAchievementModel>
    suspend fun searchAchievements(query: String, studentId: Long? = null, page: Int, pageSize: Int): List<StudentAchievementModel>

    // UPDATE
    suspend fun updateAchievement(achievement: StudentAchievementModel): StudentAchievementModel

    // DELETE
    suspend fun deleteAchievement(id: Long): Boolean

    // STATISTICS
    suspend fun countAchievementsByStudent(studentId: Long): Int
    suspend fun countAchievementsByType(studentId: Long): Map<String, Int>
    suspend fun getTotalPointsEarned(studentId: Long): Int
    suspend fun getAchievementFrequency(studentId: Long, days: Int = 30): Map<String, Int>
}

// ============================================
// INTERFACE: SubjectDataSource
// ============================================
interface SubjectDataSource {
    // CREATE
    suspend fun createSubject(subject: SubjectModel): SubjectModel

    // READ
    suspend fun getSubjectById(id: Long): SubjectModel?
    suspend fun getSubjectByCode(code: String): SubjectModel?
    suspend fun getAllSubjects(active: Boolean? = null, isCore: Boolean? = null, page: Int, pageSize: Int): List<SubjectModel>
    suspend fun getSubjectsByGradeLevel(level: GradeLevel, page: Int, pageSize: Int): List<SubjectModel>
    suspend fun getCoreSubjects(page: Int, pageSize: Int): List<SubjectModel>
    suspend fun searchSubjects(query: String, page: Int, pageSize: Int): List<SubjectModel>

    // UPDATE
    suspend fun updateSubject(subject: SubjectModel): SubjectModel
    suspend fun updateSubjectActiveStatus(id: Long, isActive: Boolean): Boolean
    suspend fun updateSubjectStatistics(id: Long): Boolean

    // DELETE
    suspend fun deleteSubject(id: Long): Boolean

    // STATISTICS
    suspend fun countSubjectsByGradeLevel(): Map<GradeLevel, Int>
    suspend fun getTotalSubjectsCount(): Int
    suspend fun getMostPopularSubjects(limit: Int = 10): List<SubjectModel>
}

// ============================================
// INTERFACE: SubscriptionDataSource
// ============================================

interface SubscriptionDataSource {
    // CREATE
    suspend fun createSubscription(subscription: SubscriptionModel): SubscriptionModel

    // READ
    suspend fun getSubscriptionById(id: Long): SubscriptionModel?
    suspend fun getSubscriptionByUser(userId: Long, userType: String, active: Boolean? = null): SubscriptionModel?
    suspend fun getSubscriptionsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<SubscriptionModel>
    suspend fun getSubscriptionsByPlan(planName: String, page: Int, pageSize: Int): List<SubscriptionModel>
    suspend fun getSubscriptionsByStatus(status: String, page: Int, pageSize: Int): List<SubscriptionModel>
    suspend fun getActiveSubscriptions(page: Int, pageSize: Int): List<SubscriptionModel>
    suspend fun getExpiringSubscriptions(days: Int = 7, page: Int, pageSize: Int): List<SubscriptionModel>
    suspend fun getCanceledSubscriptions(page: Int, pageSize: Int): List<SubscriptionModel>

    // UPDATE
    suspend fun updateSubscription(subscription: SubscriptionModel): SubscriptionModel
    suspend fun cancelSubscription(id: Long, reason: String? = null): Boolean
    suspend fun renewSubscription(id: Long, endDate: String): Boolean
    suspend fun updateAutoRenew(id: Long, autoRenew: Boolean): Boolean
    suspend fun updateSubscriptionStatus(id: Long, status: String): Boolean

    // DELETE
    suspend fun deleteSubscription(id: Long): Boolean

    // STATISTICS
    suspend fun countSubscriptionsByPlan(): Map<String, Int>
    suspend fun countSubscriptionsByStatus(): Map<String, Int>
    suspend fun getTotalActiveSubscriptions(): Int
    suspend fun getMonthlyRecurringRevenue(): Double
    suspend fun getChurnRate(startDate: String, endDate: String): Double
}

// ============================================
// INTERFACE: SystemAdministratorDataSource
// ============================================

interface SystemAdministratorDataSource {
    // CREATE
    suspend fun createAdmin(admin: SystemAdministratorModel): SystemAdministratorModel

    // READ
    suspend fun getAdminById(id: Long): SystemAdministratorModel?
    suspend fun getAdminByEmail(email: String): SystemAdministratorModel?
    suspend fun getAllAdmins(active: Boolean? = null, permissionLevel: AdminPermissionLevel? = null, page: Int, pageSize: Int): List<SystemAdministratorModel>
    suspend fun getAdminsByPermissionLevel(level: AdminPermissionLevel, page: Int, pageSize: Int): List<SystemAdministratorModel>
    suspend fun searchAdmins(query: String, page: Int, pageSize: Int): List<SystemAdministratorModel>
    suspend fun getAdminsWithTwoFactorAuth(page: Int, pageSize: Int): List<SystemAdministratorModel>

    // UPDATE
    suspend fun updateAdmin(admin: SystemAdministratorModel): SystemAdministratorModel
    suspend fun updateAdminStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateAdminPermissionLevel(id: Long, permissionLevel: AdminPermissionLevel): Boolean
    suspend fun updateAdminPermissions(id: Long, canManageUsers: Boolean? = null, canManageContent: Boolean? = null, canManagePayments: Boolean? = null): Boolean
    suspend fun updateTwoFactorAuth(id: Long, enabled: Boolean): Boolean
    suspend fun incrementActionsPerformed(id: Long): Boolean
    suspend fun updateLastLogin(id: Long, ipAddress: String? = null): Boolean
    suspend fun resetFailedLoginAttempts(id: Long): Boolean
    suspend fun incrementFailedLoginAttempts(id: Long): Boolean

    // DELETE
    suspend fun softDeleteAdmin(id: Long): Boolean
    suspend fun deleteAdmin(id: Long): Boolean

    // STATISTICS
    suspend fun countAdminsByPermissionLevel(): Map<AdminPermissionLevel, Int>
    suspend fun getTotalAdminsCount(): Int
    suspend fun getAdminActivityStats(days: Int = 30): Map<String, Any>
    suspend fun getMostActiveAdmins(limit: Int = 10): List<SystemAdministratorModel>
}

// ============================================
// INTERFACE: TeacherDataSource
// ============================================
interface TeacherDataSource {
    // CREATE
    suspend fun createTeacher(teacher: TeacherModel): TeacherModel

    // READ
    suspend fun getTeacherById(id: Long): TeacherModel?
    suspend fun getTeacherByNumber(teacherNumber: String): TeacherModel?
    suspend fun getTeacherByEmail(email: String): TeacherModel?
    suspend fun getAllTeachers(active: Boolean? = null, verified: Boolean? = null, type: TeacherType? = null, page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getTeachersBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getTeachersByType(type: TeacherType, page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getVerifiedTeachers(page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getPendingApprovalTeachers(page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getFeaturedTeachers(page: Int, pageSize: Int): List<TeacherModel>
    suspend fun searchTeachers(query: String, schoolId: Long? = null, page: Int, pageSize: Int): List<TeacherModel>
    suspend fun getTopRatedTeachers(limit: Int = 10): List<TeacherModel>

    // UPDATE
    suspend fun updateTeacher(teacher: TeacherModel): TeacherModel
    suspend fun updateTeacherStatus(id: Long, status: UserStatus): Boolean
    suspend fun verifyTeacher(id: Long, verifiedBy: Long, verifiedAt: String): Boolean
    suspend fun rejectTeacher(id: Long, rejectedBy: Long, reason: String? = null): Boolean
    suspend fun updateTeacherRating(id: Long, averageRating: Double, totalRatings: Int): Boolean
    suspend fun updateTeacherStatistics(id: Long): Boolean
    suspend fun updateMarketplaceStatus(id: Long, enabled: Boolean): Boolean
    suspend fun updateMarketplaceBalance(id: Long, balance: Double): Boolean
    suspend fun markAsFeatured(id: Long, featured: Boolean): Boolean
    suspend fun assignToSchool(teacherId: Long, schoolId: Long?): Boolean

    // DELETE
    suspend fun softDeleteTeacher(id: Long): Boolean
    suspend fun deleteTeacher(id: Long): Boolean

    // STATISTICS
    suspend fun countTeachersBySchool(schoolId: Long): Int
    suspend fun countTeachersByType(): Map<TeacherType, Int>
    suspend fun countVerifiedTeachers(): Int
    suspend fun getTotalTeachersCount(schoolId: Long? = null): Int
    suspend fun getAverageTeacherRating(schoolId: Long? = null): Double
    suspend fun getTeacherPerformanceStatistics(teacherId: Long): Map<String, Any>
}

// ============================================
// INTERFACE: TeacherAssignmentDataSource
// ============================================
interface TeacherAssignmentDataSource {
    // CREATE
    suspend fun createTeacherAssignment(assignment: TeacherAssignmentModel): TeacherAssignmentModel

    // READ
    suspend fun getTeacherAssignmentById(id: Long): TeacherAssignmentModel?
    suspend fun getAssignmentsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<TeacherAssignmentModel>
    suspend fun getAssignmentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<TeacherAssignmentModel>
    suspend fun getAssignmentsBySubject(subjectId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<TeacherAssignmentModel>
    suspend fun getHomeroomTeachers(classeId: Long? = null, page: Int, pageSize: Int): List<TeacherAssignmentModel>
    suspend fun getActiveAssignmentsBySchoolYear(schoolYear: String, teacherId: Long? = null, page: Int, pageSize: Int): List<TeacherAssignmentModel>
    suspend fun getTeacherWorkload(teacherId: Long, schoolYear: String): Map<String, Any>

    // UPDATE
    suspend fun updateTeacherAssignment(assignment: TeacherAssignmentModel): TeacherAssignmentModel
    suspend fun updateAssignmentActiveStatus(id: Long, isActive: Boolean): Boolean
    suspend fun updateHomeroomStatus(id: Long, isHomeroomTeacher: Boolean): Boolean

    // DELETE
    suspend fun deleteTeacherAssignment(id: Long): Boolean

    // STATISTICS
    suspend fun countAssignmentsByTeacher(teacherId: Long, schoolYear: String? = null): Int
    suspend fun countAssignmentsByClasse(classeId: Long, schoolYear: String? = null): Int
    suspend fun getTotalWeeklyHoursByTeacher(teacherId: Long, schoolYear: String): Int
    suspend fun getTeacherSubjectDistribution(teacherId: Long): Map<Long, Int>
}

// ============================================
// INTERFACE: TeacherCertificationDataSource
// ============================================
interface TeacherCertificationDataSource {
    // CREATE
    suspend fun createCertification(certification: TeacherCertificationModel): TeacherCertificationModel

    // READ
    suspend fun getCertificationById(id: Long): TeacherCertificationModel?
    suspend fun getCertificationsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): List<TeacherCertificationModel>
    suspend fun getCertificationsByStatus(status: VerificationStatus, teacherId: Long? = null, page: Int, pageSize: Int): List<TeacherCertificationModel>
    suspend fun getVerifiedCertifications(teacherId: Long? = null, page: Int, pageSize: Int): List<TeacherCertificationModel>
    suspend fun getExpiringCertifications(days: Int = 30, page: Int, pageSize: Int): List<TeacherCertificationModel>
    suspend fun searchCertifications(query: String, teacherId: Long? = null, page: Int, pageSize: Int): List<TeacherCertificationModel>

    // UPDATE
    suspend fun updateCertification(certification: TeacherCertificationModel): TeacherCertificationModel
    suspend fun verifyCertification(id: Long, verifiedBy: Long, verifiedAt: String): Boolean
    suspend fun rejectCertification(id: Long, rejectionReason: String? = null): Boolean
    suspend fun updateCertificationActiveStatus(id: Long, isActive: Boolean): Boolean

    // DELETE
    suspend fun deleteCertification(id: Long): Boolean

    // STATISTICS
    suspend fun countCertificationsByTeacher(teacherId: Long): Int
    suspend fun countCertificationsByStatus(teacherId: Long? = null): Map<VerificationStatus, Int>
    suspend fun getExpiredCertificationsCount(): Int
}

// ============================================
// INTERFACE: TeacherSubjectDataSource
// ============================================
interface TeacherSubjectDataSource {
    // CREATE
    suspend fun createTeacherSubject(teacherSubject: TeacherSubjectModel): TeacherSubjectModel

    // READ
    suspend fun getTeacherSubjectById(id: Long): TeacherSubjectModel?
    suspend fun getTeacherSubjectsByTeacher(teacherId: Long, primary: Boolean? = null, page: Int, pageSize: Int): List<TeacherSubjectModel>
    suspend fun getTeacherSubjectsBySubject(subjectId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel>
    suspend fun getPrimarySubjectsByTeacher(teacherId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel>
    suspend fun getTeachersBySubject(subjectId: Long, page: Int, pageSize: Int): List<TeacherSubjectModel>
    suspend fun getSubjectsByTeacherWithExperience(teacherId: Long, minYears: Int = 0, page: Int, pageSize: Int): List<TeacherSubjectModel>

    // UPDATE
    suspend fun updateTeacherSubject(teacherSubject: TeacherSubjectModel): TeacherSubjectModel
    suspend fun updatePrimaryStatus(id: Long, isPrimary: Boolean): Boolean
    suspend fun updateSubjectRating(id: Long, rating: Double): Boolean
    suspend fun incrementLessonsInSubject(id: Long): Boolean

    // DELETE
    suspend fun deleteTeacherSubject(id: Long): Boolean

    // STATISTICS
    suspend fun countSubjectsByTeacher(teacherId: Long): Int
    suspend fun countTeachersBySubject(subjectId: Long): Int
    suspend fun getAverageExperienceBySubject(subjectId: Long): Double
    suspend fun getTeacherSubjectExpertise(teacherId: Long): Map<String, Any>
}
// ============================================
// INTERFACE: TimetableDataSource
// ============================================
interface TimetableDataSource {
    // CREATE
    suspend fun createTimetable(timetable: TimetableModel): TimetableModel
    suspend fun createBulkTimetable(timetables: List<TimetableModel>): List<TimetableModel>

    // READ
    suspend fun getTimetableById(id: Long): TimetableModel?
    suspend fun getTimetableByClasse(classeId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): List<TimetableModel>
    suspend fun getTimetableByTeacher(teacherId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): List<TimetableModel>
    suspend fun getTimetableBySubject(subjectId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): List<TimetableModel>
    suspend fun getDailyTimetable(classeId: Long, dayOfWeek: Int, schoolYear: String): List<TimetableModel>
    suspend fun getWeeklyTimetable(classeId: Long, schoolYear: String): Map<Int, List<TimetableModel>>
    suspend fun getTimetableConflicts(teacherId: Long, dayOfWeek: Int, startTime: String, endTime: String, schoolYear: String): List<TimetableModel>
    suspend fun getRoomSchedule(room: String, dayOfWeek: Int? = null, schoolYear: String? = null, page: Int, pageSize: Int): List<TimetableModel>

    // UPDATE
    suspend fun updateTimetable(timetable: TimetableModel): TimetableModel
    suspend fun updateTimetableActiveStatus(id: Long, isActive: Boolean): Boolean
    suspend fun updateTimetableSlot(id: Long, dayOfWeek: Int? = null, startTime: String? = null, endTime: String? = null): Boolean

    // DELETE
    suspend fun deleteTimetable(id: Long): Boolean
    suspend fun deleteTimetableByClasse(classeId: Long, schoolYear: String): Boolean

    // STATISTICS
    suspend fun countTimetableSlotsByTeacher(teacherId: Long, schoolYear: String): Int
    suspend fun countTimetableSlotsByClasse(classeId: Long, schoolYear: String): Int
    suspend fun getTeacherWeeklySchedule(teacherId: Long, schoolYear: String): Map<Int, Int>
    suspend fun getClassWeeklyHours(classeId: Long, schoolYear: String): Int
}

// ============================================
// INTERFACE: UserActivityLogDataSource
// ============================================
interface UserActivityLogDataSource {
    // CREATE
    suspend fun createActivityLog(log: UserActivityLogModel): UserActivityLogModel

    // READ
    suspend fun getActivityLogById(id: Long): UserActivityLogModel?
    suspend fun getActivityLogsByUser(userId: Long, userType: String, page: Int, pageSize: Int): List<UserActivityLogModel>
    suspend fun getActivityLogsByActivityType(activityType: String, page: Int, pageSize: Int): List<UserActivityLogModel>
    suspend fun getRecentActivityLogs(limit: Int = 100): List<UserActivityLogModel>
    suspend fun getActivityLogsByDateRange(startDate: String, endDate: String, userId: Long? = null, userType: String? = null, page: Int, pageSize: Int): List<UserActivityLogModel>
    suspend fun getActivityLogsByIpAddress(ipAddress: String, page: Int, pageSize: Int): List<UserActivityLogModel>

    // DELETE
    suspend fun deleteActivityLog(id: Long): Boolean
    suspend fun deleteOldActivityLogs(days: Int = 90): Int

    // STATISTICS
    suspend fun countActivityLogsByUser(userId: Long, userType: String, days: Int = 30): Int
    suspend fun countActivityLogsByActivityType(days: Int = 30): Map<String, Int>
    suspend fun getMostActiveUsers(userType: String, days: Int = 30, limit: Int = 10): List<Map<String, Any>>
    suspend fun getActivityFrequency(userId: Long, userType: String, days: Int = 30): Map<String, Int>
}

// ============================================
// INTERFACE: UserSessionDataSource
// ============================================
interface UserSessionDataSource {
    // CREATE
    suspend fun createSession(session: UserSessionModel): UserSessionModel

    // READ
    suspend fun getSessionById(id: Long): UserSessionModel?
    suspend fun getSessionByToken(token: String): UserSessionModel?
    suspend fun getSessionsByUser(userId: Long, userType: String, active: Boolean? = null, page: Int, pageSize: Int): List<UserSessionModel>
    suspend fun getActiveSessions(userId: Long, userType: String): List<UserSessionModel>
    suspend fun getSessionsByDevice(deviceId: String, userId: Long? = null, userType: String? = null, page: Int, pageSize: Int): List<UserSessionModel>
    suspend fun getSessionsByIpAddress(ipAddress: String, page: Int, pageSize: Int): List<UserSessionModel>
    suspend fun getRecentSessions(userId: Long, userType: String, limit: Int = 10): List<UserSessionModel>

    // UPDATE
    suspend fun updateSession(session: UserSessionModel): UserSessionModel
    suspend fun updateLastActivity(id: Long): Boolean
    suspend fun logoutSession(id: Long): Boolean
    suspend fun logoutAllSessions(userId: Long, userType: String): Boolean
    suspend fun expireSession(id: Long): Boolean

    // DELETE
    suspend fun deleteSession(id: Long): Boolean
    suspend fun deleteExpiredSessions(): Int

    // STATISTICS
    suspend fun countActiveSessions(userId: Long? = null, userType: String? = null): Int
    suspend fun countSessionsByDeviceType(userId: Long, userType: String): Map<String, Int>
    suspend fun getAverageSessionDuration(userId: Long, userType: String, days: Int = 30): Double
    suspend fun getSessionActivity(userId: Long, userType: String, days: Int = 30): Map<String, Any>
}




// ==================== LIVE SESSION DATASOURCE ====================

/**
 * DataSource para gerenciar sessões de lives
 */
interface LiveSessionDataSource {

    // ==================== CREATE ====================

    /**
     * Criar uma nova sessão de live
     */
    suspend fun createLiveSession(request: CreateLiveSessionRequest):  LiveSessionModel

    // ==================== READ ====================

    /**
     * Buscar live por ID
     */
    suspend fun getLiveSessionById(sessionId: String):  LiveSessionModel?

    /**
     * Buscar todas as lives (com filtros opcionais)
     */
    suspend fun getAllLiveSessions(
        filters: LiveSessionFilters? = null,
        page: Int = 1,
        pageSize: Int = 20
    ):  List<LiveSessionModel>

    /**
     * Buscar lives do professor
     */
    suspend fun getLiveSessionsByTeacher(
        teacherId: String,
        page: Int = 1,
        pageSize: Int = 20
    ):  List<LiveSessionModel>

    /**
     * Buscar lives agendadas (upcoming)
     */
    suspend fun getUpcomingLiveSessions(
        page: Int = 1,
        pageSize: Int = 20
    ):  List<LiveSessionModel>

    /**
     * Buscar lives ao vivo agora
     */
    suspend fun getLiveNowSessions():  List<LiveSessionModel>

    /**
     * Buscar lives passadas (histórico)
     */
    suspend fun getPastLiveSessions(
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveSessionModel>

    /**
     * Buscar lives que o usuário participa
     */
    suspend fun getUserLiveSessions(
        userId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveSessionModel>

    /**
     * Buscar lives por status
     */
    suspend fun getLiveSessionsByStatus(
        status: LiveStatus,
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveSessionModel>

    /**
     * Buscar lives por disciplina
     */
    suspend fun getLiveSessionsBySubject(
        subject: String,
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveSessionModel>

    /**
     * Buscar lives recentes
     */
    suspend fun getRecentLiveSessions(limit: Int = 10):  List<LiveSessionModel>

    // ==================== UPDATE ====================

    /**
     * Atualizar sessão de live
     */
    suspend fun updateLiveSession(
        sessionId: String,
        request: UpdateLiveSessionRequest
    ): LiveSessionModel

    /**
     * Iniciar live (mudar status para LIVE)
     */
    suspend fun startLiveSession(sessionId: String):  LiveSessionModel

    /**
     * Pausar live
     */
    suspend fun pauseLiveSession(sessionId: String):  LiveSessionModel

    /**
     * Retomar live pausada
     */
    suspend fun resumeLiveSession(sessionId: String):  LiveSessionModel

    /**
     * Finalizar live (mudar status para ENDED)
     */
    suspend fun endLiveSession(sessionId: String):  LiveSessionModel

    /**
     * Cancelar live
     */
    suspend fun cancelLiveSession(sessionId: String): LiveSessionModel

    /**
     * Atualizar métricas da live (viewers, views)
     */
    suspend fun updateLiveMetrics(
        sessionId: String,
        participantCount: Int,
        viewCount: Int
    ):  Boolean

    /**
     * Incrementar contador de participantes
     */
    suspend fun incrementParticipantCount(sessionId: String):  Boolean

    /**
     * Decrementar contador de participantes
     */
    suspend fun decrementParticipantCount(sessionId: String):  Boolean

    // ==================== DELETE ====================

    /**
     * Deletar sessão de live
     */
    suspend fun deleteLiveSession(sessionId: String):  Boolean

    /**
     * Deletar lives canceladas antigas
     */
    suspend fun deleteCancelledSessions(olderThanDays: Int = 30):  Int

    // ==================== STREAMING ====================

    /**
     * Obter informações de streaming (RTMP, playback URL, etc)
     */
    suspend fun getStreamInfo(sessionId: String): StreamInfoResponse

    /**
     * Gerar novo stream key
     */
    suspend fun generateStreamKey(sessionId: String):  String

    // ==================== STATISTICS ====================

    /**
     * Contar lives por status
     */
    suspend fun countLivesByStatus(status: LiveStatus):  Int

    /**
     * Contar lives do professor
     */
    suspend fun countTeacherLives(
        teacherId: String,
        status: LiveStatus? = null
    ): Int

    /**
     * Obter estatísticas gerais das lives
     */
    suspend fun getLiveStatistics(
        teacherId: String? = null,
        days: Int = 30
    ):  Map<String, Any>

    // ==================== REALTIME ====================

    /**
     * Observar mudanças em uma live específica (Realtime)
     */
    fun observeLiveSession(sessionId: String):  Flow<LiveSessionModel>

    /**
     * Observar todas as lives ao vivo agora (Realtime)
     */
    fun observeLiveNowSessions():  Flow<List<LiveSessionModel>>
}

// ==================== LIVE PARTICIPANT DATASOURCE ====================

/**
 * DataSource para gerenciar participantes das lives
 */
interface LiveParticipantDataSource {

    // ==================== CREATE ====================

    /**
     * Entrar em uma live
     */
    suspend fun joinLiveSession(request: JoinLiveSessionRequest):  LiveParticipantModel

    // ==================== READ ====================

    /**
     * Buscar participantes de uma live
     */
    suspend fun getParticipants(
        sessionId: String,
        page: Int = 1,
        pageSize: Int = 100
    ):  List<LiveParticipantModel>

    /**
     * Buscar participante específico
     */
    suspend fun getParticipant(
        sessionId: String,
        userId: String
    ): LiveParticipantModel?

    /**
     * Buscar participantes por papel
     */
    suspend fun getParticipantsByRole(
        sessionId: String,
        role: ParticipantRole
    ): List<LiveParticipantModel>

    /**
     * Buscar participantes online
     */
    suspend fun getOnlineParticipants(sessionId: String): List<LiveParticipantModel>

    /**
     * Buscar participantes pendentes de aprovação
     */
    suspend fun getPendingApprovals(sessionId: String): List<LiveParticipantModel>

    /**
     * Buscar histórico de participações do usuário
     */
    suspend fun getUserParticipationHistory(
        userId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveParticipantModel>

    // ==================== UPDATE ====================

    /**
     * Sair de uma live
     */
    suspend fun leaveLiveSession(
        sessionId: String,
        userId: String
    ): Boolean

    /**
     * Atualizar papel do participante (promover/rebaixar)
     */
    suspend fun updateParticipantRole(
        participantId: String,
        newRole: ParticipantRole
    ): LiveParticipantModel

    /**
     * Aprovar participante (quando requires_approval = true)
     */
    suspend fun approveParticipant(participantId: String): LiveParticipantModel

    /**
     * Rejeitar participante
     */
    suspend fun rejectParticipant(participantId: String): Boolean

    // ==================== DELETE ====================

    /**
     * Remover participante da live
     */
    suspend fun removeParticipant(participantId: String):  Boolean

    /**
     * Limpar participantes offline antigos
     */
    suspend fun cleanupOfflineParticipants(sessionId: String): Int

    // ==================== STATISTICS ====================

    /**
     * Contar participantes da live
     */
    suspend fun countParticipants(sessionId: String, onlineOnly: Boolean = false): Int

    /**
     * Contar participações do usuário
     */
    suspend fun countUserParticipations(userId: String):  Int

    /**
     * Verificar se usuário está na live
     */
    suspend fun isUserInLive(
        sessionId: String,
        userId: String
    ): Boolean

    // ==================== REALTIME ====================

    /**
     * Observar participantes em tempo real
     */
    fun observeParticipants(sessionId: String):  Flow<List<LiveParticipantModel>>

    /**
     * Observar contador de participantes online
     */
    fun observeOnlineCount(sessionId: String):  Flow<Int>
}

// ==================== LIVE CHAT DATASOURCE ====================

/**
 * DataSource para gerenciar chat das lives
 */
interface LiveChatDataSource  {

    // ==================== CREATE ====================

    /**
     * Enviar mensagem no chat
     */
    suspend fun sendMessage(request: SendChatMessageRequest):  LiveChatMessageModel

    // ==================== READ ====================

    /**
     * Buscar mensagens do chat
     */
    suspend fun getMessages(
        sessionId: String,
        limit: Int = 100,
        offset: Int = 0
    ):  List<LiveChatMessageModel>

    /**
     * Buscar mensagens recentes
     */
    suspend fun getRecentMessages(
        sessionId: String,
        limit: Int = 50
    ):  List<LiveChatMessageModel>

    /**
     * Buscar mensagens por tipo
     */
    suspend fun getMessagesByType(
        sessionId: String,
        messageType: ChatMessageType,
        limit: Int = 100
    ): List<LiveChatMessageModel>

    /**
     * Buscar perguntas não respondidas
     */
    suspend fun getUnansweredQuestions(sessionId: String):  List<LiveChatMessageModel>

    /**
     * Buscar mensagens do usuário
     */
    suspend fun getUserMessages(
        sessionId: String,
        userId: String
    ):  List<LiveChatMessageModel>

    /**
     * Buscar mensagens pendentes de aprovação
     */
    suspend fun getPendingMessages(sessionId: String): List<LiveChatMessageModel>

    // ==================== UPDATE ====================

    /**
     * Aprovar mensagem (quando chat_moderated = true)
     */
    suspend fun approveMessage(messageId: String): LiveChatMessageModel

    /**
     * Fixar mensagem importante
     */
    suspend fun pinMessage(messageId: String):  LiveChatMessageModel

    /**
     * Despintar mensagem
     */
    suspend fun unpinMessage(messageId: String):  Boolean

    // ==================== DELETE ====================

    /**
     * Deletar mensagem
     */
    suspend fun deleteMessage(messageId: String):  Boolean

    /**
     * Deletar todas as mensagens do usuário
     */
    suspend fun deleteUserMessages(
        sessionId: String,
        userId: String
    ):  Int

    /**
     * Limpar mensagens antigas
     */
    suspend fun clearOldMessages(
        sessionId: String,
        olderThanMinutes: Int = 60
    ): Int

    // ==================== STATISTICS ====================

    /**
     * Contar mensagens da live
     */
    suspend fun countMessages(sessionId: String): Int

    /**
     * Contar mensagens do usuário
     */
    suspend fun countUserMessages(
        sessionId: String,
        userId: String
    ):  Int

    /**
     * Contar mensagens por tipo
     */
    suspend fun countMessagesByType(sessionId: String):  Map<ChatMessageType, Int>

    // ==================== REALTIME ====================

    /**
     * Observar mensagens em tempo real
     */
    fun observeMessages(sessionId: String):  Flow<LiveChatMessageModel>
}

// ==================== LIVE REACTION DATASOURCE ====================

/**
 * DataSource para gerenciar reações nas lives
 */
interface LiveReactionDataSource  {

    // ==================== CREATE ====================

    /**
     * Enviar reação (emoji)
     */
    suspend fun sendReaction(
        sessionId: String,
        userId: String,
        emoji: String
    ): LiveReactionModel

    // ==================== READ ====================

    /**
     * Buscar reações da live
     */
    suspend fun getReactions(
        sessionId: String,
        limit: Int = 50
    ):  List<LiveReactionModel>

    /**
     * Buscar reações recentes
     */
    suspend fun getRecentReactions(
        sessionId: String,
        seconds: Int = 5
    ):  List<LiveReactionModel>

    // ==================== DELETE ====================

    /**
     * Limpar reações antigas (> 5 segundos)
     */
    suspend fun clearOldReactions(
        sessionId: String,
        olderThanSeconds: Int = 5
    ): Int

    // ==================== STATISTICS ====================

    /**
     * Contar reações por tipo
     */
    suspend fun countReactionsByType(sessionId: String):  Map<String, Int>

    /**
     * Contar total de reações
     */
    suspend fun countTotalReactions(sessionId: String):  Int

    // ==================== REALTIME ====================

    /**
     * Observar reações em tempo real
     */
    fun observeReactions(sessionId: String): Flow<LiveReactionModel>
}

// ==================== LIVE POLL DATASOURCE ====================

/**
 * DataSource para gerenciar enquetes nas lives
 */
interface LivePollDataSource {

    // ==================== CREATE ====================

    /**
     * Criar enquete
     */
    suspend fun createPoll(
        sessionId: String,
        question: String,
        options: List<String>,
        durationSeconds: Int? = null
    ):  LivePollModel

    /**
     * Votar em enquete
     */
    suspend fun votePoll(
        pollId: String,
        optionId: String,
        userId: String
    ): PollVoteModel

    // ==================== READ ====================

    /**
     * Buscar enquetes da live
     */
    suspend fun getPolls(sessionId: String): List<LivePollModel>

    /**
     * Buscar enquete por ID
     */
    suspend fun getPollById(pollId: String): LivePollModel?

    /**
     * Buscar enquetes ativas
     */
    suspend fun getActivePolls(sessionId: String):  List<LivePollModel>

    /**
     * Buscar votos de uma enquete
     */
    suspend fun getPollVotes(pollId: String): List<PollVoteModel>

    /**
     * Verificar se usuário já votou
     */
    suspend fun hasUserVoted(
        pollId: String,
        userId: String
    ):  Boolean

    // ==================== UPDATE ====================

    /**
     * Fechar enquete
     */
    suspend fun closePoll(pollId: String):  LivePollModel

    // ==================== DELETE ====================

    /**
     * Deletar enquete
     */
    suspend fun deletePoll(pollId: String):  Boolean

    // ==================== STATISTICS ====================

    /**
     * Contar votos totais da enquete
     */
    suspend fun countPollVotes(pollId: String):  Int

    /**
     * Obter resultados da enquete
     */
    suspend fun getPollResults(pollId: String): Map<String, Int>

    // ==================== REALTIME ====================

    /**
     * Observar resultados da enquete em tempo real
     */
    fun observePollResults(pollId: String):  Flow<LivePollModel>
}

// ==================== LIVE STATS DATASOURCE ====================

/**
 * DataSource para estatísticas das lives
 */
interface LiveStatsDataSource  {

    // ==================== CREATE/UPDATE ====================

    /**
     * Criar estatísticas iniciais
     */
    suspend fun createLiveStats(sessionId: String):  LiveStreamStats

    /**
     * Atualizar estatísticas
     */
    suspend fun updateLiveStats(stats: LiveStreamStats): LiveStreamStats

    /**
     * Registrar visualização (incrementar view_count)
     */
    suspend fun recordView(sessionId: String, userId: String):  Boolean

    /**
     * Registrar tempo de visualização
     */
    suspend fun recordWatchTime(
        sessionId: String,
        userId: String,
        seconds: Int
    ): Boolean

    /**
     * Incrementar contador de mensagens
     */
    suspend fun incrementMessageCount(sessionId: String):  Boolean

    /**
     * Incrementar contador de reações
     */
    suspend fun incrementReactionCount(sessionId: String):  Boolean

    /**
     * Atualizar pico de espectadores
     */
    suspend fun updatePeakViewers(sessionId: String, viewers: Int):  Boolean

    // ==================== READ ====================

    /**
     * Buscar estatísticas da live
     */
    suspend fun getLiveStats(sessionId: String): LiveStreamStats?

    /**
     * Buscar estatísticas do professor (todas as lives)
     */
    suspend fun getTeacherStats(teacherId: String):  TeacherLiveStats

    /**
     * Buscar estatísticas por período
     */
    suspend fun getStatsByPeriod(
        teacherId: String,
        startDate: String,
        endDate: String
    ): Map<String, Any>

    // ==================== STATISTICS ====================

    /**
     * Calcular média de espectadores
     */
    suspend fun calculateAverageViewers(sessionId: String):  Double

    /**
     * Calcular taxa de retenção
     */
    suspend fun calculateRetentionRate(sessionId: String): Double

    /**
     * Calcular taxa de engajamento
     */
    suspend fun calculateEngagementRate(sessionId: String):  Double

    // ==================== REALTIME ====================

    /**
     * Observar estatísticas em tempo real
     */
    fun observeLiveStats(sessionId: String): Flow<LiveStreamStats>
}

// ==================== LIVE RECORDING DATASOURCE ====================

/**
 * DataSource para gravações das lives
 */
interface LiveRecordingDataSource  {

    // ==================== CREATE ====================

    /**
     * Iniciar gravação
     */
    suspend fun startRecording(sessionId: String):  LiveRecordingModel

    // ==================== READ ====================

    /**
     * Buscar gravação por ID
     */
    suspend fun getRecordingById(recordingId: String): LiveRecordingModel?

    /**
     * Buscar gravações de uma live
     */
    suspend fun getRecordingsBySession(sessionId: String):  List<LiveRecordingModel>

    /**
     * Buscar gravações do professor
     */
    suspend fun getRecordingsByTeacher(
        teacherId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveRecordingModel>

    /**
     * Buscar gravações públicas
     */
    suspend fun getPublicRecordings(
        page: Int = 1,
        pageSize: Int = 20
    ): List<LiveRecordingModel>

    /**
     * Buscar gravações recentes
     */
    suspend fun getRecentRecordings(limit: Int = 10):  List<LiveRecordingModel>

    // ==================== UPDATE ====================

    /**
     * Parar gravação
     */
    suspend fun stopRecording(recordingId: String): LiveRecordingModel

    /**
     * Tornar gravação pública/privada
     */
    suspend fun updateRecordingVisibility(
        recordingId: String,
        isPublic: Boolean
    ):  LiveRecordingModel

    /**
     * Atualizar status de processamento
     */
    suspend fun updateProcessingStatus(
        recordingId: String,
        isProcessing: Boolean
    ): LiveRecordingModel

    // ==================== DELETE ====================

    /**
     * Deletar gravação
     */
    suspend fun deleteRecording(recordingId: String): Boolean

    // ==================== STATISTICS ====================

    /**
     * Contar gravações do professor
     */
    suspend fun countTeacherRecordings(teacherId: String):  Int

    /**
     * Obter tamanho total das gravações
     */
    suspend fun getTotalRecordingSize(teacherId: String):  Double
}

// ==================== LIVE NOTIFICATION DATASOURCE ====================

/**
 * DataSource para notificações de lives
 */
interface LiveNotificationDataSource  {

    // ==================== CREATE ====================

    /**
     * Criar notificação
     */
    suspend fun createNotification(
        sessionId: String,
        userId: String,
        type: NotificationType
    ):  LiveNotificationModel

    /**
     * Enviar notificação "live começando" para todos inscritos
     */
    suspend fun notifyLiveStartingSoon(sessionId: String): Int

    /**
     * Enviar notificação "live começou" para todos inscritos
     */
    suspend fun notifyLiveStarted(sessionId: String):  Int

    /**
     * Enviar notificação "gravação disponível"
     */
    suspend fun notifyRecordingAvailable(
        sessionId: String,
        recordingId: String
    ):  Int

    // ==================== READ ====================

    /**
     * Buscar notificações do usuário
     */
    suspend fun getUserNotifications(
        userId: String,
        page: Int = 1,
        pageSize: Int = 20
    ):  List<LiveNotificationModel>

    /**
     * Buscar notificações não enviadas
     */
    suspend fun getPendingNotifications():  List<LiveNotificationModel>

    // ==================== UPDATE ====================

    /**
     * Marcar notificação como enviada
     */
    suspend fun markAsSent(notificationId: String):  LiveNotificationModel

    // ==================== DELETE ====================

    /**
     * Deletar notificação
     */
    suspend fun deleteNotification(notificationId: String):  Boolean

    /**
     * Limpar notificações antigas
     */
    suspend fun clearOldNotifications(olderThanDays: Int = 30):  Int

    // ==================== STATISTICS ====================

    /**
     * Contar notificações pendentes
     */
    suspend fun countPendingNotifications(userId: String? = null): Int
}


class datasource {
}


