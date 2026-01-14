package com.tshikasi.tshikasi_auto_school.domain.repository

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.*
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
// ============================================
// INTERFACE: AssignmentRepository
// ============================================
interface AssignmentRepository {
    // CREATE
    suspend fun createAssignment(assignment: AssignmentModel): Either<NetworkError, AssignmentModel>

    // READ
    suspend fun getAssignmentById(id: Long): Either<NetworkError, AssignmentModel?>
    suspend fun getAssignmentsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentModel>>
    suspend fun getAssignmentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentModel>>
    suspend fun getAssignmentsBySubject(subjectId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentModel>>
    suspend fun getAssignmentsByGrade(gradeId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentModel>>
    suspend fun getUpcomingAssignments(studentId: Long, days: Int = 7): Either<NetworkError, List<AssignmentModel>>
    suspend fun getOverdueAssignments(studentId: Long): Either<NetworkError, List<AssignmentModel>>
    suspend fun searchAssignments(query: String, teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentModel>>

    // UPDATE
    suspend fun updateAssignment(assignment: AssignmentModel): Either<NetworkError, AssignmentModel>
    suspend fun updateAssignmentStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateAssignmentStatistics(id: Long): Either<NetworkError, Boolean>
    suspend fun updateTotalSubmissions(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteAssignment(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countAssignmentsByTeacher(teacherId: Long): Either<NetworkError, Int>
    suspend fun countAssignmentsByClasse(classeId: Long): Either<NetworkError, Int>
    suspend fun getAverageScoreByAssignment(assignmentId: Long): Either<NetworkError, Double>
    suspend fun getAssignmentCompletionRate(assignmentId: Long): Either<NetworkError, Double>
}

// ============================================
// INTERFACE: UserRepository
// ============================================
interface UserRepository {
    // CREATE
    suspend fun createUser(user: UserModel): Either<NetworkError, UserModel>

    // READ
    suspend fun getUserById(id: Long): Either<NetworkError, UserModel?>
    suspend fun getUserByEmail(email: String): Either<NetworkError, UserModel?>
    suspend fun getUsersByType(userType: UserType, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<UserModel>>
    suspend fun searchUsers(query: String, userType: UserType? = null, page: Int, pageSize: Int): Either<NetworkError, List<UserModel>>
    suspend fun getUsersByStatus(status: UserStatus, page: Int, pageSize: Int): Either<NetworkError, List<UserModel>>
    suspend fun getRecentlyActiveUsers(days: Int = 7, limit: Int = 50): Either<NetworkError, List<UserModel>>

    // UPDATE
    suspend fun updateUser(user: UserModel): Either<NetworkError, UserModel>
    suspend fun updateUserStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateUserProfile(id: Long, fullName: String? = null, phone: String? = null, profilePhoto: String? = null): Either<NetworkError, Boolean>
    suspend fun updateUserPreferences(id: Long, preferences: NotificationPreferences): Either<NetworkError, Boolean>
    suspend fun updateLastLogin(id: Long, ipAddress: String? = null): Either<NetworkError, Boolean>
    suspend fun incrementFailedLoginAttempts(id: Long): Either<NetworkError, Boolean>
    suspend fun resetFailedLoginAttempts(id: Long): Either<NetworkError, Boolean>
    suspend fun lockUserAccount(id: Long, until: String): Either<NetworkError, Boolean>
    suspend fun unlockUserAccount(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteUser(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteUser(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countUsersByType(): Either<NetworkError, Map<UserType, Int>>
    suspend fun countUsersByStatus(): Either<NetworkError, Map<UserStatus, Int>>
    suspend fun getTotalUsersCount(): Either<NetworkError, Int>
    suspend fun getNewUsersCount(days: Int = 30): Either<NetworkError, Int>
}

// ============================================
// INTERFACE: AssignmentSubmissionRepository
// ============================================
interface AssignmentSubmissionRepository {
    // CREATE
    suspend fun createSubmission(submission: AssignmentSubmissionModel): Either<NetworkError, AssignmentSubmissionModel>

    // READ
    suspend fun getSubmissionById(id: Long): Either<NetworkError, AssignmentSubmissionModel?>
    suspend fun getSubmissionsByAssignment(assignmentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>>
    suspend fun getSubmissionsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>>
    suspend fun getSubmissionByAssignmentAndStudent(assignmentId: Long, studentId: Long): Either<NetworkError, AssignmentSubmissionModel?>
    suspend fun getLateSubmissions(assignmentId: Long? = null): Either<NetworkError, List<AssignmentSubmissionModel>>
    suspend fun getUngradedSubmissions(teacherId: Long, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>>
    suspend fun searchSubmissions(query: String, assignmentId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<AssignmentSubmissionModel>>

    // UPDATE
    suspend fun updateSubmission(submission: AssignmentSubmissionModel): Either<NetworkError, AssignmentSubmissionModel>
    suspend fun gradeSubmission(id: Long, grade: Double, feedback: String? = null, gradedBy: Long): Either<NetworkError, Boolean>
    suspend fun updateSubmissionStatus(id: Long, status: AssignmentStatus): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSubmission(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSubmissionsByAssignment(assignmentId: Long): Either<NetworkError, Int>
    suspend fun countSubmissionsByStudent(studentId: Long): Either<NetworkError, Int>
    suspend fun getAverageGradeByAssignment(assignmentId: Long): Either<NetworkError, Double>
    suspend fun getSubmissionTimeliness(studentId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: AttendanceRepository
// ============================================
interface AttendanceRepository {
    // CREATE
    suspend fun createAttendance(attendance: AttendanceModel): Either<NetworkError, AttendanceModel>
    suspend fun createBulkAttendance(attendances: List<AttendanceModel>): Either<NetworkError, List<AttendanceModel>>

    // READ
    suspend fun getAttendanceById(id: Long): Either<NetworkError, AttendanceModel?>
    suspend fun getAttendanceByStudent(studentId: Long, startDate: String? = null, endDate: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<AttendanceModel>>
    suspend fun getAttendanceByClasse(classeId: Long, date: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<AttendanceModel>>
    suspend fun getAttendanceByTeacher(teacherId: Long, date: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<AttendanceModel>>
    suspend fun getDailyAttendance(classeId: Long, date: String): Either<NetworkError, List<AttendanceModel>>
    suspend fun getAttendanceSummary(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Map<String, Any>>
    suspend fun getAbsentStudents(classeId: Long, date: String): Either<NetworkError, List<AttendanceModel>>

    // UPDATE
    suspend fun updateAttendance(attendance: AttendanceModel): Either<NetworkError, AttendanceModel>
    suspend fun updateAttendanceStatus(id: Long, status: AttendanceStatus, notes: String? = null): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteAttendance(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countAttendanceByStatus(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Map<AttendanceStatus, Int>>
    suspend fun calculateAttendanceRate(studentId: Long, startDate: String, endDate: String): Either<NetworkError, Double>
    suspend fun getClassAttendanceStats(classeId: Long, date: String): Either<NetworkError, Map<String, Any>>
    suspend fun getStudentAttendanceTrend(studentId: Long, days: Int = 30): Either<NetworkError, Map<String, Double>>
}

// ============================================
// INTERFACE: ClasseRepository
// ============================================
interface ClasseRepository {
    // CREATE
    suspend fun createClasse(classe: ClasseModel): Either<NetworkError, ClasseModel>

    // READ
    suspend fun getClasseById(id: Long): Either<NetworkError, ClasseModel?>
    suspend fun getClassesBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>>
    suspend fun getClassesByGrade(gradeId: Long, schoolId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>>
    suspend fun getClassesByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>>
    suspend fun searchClasses(query: String, schoolId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ClasseModel>>
    suspend fun getClassSchedule(classeId: Long): Either<NetworkError, Map<String, Any>>

    // UPDATE
    suspend fun updateClasse(classe: ClasseModel): Either<NetworkError, ClasseModel>
    suspend fun updateClasseStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateClasseTeacher(id: Long, teacherId: Long?): Either<NetworkError, Boolean>
    suspend fun updateStudentCount(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteClasse(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countClassesBySchool(schoolId: Long): Either<NetworkError, Int>
    suspend fun countClassesByGrade(schoolId: Long): Either<NetworkError, Map<Long, Int>>
    suspend fun getClassCapacityUtilization(classeId: Long): Either<NetworkError, Double>
    suspend fun getSchoolClassDistribution(schoolId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: ExamRepository
// ============================================
interface ExamRepository {
    // CREATE
    suspend fun createExam(exam: ExamModel): Either<NetworkError, ExamModel>

    // READ
    suspend fun getExamById(id: Long): Either<NetworkError, ExamModel?>
    suspend fun getExamsByTeacher(teacherId: Long, published: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>>
    suspend fun getExamsByClasse(classeId: Long, published: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>>
    suspend fun getExamsBySubject(subjectId: Long, published: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>>
    suspend fun getUpcomingExams(studentId: Long, days: Int = 30): Either<NetworkError, List<ExamModel>>
    suspend fun getPublishedExams(classeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>>
    suspend fun searchExams(query: String, teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamModel>>

    // UPDATE
    suspend fun updateExam(exam: ExamModel): Either<NetworkError, ExamModel>
    suspend fun publishExam(id: Long): Either<NetworkError, Boolean>
    suspend fun unpublishExam(id: Long): Either<NetworkError, Boolean>
    suspend fun updateExamStatistics(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteExam(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countExamsByTeacher(teacherId: Long): Either<NetworkError, Int>
    suspend fun countExamsByClasse(classeId: Long): Either<NetworkError, Int>
    suspend fun getExamPerformanceStatistics(examId: Long): Either<NetworkError, Map<String, Any>>
    suspend fun getUpcomingExamsCount(studentId: Long): Either<NetworkError, Int>
}

// ============================================
// INTERFACE: ExamResultRepository
// ============================================
interface ExamResultRepository {
    // CREATE
    suspend fun createExamResult(result: ExamResultModel): Either<NetworkError, ExamResultModel>

    // READ
    suspend fun getExamResultById(id: Long): Either<NetworkError, ExamResultModel?>
    suspend fun getExamResultsByExam(examId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>>
    suspend fun getExamResultsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>>
    suspend fun getExamResultByExamAndStudent(examId: Long, studentId: Long): Either<NetworkError, ExamResultModel?>
    suspend fun getTopPerformers(examId: Long, limit: Int = 10): Either<NetworkError, List<ExamResultModel>>
    suspend fun getFailedStudents(examId: Long): Either<NetworkError, List<ExamResultModel>>
    suspend fun searchExamResults(query: String, examId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExamResultModel>>

    // UPDATE
    suspend fun updateExamResult(result: ExamResultModel): Either<NetworkError, ExamResultModel>
    suspend fun gradeExamResult(id: Long, score: Double, feedback: String? = null, gradedBy: Long): Either<NetworkError, Boolean>
    suspend fun updateRankInClass(examId: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteExamResult(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countExamResultsByExam(examId: Long): Either<NetworkError, Int>
    suspend fun getExamAverageScore(examId: Long): Either<NetworkError, Double>
    suspend fun getStudentExamPerformance(studentId: Long): Either<NetworkError, Map<String, Any>>
    suspend fun getClassExamRanking(examId: Long): Either<NetworkError, List<ExamResultModel>>
}

// ============================================
// INTERFACE: ExerciseRepository
// ============================================
interface ExerciseRepository {
    // CREATE
    suspend fun createExercise(exercise: ExerciseModel): Either<NetworkError, ExerciseModel>
    suspend fun createBulkExercises(exercises: List<ExerciseModel>): Either<NetworkError, List<ExerciseModel>>

    // READ
    suspend fun getExerciseById(id: Long): Either<NetworkError, ExerciseModel?>
    suspend fun getExercisesByLesson(lessonId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseModel>>
    suspend fun getExercisesByType(questionType: String, lessonId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseModel>>
    suspend fun getExercisesByDifficulty(difficulty: DifficultyLevel, lessonId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseModel>>
    suspend fun searchExercises(query: String, lessonId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseModel>>

    // UPDATE
    suspend fun updateExercise(exercise: ExerciseModel): Either<NetworkError, ExerciseModel>
    suspend fun updateExerciseActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>
    suspend fun reorderExercises(lessonId: Long, exerciseOrder: Map<Long, Int>): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteExercise(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countExercisesByLesson(lessonId: Long): Either<NetworkError, Int>
    suspend fun countExercisesByType(lessonId: Long): Either<NetworkError, Map<String, Int>>
    suspend fun getExerciseDifficultyDistribution(lessonId: Long): Either<NetworkError, Map<DifficultyLevel, Int>>
}

// ============================================
// INTERFACE: ExerciseResultRepository
// ============================================
interface ExerciseResultRepository {
    // CREATE
    suspend fun createExerciseResult(result: ExerciseResultModel): Either<NetworkError, ExerciseResultModel>

    // READ
    suspend fun getExerciseResultById(id: Long): Either<NetworkError, ExerciseResultModel?>
    suspend fun getExerciseResultsByExercise(exerciseId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseResultModel>>
    suspend fun getExerciseResultsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<ExerciseResultModel>>
    suspend fun getExerciseResultByExerciseAndStudent(exerciseId: Long, studentId: Long): Either<NetworkError, ExerciseResultModel?>
    suspend fun getRecentExerciseResults(studentId: Long, limit: Int = 20): Either<NetworkError, List<ExerciseResultModel>>
    suspend fun getIncorrectExercises(studentId: Long, lessonId: Long? = null): Either<NetworkError, List<ExerciseResultModel>>

    // UPDATE
    suspend fun updateExerciseResult(result: ExerciseResultModel): Either<NetworkError, ExerciseResultModel>
    suspend fun incrementAttempts(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteExerciseResult(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countExerciseResultsByStudent(studentId: Long): Either<NetworkError, Int>
    suspend fun getExerciseSuccessRate(studentId: Long, lessonId: Long? = null): Either<NetworkError, Double>
    suspend fun getAverageAttemptsByExercise(exerciseId: Long): Either<NetworkError, Double>
    suspend fun getStudentExerciseProgress(studentId: Long, days: Int = 30): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: GradeRepository
// ============================================
interface GradeRepository {
    // CREATE
    suspend fun createGrade(grade: GradeModel): Either<NetworkError, GradeModel>

    // READ
    suspend fun getGradeById(id: Long): Either<NetworkError, GradeModel?>
    suspend fun getGradeByNumber(number: Int, level: GradeLevel): Either<NetworkError, GradeModel?>
    suspend fun getAllGrades(active: Boolean? = null, level: GradeLevel? = null, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>>
    suspend fun getGradesByLevel(level: GradeLevel, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>>
    suspend fun searchGrades(query: String, page: Int, pageSize: Int): Either<NetworkError, List<GradeModel>>

    // UPDATE
    suspend fun updateGrade(grade: GradeModel): Either<NetworkError, GradeModel>
    suspend fun updateGradeActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>
    suspend fun updateSubjectsCount(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteGrade(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countGradesByLevel(): Either<NetworkError, Map<GradeLevel, Int>>
    suspend fun getTotalGradesCount(): Either<NetworkError, Int>
    suspend fun getGradeWithMostSubjects(): Either<NetworkError, GradeModel?>
}

// ============================================
// INTERFACE: GuardianRepository
// ============================================
interface GuardianRepository {
    // CREATE
    suspend fun createGuardian(guardian: GuardianModel): Either<NetworkError, GuardianModel>

    // READ
    suspend fun getGuardianById(id: Long): Either<NetworkError, GuardianModel?>
    suspend fun getGuardianByPhone(phone: String): Either<NetworkError, GuardianModel?>
    suspend fun getGuardianByEmail(email: String): Either<NetworkError, GuardianModel?>
    suspend fun getAllGuardians(active: Boolean? = null, communeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>>
    suspend fun getGuardiansByCommune(communeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>>
    suspend fun searchGuardians(query: String, communeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>>
    suspend fun getGuardiansWithMultipleStudents(minStudents: Int = 2, page: Int, pageSize: Int): Either<NetworkError, List<GuardianModel>>

    // UPDATE
    suspend fun updateGuardian(guardian: GuardianModel): Either<NetworkError, GuardianModel>
    suspend fun updateGuardianStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateGuardianPreferences(id: Long, preferences: NotificationPreferences): Either<NetworkError, Boolean>
    suspend fun updateParentalControl(id: Long, active: Boolean, blockedCategories: List<String>? = null, timeLimit: Int? = null): Either<NetworkError, Boolean>
    suspend fun updateReportFrequency(id: Long, frequency: ReportFrequency): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteGuardian(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteGuardian(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countGuardiansByCommune(communeId: Long): Either<NetworkError, Int>
    suspend fun countGuardiansByRelation(): Either<NetworkError, Map<GuardianRelation, Int>>
    suspend fun getTotalGuardiansCount(): Either<NetworkError, Int>
    suspend fun getGuardianStudentStats(guardianId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: LessonRepository
// ============================================
interface LessonRepository {
    // CREATE
    suspend fun createLesson(lesson: LessonModel): Either<NetworkError, LessonModel>

    // READ
    suspend fun getLessonById(id: Long): Either<NetworkError, LessonModel?>
    suspend fun getLessonsByGrade(gradeId: Long, approved: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>>
    suspend fun getLessonsBySubject(subjectId: Long, approved: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>>
    suspend fun getLessonsByTeacher(teacherId: Long, approved: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>>
    suspend fun getFeaturedLessons(limit: Int = 10): Either<NetworkError, List<LessonModel>>
    suspend fun getFreeLessons(page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>>
    suspend fun getPopularLessons(limit: Int = 20): Either<NetworkError, List<LessonModel>>
    suspend fun searchLessons(query: String, gradeId: Long? = null, subjectId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonModel>>

    // UPDATE
    suspend fun updateLesson(lesson: LessonModel): Either<NetworkError, LessonModel>
    suspend fun approveLesson(id: Long, approvedBy: Long): Either<NetworkError, Boolean>
    suspend fun rejectLesson(id: Long, reason: String? = null): Either<NetworkError, Boolean>
    suspend fun updateLessonStatistics(id: Long): Either<NetworkError, Boolean>
    suspend fun incrementViewCount(id: Long): Either<NetworkError, Boolean>
    suspend fun markAsFeatured(id: Long, featured: Boolean): Either<NetworkError, Boolean>
    suspend fun updateLessonRating(id: Long, rating: Int): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteLesson(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countLessonsByTeacher(teacherId: Long): Either<NetworkError, Int>
    suspend fun countLessonsBySubject(subjectId: Long): Either<NetworkError, Int>
    suspend fun getTotalLessonsCount(): Either<NetworkError, Int>
    suspend fun getLessonEngagementStatistics(lessonId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: LessonProgressRepository
// ============================================
interface LessonProgressRepository {
    // CREATE
    suspend fun createLessonProgress(progress: LessonProgressModel): Either<NetworkError, LessonProgressModel>

    // READ
    suspend fun getLessonProgressById(id: Long): Either<NetworkError, LessonProgressModel?>
    suspend fun getLessonProgressByLessonAndStudent(lessonId: Long, studentId: Long): Either<NetworkError, LessonProgressModel?>
    suspend fun getProgressByStudent(studentId: Long, completed: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonProgressModel>>
    suspend fun getProgressByLesson(lessonId: Long, completed: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<LessonProgressModel>>
    suspend fun getRecentProgress(studentId: Long, limit: Int = 10): Either<NetworkError, List<LessonProgressModel>>
    suspend fun getBookmarkedLessons(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<LessonProgressModel>>
    suspend fun getInProgressLessons(studentId: Long): Either<NetworkError, List<LessonProgressModel>>

    // UPDATE
    suspend fun updateLessonProgress(progress: LessonProgressModel): Either<NetworkError, LessonProgressModel>
    suspend fun updateProgressPercentage(id: Long, percentage: Int, lastPosition: Int): Either<NetworkError, Boolean>
    suspend fun markAsCompleted(id: Long): Either<NetworkError, Boolean>
    suspend fun toggleBookmark(id: Long, bookmarked: Boolean): Either<NetworkError, Boolean>
    suspend fun addNote(id: Long, note: String): Either<NetworkError, Boolean>
    suspend fun rateLesson(id: Long, rating: Int, feedback: String? = null): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteLessonProgress(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countCompletedLessons(studentId: Long): Either<NetworkError, Int>
    suspend fun getAverageCompletionTime(studentId: Long): Either<NetworkError, Double>
    suspend fun getStudentLearningTrend(studentId: Long, days: Int = 30): Either<NetworkError, Map<String, Int>>
    suspend fun getLessonCompletionRate(lessonId: Long): Either<NetworkError, Double>
}

// ============================================
// INTERFACE: MessageRepository
// ============================================
interface MessageRepository {
    // CREATE
    suspend fun createMessage(message: MessageModel): Either<NetworkError, MessageModel>

    // READ
    suspend fun getMessageById(id: Long): Either<NetworkError, MessageModel?>
    suspend fun getMessagesBySender(senderId: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>>
    suspend fun getMessagesByReceiver(receiverId: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>>
    suspend fun getConversation(user1Id: Long, user2Id: Long, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>>
    suspend fun getUnreadMessages(userId: Long): Either<NetworkError, List<MessageModel>>
    suspend fun getRecentConversations(userId: Long, limit: Int = 10): Either<NetworkError, List<MessageModel>>
    suspend fun searchMessages(query: String, userId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<MessageModel>>

    // UPDATE
    suspend fun updateMessage(message: MessageModel): Either<NetworkError, MessageModel>
    suspend fun markAsRead(id: Long): Either<NetworkError, Boolean>
    suspend fun markMultipleAsRead(messageIds: List<Long>): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteMessage(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteConversation(user1Id: Long, user2Id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countUnreadMessages(userId: Long): Either<NetworkError, Int>
    suspend fun countMessagesByType(userId: Long): Either<NetworkError, Map<MessageType, Int>>
    suspend fun getMessageActivity(userId: Long, days: Int = 30): Either<NetworkError, Map<String, Int>>
}

// ============================================
// INTERFACE: NotificationRepository
// ============================================
interface NotificationRepository {
    // CREATE
    suspend fun createNotification(notification: NotificationModel): Either<NetworkError, NotificationModel>
    suspend fun createBulkNotifications(notifications: List<NotificationModel>): Either<NetworkError, List<NotificationModel>>

    // READ
    suspend fun getNotificationById(id: Long): Either<NetworkError, NotificationModel?>
    suspend fun getNotificationsByUser(userId: Long, read: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>>
    suspend fun getNotificationsByType(notificationType: NotificationType, userId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>>
    suspend fun getUnreadNotifications(userId: Long, limit: Int = 20): Either<NetworkError, List<NotificationModel>>
    suspend fun getRecentNotifications(userId: Long, limit: Int = 10): Either<NetworkError, List<NotificationModel>>
    suspend fun getNotificationsByRelated(relatedId: Long, relatedType: String, page: Int, pageSize: Int): Either<NetworkError, List<NotificationModel>>

    // UPDATE
    suspend fun updateNotification(notification: NotificationModel): Either<NetworkError, NotificationModel>
    suspend fun markAsRead(id: Long): Either<NetworkError, Boolean>
    suspend fun markAllAsRead(userId: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteNotification(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteExpiredNotifications(): Either<NetworkError, Int>
    suspend fun deleteOldNotifications(days: Int = 90): Either<NetworkError, Int>

    // STATISTICS
    suspend fun countUnreadNotifications(userId: Long): Either<NetworkError, Int>
    suspend fun countNotificationsByType(userId: Long): Either<NetworkError, Map<NotificationType, Int>>
    suspend fun getNotificationDeliveryStats(days: Int = 30): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: PaymentRepository
// ============================================
interface PaymentRepository {
    // CREATE
    suspend fun createPayment(payment: PaymentModel): Either<NetworkError, PaymentModel>

    // READ
    suspend fun getPaymentById(id: Long): Either<NetworkError, PaymentModel?>
    suspend fun getPaymentByTransactionId(transactionId: String): Either<NetworkError, PaymentModel?>
    suspend fun getPaymentsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>>
    suspend fun getPaymentsByStatus(status: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>>
    suspend fun getPaymentsByDateRange(startDate: String, endDate: String, userId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>>
    suspend fun getRecentPayments(limit: Int = 50): Either<NetworkError, List<PaymentModel>>
    suspend fun searchPayments(query: String, page: Int, pageSize: Int): Either<NetworkError, List<PaymentModel>>

    // UPDATE
    suspend fun updatePayment(payment: PaymentModel): Either<NetworkError, PaymentModel>
    suspend fun updatePaymentStatus(id: Long, status: String): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deletePayment(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countPaymentsByStatus(): Either<NetworkError, Map<String, Int>>
    suspend fun getTotalRevenue(startDate: String, endDate: String): Either<NetworkError, Double>
    suspend fun getAveragePaymentAmount(): Either<NetworkError, Double>
    suspend fun getPaymentMethodDistribution(): Either<NetworkError, Map<String, Int>>
}

// ============================================
// INTERFACE: SchoolRepository
// ============================================
interface SchoolRepository {
    // CREATE
    suspend fun createSchool(school: SchoolModel): Either<NetworkError, SchoolModel>

    // READ
    suspend fun getSchoolById(id: Long): Either<NetworkError, SchoolModel?>
    suspend fun getSchoolByCode(code: String): Either<NetworkError, SchoolModel?>
    suspend fun getAllSchools(active: Boolean? = null, schoolTypeId: Long? = null, communeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<SchoolModel>>
    suspend fun getSchoolsByCommune(communeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<SchoolModel>>
    suspend fun getSchoolsByType(schoolTypeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<SchoolModel>>
    suspend fun searchSchools(query: String, communeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<SchoolModel>>
    suspend fun getTopSchoolsByStudents(limit: Int = 10): Either<NetworkError, List<SchoolModel>>

    // UPDATE
    suspend fun updateSchool(school: SchoolModel): Either<NetworkError, SchoolModel>
    suspend fun updateSchoolStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateSchoolStatistics(id: Long): Either<NetworkError, Boolean>
    suspend fun updateSubscription(id: Long, plan: String? = null, expiresAt: String? = null): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSchool(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSchoolsByCommune(communeId: Long): Either<NetworkError, Int>
    suspend fun countSchoolsByType(): Either<NetworkError, Map<Long, Int>>
    suspend fun getTotalSchoolsCount(): Either<NetworkError, Int>
    suspend fun getSchoolPerformanceMetrics(schoolId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: SchoolDirectorRepository
// ============================================
interface SchoolDirectorRepository {
    // CREATE
    suspend fun createDirector(director: SchoolDirectorModel): Either<NetworkError, SchoolDirectorModel>

    // READ
    suspend fun getDirectorById(id: Long): Either<NetworkError, SchoolDirectorModel?>
    suspend fun getDirectorByEmail(email: String): Either<NetworkError, SchoolDirectorModel?>
    suspend fun getDirectorBySchool(schoolId: Long): Either<NetworkError, SchoolDirectorModel?>
    suspend fun getAllDirectors(active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<SchoolDirectorModel>>
    suspend fun searchDirectors(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SchoolDirectorModel>>
    suspend fun getDirectorsWithExpiringLicense(days: Int = 30): Either<NetworkError, List<SchoolDirectorModel>>

    // UPDATE
    suspend fun updateDirector(director: SchoolDirectorModel): Either<NetworkError, SchoolDirectorModel>
    suspend fun updateDirectorStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateLicenseStatus(id: Long, active: Boolean, expirationDate: String? = null): Either<NetworkError, Boolean>
    suspend fun updateDirectorPermissions(id: Long, canManageTeachers: Boolean? = null, canManageStudents: Boolean? = null, canViewReports: Boolean? = null, canApproveContent: Boolean? = null): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteDirector(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteDirector(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countDirectorsBySchool(): Either<NetworkError, Map<Long, Int>>
    suspend fun getTotalDirectorsCount(): Either<NetworkError, Int>
    suspend fun getDirectorLicenseStats(): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: SchoolTypeRepository
// ============================================
interface SchoolTypeRepository {
    // CREATE
    suspend fun createSchoolType(schoolType: SchoolTypeModel): Either<NetworkError, SchoolTypeModel>

    // READ
    suspend fun getSchoolTypeById(id: Long): Either<NetworkError, SchoolTypeModel?>
    suspend fun getSchoolTypeByCode(code: String): Either<NetworkError, SchoolTypeModel?>
    suspend fun getAllSchoolTypes(active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<SchoolTypeModel>>
    suspend fun searchSchoolTypes(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SchoolTypeModel>>

    // UPDATE
    suspend fun updateSchoolType(schoolType: SchoolTypeModel): Either<NetworkError, SchoolTypeModel>
    suspend fun updateSchoolTypeActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSchoolType(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSchoolsByType(): Either<NetworkError, Map<Long, Int>>
    suspend fun getTotalSchoolTypesCount(): Either<NetworkError, Int>
}

// ============================================
// INTERFACE: StateManagerRepository
// ============================================
interface StateManagerRepository {
    // CREATE
    suspend fun createStateManager(manager: StateManagerModel): Either<NetworkError, StateManagerModel>

    // READ
    suspend fun getStateManagerById(id: Long): Either<NetworkError, StateManagerModel?>
    suspend fun getStateManagerByEmail(email: String): Either<NetworkError, StateManagerModel?>
    suspend fun getAllStateManagers(active: Boolean? = null, accessLevel: ManagerAccessLevel? = null, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>>
    suspend fun getStateManagersByProvince(provinceId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>>
    suspend fun getStateManagersByAccessLevel(level: ManagerAccessLevel, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>>
    suspend fun searchStateManagers(query: String, page: Int, pageSize: Int): Either<NetworkError, List<StateManagerModel>>

    // UPDATE
    suspend fun updateStateManager(manager: StateManagerModel): Either<NetworkError, StateManagerModel>
    suspend fun updateStateManagerStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateStateManagerAccessLevel(id: Long, accessLevel: ManagerAccessLevel): Either<NetworkError, Boolean>
    suspend fun updateStateManagerPermissions(id: Long, canExportData: Boolean? = null, canViewNationalReports: Boolean? = null, canApproveContent: Boolean? = null): Either<NetworkError, Boolean>
    suspend fun incrementReportsGenerated(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteStateManager(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteStateManager(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countStateManagersByAccessLevel(): Either<NetworkError, Map<ManagerAccessLevel, Int>>
    suspend fun countStateManagersByProvince(provinceId: Long): Either<NetworkError, Int>
    suspend fun getTotalStateManagersCount(): Either<NetworkError, Int>
    suspend fun getManagerActivityStats(managerId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: StudentRepository
// ============================================
interface StudentRepository {
    // CREATE
    suspend fun createStudent(student: StudentModel): Either<NetworkError, StudentModel>

    // READ
    suspend fun getStudentById(id: Long): Either<NetworkError, StudentModel?>
    suspend fun getStudentByNumber(studentNumber: String): Either<NetworkError, StudentModel?>
    suspend fun getStudentByEmail(email: String): Either<NetworkError, StudentModel?>
    suspend fun getAllStudents(active: Boolean? = null, accountType: StudentAccountType? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun getStudentsBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun getStudentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun getStudentsByGrade(gradeId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun getStudentsByGuardian(guardianId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun searchStudents(query: String, schoolId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentModel>>
    suspend fun getTopStudentsByPoints(limit: Int = 10): Either<NetworkError, List<StudentModel>>
    suspend fun getActiveStudentsWithStreak(minStreak: Int = 7): Either<NetworkError, List<StudentModel>>

    // UPDATE
    suspend fun updateStudent(student: StudentModel): Either<NetworkError, StudentModel>
    suspend fun updateStudentStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateStudentAccountType(id: Long, accountType: StudentAccountType): Either<NetworkError, Boolean>
    suspend fun updateStudentPoints(id: Long, points: Int): Either<NetworkError, Boolean>
    suspend fun updateStudentLevel(id: Long, level: Int): Either<NetworkError, Boolean>
    suspend fun updateStudentStreak(id: Long, streakDays: Int, currentStreak: Int, longestStreak: Int): Either<NetworkError, Boolean>
    suspend fun updateStudentStatistics(id: Long): Either<NetworkError, Boolean>
    suspend fun updateLastLogin(id: Long): Either<NetworkError, Boolean>
    suspend fun assignGuardian(studentId: Long, guardianId: Long): Either<NetworkError, Boolean>
    suspend fun assignToClasse(studentId: Long, classeId: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteStudent(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteStudent(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countStudentsBySchool(schoolId: Long): Either<NetworkError, Int>
    suspend fun countStudentsByGrade(gradeId: Long): Either<NetworkError, Int>
    suspend fun countStudentsByClasse(classeId: Long): Either<NetworkError, Int>
    suspend fun countStudentsByAccountType(): Either<NetworkError, Map<StudentAccountType, Int>>
    suspend fun getTotalStudentsCount(schoolId: Long? = null): Either<NetworkError, Int>
    suspend fun getAverageStudentScore(schoolId: Long? = null): Either<NetworkError, Double>
    suspend fun getStudentProgressStatistics(studentId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: StudentAchievementRepository
// ============================================
interface StudentAchievementRepository {
    // CREATE
    suspend fun createAchievement(achievement: StudentAchievementModel): Either<NetworkError, StudentAchievementModel>

    // READ
    suspend fun getAchievementById(id: Long): Either<NetworkError, StudentAchievementModel?>
    suspend fun getAchievementsByStudent(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>>
    suspend fun getAchievementsByType(type: String, studentId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>>
    suspend fun getRecentAchievements(studentId: Long, limit: Int = 10): Either<NetworkError, List<StudentAchievementModel>>
    suspend fun getAchievementsWithBadges(studentId: Long, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>>
    suspend fun searchAchievements(query: String, studentId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<StudentAchievementModel>>

    // UPDATE
    suspend fun updateAchievement(achievement: StudentAchievementModel): Either<NetworkError, StudentAchievementModel>

    // DELETE
    suspend fun deleteAchievement(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countAchievementsByStudent(studentId: Long): Either<NetworkError, Int>
    suspend fun countAchievementsByType(studentId: Long): Either<NetworkError, Map<String, Int>>
    suspend fun getTotalPointsEarned(studentId: Long): Either<NetworkError, Int>
    suspend fun getAchievementFrequency(studentId: Long, days: Int = 30): Either<NetworkError, Map<String, Int>>
}

// ============================================
// INTERFACE: SubjectRepository
// ============================================
interface SubjectRepository {
    // CREATE
    suspend fun createSubject(subject: SubjectModel): Either<NetworkError, SubjectModel>

    // READ
    suspend fun getSubjectById(id: Long): Either<NetworkError, SubjectModel?>
    suspend fun getSubjectByCode(code: String): Either<NetworkError, SubjectModel?>
    suspend fun getAllSubjects(active: Boolean? = null, isCore: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>>
    suspend fun getSubjectsByGradeLevel(level: GradeLevel, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>>
    suspend fun getCoreSubjects(page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>>
    suspend fun searchSubjects(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SubjectModel>>

    // UPDATE
    suspend fun updateSubject(subject: SubjectModel): Either<NetworkError, SubjectModel>
    suspend fun updateSubjectActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>
    suspend fun updateSubjectStatistics(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSubject(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSubjectsByGradeLevel(): Either<NetworkError, Map<GradeLevel, Int>>
    suspend fun getTotalSubjectsCount(): Either<NetworkError, Int>
    suspend fun getMostPopularSubjects(limit: Int = 10): Either<NetworkError, List<SubjectModel>>
}

// ============================================
// INTERFACE: SubscriptionRepository
// ============================================
interface SubscriptionRepository {
    // CREATE
    suspend fun createSubscription(subscription: SubscriptionModel): Either<NetworkError, SubscriptionModel>

    // READ
    suspend fun getSubscriptionById(id: Long): Either<NetworkError, SubscriptionModel?>
    suspend fun getSubscriptionByUser(userId: Long, userType: String, active: Boolean? = null): Either<NetworkError, SubscriptionModel?>
    suspend fun getSubscriptionsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>
    suspend fun getSubscriptionsByPlan(planName: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>
    suspend fun getSubscriptionsByStatus(status: String, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>
    suspend fun getActiveSubscriptions(page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>
    suspend fun getExpiringSubscriptions(days: Int = 7, page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>
    suspend fun getCanceledSubscriptions(page: Int, pageSize: Int): Either<NetworkError, List<SubscriptionModel>>

    // UPDATE
    suspend fun updateSubscription(subscription: SubscriptionModel): Either<NetworkError, SubscriptionModel>
    suspend fun cancelSubscription(id: Long, reason: String? = null): Either<NetworkError, Boolean>
    suspend fun renewSubscription(id: Long, endDate: String): Either<NetworkError, Boolean>
    suspend fun updateAutoRenew(id: Long, autoRenew: Boolean): Either<NetworkError, Boolean>
    suspend fun updateSubscriptionStatus(id: Long, status: String): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSubscription(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSubscriptionsByPlan(): Either<NetworkError, Map<String, Int>>
    suspend fun countSubscriptionsByStatus(): Either<NetworkError, Map<String, Int>>
    suspend fun getTotalActiveSubscriptions(): Either<NetworkError, Int>
    suspend fun getMonthlyRecurringRevenue(): Either<NetworkError, Double>
    suspend fun getChurnRate(startDate: String, endDate: String): Either<NetworkError, Double>
}

// ============================================
// INTERFACE: SystemAdministratorRepository
// ============================================
interface SystemAdministratorRepository {
    // CREATE
    suspend fun createAdmin(admin: SystemAdministratorModel): Either<NetworkError, SystemAdministratorModel>

    // READ
    suspend fun getAdminById(id: Long): Either<NetworkError, SystemAdministratorModel?>
    suspend fun getAdminByEmail(email: String): Either<NetworkError, SystemAdministratorModel?>
    suspend fun getAllAdmins(active: Boolean? = null, permissionLevel: AdminPermissionLevel? = null, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>>
    suspend fun getAdminsByPermissionLevel(level: AdminPermissionLevel, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>>
    suspend fun searchAdmins(query: String, page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>>
    suspend fun getAdminsWithTwoFactorAuth(page: Int, pageSize: Int): Either<NetworkError, List<SystemAdministratorModel>>

    // UPDATE
    suspend fun updateAdmin(admin: SystemAdministratorModel): Either<NetworkError, SystemAdministratorModel>
    suspend fun updateAdminStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun updateAdminPermissionLevel(id: Long, permissionLevel: AdminPermissionLevel): Either<NetworkError, Boolean>
    suspend fun updateAdminPermissions(id: Long, canManageUsers: Boolean? = null, canManageContent: Boolean? = null, canManagePayments: Boolean? = null): Either<NetworkError, Boolean>
    suspend fun updateTwoFactorAuth(id: Long, enabled: Boolean): Either<NetworkError, Boolean>
    suspend fun incrementActionsPerformed(id: Long): Either<NetworkError, Boolean>
    suspend fun updateLastLogin(id: Long, ipAddress: String? = null): Either<NetworkError, Boolean>
    suspend fun resetFailedLoginAttempts(id: Long): Either<NetworkError, Boolean>
    suspend fun incrementFailedLoginAttempts(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteAdmin(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteAdmin(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countAdminsByPermissionLevel(): Either<NetworkError, Map<AdminPermissionLevel, Int>>
    suspend fun getTotalAdminsCount(): Either<NetworkError, Int>
    suspend fun getAdminActivityStats(days: Int = 30): Either<NetworkError, Map<String, Any>>
    suspend fun getMostActiveAdmins(limit: Int = 10): Either<NetworkError, List<SystemAdministratorModel>>
}

// ============================================
// INTERFACE: TeacherRepository
// ============================================
interface TeacherRepository {
    // CREATE
    suspend fun createTeacher(teacher: TeacherModel): Either<NetworkError, TeacherModel>

    // READ
    suspend fun getTeacherById(id: Long): Either<NetworkError, TeacherModel?>
    suspend fun getTeacherByNumber(teacherNumber: String): Either<NetworkError, TeacherModel?>
    suspend fun getTeacherByEmail(email: String): Either<NetworkError, TeacherModel?>
    suspend fun getAllTeachers(active: Boolean? = null, verified: Boolean? = null, type: TeacherType? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getTeachersBySchool(schoolId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getTeachersByType(type: TeacherType, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getVerifiedTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getPendingApprovalTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getFeaturedTeachers(page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun searchTeachers(query: String, schoolId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherModel>>
    suspend fun getTopRatedTeachers(limit: Int = 10): Either<NetworkError, List<TeacherModel>>

    // UPDATE
    suspend fun updateTeacher(teacher: TeacherModel): Either<NetworkError, TeacherModel>
    suspend fun updateTeacherStatus(id: Long, status: UserStatus): Either<NetworkError, Boolean>
    suspend fun verifyTeacher(id: Long, verifiedBy: Long, verifiedAt: String): Either<NetworkError, Boolean>
    suspend fun rejectTeacher(id: Long, rejectedBy: Long, reason: String? = null): Either<NetworkError, Boolean>
    suspend fun updateTeacherRating(id: Long, averageRating: Double, totalRatings: Int): Either<NetworkError, Boolean>
    suspend fun updateTeacherStatistics(id: Long): Either<NetworkError, Boolean>
    suspend fun updateMarketplaceStatus(id: Long, enabled: Boolean): Either<NetworkError, Boolean>
    suspend fun updateMarketplaceBalance(id: Long, balance: Double): Either<NetworkError, Boolean>
    suspend fun markAsFeatured(id: Long, featured: Boolean): Either<NetworkError, Boolean>
    suspend fun assignToSchool(teacherId: Long, schoolId: Long?): Either<NetworkError, Boolean>

    // DELETE
    suspend fun softDeleteTeacher(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteTeacher(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countTeachersBySchool(schoolId: Long): Either<NetworkError, Int>
    suspend fun countTeachersByType(): Either<NetworkError, Map<TeacherType, Int>>
    suspend fun countVerifiedTeachers(): Either<NetworkError, Int>
    suspend fun getTotalTeachersCount(schoolId: Long? = null): Either<NetworkError, Int>
    suspend fun getAverageTeacherRating(schoolId: Long? = null): Either<NetworkError, Double>
    suspend fun getTeacherPerformanceStatistics(teacherId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: TeacherAssignmentRepository
// ============================================
interface TeacherAssignmentRepository {
    // CREATE
    suspend fun createTeacherAssignment(assignment: TeacherAssignmentModel): Either<NetworkError, TeacherAssignmentModel>

    // READ
    suspend fun getTeacherAssignmentById(id: Long): Either<NetworkError, TeacherAssignmentModel?>
    suspend fun getAssignmentsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>>
    suspend fun getAssignmentsByClasse(classeId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>>
    suspend fun getAssignmentsBySubject(subjectId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>>
    suspend fun getHomeroomTeachers(classeId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>>
    suspend fun getActiveAssignmentsBySchoolYear(schoolYear: String, teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherAssignmentModel>>
    suspend fun getTeacherWorkload(teacherId: Long, schoolYear: String): Either<NetworkError, Map<String, Any>>

    // UPDATE
    suspend fun updateTeacherAssignment(assignment: TeacherAssignmentModel): Either<NetworkError, TeacherAssignmentModel>
    suspend fun updateAssignmentActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>
    suspend fun updateHomeroomStatus(id: Long, isHomeroomTeacher: Boolean): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteTeacherAssignment(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countAssignmentsByTeacher(teacherId: Long, schoolYear: String? = null): Either<NetworkError, Int>
    suspend fun countAssignmentsByClasse(classeId: Long, schoolYear: String? = null): Either<NetworkError, Int>
    suspend fun getTotalWeeklyHoursByTeacher(teacherId: Long, schoolYear: String): Either<NetworkError, Int>
    suspend fun getTeacherSubjectDistribution(teacherId: Long): Either<NetworkError, Map<Long, Int>>
}

// ============================================
// INTERFACE: TeacherCertificationRepository
// ============================================
interface TeacherCertificationRepository {
    // CREATE
    suspend fun createCertification(certification: TeacherCertificationModel): Either<NetworkError, TeacherCertificationModel>

    // READ
    suspend fun getCertificationById(id: Long): Either<NetworkError, TeacherCertificationModel?>
    suspend fun getCertificationsByTeacher(teacherId: Long, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>>
    suspend fun getCertificationsByStatus(status: VerificationStatus, teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>>
    suspend fun getVerifiedCertifications(teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>>
    suspend fun getExpiringCertifications(days: Int = 30, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>>
    suspend fun searchCertifications(query: String, teacherId: Long? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherCertificationModel>>

    // UPDATE
    suspend fun updateCertification(certification: TeacherCertificationModel): Either<NetworkError, TeacherCertificationModel>
    suspend fun verifyCertification(id: Long, verifiedBy: Long, verifiedAt: String): Either<NetworkError, Boolean>
    suspend fun rejectCertification(id: Long, rejectionReason: String? = null): Either<NetworkError, Boolean>
    suspend fun updateCertificationActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteCertification(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countCertificationsByTeacher(teacherId: Long): Either<NetworkError, Int>
    suspend fun countCertificationsByStatus(teacherId: Long? = null): Either<NetworkError, Map<VerificationStatus, Int>>
    suspend fun getExpiredCertificationsCount(): Either<NetworkError, Int>
}

// ============================================
// INTERFACE: TeacherSubjectRepository
// ============================================
interface TeacherSubjectRepository {
    // CREATE
    suspend fun createTeacherSubject(teacherSubject: TeacherSubjectModel): Either<NetworkError, TeacherSubjectModel>

    // READ
    suspend fun getTeacherSubjectById(id: Long): Either<NetworkError, TeacherSubjectModel?>
    suspend fun getTeacherSubjectsByTeacher(teacherId: Long, primary: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>>
    suspend fun getTeacherSubjectsBySubject(subjectId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>>
    suspend fun getPrimarySubjectsByTeacher(teacherId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>>
    suspend fun getTeachersBySubject(subjectId: Long, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>>
    suspend fun getSubjectsByTeacherWithExperience(teacherId: Long, minYears: Int = 0, page: Int, pageSize: Int): Either<NetworkError, List<TeacherSubjectModel>>

    // UPDATE
    suspend fun updateTeacherSubject(teacherSubject: TeacherSubjectModel): Either<NetworkError, TeacherSubjectModel>
    suspend fun updatePrimaryStatus(id: Long, isPrimary: Boolean): Either<NetworkError, Boolean>
    suspend fun updateSubjectRating(id: Long, rating: Double): Either<NetworkError, Boolean>
    suspend fun incrementLessonsInSubject(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteTeacherSubject(id: Long): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countSubjectsByTeacher(teacherId: Long): Either<NetworkError, Int>
    suspend fun countTeachersBySubject(subjectId: Long): Either<NetworkError, Int>
    suspend fun getAverageExperienceBySubject(subjectId: Long): Either<NetworkError, Double>
    suspend fun getTeacherSubjectExpertise(teacherId: Long): Either<NetworkError, Map<String, Any>>
}

// ============================================
// INTERFACE: TimetableRepository
// ============================================
interface TimetableRepository {
    // CREATE
    suspend fun createTimetable(timetable: TimetableModel): Either<NetworkError, TimetableModel>
    suspend fun createBulkTimetable(timetables: List<TimetableModel>): Either<NetworkError, List<TimetableModel>>

    // READ
    suspend fun getTimetableById(id: Long): Either<NetworkError, TimetableModel?>
    suspend fun getTimetableByClasse(classeId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>>
    suspend fun getTimetableByTeacher(teacherId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>>
    suspend fun getTimetableBySubject(subjectId: Long, active: Boolean? = null, schoolYear: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>>
    suspend fun getDailyTimetable(classeId: Long, dayOfWeek: Int, schoolYear: String): Either<NetworkError, List<TimetableModel>>
    suspend fun getWeeklyTimetable(classeId: Long, schoolYear: String): Either<NetworkError, Map<Int, List<TimetableModel>>>
    suspend fun getTimetableConflicts(teacherId: Long, dayOfWeek: Int, startTime: String, endTime: String, schoolYear: String): Either<NetworkError, List<TimetableModel>>
    suspend fun getRoomSchedule(room: String, dayOfWeek: Int? = null, schoolYear: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<TimetableModel>>

    // UPDATE
    suspend fun updateTimetable(timetable: TimetableModel): Either<NetworkError, TimetableModel>
    suspend fun updateTimetableActiveStatus(id: Long, isActive: Boolean): Either<NetworkError, Boolean>
    suspend fun updateTimetableSlot(id: Long, dayOfWeek: Int? = null, startTime: String? = null, endTime: String? = null): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteTimetable(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteTimetableByClasse(classeId: Long, schoolYear: String): Either<NetworkError, Boolean>

    // STATISTICS
    suspend fun countTimetableSlotsByTeacher(teacherId: Long, schoolYear: String): Either<NetworkError, Int>
    suspend fun countTimetableSlotsByClasse(classeId: Long, schoolYear: String): Either<NetworkError, Int>
    suspend fun getTeacherWeeklySchedule(teacherId: Long, schoolYear: String): Either<NetworkError, Map<Int, Int>>
    suspend fun getClassWeeklyHours(classeId: Long, schoolYear: String): Either<NetworkError, Int>
}

// ============================================
// INTERFACE: UserActivityLogRepository
// ============================================
interface UserActivityLogRepository {
    // CREATE
    suspend fun createActivityLog(log: UserActivityLogModel): Either<NetworkError, UserActivityLogModel>

    // READ
    suspend fun getActivityLogById(id: Long): Either<NetworkError, UserActivityLogModel?>
    suspend fun getActivityLogsByUser(userId: Long, userType: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>>
    suspend fun getActivityLogsByActivityType(activityType: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>>
    suspend fun getRecentActivityLogs(limit: Int = 100): Either<NetworkError, List<UserActivityLogModel>>
    suspend fun getActivityLogsByDateRange(startDate: String, endDate: String, userId: Long? = null, userType: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>>
    suspend fun getActivityLogsByIpAddress(ipAddress: String, page: Int, pageSize: Int): Either<NetworkError, List<UserActivityLogModel>>

    // DELETE
    suspend fun deleteActivityLog(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteOldActivityLogs(days: Int = 90): Either<NetworkError, Int>

    // STATISTICS
    suspend fun countActivityLogsByUser(userId: Long, userType: String, days: Int = 30): Either<NetworkError, Int>
    suspend fun countActivityLogsByActivityType(days: Int = 30): Either<NetworkError, Map<String, Int>>
    suspend fun getMostActiveUsers(userType: String, days: Int = 30, limit: Int = 10): Either<NetworkError, List<Map<String, Any>>>
    suspend fun getActivityFrequency(userId: Long, userType: String, days: Int = 30): Either<NetworkError, Map<String, Int>>
}

// ============================================
// INTERFACE: UserSessionRepository
// ============================================
interface UserSessionRepository {
    // CREATE
    suspend fun createSession(session: UserSessionModel): Either<NetworkError, UserSessionModel>

    // READ
    suspend fun getSessionById(id: Long): Either<NetworkError, UserSessionModel?>
    suspend fun getSessionByToken(token: String): Either<NetworkError, UserSessionModel?>
    suspend fun getSessionsByUser(userId: Long, userType: String, active: Boolean? = null, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>>
    suspend fun getActiveSessions(userId: Long, userType: String): Either<NetworkError, List<UserSessionModel>>
    suspend fun getSessionsByDevice(deviceId: String, userId: Long? = null, userType: String? = null, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>>
    suspend fun getSessionsByIpAddress(ipAddress: String, page: Int, pageSize: Int): Either<NetworkError, List<UserSessionModel>>
    suspend fun getRecentSessions(userId: Long, userType: String, limit: Int = 10): Either<NetworkError, List<UserSessionModel>>

    // UPDATE
    suspend fun updateSession(session: UserSessionModel): Either<NetworkError, UserSessionModel>
    suspend fun updateLastActivity(id: Long): Either<NetworkError, Boolean>
    suspend fun logoutSession(id: Long): Either<NetworkError, Boolean>
    suspend fun logoutAllSessions(userId: Long, userType: String): Either<NetworkError, Boolean>
    suspend fun expireSession(id: Long): Either<NetworkError, Boolean>

    // DELETE
    suspend fun deleteSession(id: Long): Either<NetworkError, Boolean>
    suspend fun deleteExpiredSessions(): Either<NetworkError, Int>

    // STATISTICS
    suspend fun countActiveSessions(userId: Long? = null, userType: String? = null): Either<NetworkError, Int>
    suspend fun countSessionsByDeviceType(userId: Long, userType: String): Either<NetworkError, Map<String, Int>>
    suspend fun getAverageSessionDuration(userId: Long, userType: String, days: Int = 30): Either<NetworkError, Double>
    suspend fun getSessionActivity(userId: Long, userType: String, days: Int = 30): Either<NetworkError, Map<String, Any>>
}