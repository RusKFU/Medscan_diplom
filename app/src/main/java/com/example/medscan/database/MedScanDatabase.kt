package com.example.medscan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medscan.database.dao.MedicalDocumentDao
import com.example.medscan.database.entity.MedicalDocument

@Database(entities = [MedicalDocument::class], version = 2)
abstract class MedScanDatabase : RoomDatabase() {
    abstract fun documentDao(): MedicalDocumentDao

    companion object {
        @Volatile private var INSTANCE: MedScanDatabase? = null

        fun getInstance(context: Context): MedScanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedScanDatabase::class.java,
                    "med_scan.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}