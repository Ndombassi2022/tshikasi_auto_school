package com.tshikasi.tshikasi_auto_school.domain.usecase.live_session

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.CreateLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionFilters
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionModel
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStatus
import com.tshikasi.tshikasi_auto_school.domain.model.StreamInfoResponse
import com.tshikasi.tshikasi_auto_school.domain.model.UpdateLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveSessionRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface CreateLiveSessionUseCase {
    suspend operator fun invoke(request: CreateLiveSessionRequest): Either<NetworkError, LiveSessionModel>
}

class CreateLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : CreateLiveSessionUseCase {
    override suspend fun invoke(request: CreateLiveSessionRequest): Either<NetworkError, LiveSessionModel> {
        return repository.createLiveSession(request)
    }
}

interface GetLiveSessionByIdUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel?>
}

class GetLiveSessionByIdUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveSessionByIdUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel?> {
        return repository.getLiveSessionById(sessionId)
    }
}

interface GetAllLiveSessionsUseCase {
    suspend operator fun invoke(
        filters: LiveSessionFilters? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): Either<NetworkError, List<LiveSessionModel>>
}

class GetAllLiveSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetAllLiveSessionsUseCase {
    override suspend fun invoke(
        filters: LiveSessionFilters?,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getAllLiveSessions(filters, page, pageSize)
    }
}

interface GetLiveSessionsByTeacherUseCase {
    suspend operator fun invoke(
        teacherId: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Either<NetworkError, List<LiveSessionModel>>
}

class GetLiveSessionsByTeacherUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveSessionsByTeacherUseCase {
    override suspend fun invoke(
        teacherId: String,
        page: Int,
        pageSize: Int
    ): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getLiveSessionsByTeacher(teacherId, page, pageSize)
    }
}

interface GetUpcomingLiveSessionsUseCase {
    suspend operator fun invoke(page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveSessionModel>>
}

class GetUpcomingLiveSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetUpcomingLiveSessionsUseCase {
    override suspend fun invoke(page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getUpcomingLiveSessions(page, pageSize)
    }
}

interface GetLiveNowSessionsUseCase {
    suspend operator fun invoke(): Either<NetworkError, List<LiveSessionModel>>
}

class GetLiveNowSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveNowSessionsUseCase {
    override suspend fun invoke(): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getLiveNowSessions()
    }
}

interface GetPastLiveSessionsUseCase {
    suspend operator fun invoke(page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveSessionModel>>
}

class GetPastLiveSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetPastLiveSessionsUseCase {
    override suspend fun invoke(page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getPastLiveSessions(page, pageSize)
    }
}

interface GetUserLiveSessionsUseCase {
    suspend operator fun invoke(userId: String, page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveSessionModel>>
}

class GetUserLiveSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetUserLiveSessionsUseCase {
    override suspend fun invoke(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getUserLiveSessions(userId, page, pageSize)
    }
}

interface GetLiveSessionsByStatusUseCase {
    suspend operator fun invoke(status: LiveStatus, page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveSessionModel>>
}

class GetLiveSessionsByStatusUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveSessionsByStatusUseCase {
    override suspend fun invoke(status: LiveStatus, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getLiveSessionsByStatus(status, page, pageSize)
    }
}

interface GetLiveSessionsBySubjectUseCase {
    suspend operator fun invoke(subject: String, page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveSessionModel>>
}

class GetLiveSessionsBySubjectUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveSessionsBySubjectUseCase {
    override suspend fun invoke(subject: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getLiveSessionsBySubject(subject, page, pageSize)
    }
}

interface GetRecentLiveSessionsUseCase {
    suspend operator fun invoke(limit: Int = 10): Either<NetworkError, List<LiveSessionModel>>
}

class GetRecentLiveSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetRecentLiveSessionsUseCase {
    override suspend fun invoke(limit: Int): Either<NetworkError, List<LiveSessionModel>> {
        return repository.getRecentLiveSessions(limit)
    }
}

interface UpdateLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String, request: UpdateLiveSessionRequest): Either<NetworkError, LiveSessionModel>
}

class UpdateLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : UpdateLiveSessionUseCase {
    override suspend fun invoke(sessionId: String, request: UpdateLiveSessionRequest): Either<NetworkError, LiveSessionModel> {
        return repository.updateLiveSession(sessionId, request)
    }
}

interface StartLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel>
}

class StartLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : StartLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return repository.startLiveSession(sessionId)
    }
}

