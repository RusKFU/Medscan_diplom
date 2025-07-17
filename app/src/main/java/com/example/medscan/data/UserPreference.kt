//package com.example.medscan.data
//
//import android.content.Context
//import androidx.datastore.preferences.core.*
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//val Context.dataStore by preferencesDataStore(name = "user_prefs")
//
//class UserPreference(private val context: Context) {
//    companion object {
//        private val USER_ID = longPreferencesKey("user_id")
//    }
//
//    suspend fun saveUserId(userId: Long) {
//        context.dataStore.edit { it[USER_ID] = userId }
//    }
//
//    fun getUserId(): Flow<Long> {
//        return context.dataStore.data.map { it[USER_ID] ?: -1 }
//    }
//
//    suspend fun clear() {
//        context.dataStore.edit { it.clear() }
//    }
//}
