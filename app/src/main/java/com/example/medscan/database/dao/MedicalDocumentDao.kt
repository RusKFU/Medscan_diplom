package com.example.medscan.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medscan.database.entity.MedicalDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalDocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: MedicalDocument)

    @Query("SELECT * FROM medical_documents ORDER BY date DESC")
    fun getAllDocuments(): Flow<List<MedicalDocument>>

    @Query("SELECT * FROM medical_documents WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MedicalDocument?  // Изменил Int на Long для согласованности

    @Update
    suspend fun update(document: MedicalDocument)

    @Query("SELECT * FROM medical_documents WHERE id = :id")
    fun getDocumentByIdFlow(id: Long): Flow<MedicalDocument?>  // Переименовал для ясности

    @Query("SELECT * FROM medical_documents WHERE id = :id")
    fun getDocumentById(id: Long): Flow<MedicalDocument?>
}
