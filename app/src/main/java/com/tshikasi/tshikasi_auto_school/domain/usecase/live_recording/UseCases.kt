package com.tshikasi.tshikasi_auto_school.domain.usecase.live_recording

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.LiveRecordingModel
import com.tshikasi.tshikasi_auto_school.domain.repository.LiveRecordingRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

interface StartRecordingUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, LiveRecordingModel>
}

class StartRecordingUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : StartRecordingUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, LiveRecordingModel> {
        return repository.startRecording(sessionId)
    }
}

interface GetRecordingByIdUseCase {
    suspend operator fun invoke(recordingId: String): Either<NetworkError, LiveRecordingModel?>
}

class GetRecordingByIdUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : GetRecordingByIdUseCase {
    override suspend fun invoke(recordingId: String): Either<NetworkError, LiveRecordingModel?> {
        return repository.getRecordingById(recordingId)
    }
}

interface GetRecordingsBySessionUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LiveRecordingModel>>
}

class GetRecordingsBySessionUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : GetRecordingsBySessionUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LiveRecordingModel>> {
        return repository.getRecordingsBySession(sessionId)
    }
}

interface StopRecordingUseCase {
    suspend operator fun invoke(recordingId: String): Either<NetworkError, LiveRecordingModel>
}

class StopRecordingUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : StopRecordingUseCase {
    override suspend fun invoke(recordingId: String): Either<NetworkError, LiveRecordingModel> {
        return repository.stopRecording(recordingId)
    }
}

interface DeleteRecordingUseCase {
    suspend operator fun invoke(recordingId: String): Either<NetworkError, Boolean>
}

class DeleteRecordingUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : DeleteRecordingUseCase {
    override suspend fun invoke(recordingId: String): Either<NetworkError, Boolean> {
        return repository.deleteRecording(recordingId)
    }
}

interface CountTeacherRecordingsUseCase {
    suspend operator fun invoke(teacherId: String): Either<NetworkError, Int>
}

class CountTeacherRecordingsUseCaseImpl @Inject constructor(
    private val repository: LiveRecordingRepository
) : CountTeacherRecordingsUseCase {
    override suspend fun invoke(teacherId: String): Either<NetworkError, Int> {
        return repository.countTeacherRecordings(teacherId)
    }
}
class UseCases {
}