package com.tshikasi.tshikasi_auto_school.domain.usecase.live_stats

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.LiveStreamStats
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveStatsRepository
import com.tshikasi.tshikasi_auto_school.domain.repository.TeacherLiveStats
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// CREATE/UPDATE
interface CreateLiveStatsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveStreamStats>
}

class CreateLiveStatsUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : CreateLiveStatsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveStreamStats> {
        return repository.createLiveStats(sessionId)
    }
}

interface UpdateLiveStatsUseCase {
    suspend operator fun invoke(stats: LiveStreamStats): Either<NetworkError, LiveStreamStats>
}

class UpdateLiveStatsUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : UpdateLiveStatsUseCase {
    override suspend fun invoke(stats: LiveStreamStats): Either<NetworkError, LiveStreamStats> {
        return repository.updateLiveStats(stats)
    }
}

interface RecordViewUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean>
}

class RecordViewUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : RecordViewUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return repository.recordView(sessionId, userId)
    }
}

interface RecordWatchTimeUseCase {
    suspend operator fun invoke(sessionId: String, userId: String, seconds: Int): Either<NetworkError, Boolean>
}

class RecordWatchTimeUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : RecordWatchTimeUseCase {
    override suspend fun invoke(sessionId: String, userId: String, seconds: Int): Either<NetworkError, Boolean> {
        return repository.recordWatchTime(sessionId, userId, seconds)
    }
}

// ... (continua com incrementMessageCount, incrementReactionCount, updatePeakViewers de forma idêntica)

// READ
interface GetLiveStatsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveStreamStats?>
}

class GetLiveStatsUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : GetLiveStatsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveStreamStats?> {
        return repository.getLiveStats(sessionId)
    }
}

interface GetTeacherStatsUseCase {
    suspend operator fun invoke(teacherId: String): Either<NetworkError, TeacherLiveStats>
}

class GetTeacherStatsUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : GetTeacherStatsUseCase {
    override suspend fun invoke(teacherId: String): Either<NetworkError, TeacherLiveStats> {
        return repository.getTeacherStats(teacherId)
    }
}

interface GetStatsByPeriodUseCase {
    suspend operator fun invoke(teacherId: String, startDate: String, endDate: String): Either<NetworkError, Map<String, Any>>
}

class GetStatsByPeriodUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : GetStatsByPeriodUseCase {
    override suspend fun invoke(teacherId: String, startDate: String, endDate: String): Either<NetworkError, Map<String, Any>> {
        return repository.getStatsByPeriod(teacherId, startDate, endDate)
    }
}

// STATISTICS
interface CalculateAverageViewersUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Double>
}

class CalculateAverageViewersUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : CalculateAverageViewersUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Double> {
        return repository.calculateAverageViewers(sessionId)
    }
}

interface CalculateRetentionRateUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Double>
}

class CalculateRetentionRateUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : CalculateRetentionRateUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Double> {
        return repository.calculateRetentionRate(sessionId)
    }
}

interface CalculateEngagementRateUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Double>
}

class CalculateEngagementRateUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : CalculateEngagementRateUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Double> {
        return repository.calculateEngagementRate(sessionId)
    }
}

// REALTIME
interface ObserveLiveStatsUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, LiveStreamStats>>
}

class ObserveLiveStatsUseCaseImpl @Inject constructor(
    private val repository: LiveStatsRepository
) : ObserveLiveStatsUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, LiveStreamStats>> {
        return repository.observeLiveStats(sessionId)
    }
}
class UseCases {
}