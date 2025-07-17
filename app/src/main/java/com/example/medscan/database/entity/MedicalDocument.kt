package com.example.medscan.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_documents")
data class MedicalDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val filePath: String,
    val date: Long,
    val extractedText: String,
    val aiAnalysis: String? = null
)
