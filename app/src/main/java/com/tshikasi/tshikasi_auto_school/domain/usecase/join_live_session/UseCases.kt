package com.tshikasi.tshikasi_auto_school.domain.usecase.join_live_session

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.JoinLiveSessionRequest
import com.tshikasi.tshikasi_auto_school.domain.model.LiveParticipantModel
import com.tshikasi.tshikasi_auto_school.domain.model.ParticipantRole
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveParticipantRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// ==================== CREATE ====================
interface JoinLiveSessionUseCase {
    suspend operator fun invoke(request: JoinLiveSessionRequest): Either<NetworkError, LiveParticipantModel>
}

class JoinLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : JoinLiveSessionUseCase {
    override suspend fun invoke(request: JoinLiveSessionRequest): Either<NetworkError, LiveParticipantModel> {
        return repository.joinLiveSession(request)
    }
}

// ==================== READ ====================
interface GetParticipantsUseCase {
    suspend operator fun invoke(sessionId: String, page: Int = 1, pageSize: Int = 100): Either<NetworkError, List<LiveParticipantModel>>
}

class GetParticipantsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetParticipantsUseCase {
    override suspend fun invoke(sessionId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveParticipantModel>> {
        return repository.getParticipants(sessionId, page, pageSize)
    }
}

interface GetParticipantUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, LiveParticipantModel?>
}

class GetParticipantUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetParticipantUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, LiveParticipantModel?> {
        return repository.getParticipant(sessionId, userId)
    }
}

interface GetParticipantsByRoleUseCase {
    suspend operator fun invoke(sessionId: String, role: ParticipantRole): Either<NetworkError, List<LiveParticipantModel>>
}

class GetParticipantsByRoleUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetParticipantsByRoleUseCase {
    override suspend fun invoke(sessionId: String, role: ParticipantRole): Either<NetworkError, List<LiveParticipantModel>> {
        return repository.getParticipantsByRole(sessionId, role)
    }
}

interface GetOnlineParticipantsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LiveParticipantModel>>
}

class GetOnlineParticipantsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetOnlineParticipantsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LiveParticipantModel>> {
        return repository.getOnlineParticipants(sessionId)
    }
}

interface GetPendingApprovalsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LiveParticipantModel>>
}

class GetPendingApprovalsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetPendingApprovalsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LiveParticipantModel>> {
        return repository.getPendingApprovals(sessionId)
    }
}

interface GetUserParticipationHistoryUseCase {
    suspend operator fun invoke(userId: String, page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveParticipantModel>>
}

class GetUserParticipationHistoryUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : GetUserParticipationHistoryUseCase {
    override suspend fun invoke(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveParticipantModel>> {
        return repository.getUserParticipationHistory(userId, page, pageSize)
    }
}

// ==================== UPDATE ====================
interface LeaveLiveSessionUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean>
}

class LeaveLiveSessionUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : LeaveLiveSessionUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return repository.leaveLiveSession(sessionId, userId)
    }
}

interface UpdateParticipantRoleUseCase {
    suspend operator fun invoke(participantId: String, newRole: ParticipantRole): Either<NetworkError, LiveParticipantModel>
}

class UpdateParticipantRoleUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : UpdateParticipantRoleUseCase {
    override suspend fun invoke(participantId: String, newRole: ParticipantRole): Either<NetworkError, LiveParticipantModel> {
        return repository.updateParticipantRole(participantId, newRole)
    }
}

interface ApproveParticipantUseCase {
    suspend operator fun invoke(participantId: String): Either<NetworkError, LiveParticipantModel>
}

class ApproveParticipantUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : ApproveParticipantUseCase {
    override suspend fun invoke(participantId: String): Either<NetworkError, LiveParticipantModel> {
        return repository.approveParticipant(participantId)
    }
}

interface RejectParticipantUseCase {
    suspend operator fun invoke(participantId: String): Either<NetworkError, Boolean>
}

class RejectParticipantUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : RejectParticipantUseCase {
    override suspend fun invoke(participantId: String): Either<NetworkError, Boolean> {
        return repository.rejectParticipant(participantId)
    }
}

// ==================== DELETE ====================
interface RemoveParticipantUseCase {
    suspend operator fun invoke(participantId: String): Either<NetworkError, Boolean>
}

class RemoveParticipantUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : RemoveParticipantUseCase {
    override suspend fun invoke(participantId: String): Either<NetworkError, Boolean> {
        return repository.removeParticipant(participantId)
    }
}

interface CleanupOfflineParticipantsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Int>
}

class CleanupOfflineParticipantsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : CleanupOfflineParticipantsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Int> {
        return repository.cleanupOfflineParticipants(sessionId)
    }
}

// ==================== STATISTICS ====================
interface CountParticipantsUseCase {
    suspend operator fun invoke(sessionId: String, onlineOnly: Boolean = false): Either<NetworkError, Int>
}

class CountParticipantsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : CountParticipantsUseCase {
    override suspend fun invoke(sessionId: String, onlineOnly: Boolean): Either<NetworkError, Int> {
        return repository.countParticipants(sessionId, onlineOnly)
    }
}

interface CountUserParticipationsUseCase {
    suspend operator fun invoke(userId: String): Either<NetworkError, Int>
}

class CountUserParticipationsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : CountUserParticipationsUseCase {
    override suspend fun invoke(userId: String): Either<NetworkError, Int> {
        return repository.countUserParticipations(userId)
    }
}

interface IsUserInLiveUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean>
}

class IsUserInLiveUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : IsUserInLiveUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, Boolean> {
        return repository.isUserInLive(sessionId, userId)
    }
}

// ==================== REALTIME ====================
interface ObserveParticipantsUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, List<LiveParticipantModel>>>
}

class ObserveParticipantsUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : ObserveParticipantsUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, List<LiveParticipantModel>>> {
        return repository.observeParticipants(sessionId)
    }
}

interface ObserveOnlineCountUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, Int>>
}

class ObserveOnlineCountUseCaseImpl @Inject constructor(
    private val repository: LiveParticipantRepository
) : ObserveOnlineCountUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, Int>> {
        return repository.observeOnlineCount(sessionId)
    }
}
class UseCases {
}