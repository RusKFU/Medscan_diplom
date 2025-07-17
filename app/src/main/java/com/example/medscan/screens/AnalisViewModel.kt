package com.example.medscan.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medscan.database.dao.HealthEntryDao
import com.example.medscan.database.entity.HealthEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel(private val repository: HealthEntryDao) : ViewModel() {

    private val _entries = MutableStateFlow<List<HealthEntry>>(emptyList())
    val entries: StateFlow<List<HealthEntry>> = _entries

    init {
        viewModelScope.launch {
            repository.getAllEntries().collect {
                _entries.value = it
            }
        }
    }

    fun addEntry(entry: HealthEntry) {
        viewModelScope.launch {
            repository.insert(entry)
        }
    }


}
