package com.example.medscan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medscan.database.dao.DocumentDao
import com.example.medscan.database.dao.HealthEntryDao
import com.example.medscan.database.dao.MedicalDocumentDao
import com.example.medscan.database.entity.HealthEntry
import com.example.medscan.database.entity.MedicalDocument

@Database(
    entities = [MedicalDocument::class, HealthEntry::class], // ✅ Добавлены обе сущности
    version = 3, // ✅ Повышаем версию, если структура изменилась
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicalDocumentDao(): MedicalDocumentDao
    abstract fun healthEntryDao(): HealthEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medscan_database"
                )
                    .fallbackToDestructiveMigration() // ✅ Добавлено
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
