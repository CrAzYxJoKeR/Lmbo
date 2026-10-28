package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missed_trades")
data class MissedTrade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val asset: String = "EUR/USD",
    val direction: String = "CALL",
    val setupReason: String = "",
    val reasonMissed: String = "", // e.g. "Hesitated", "Stepped away", "Strict checklist rule saved me"
    val reflection: String = "A missed trade is not a loss. Chasing it usually is. Journal it and wait for a fresh setup."
)
