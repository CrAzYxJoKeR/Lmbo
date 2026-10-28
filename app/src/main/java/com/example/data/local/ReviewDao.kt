package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SessionReview
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM session_reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<SessionReview>>

    @Query("SELECT * FROM session_reviews WHERE sessionId = :sessionId LIMIT 1")
    suspend fun getReviewForSession(sessionId: Long): SessionReview?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: SessionReview): Long
}
