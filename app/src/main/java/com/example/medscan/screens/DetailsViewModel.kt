package com.example.medscan.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.medscan.data.DocumentRepository
import com.example.medscan.database.entity.MedicalDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import com.example.medscan.screens.home.HomeViewModel // Corrected import


class DetailsViewModel(private val repository: DocumentRepository) : ViewModel() {

    private val _documentState = MutableStateFlow<MedicalDocument?>(null)
    val documentState: StateFlow<MedicalDocument?> = _documentState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getDocumentDetails(documentId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getDocumentById(documentId).collectLatest { document ->
                _documentState.value = document
                _isLoading.value = false
            }
        }
    }
}

class DetailsViewModelFactory(private val repository: DocumentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
