package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trading_sessions")
data class TradingSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateMillis: Long = System.currentTimeMillis(),
    val startingBalance: Double = 1000.0,
    val stakeAmount: Double = 20.0, // Fixed 2% calculated at start
    val profitTarget: Double = 60.0,
    val currentProfit: Double = 0.0,
    val totalTrades: Int = 0,
    val losses: Int = 0,
    val consecutiveLosses: Int = 0,
    val winStreak: Int = 0,
    val isLocked: Boolean = false,
    val lockReason: String = "", // "MAX_LOSSES", "MAX_TRADES", "PROFIT_TARGET", "NON_TRADING_DAY", "COMPLETED", "NO_TRADE_DECISION"
    val cooldownUntilMillis: Long = 0L,
    val cooldownType: String = "NONE", // "NONE", "LOSS_REVENGE", "WIN_STREAK_EUPHORIA", "MID_SESSION_BREAK"
    val strictCooldown: Boolean = false,
    val isCompleted: Boolean = false,
    val reviewCompleted: Boolean = false
) {
    val maxTrades: Int get() = 5
    val maxLosses: Int get() = 2

    fun isCooldownActive(): Boolean {
        return cooldownUntilMillis > System.currentTimeMillis()
    }

    fun remainingCooldownSeconds(): Long {
        val diff = cooldownUntilMillis - System.currentTimeMillis()
        return if (diff > 0) (diff + 999) / 1000 else 0
    }
}
