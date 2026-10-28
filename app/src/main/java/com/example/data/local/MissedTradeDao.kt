package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MissedTrade
import kotlinx.coroutines.flow.Flow

@Dao
interface MissedTradeDao {
    @Query("SELECT * FROM missed_trades ORDER BY timestamp DESC")
    fun getAllMissedTrades(): Flow<List<MissedTrade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissedTrade(missedTrade: MissedTrade): Long
}
