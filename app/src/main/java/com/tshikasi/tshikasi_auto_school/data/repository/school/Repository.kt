package com.tshikasi.tshikasi_auto_school.data.repository.school

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.data.mapper.toNetworkError
import com.tshikasi.tshikasi_auto_school.domain.datasource.LiveSessionDataSource
import com.tshikasi.tshikasi_auto_school.domain.datasource.school.SchoolDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.SchoolModel
import com.tshikasi.tshikasi_auto_school.domain.model.UserStatus
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveSessionRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.SchoolRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError


class SchoolRepositoryImpl(
    private val dataSource: SchoolDataSource
) : SchoolRepository {
    override suspend fun createSchool(school: SchoolModel): Either<NetworkError, SchoolModel> {
        return Either.catch {
            dataSource.createSchool(school = school)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolById(id: Long): Either<NetworkError, SchoolModel?> {
        return Either.catch {
            dataSource.getSchoolById(id = id)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolByCode(code: String): Either<NetworkError, SchoolModel?> {
        return Either.catch {
            dataSource.getSchoolByCode(code = code)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getAllSchools(
        active: Boolean?,
        schoolTypeId: Long?,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<SchoolModel>> {
        return Either.catch {
            dataSource.getAllSchools(active = active, schoolTypeId = schoolTypeId, communeId = communeId, page = page, pageSize = pageSize)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolsByCommune(
        communeId: Long,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<SchoolModel>> {
        return Either.catch {
            dataSource.getAllSchools( communeId = communeId, page = page, pageSize = pageSize)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolsByType(
        schoolTypeId: Long,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<SchoolModel>> {
        return Either.catch {
            dataSource.getAllSchools(schoolTypeId = schoolTypeId, page = page, pageSize = pageSize)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun searchSchools(
        query: String,
        communeId: Long?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<SchoolModel>> {
        return Either.catch {
            dataSource.searchSchools(query=query, communeId = communeId, page = page, pageSize = pageSize)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTopSchoolsByStudents(limit: Int): Either<NetworkError, List<SchoolModel>> {
        return Either.catch {
            dataSource.getTopSchoolsByStudents(limit = limit)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSchool(school: SchoolModel): Either<NetworkError, SchoolModel> {
        return Either.catch {
            dataSource.updateSchool(school = school)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSchoolStatus(
        id: Long,
        status: UserStatus
    ): Either<NetworkError, Boolean> {
        return Either.catch {
            dataSource.updateSchoolStatus(id=id, status = status)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSchoolStatistics(id: Long): Either<NetworkError, Boolean> {
        return Either.catch {
            dataSource.updateSchoolStatistics(id = id)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateSubscription(
        id: Long,
        plan: String?,
        expiresAt: String?
    ): Either<NetworkError, Boolean> {
        return Either.catch {
            dataSource.updateSubscription(id = id, plan = plan, expiresAt = expiresAt)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteSchool(id: Long): Either<NetworkError, Boolean> {
        return Either.catch {
            dataSource.deleteSchool(id = id)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSchoolsByCommune(communeId: Long): Either<NetworkError, Int> {
        return Either.catch {
            dataSource.countSchoolsByCommune(communeId = communeId)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun countSchoolsByType(schoolType:String): Either<NetworkError, Map<Long, Int>> {
        return Either.catch {
            dataSource.countSchoolsByType(schoolType = schoolType)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getTotalSchoolsCount(): Either<NetworkError, Int> {
        return Either.catch {
            dataSource.getTotalSchoolsCount()
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getSchoolPerformanceMetrics(schoolId: Long): Either<NetworkError, Map<String, Any>> {
        return Either.catch {
            dataSource.getSchoolPerformanceMetrics(schoolId = schoolId)
        }.mapLeft { it.toNetworkError() }
    }
}