interface PauseLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel>
}

class PauseLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : PauseLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return repository.pauseLiveSession(sessionId)
    }
}

interface ResumeLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel>
}

class ResumeLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : ResumeLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return repository.resumeLiveSession(sessionId)
    }
}

interface EndLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel>
}

class EndLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : EndLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return repository.endLiveSession(sessionId)
    }
}

interface CancelLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel>
}

class CancelLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : CancelLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveSessionModel> {
        return repository.cancelLiveSession(sessionId)
    }
}

interface UpdateLiveMetricsUseCase {
    suspend operator fun invoke(sessionId: String, participantCount: Int, viewCount: Int): Either<NetworkError, Boolean>
}

class UpdateLiveMetricsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : UpdateLiveMetricsUseCase {
    override suspend fun invoke(sessionId: String, participantCount: Int, viewCount: Int): Either<NetworkError, Boolean> {
        return repository.updateLiveMetrics(sessionId, participantCount, viewCount)
    }
}

interface IncrementParticipantCountUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Boolean>
}

class IncrementParticipantCountUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : IncrementParticipantCountUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Boolean> {
        return repository.incrementParticipantCount(sessionId)
    }
}

interface DecrementParticipantCountUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Boolean>
}

class DecrementParticipantCountUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : DecrementParticipantCountUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Boolean> {
        return repository.decrementParticipantCount(sessionId)
    }
}

interface DeleteLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Boolean>
}

class DeleteLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : DeleteLiveSessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Boolean> {
        return repository.deleteLiveSession(sessionId)
    }
}

interface DeleteCancelledSessionsUseCase {
    suspend operator fun invoke(olderThanDays: Int = 30): Either<NetworkError, Int>
}

class DeleteCancelledSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : DeleteCancelledSessionsUseCase {
    override suspend fun invoke(olderThanDays: Int): Either<NetworkError, Int> {
        return repository.deleteCancelledSessions(olderThanDays)
    }
}


interface GetStreamInfoUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, StreamInfoResponse>
}

class GetStreamInfoUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetStreamInfoUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, StreamInfoResponse> {
        return repository.getStreamInfo(sessionId)
    }
}

interface GenerateStreamKeyUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, String>
}

class GenerateStreamKeyUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GenerateStreamKeyUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, String> {
        return repository.generateStreamKey(sessionId)
    }
}

interface CountLivesByStatusUseCase {
    suspend operator fun invoke(status: LiveStatus): Either<NetworkError, Int>
}

class CountLivesByStatusUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : CountLivesByStatusUseCase {
    override suspend fun invoke(status: LiveStatus): Either<NetworkError, Int> {
        return repository.countLivesByStatus(status)
    }
}

interface CountTeacherLivesUseCase {
    suspend operator fun invoke(teacherId: String, status: LiveStatus? = null): Either<NetworkError, Int>
}

class CountTeacherLivesUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : CountTeacherLivesUseCase {
    override suspend fun invoke(teacherId: String, status: LiveStatus?): Either<NetworkError, Int> {
        return repository.countTeacherLives(teacherId, status)
    }
}

interface GetLiveStatisticsUseCase {
    suspend operator fun invoke(teacherId: String? = null, days: Int = 30): Either<NetworkError, Map<String, Any>>
}

class GetLiveStatisticsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : GetLiveStatisticsUseCase {
    override suspend fun invoke(teacherId: String?, days: Int): Either<NetworkError, Map<String, Any>> {
        return repository.getLiveStatistics(teacherId, days)
    }
}

interface ObserveLiveSessionUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, LiveSessionModel>>
}

class ObserveLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : ObserveLiveSessionUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, LiveSessionModel>> {
        return repository.observeLiveSession(sessionId)
    }
}

interface ObserveLiveNowSessionsUseCase {
    operator fun invoke(): Flow<Either<NetworkError, List<LiveSessionModel>>>
}

class ObserveLiveNowSessionsUseCaseImpl @Inject constructor(
    private val repository: LiveSessionRepository
) : ObserveLiveNowSessionsUseCase {
    override fun invoke(): Flow<Either<NetworkError, List<LiveSessionModel>>> {
        return repository.observeLiveNowSessions()
    }
}
class UseCases {
}