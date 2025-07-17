package com.example.medscan.data


import android.content.Context
import androidx.room.Room
import com.example.medscan.database.MedScanDatabase

object DatabaseProvider {
    private var instance: MedScanDatabase? = null

    fun getDatabase(context: Context): MedScanDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                MedScanDatabase::class.java,
                "medscan_db"
            ).build()
        }
        return instance!!
    }
}
