package com.tshikasi.tshikasi_auto_school.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tshikasi.tshikasi_auto_school.domain.model.*
import com.tshikasi.tshikasi_auto_school.domain.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LiveStreamViewModel(
    private val liveSessionRepository: LiveSessionRepository,
    private val liveParticipantRepository: LiveParticipantRepository,
    private val liveChatRepository: LiveChatRepository,
    private val liveReactionRepository: LiveReactionRepository,
    private val livePollRepository: LivePollRepository,
    private val liveStatsRepository: LiveStatsRepository
) : ViewModel() {

    // ==================== STATES ====================

    // Lista de lives
    private val _upcomingLives = MutableStateFlow<List<LiveSessionModel>>(emptyList())
    val upcomingLives: StateFlow<List<LiveSessionModel>> = _upcomingLives.asStateFlow()

    private val _liveNowSessions = MutableStateFlow<List<LiveSessionModel>>(emptyList())
    val liveNowSessions: StateFlow<List<LiveSessionModel>> = _liveNowSessions.asStateFlow()

    private val _pastLives = MutableStateFlow<List<LiveSessionModel>>(emptyList())
    val pastLives: StateFlow<List<LiveSessionModel>> = _pastLives.asStateFlow()

    // Live atual
    private val _currentLive = MutableStateFlow<LiveSessionModel?>(null)
    val currentLive: StateFlow<LiveSessionModel?> = _currentLive.asStateFlow()

    // Participantes
    private val _participants = MutableStateFlow<List<LiveParticipantModel>>(emptyList())
    val participants: StateFlow<List<LiveParticipantModel>> = _participants.asStateFlow()

    private val _onlineCount = MutableStateFlow(0)
    val onlineCount: StateFlow<Int> = _onlineCount.asStateFlow()

    // Chat
    private val _chatMessages = MutableStateFlow<List<LiveChatMessageModel>>(emptyList())
    val chatMessages: StateFlow<List<LiveChatMessageModel>> = _chatMessages.asStateFlow()

    // Polls
    private val _activePolls = MutableStateFlow<List<LivePollModel>>(emptyList())
    val activePolls: StateFlow<List<LivePollModel>> = _activePolls.asStateFlow()

    // Stats
    private val _liveStats = MutableStateFlow<LiveStreamStats?>(null)
    val liveStats: StateFlow<LiveStreamStats?> = _liveStats.asStateFlow()

    // Loading & Error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ==================== LIVE SESSIONS ====================

    /**
     * Carregar lives agendadas
     */
    fun loadUpcomingLives() {
        viewModelScope.launch {
            _isLoading.value = true
            liveSessionRepository.getUpcomingLiveSessions()
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar lives agendadas: ${error.error}"
                    },
                    ifRight = { lives ->
                        _upcomingLives.value = lives
                    }
                )
            _isLoading.value = false
        }
    }

    /**
     * Carregar lives ao vivo agora
     */
    fun loadLiveNowSessions() {
        viewModelScope.launch {
            liveSessionRepository.getLiveNowSessions()
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar lives ao vivo: ${error.error}"
                    },
                    ifRight = { lives ->
                        _liveNowSessions.value = lives
                    }
                )
        }
    }

    /**
     * Carregar lives passadas
     */
    fun loadPastLives() {
        viewModelScope.launch {
            liveSessionRepository.getPastLiveSessions()
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar lives passadas: ${error.error}"
                    },
                    ifRight = { lives ->
                        _pastLives.value = lives
                    }
                )
        }
    }

    /**
     * Carregar live específica
     */
    fun loadLiveSession(sessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            liveSessionRepository.getLiveSessionById(sessionId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar live: ${error.error}"
                    },
                    ifRight = { live ->
                        _currentLive.value = live
                        if (live != null) {
                            // Carregar dados relacionados
                            loadParticipants(sessionId)
                            loadChatMessages(sessionId)
                            loadLiveStats(sessionId)
                            observeLiveRealtime(sessionId)
                        }
                    }
                )
            _isLoading.value = false
        }
    }

    /**
     * Criar nova live
     */
    fun createLiveSession(request: CreateLiveSessionRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            liveSessionRepository.createLiveSession(request)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao criar live: ${error.error}"
                    },
                    ifRight = { live ->
                        _currentLive.value = live
                        // Recarregar lista de lives agendadas
                        loadUpcomingLives()
                    }
                )
            _isLoading.value = false
        }
    }

    /**
     * Iniciar live
     */
    fun startLiveSession(sessionId: String) {
        viewModelScope.launch {
            liveSessionRepository.startLiveSession(sessionId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao iniciar live: ${error.error}"
                    },
                    ifRight = { live ->
                        _currentLive.value = live
                        loadLiveNowSessions()
                    }
                )
        }
    }

    /**
     * Finalizar live
     */
    fun endLiveSession(sessionId: String) {
        viewModelScope.launch {
            liveSessionRepository.endLiveSession(sessionId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao finalizar live: ${error.error}"
                    },
                    ifRight = { live ->
                        _currentLive.value = live
                        loadLiveNowSessions()
                        loadPastLives()
                    }
                )
        }
    }

    // ==================== PARTICIPANTS ====================

    /**
     * Entrar na live
     */
    fun joinLiveSession(sessionId: String, role: ParticipantRole = ParticipantRole.VIEWER) {
        viewModelScope.launch {
            val request = JoinLiveSessionRequest(
                liveSessionId = sessionId,
                role = role
            )

            liveParticipantRepository.joinLiveSession(request)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao entrar na live: ${error.error}"
                    },
                    ifRight = {
                        // Incrementar contador de participantes
                        liveSessionRepository.incrementParticipantCount(sessionId)
                        loadParticipants(sessionId)
                    }
                )
        }
    }

    /**
     * Sair da live
     */
    fun leaveLiveSession(sessionId: String, userId: String) {
        viewModelScope.launch {
            liveParticipantRepository.leaveLiveSession(sessionId, userId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao sair da live: ${error.error}"
                    },
                    ifRight = {
                        liveSessionRepository.decrementParticipantCount(sessionId)
                        loadParticipants(sessionId)
                    }
                )
        }
    }

    /**
     * Carregar participantes
     */
    private fun loadParticipants(sessionId: String) {
        viewModelScope.launch {
            liveParticipantRepository.getParticipants(sessionId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar participantes: ${error.error}"
                    },
                    ifRight = { participants ->
                        _participants.value = participants
                        _onlineCount.value = participants.count { it.leftAt == null }
                    }
                )
        }
    }

    // ==================== CHAT ====================

    /**
     * Enviar mensagem no chat
     */
    fun sendChatMessage(sessionId: String, message: String, type: ChatMessageType = ChatMessageType.TEXT) {
        viewModelScope.launch {
            val request = SendChatMessageRequest(
                liveSessionId = sessionId,
                message = message,
                messageType = type
            )

            liveChatRepository.sendMessage(request)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao enviar mensagem: ${error.error}"
                    },
                    ifRight = { newMessage ->
                        // Mensagem será recebida via Realtime
                        // Incrementar contador de mensagens
                        liveStatsRepository.incrementMessageCount(sessionId)
                    }
                )
        }
    }

    /**
     * Carregar mensagens do chat
     */
    private fun loadChatMessages(sessionId: String) {
        viewModelScope.launch {
            liveChatRepository.getRecentMessages(sessionId, limit = 100)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar mensagens: ${error.error}"
                    },
                    ifRight = { messages ->
                        _chatMessages.value = messages
                    }
                )
        }
    }

    /**
     * Deletar mensagem
     */
    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            liveChatRepository.deleteMessage(messageId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao deletar mensagem: ${error.error}"
                    },
                    ifRight = {
                        // Mensagem será removida via Realtime ou atualizar local
                        _chatMessages.value = _chatMessages.value.filter { it.id != messageId }
                    }
                )
        }
    }

    // ==================== REACTIONS ====================

    /**
     * Enviar reação
     */
    fun sendReaction(sessionId: String, userId: String, emoji: String) {
        viewModelScope.launch {
            liveReactionRepository.sendReaction(sessionId, userId, emoji)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao enviar reação: ${error.error}"
                    },
                    ifRight = {
                        liveStatsRepository.incrementReactionCount(sessionId)
                    }
                )
        }
    }

    // ==================== POLLS ====================

    /**
     * Criar enquete
     */
    fun createPoll(sessionId: String, question: String, options: List<String>) {
        viewModelScope.launch {
            livePollRepository.createPoll(sessionId, question, options)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao criar enquete: ${error.error}"
                    },
                    ifRight = { poll ->
                        _activePolls.value = _activePolls.value + poll
                    }
                )
        }
    }

    /**
     * Votar em enquete
     */
    fun votePoll(pollId: String, optionId: String, userId: String) {
        viewModelScope.launch {
            livePollRepository.votePoll(pollId, optionId, userId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao votar: ${error.error}"
                    },
                    ifRight = {
                        // Resultados atualizados via Realtime
                    }
                )
        }
    }

    // ==================== STATISTICS ====================

    /**
     * Carregar estatísticas da live
     */
    private fun loadLiveStats(sessionId: String) {
        viewModelScope.launch {
            liveStatsRepository.getLiveStats(sessionId)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao carregar estatísticas: ${error.error}"
                    },
                    ifRight = { stats ->
                        _liveStats.value = stats
                    }
                )
        }
    }

    /**
     * Registrar visualização
     */
    fun recordView(sessionId: String, userId: String) {
        viewModelScope.launch {
            liveStatsRepository.recordView(sessionId, userId)
        }
    }

    // ==================== REALTIME ====================

    /**
     * Observar atualizações da live em tempo real
     */
    private fun observeLiveRealtime(sessionId: String) {
        // Observar live session
        viewModelScope.launch {
            liveSessionRepository.observeLiveSession(sessionId)
                .collect { result ->
                    result.fold(
                        ifLeft = { /* Ignorar erros */ },
                        ifRight = { live ->
                            _currentLive.value = live
                        }
                    )
                }
        }

        // Observar participantes
        viewModelScope.launch {
            liveParticipantRepository.observeParticipants(sessionId)
                .collect { result ->
                    result.fold(
                        ifLeft = { /* Ignorar erros */ },
                        ifRight = { participants ->
                            _participants.value = participants
                            _onlineCount.value = participants.count { it.leftAt == null }
                        }
                    )
                }
        }

        // Observar mensagens do chat
        viewModelScope.launch {
            liveChatRepository.observeMessages(sessionId)
                .collect { result ->
                    result.fold(
                        ifLeft = { /* Ignorar erros */ },
                        ifRight = { newMessage ->
                            _chatMessages.value = _chatMessages.value + newMessage
                        }
                    )
                }
        }

        // Observar estatísticas
        viewModelScope.launch {
            liveStatsRepository.observeLiveStats(sessionId)
                .collect { result ->
                    result.fold(
                        ifLeft = { /* Ignorar erros */ },
                        ifRight = { stats ->
                            _liveStats.value = stats
                        }
                    )
                }
        }
    }

    // ==================== UTILITIES ====================

    /**
     * Limpar erro
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Pesquisar lives
     */
    fun searchLives(query: String) {
        viewModelScope.launch {
            val filters = LiveSessionFilters(
                searchQuery = query
            )

            liveSessionRepository.getAllLiveSessions(filters)
                .fold(
                    ifLeft = { error ->
                        _error.value = "Erro ao pesquisar: ${error.error}"
                    },
                    ifRight = { lives ->
                        // Atualizar listas conforme status
                        _upcomingLives.value = lives.filter { it.status == LiveStatus.SCHEDULED }
                        _liveNowSessions.value = lives.filter { it.status == LiveStatus.LIVE }
                        _pastLives.value = lives.filter { it.status == LiveStatus.ENDED }
                    }
                )
        }
    }

    /**
     * Atualizar pico de espectadores
     */
    fun updatePeakViewers(sessionId: String, viewers: Int) {
        viewModelScope.launch {
            liveStatsRepository.updatePeakViewers(sessionId, viewers)
        }
    }

    init {
        // Carregar lives ao iniciar
        loadUpcomingLives()
        loadLiveNowSessions()
    }
}