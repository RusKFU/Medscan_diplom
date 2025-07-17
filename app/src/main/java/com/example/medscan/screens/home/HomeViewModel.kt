package com.example.medscan.screens.home

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.medscan.database.AppDatabase
import com.example.medscan.database.entity.MedicalDocument
import com.example.medscan.network.AIService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.medscan.data.DocumentRepository
import com.example.medscan.database.dao.MedicalDocumentDao
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "medscan.db"
    ).fallbackToDestructiveMigration()
        .build()

    private val dao = db.medicalDocumentDao()

    // Поток всех документов из БД
    val documents: StateFlow<List<MedicalDocument>> =
        dao.getAllDocuments()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Добавление нового документа
    fun addDocument(document: MedicalDocument) {
        viewModelScope.launch {
            dao.insert(document)
        }
    }

    // Обновление анализа ИИ (по id)
    fun updateAiAnalysis(documentId: Int, aiAnalysis: String) {
        viewModelScope.launch {
            val currentList = documents.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == documentId }
            if (index != -1) {
                val oldDoc = currentList[index]
                val updatedDoc = oldDoc.copy(aiAnalysis = aiAnalysis)
                dao.insert(updatedDoc) // перезапись по ID
            }
        }
    }

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun sendToAI(document: MedicalDocument) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val analysis = AIService.analyzeText(document.extractedText)
                val updatedDoc = document.copy(aiAnalysis = analysis)
                dao.insert(updatedDoc)
            } catch (e: Exception) {
                // Логируем ошибку или показываем ошибку пользователю
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getDocumentById(repository: DocumentRepository, id: Long): Flow<MedicalDocument?> {
        return repository.getDocumentById(id)
    }
}
