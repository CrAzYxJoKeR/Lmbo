package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_reviews")
data class SessionReview(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val followedChecklist: Boolean = true,
    val respectedLimits: Boolean = true,
    val emotionDroveDecision: Boolean = false,
    val emotionNotes: String = "",
    val setupQualityRating: Int = 8, // 1-10
    val keyLesson: String = ""
)
