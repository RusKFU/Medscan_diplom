package com.example.medscan.screens.forUI

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.medscan.network.AIService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var isLoading by remember { mutableStateOf(false) }
    var aiResponse by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    var headache by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var cough by remember { mutableStateOf("") }
    var otherSymptoms by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Анализ симптомов") },
            text = {
                Crossfade(targetState = isLoading) { loading ->
                    if (loading) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Text(aiResponse ?: "Не удалось получить анализ")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Введите симптомы",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = headache,
                    onValueChange = { headache = it },
                    label = { Text("Головная боль (описание)") },
                    leadingIcon = { Icon(Icons.Default.SentimentDissatisfied, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = temperature,
                    onValueChange = { temperature = it },
                    label = { Text("Температура тела (°C)") },
                    leadingIcon = { Icon(Icons.Default.Thermostat, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cough,
                    onValueChange = { cough = it },
                    label = { Text("Кашель (описание)") },
                    leadingIcon = { Icon(Icons.Default.Sick, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = otherSymptoms,
                    onValueChange = { otherSymptoms = it },
                    label = { Text("Другие симптомы") },
                    leadingIcon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                if (headache.isBlank() && temperature.isBlank() &&
                    cough.isBlank() && otherSymptoms.isBlank()
                ) {
                    aiResponse = "Пожалуйста, введите хотя бы один симптом"
                    showDialog = true
                } else {
                    scope.launch {
                        isLoading = true
                        showDialog = true
                        val symptomsText = """
                            Головная боль: $headache
                            Температура: $temperature °C
                            Кашель: $cough
                            Другие симптомы: $otherSymptoms
                        """.trimIndent()
                        aiResponse = AIService.analyzeSymptoms(symptomsText)
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Проанализировать симптомы")
            }
        }
    }
}
