package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class Trade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val asset: String = "EUR/USD",
    val marketType: String = "Standard", // "Standard", "OTC"
    val direction: String = "CALL", // "CALL" or "PUT"
    val payoutPct: Double = 85.0,
    val breakEvenWinRate: Double = 54.05,
    val stakeAmount: Double = 20.0,
    val oneSentenceExplanation: String = "",
    val invalidationArgument: String = "",
    val checklistAllPassed: Boolean = true,
    val outcome: String = "WIN", // "WIN", "CLEAN_LOSS", "NEAR_MISS_LOSS", "NO_TRADE"
    val profitOrLoss: Double = 17.0, // e.g. +17.0 for win at 85% with $20 stake, or -$20 for loss
    val emotionalStateTag: String = "Focused", // e.g., "Calm", "Focused", "Hesitant", "FOMO", "Urgent"
    val chartScreenshotUrl: String = "",
    val lessonLearned: String = "",
    val disciplineRespected: Boolean = true
)
