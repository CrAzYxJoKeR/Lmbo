package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.coach.CoachDiagnosticEngine
import com.example.data.coach.DiagnosticReport
import com.example.data.local.AppDatabase
import com.example.data.local.SettingsManager
import com.example.data.model.MissedTrade
import com.example.data.model.PreTradeState
import com.example.data.model.SessionReview
import com.example.data.model.Trade
import com.example.data.model.TradingSession
import com.example.data.repository.GatekeeperRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GatekeeperViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val settingsManager = SettingsManager(application)
    val repository = GatekeeperRepository(db, settingsManager)

    val activeSession: StateFlow<TradingSession?> = repository.activeSession
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTrades: StateFlow<List<Trade>> = repository.allTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val last50Trades: StateFlow<List<Trade>> = repository.last50Trades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMissedTrades: StateFlow<List<MissedTrade>> = repository.allMissedTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviews: StateFlow<List<SessionReview>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Pre-Trade Gatekeeper state
    private val _preTradeState = MutableStateFlow(PreTradeState())
    val preTradeState: StateFlow<PreTradeState> = _preTradeState.asStateFlow()

    // 3-Second Hold / Verification countdown state
    private val _approvalCountdown = MutableStateFlow(0) // 3 -> 2 -> 1 -> 0
    val approvalCountdown: StateFlow<Int> = _approvalCountdown.asStateFlow()

    private val _isTradeApproved = MutableStateFlow(false)
    val isTradeApproved: StateFlow<Boolean> = _isTradeApproved.asStateFlow()

    // Active trade in execution (waiting for outcome resolution)
    private val _activePendingTrade = MutableStateFlow<Trade?>(null)
    val activePendingTrade: StateFlow<Trade?> = _activePendingTrade.asStateFlow()

    // Cooldown live ticking remaining seconds
    private val _cooldownSecondsRemaining = MutableStateFlow(0L)
    val cooldownSecondsRemaining: StateFlow<Long> = _cooldownSecondsRemaining.asStateFlow()

    // Same asset revenge check result
    private val _sameAssetRevengeBlocked = MutableStateFlow(false)
    val sameAssetRevengeBlocked: StateFlow<Boolean> = _sameAssetRevengeBlocked.asStateFlow()

    private val _sameAssetRemainingSeconds = MutableStateFlow(0L)
    val sameAssetRemainingSeconds: StateFlow<Long> = _sameAssetRemainingSeconds.asStateFlow()

    // Active Diagnostic Report
    private val _diagnosticReport = MutableStateFlow<DiagnosticReport?>(null)
    val diagnosticReport: StateFlow<DiagnosticReport?> = _diagnosticReport.asStateFlow()

    private var countdownJob: Job? = null
    private var tickerJob: Job? = null

    init {
        startCooldownTicker()
    }

    private fun startCooldownTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (isActive) {
                val session = activeSession.value
                if (session != null && session.isCooldownActive()) {
                    _cooldownSecondsRemaining.value = session.remainingCooldownSeconds()
                } else {
                    _cooldownSecondsRemaining.value = 0L
                }

                // Check revenge lockout on currently selected asset
                val currentAsset = _preTradeState.value.asset
                val (blocked, remaining) = repository.checkSameAssetRevenge(currentAsset)
                _sameAssetRevengeBlocked.value = blocked
                _sameAssetRemainingSeconds.value = remaining

                delay(1000)
            }
        }
    }

    fun startNewSession(startingBalance: Double, profitTarget: Double, strictCooldown: Boolean) {
        viewModelScope.launch {
            repository.startNewSession(startingBalance, profitTarget, strictCooldown)
            resetPreTradeChecklist()
        }
    }

    fun updateChecklist(update: (PreTradeState) -> PreTradeState) {
        _preTradeState.value = update(_preTradeState.value)
    }

    fun setAsset(asset: String) {
        _preTradeState.value = _preTradeState.value.copy(asset = asset)
        viewModelScope.launch {
            val (blocked, remaining) = repository.checkSameAssetRevenge(asset)
            _sameAssetRevengeBlocked.value = blocked
            _sameAssetRemainingSeconds.value = remaining
        }
    }

    fun setMarketType(type: String) {
        _preTradeState.value = _preTradeState.value.copy(marketType = type)
    }

    fun setDirection(dir: String) {
        _preTradeState.value = _preTradeState.value.copy(direction = dir)
    }

    fun setPayoutPct(pct: Double) {
        _preTradeState.value = _preTradeState.value.copy(payoutPct = pct)
    }

    fun setExplanation(text: String) {
        _preTradeState.value = _preTradeState.value.copy(oneSentenceExplanation = text)
    }

    fun setInvalidation(text: String) {
        _preTradeState.value = _preTradeState.value.copy(invalidationArgument = text)
    }

    fun startApprovalCountdown() {
        countdownJob?.cancel()
        _approvalCountdown.value = 3
        countdownJob = viewModelScope.launch {
            delay(1000)
            _approvalCountdown.value = 2
            delay(1000)
            _approvalCountdown.value = 1
            delay(1000)
            _approvalCountdown.value = 0
            _isTradeApproved.value = true

            // Form pending trade for active execution
            val session = activeSession.value
            val currentStake = session?.stakeAmount ?: 20.0
            val state = _preTradeState.value

            val pending = Trade(
                sessionId = session?.id ?: 0L,
                timestamp = System.currentTimeMillis(),
                asset = state.asset,
                marketType = state.marketType,
                direction = state.direction,
                payoutPct = state.payoutPct,
                breakEvenWinRate = state.breakEvenWinRate,
                stakeAmount = currentStake,
                oneSentenceExplanation = state.oneSentenceExplanation,
                invalidationArgument = state.invalidationArgument,
                checklistAllPassed = state.allConditionsPass,
                outcome = "PENDING",
                profitOrLoss = 0.0,
                emotionalStateTag = "Focused",
                chartScreenshotUrl = "",
                lessonLearned = "",
                disciplineRespected = true
            )
            _activePendingTrade.value = pending
        }
    }

    fun cancelApprovalCountdown() {
        countdownJob?.cancel()
        _approvalCountdown.value = 0
    }

    fun logTradeOutcome(
        outcome: String, // "WIN", "CLEAN_LOSS", "NEAR_MISS_LOSS"
        emotionalStateTag: String,
        screenshotUrl: String,
        lesson: String
    ) {
        val session = activeSession.value ?: return
        val pending = _activePendingTrade.value ?: return

        val profit = when (outcome) {
            "WIN" -> pending.stakeAmount * (pending.payoutPct / 100.0)
            else -> -pending.stakeAmount
        }

        val completedTrade = pending.copy(
            outcome = outcome,
            profitOrLoss = profit,
            emotionalStateTag = emotionalStateTag,
            chartScreenshotUrl = screenshotUrl,
            lessonLearned = lesson,
            disciplineRespected = true
        )

        viewModelScope.launch {
            repository.logTrade(session, completedTrade)
            _activePendingTrade.value = null
            _isTradeApproved.value = false
            resetPreTradeChecklist()
        }
    }

    fun logNoTradeDecision() {
        val session = activeSession.value ?: return
        viewModelScope.launch {
            repository.logNoTradeDecision(session)
            resetPreTradeChecklist()
        }
    }

    fun logMissedTrade(asset: String, direction: String, reason: String, reflection: String) {
        viewModelScope.launch {
            repository.logMissedTrade(asset, direction, reason, reflection)
        }
    }

    fun submitSessionReview(
        followedChecklist: Boolean,
        respectedLimits: Boolean,
        emotionDroveDecision: Boolean,
        emotionNotes: String,
        setupQualityRating: Int,
        keyLesson: String
    ) {
        val session = activeSession.value
        val review = SessionReview(
            sessionId = session?.id ?: 0L,
            timestamp = System.currentTimeMillis(),
            followedChecklist = followedChecklist,
            respectedLimits = respectedLimits,
            emotionDroveDecision = emotionDroveDecision,
            emotionNotes = emotionNotes,
            setupQualityRating = setupQualityRating,
            keyLesson = keyLesson
        )
        viewModelScope.launch {
            repository.submitReview(review, session)
        }
    }

    fun resetActiveSession() {
        val session = activeSession.value ?: return
        viewModelScope.launch {
            repository.resetSession(session)
            _activePendingTrade.value = null
            _isTradeApproved.value = false
            resetPreTradeChecklist()
        }
    }

    fun resetPreTradeChecklist() {
        _preTradeState.value = PreTradeState(
            asset = _preTradeState.value.asset,
            payoutPct = _preTradeState.value.payoutPct
        )
        _approvalCountdown.value = 0
        _isTradeApproved.value = false
    }

    fun runDiagnostic() {
        val trades = allTrades.value
        _diagnosticReport.value = CoachDiagnosticEngine.analyzeTrades(trades)
    }

    fun clearDiagnostic() {
        _diagnosticReport.value = null
    }

    fun exportJournalJson(): String {
        val trades = allTrades.value
        val sb = StringBuilder("[\n")
        trades.forEachIndexed { i, t ->
            sb.append("  {\n")
            sb.append("    \"id\": ${t.id},\n")
            sb.append("    \"timestamp\": ${t.timestamp},\n")
            sb.append("    \"asset\": \"${t.asset}\",\n")
            sb.append("    \"marketType\": \"${t.marketType}\",\n")
            sb.append("    \"direction\": \"${t.direction}\",\n")
            sb.append("    \"payoutPct\": ${t.payoutPct},\n")
            sb.append("    \"stakeAmount\": ${t.stakeAmount},\n")
            sb.append("    \"outcome\": \"${t.outcome}\",\n")
            sb.append("    \"profitOrLoss\": ${t.profitOrLoss},\n")
            sb.append("    \"emotionalStateTag\": \"${t.emotionalStateTag}\",\n")
            sb.append("    \"oneSentenceExplanation\": \"${t.oneSentenceExplanation.replace("\"", "\\\"")}\",\n")
            sb.append("    \"lessonLearned\": \"${t.lessonLearned.replace("\"", "\\\"")}\",\n")
            sb.append("    \"disciplineRespected\": ${t.disciplineRespected}\n")
            sb.append("  }${if (i < trades.size - 1) "," else ""}\n")
        }
        sb.append("]")
        return sb.toString()
    }

    fun exportJournalCsv(): String {
        val trades = allTrades.value
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val sb = StringBuilder("Date,Asset,Type,Direction,Payout%,Stake,Outcome,Profit/Loss,Emotion,Discipline,Explanation,Lesson\n")
        trades.forEach { t ->
            val dateStr = sdf.format(Date(t.timestamp))
            sb.append("\"$dateStr\",")
            sb.append("\"${t.asset}\",")
            sb.append("\"${t.marketType}\",")
            sb.append("\"${t.direction}\",")
            sb.append("${t.payoutPct},")
            sb.append("${t.stakeAmount},")
            sb.append("\"${t.outcome}\",")
            sb.append("${t.profitOrLoss},")
            sb.append("\"${t.emotionalStateTag}\",")
            sb.append("${t.disciplineRespected},")
            sb.append("\"${t.oneSentenceExplanation.replace("\"", "\"\"")}\",")
            sb.append("\"${t.lessonLearned.replace("\"", "\"\"")}\"\n")
        }
        return sb.toString()
    }

    fun deleteTrade(tradeId: Long) {
        viewModelScope.launch {
            repository.deleteTrade(tradeId)
        }
    }
}

