package com.tshikasi.tshikasi_auto_school.domain.usecase.live_notification

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.LiveNotificationModel
import com.tshikasi.tshikasi_auto_school.domain.model.NotificationType
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveNotificationRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject


interface CreateNotificationUseCase {
    suspend operator fun invoke(sessionId: String, userId: String, type: NotificationType): Either<NetworkError, LiveNotificationModel>
}

class CreateNotificationUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : CreateNotificationUseCase {
    override suspend fun invoke(sessionId: String, userId: String, type: NotificationType): Either<NetworkError, LiveNotificationModel> {
        return repository.createNotification(sessionId, userId, type)
    }
}

interface NotifyLiveStartingSoonUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Int>
}

class NotifyLiveStartingSoonUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : NotifyLiveStartingSoonUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Int> {
        return repository.notifyLiveStartingSoon(sessionId)
    }
}

interface NotifyLiveStartedUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, Int>
}

class NotifyLiveStartedUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : NotifyLiveStartedUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, Int> {
        return repository.notifyLiveStarted(sessionId)
    }
}

interface NotifyRecordingAvailableUseCase {
    suspend operator fun invoke(sessionId: String, recordingId: String): Either<NetworkError, Int>
}

class NotifyRecordingAvailableUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : NotifyRecordingAvailableUseCase {
    override suspend fun invoke(sessionId: String, recordingId: String): Either<NetworkError, Int> {
        return repository.notifyRecordingAvailable(sessionId, recordingId)
    }
}

interface GetUserNotificationsUseCase {
    suspend operator fun invoke(userId: String, page: Int = 1, pageSize: Int = 20): Either<NetworkError, List<LiveNotificationModel>>
}

class GetUserNotificationsUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : GetUserNotificationsUseCase {
    override suspend fun invoke(userId: String, page: Int, pageSize: Int): Either<NetworkError, List<LiveNotificationModel>> {
        return repository.getUserNotifications(userId, page, pageSize)
    }
}

interface MarkAsSentUseCase {
    suspend operator fun invoke(notificationId: String): Either<NetworkError, LiveNotificationModel>
}

class MarkAsSentUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : MarkAsSentUseCase {
    override suspend fun invoke(notificationId: String): Either<NetworkError, LiveNotificationModel> {
        return repository.markAsSent(notificationId)
    }
}

interface DeleteNotificationUseCase {
    suspend operator fun invoke(notificationId: String): Either<NetworkError, Boolean>
}

class DeleteNotificationUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : DeleteNotificationUseCase {
    override suspend fun invoke(notificationId: String): Either<NetworkError, Boolean> {
        return repository.deleteNotification(notificationId)
    }
}

interface ClearOldNotificationsUseCase {
    suspend operator fun invoke(olderThanDays: Int = 30): Either<NetworkError, Int>
}

class ClearOldNotificationsUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : ClearOldNotificationsUseCase {
    override suspend fun invoke(olderThanDays: Int): Either<NetworkError, Int> {
        return repository.clearOldNotifications(olderThanDays)
    }
}

interface CountPendingNotificationsUseCase {
    suspend operator fun invoke(userId: String? = null): Either<NetworkError, Int>
}

class CountPendingNotificationsUseCaseImpl @Inject constructor(
    private val repository: LiveNotificationRepository
) : CountPendingNotificationsUseCase {
    override suspend fun invoke(userId: String?): Either<NetworkError, Int> {
        return repository.countPendingNotifications(userId)
    }
}
class UseCases {
}