package com.tshikasi.tshikasi_auto_school.domain.usecase.live_poll

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.LivePollModel
import com.tshikasi.tshikasi_auto_school.domain.model.PollVoteModel
import com.tshikasi.tshikasi_auto_school.domain.repository.LivePollRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// CREATE
interface CreatePollUseCase {
    suspend operator fun invoke(sessionId: String, question: String, options: List<String>, durationSeconds: Int? = null): Either<NetworkError, LivePollModel>
}

class CreatePollUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : CreatePollUseCase {
    override suspend fun invoke(sessionId: String, question: String, options: List<String>, durationSeconds: Int?): Either<NetworkError, LivePollModel> {
        return repository.createPoll(sessionId, question, options, durationSeconds)
    }
}

interface VotePollUseCase {
    suspend operator fun invoke(pollId: String, optionId: String, userId: String): Either<NetworkError, PollVoteModel>
}

class VotePollUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : VotePollUseCase {
    override suspend fun invoke(pollId: String, optionId: String, userId: String): Either<NetworkError, PollVoteModel> {
        return repository.votePoll(pollId, optionId, userId)
    }
}

// READ
interface GetPollsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LivePollModel>>
}

class GetPollsUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : GetPollsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LivePollModel>> {
        return repository.getPolls(sessionId)
    }
}

interface GetPollByIdUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, LivePollModel?>
}

class GetPollByIdUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : GetPollByIdUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, LivePollModel?> {
        return repository.getPollById(pollId)
    }
}

interface GetActivePollsUseCase {
    suspend operator fun invoke(sessionId: String): Either<NetworkError, List<LivePollModel>>
}

class GetActivePollsUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : GetActivePollsUseCase {
    override suspend fun invoke(sessionId: String): Either<NetworkError, List<LivePollModel>> {
        return repository.getActivePolls(sessionId)
    }
}

interface GetPollVotesUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, List<PollVoteModel>>
}

class GetPollVotesUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : GetPollVotesUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, List<PollVoteModel>> {
        return repository.getPollVotes(pollId)
    }
}

interface HasUserVotedUseCase {
    suspend operator fun invoke(pollId: String, userId: String): Either<NetworkError, Boolean>
}

class HasUserVotedUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : HasUserVotedUseCase {
    override suspend fun invoke(pollId: String, userId: String): Either<NetworkError, Boolean> {
        return repository.hasUserVoted(pollId, userId)
    }
}

// UPDATE
interface ClosePollUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, LivePollModel>
}

class ClosePollUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : ClosePollUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, LivePollModel> {
        return repository.closePoll(pollId)
    }
}

// DELETE
interface DeletePollUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, Boolean>
}

class DeletePollUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : DeletePollUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, Boolean> {
        return repository.deletePoll(pollId)
    }
}

// STATISTICS
interface CountPollVotesUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, Int>
}

class CountPollVotesUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : CountPollVotesUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, Int> {
        return repository.countPollVotes(pollId)
    }
}

interface GetPollResultsUseCase {
    suspend operator fun invoke(pollId: String): Either<NetworkError, Map<String, Int>>
}

class GetPollResultsUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : GetPollResultsUseCase {
    override suspend fun invoke(pollId: String): Either<NetworkError, Map<String, Int>> {
        return repository.getPollResults(pollId)
    }
}

// REALTIME
interface ObservePollResultsUseCase {
    operator fun invoke(pollId: String): Flow<Either<NetworkError, LivePollModel>>
}

class ObservePollResultsUseCaseImpl @Inject constructor(
    private val repository: LivePollRepository
) : ObservePollResultsUseCase {
    override fun invoke(pollId: String): Flow<Either<NetworkError, LivePollModel>> {
        return repository.observePollResults(pollId)
    }
}


class UseCases {
}