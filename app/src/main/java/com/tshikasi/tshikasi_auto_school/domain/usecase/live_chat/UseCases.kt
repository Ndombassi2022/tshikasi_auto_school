package com.tshikasi.tshikasi_auto_school.domain.usecase.live_chat

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.ChatMessageType
import com.tshikasi.tshikasi_auto_school.domain.model.LiveChatMessageModel
import com.tshikasi.tshikasi_auto_school.domain.model.SendChatMessageRequest
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveChatRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// ==================== CREATE ====================
interface SendMessageUseCase {
    suspend operator fun invoke(request: SendChatMessageRequest): Either<NetworkError, LiveChatMessageModel>
}

class SendMessageUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : SendMessageUseCase {
    override suspend fun invoke(request: SendChatMessageRequest): Either<NetworkError, LiveChatMessageModel> {
        return repository.sendMessage(request)
    }
}

// ==================== READ ====================
interface GetMessagesUseCase {
    suspend operator fun invoke(sessionId: String, limit: Int = 100, offset: Int = 0): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetMessagesUseCase {
    override suspend fun invoke(sessionId: String, limit: Int, offset: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getMessages(sessionId, limit, offset)
    }
}

interface GetRecentMessagesUseCase {
    suspend operator fun invoke(sessionId: String, limit: Int = 50): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetRecentMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetRecentMessagesUseCase {
    override suspend fun invoke(sessionId: String, limit: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getRecentMessages(sessionId, limit)
    }
}

interface GetMessagesByTypeUseCase {
    suspend operator fun invoke(sessionId: String, messageType: ChatMessageType, limit: Int = 100): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetMessagesByTypeUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetMessagesByTypeUseCase {
    override suspend fun invoke(sessionId: String, messageType: ChatMessageType, limit: Int): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getMessagesByType(sessionId, messageType, limit)
    }
}

interface GetUnansweredQuestionsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetUnansweredQuestionsUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetUnansweredQuestionsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getUnansweredQuestions(sessionId)
    }
}

interface GetUserMessagesUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetUserMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetUserMessagesUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getUserMessages(sessionId, userId)
    }
}

interface GetPendingMessagesUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>>
}

class GetPendingMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : GetPendingMessagesUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LiveChatMessageModel>> {
        return repository.getPendingMessages(sessionId)
    }
}

// ==================== UPDATE ====================
interface ApproveMessageUseCase {
    suspend operator fun invoke(messageId: String): Either<NetworkError, LiveChatMessageModel>
}

class ApproveMessageUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : ApproveMessageUseCase {
    override suspend fun invoke(messageId: String): Either<NetworkError, LiveChatMessageModel> {
        return repository.approveMessage(messageId)
    }
}

interface PinMessageUseCase {
    suspend operator fun invoke(messageId: String): Either<NetworkError, LiveChatMessageModel>
}

class PinMessageUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : PinMessageUseCase {
    override suspend fun invoke(messageId: String): Either<NetworkError, LiveChatMessageModel> {
        return repository.pinMessage(messageId)
    }
}

interface UnpinMessageUseCase {
    suspend operator fun invoke(messageId: String): Either<NetworkError, Boolean>
}

class UnpinMessageUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : UnpinMessageUseCase {
    override suspend fun invoke(messageId: String): Either<NetworkError, Boolean> {
        return repository.unpinMessage(messageId)
    }
}

// ==================== DELETE ====================
interface DeleteMessageUseCase {
    suspend operator fun invoke(messageId: String): Either<NetworkError, Boolean>
}

class DeleteMessageUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : DeleteMessageUseCase {
    override suspend fun invoke(messageId: String): Either<NetworkError, Boolean> {
        return repository.deleteMessage(messageId)
    }
}

interface DeleteUserMessagesUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, Int>
}

class DeleteUserMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : DeleteUserMessagesUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, Int> {
        return repository.deleteUserMessages(sessionId, userId)
    }
}

interface ClearOldMessagesUseCase {
    suspend operator fun invoke(sessionId: String, olderThanMinutes: Int = 60): Either<NetworkError, Int>
}

class ClearOldMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : ClearOldMessagesUseCase {
    override suspend fun invoke(sessionId: String, olderThanMinutes: Int): Either<NetworkError, Int> {
        return repository.clearOldMessages(sessionId, olderThanMinutes)
    }
}

// ==================== STATISTICS ====================
interface CountMessagesUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Int>
}

class CountMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : CountMessagesUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Int> {
        return repository.countMessages(sessionId)
    }
}

interface CountUserMessagesUseCase {
    suspend operator fun invoke(sessionId: String, userId: String): Either<NetworkError, Int>
}

class CountUserMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : CountUserMessagesUseCase {
    override suspend fun invoke(sessionId: String, userId: String): Either<NetworkError, Int> {
        return repository.countUserMessages(sessionId, userId)
    }
}

interface CountMessagesByTypeUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Map<ChatMessageType, Int>>
}

class CountMessagesByTypeUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : CountMessagesByTypeUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Map<ChatMessageType, Int>> {
        return repository.countMessagesByType(sessionId)
    }
}

// ==================== REALTIME ====================
interface ObserveMessagesUseCase {
    operator fun invoke(sessionId: String): Flow<Either<NetworkError, LiveChatMessageModel>>
}

class ObserveMessagesUseCaseImpl @Inject constructor(
    private val repository: LiveChatRepository
) : ObserveMessagesUseCase {
    override fun invoke(sessionId: String): Flow<Either<NetworkError, LiveChatMessageModel>> {
        return repository.observeMessages(sessionId)
    }
}

class UseCases {
}