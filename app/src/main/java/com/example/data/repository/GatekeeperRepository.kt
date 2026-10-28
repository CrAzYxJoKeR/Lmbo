package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SettingsManager
import com.example.data.model.MissedTrade
import com.example.data.model.SessionReview
import com.example.data.model.Trade
import com.example.data.model.TradingSession
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class GatekeeperRepository(
    private val db: AppDatabase,
    private val settingsManager: SettingsManager
) {
    val activeSession: Flow<TradingSession?> = db.sessionDao().getActiveSession()
    val allTrades: Flow<List<Trade>> = db.tradeDao().getAllTrades()
    val last50Trades: Flow<List<Trade>> = db.tradeDao().getLast50Trades()
    val allMissedTrades: Flow<List<MissedTrade>> = db.missedTradeDao().getAllMissedTrades()
    val allReviews: Flow<List<SessionReview>> = db.reviewDao().getAllReviews()

    fun isTodayNonTradingDay(): Boolean = settingsManager.isTodayNonTradingDay()

    fun getNonTradingDays(): Set<Int> = settingsManager.getNonTradingDays()

    fun setNonTradingDays(days: Set<Int>) = settingsManager.setNonTradingDays(days)

    suspend fun startNewSession(
        startingBalance: Double,
        profitTarget: Double,
        strictCooldown: Boolean
    ): Long {
        val stakeAmount = startingBalance * 0.02 // Strict 2% fixed stake
        val isNonTrading = settingsManager.isTodayNonTradingDay()

        val session = TradingSession(
            dateMillis = System.currentTimeMillis(),
            startingBalance = startingBalance,
            stakeAmount = stakeAmount,
            profitTarget = profitTarget,
            currentProfit = 0.0,
            totalTrades = 0,
            losses = 0,
            consecutiveLosses = 0,
            winStreak = 0,
            isLocked = isNonTrading,
            lockReason = if (isNonTrading) "NON_TRADING_DAY" else "",
            cooldownUntilMillis = 0L,
            cooldownType = "NONE",
            strictCooldown = strictCooldown,
            isCompleted = false,
            reviewCompleted = false
        )
        return db.sessionDao().insertSession(session)
    }

    suspend fun checkSameAssetRevenge(asset: String): Pair<Boolean, Long> {
        val latest = db.tradeDao().getLatestTradeForAsset(asset) ?: return Pair(false, 0L)
        val now = System.currentTimeMillis()
        val elapsed = now - latest.timestamp
        val isLoss = latest.outcome == "CLEAN_LOSS" || latest.outcome == "NEAR_MISS_LOSS"
        val isWithin5Min = elapsed < 5 * 60 * 1000 // 5 minutes
        return if (isLoss && isWithin5Min) {
            val remainingSec = ((5 * 60 * 1000 - elapsed) + 999) / 1000
            Pair(true, remainingSec)
        } else {
            Pair(false, 0L)
        }
    }

    suspend fun logTrade(
        currentSession: TradingSession,
        trade: Trade
    ) {
        // Save trade to database
        db.tradeDao().insertTrade(trade)

        val newTotalTrades = currentSession.totalTrades + 1
        var newLosses = currentSession.losses
        var newConsecutiveLosses = currentSession.consecutiveLosses
        var newWinStreak = currentSession.winStreak
        var newProfit = currentSession.currentProfit + trade.profitOrLoss
        var isLocked = currentSession.isLocked
        var lockReason = currentSession.lockReason
        var cooldownUntil = 0L
        var cooldownType = "NONE"

        val now = System.currentTimeMillis()

        if (trade.outcome == "WIN") {
            newWinStreak += 1
            newConsecutiveLosses = 0

            // Check Win-Streak Euphoria Pause: every 3 consecutive wins
            if (newWinStreak >= 3 && newWinStreak % 3 == 0) {
                cooldownUntil = now + (5 * 60 * 1000) // 5 minutes pause
                cooldownType = "WIN_STREAK_EUPHORIA"
            }

            // Check Mid-Session Break: at 3 total trades
            if (newTotalTrades == 3 && cooldownUntil == 0L) {
                cooldownUntil = now + (15 * 60 * 1000) // 15 minutes break
                cooldownType = "MID_SESSION_BREAK"
            }

            // Check Profit Target hit
            if (newProfit >= currentSession.profitTarget) {
                isLocked = true
                lockReason = "PROFIT_TARGET"
            }
        } else if (trade.outcome == "CLEAN_LOSS" || trade.outcome == "NEAR_MISS_LOSS") {
            newLosses += 1
            newConsecutiveLosses += 1
            newWinStreak = 0

            if (newLosses >= 2 || newConsecutiveLosses >= 2) {
                // HARD STOP LOCKOUT SCREEN (2 losses ends session unconditionally)
                isLocked = true
                lockReason = "MAX_LOSSES"
            } else {
                // 1st Loss: 5-minute expiry revenge cooldown (or 15 min if strict mode)
                val durationMs = if (currentSession.strictCooldown) 15 * 60 * 1000L else 5 * 60 * 1000L
                cooldownUntil = now + durationMs
                cooldownType = "LOSS_REVENGE"
            }
        }

        // Check Max Trades limit (5 trades max)
        if (newTotalTrades >= 5 && !isLocked) {
            isLocked = true
            lockReason = "MAX_TRADES"
        }

        val updatedSession = currentSession.copy(
            totalTrades = newTotalTrades,
            losses = newLosses,
            consecutiveLosses = newConsecutiveLosses,
            winStreak = newWinStreak,
            currentProfit = newProfit,
            isLocked = isLocked,
            lockReason = lockReason,
            cooldownUntilMillis = cooldownUntil,
            cooldownType = cooldownType
        )
        db.sessionDao().updateSession(updatedSession)
    }

    suspend fun logNoTradeDecision(currentSession: TradingSession) {
        val trade = Trade(
            sessionId = currentSession.id,
            timestamp = System.currentTimeMillis(),
            asset = "NO-TRADE",
            marketType = "Standard",
            direction = "NONE",
            payoutPct = 0.0,
            breakEvenWinRate = 0.0,
            stakeAmount = 0.0,
            oneSentenceExplanation = "Disciplined decision: market conditions did not present an A+ setup conforming to rules.",
            invalidationArgument = "Market was choppy/low volume or lack of clear confluence. Sitting out was the optimal choice.",
            checklistAllPassed = true,
            outcome = "NO_TRADE",
            profitOrLoss = 0.0,
            emotionalStateTag = "Disciplined",
            lessonLearned = "The no-trade decision is a valid decision. Capital preserved is edge retained.",
            disciplineRespected = true
        )
        db.tradeDao().insertTrade(trade)

        val updated = currentSession.copy(
            isLocked = true,
            lockReason = "NO_TRADE_DECISION",
            isCompleted = true
        )
        db.sessionDao().updateSession(updated)
    }

    suspend fun logMissedTrade(asset: String, direction: String, reason: String, reflection: String) {
        val missed = MissedTrade(
            timestamp = System.currentTimeMillis(),
            asset = asset,
            direction = direction,
            setupReason = "Missed entry trigger",
            reasonMissed = reason,
            reflection = reflection
        )
        db.missedTradeDao().insertMissedTrade(missed)
    }

    suspend fun submitReview(review: SessionReview, currentSession: TradingSession?) {
        db.reviewDao().insertReview(review)
        currentSession?.let {
            val updated = it.copy(
                isCompleted = true,
                reviewCompleted = true
            )
            db.sessionDao().updateSession(updated)
        }
    }

    suspend fun resetSession(session: TradingSession) {
        val updated = session.copy(isCompleted = true)
        db.sessionDao().updateSession(updated)
    }

    suspend fun clearHistory() {
        db.tradeDao().clearAllTrades()
    }

    suspend fun deleteTrade(id: Long) {
        db.tradeDao().deleteTrade(id)
    }
}

