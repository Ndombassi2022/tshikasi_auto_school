package com.tshikasi.tshikasi_auto_school.domain.datasource.school

import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserStatus

// ============================================
// INTERFACE: SchoolDataSource
// ============================================
interface SchoolDataSource {
    // CREATE
    suspend fun createSchool(school: SchoolModel): SchoolModel

    // READ
    suspend fun getSchoolById(id: Long): SchoolModel?
    suspend fun getSchoolByCode(code: String): SchoolModel?
    suspend fun getAllSchools(active: Boolean? = null, schoolTypeId: Long? = null, communeId: Long? = null, page: Int, pageSize: Int): List<SchoolModel>
    suspend fun getSchoolsByCommune(communeId: Long, page: Int, pageSize: Int): List<SchoolModel>
    suspend fun getSchoolsByType(schoolTypeId: Long, page: Int, pageSize: Int): List<SchoolModel>
    suspend fun searchSchools(query: String, communeId: Long? = null, page: Int, pageSize: Int): List<SchoolModel>
    suspend fun getTopSchoolsByStudents(limit: Int = 10): List<SchoolModel>

    // UPDATE
    suspend fun updateSchool(school: SchoolModel): SchoolModel
    suspend fun updateSchoolStatus(id: Long, status: UserStatus): Boolean
    suspend fun updateSchoolStatistics(id: Long): Boolean
    suspend fun updateSubscription(id: Long, plan: String? = null, expiresAt: String? = null): Boolean

    // DELETE
    suspend fun deleteSchool(id: Long): Boolean

    // STATISTICS
    suspend fun countSchoolsByCommune(communeId: Long): Int
    suspend fun countSchoolsByType(schoolType:String): Map<Long, Int>
    suspend fun getTotalSchoolsCount(): Int
    suspend fun getSchoolPerformanceMetrics(schoolId: Long): Map<String, Any>
}