//package com.example.medscan.data
//
//import android.content.Context
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//import androidx.datastore.preferences.core.edit
//
//import kotlinx.coroutines.flow.map
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
//
//class UserPreference(context: Context) {
//    private val Context.dataStore by preferencesDataStore("user_prefs")
//    private val USER_ID_KEY = intPreferencesKey("user_id")
//
//    suspend fun saveUserId(id: Int) {
//        context.dataStore.edit { it[USER_ID_KEY] = id }
//    }
//
//    fun getUserId(): Flow<Int> = context.dataStore.data
//        .map { it[USER_ID_KEY] ?: -1 }
//}