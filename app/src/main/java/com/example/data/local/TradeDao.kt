package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Trade
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Query("SELECT * FROM trades ORDER BY timestamp DESC")
    fun getAllTrades(): Flow<List<Trade>>

    @Query("SELECT * FROM trades ORDER BY timestamp DESC LIMIT 50")
    fun getLast50Trades(): Flow<List<Trade>>

    @Query("SELECT * FROM trades WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getTradesForSession(sessionId: Long): Flow<List<Trade>>

    @Query("SELECT * FROM trades ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestTrade(): Trade?

    @Query("SELECT * FROM trades WHERE asset = :asset ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestTradeForAsset(asset: String): Trade?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: Trade): Long

    @Query("DELETE FROM trades WHERE id = :id")
    suspend fun deleteTrade(id: Long)

    @Query("DELETE FROM trades")
    suspend fun clearAllTrades()
}
