package com.example.medscan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medscan.database.entity.HealthEntry
import com.example.medscan.database.entity.MedicalDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: HealthEntry)

    @Query("SELECT * FROM health_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<HealthEntry>>

//    @Query("SELECT * FROM health_entries WHERE userId = :userId ORDER BY timestamp DESC")
//    fun getEntriesForUser(userId: Int): Flow<List<HealthEntry>>
}
