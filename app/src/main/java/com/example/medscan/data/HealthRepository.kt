package com.example.medscan.data

import com.example.medscan.database.dao.HealthEntryDao
import com.example.medscan.database.entity.HealthEntry
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val dao: HealthEntryDao) {
    suspend fun insert(entry: HealthEntry) = dao.insert(entry)
    fun getAllEntries(): Flow<List<HealthEntry>> = dao.getAllEntries()
}