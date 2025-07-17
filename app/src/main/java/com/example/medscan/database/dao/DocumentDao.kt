package com.example.medscan.database.dao


import androidx.room.*
import com.example.medscan.database.entity.MedicalDocument
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: MedicalDocument)


}
