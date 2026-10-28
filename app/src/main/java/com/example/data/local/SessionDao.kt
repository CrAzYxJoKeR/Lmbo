package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.TradingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM trading_sessions WHERE isCompleted = 0 ORDER BY id DESC LIMIT 1")
    fun getActiveSession(): Flow<TradingSession?>

    @Query("SELECT * FROM trading_sessions ORDER BY dateMillis DESC")
    fun getAllSessions(): Flow<List<TradingSession>>

    @Query("SELECT * FROM trading_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Long): TradingSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TradingSession): Long

    @Update
    suspend fun updateSession(session: TradingSession)

    @Query("DELETE FROM trading_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)
}
