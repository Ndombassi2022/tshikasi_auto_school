package com.tshikasi.tshikasi_auto_school.domain.usecase.send_reaction

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.LiveReactionModel
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveReactionRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// ==================== CREATE ====================
interface SendReactionUseCase {
    suspend operator fun invoke(sessionId: String, userId: String, emoji: String): Either<NetworkError, LiveReactionModel>
}

class SendReactionUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : SendReactionUseCase {
    override suspend fun invoke(sessionId: String, userId: String, emoji: String): Either<NetworkError, LiveReactionModel> {
        return repository.sendReaction(sessionId, userId, emoji)
    }
}

// ==================== READ ====================
interface GetReactionsUseCase {
    suspend operator fun invoke(sessionId: String, limit: Int = 50): Either<NetworkError, List<LiveReactionModel>>
}

class GetReactionsUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : GetReactionsUseCase {
    override suspend fun invoke(sessionId: String, limit: Int): Either<NetworkError, List<LiveReactionModel>> {
        return repository.getReactions(sessionId, limit)
    }
}

interface GetRecentReactionsUseCase {
    suspend operator fun invoke(sessionId: String, seconds: Int = 5): Either<NetworkError, List<LiveReactionModel>>
}

class GetRecentReactionsUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : GetRecentReactionsUseCase {
    override suspend fun invoke(sessionId: String, seconds: Int): Either<NetworkError, List<LiveReactionModel>> {
        return repository.getRecentReactions(sessionId, seconds)
    }
}

// ==================== DELETE ====================
interface ClearOldReactionsUseCase {
    suspend operator fun invoke(sessionId: String, olderThanSeconds: Int = 5): Either<NetworkError, Int>
}

class ClearOldReactionsUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : ClearOldReactionsUseCase {
    override suspend fun invoke(sessionId: String, olderThanSeconds: Int): Either<NetworkError, Int> {
        return repository.clearOldReactions(sessionId, olderThanSeconds)
    }
}

// ==================== STATISTICS ====================
interface CountReactionsByTypeUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Map<String, Int>>
}

class CountReactionsByTypeUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : CountReactionsByTypeUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Map<String, Int>> {
        return repository.countReactionsByType(sessionId)
    }
}

interface CountTotalReactionsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Int>
}

class CountTotalReactionsUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : CountTotalReactionsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Int> {
        return repository.countTotalReactions(sessionId)
    }
}

// ==================== REALTIME ====================
interface ObserveReactionsUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, LiveReactionModel>>
}

class ObserveReactionsUseCaseImpl @Inject constructor(
    private val repository: LiveReactionRepository
) : ObserveReactionsUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, LiveReactionModel>> {
        return repository.observeReactions(sessionId)
    }
}
class UseCases {
}