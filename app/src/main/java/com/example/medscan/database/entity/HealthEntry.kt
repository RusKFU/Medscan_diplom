package com.example.medscan.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "health_entries")
data class HealthEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val userId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val temperature: Float?,
    val systolicPressure: Int?,
    val diastolicPressure: Int?,
    val bloodSugar: Float?,
    val weight: Float?
)


