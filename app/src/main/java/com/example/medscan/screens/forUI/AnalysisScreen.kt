package com.example.medscan.screens.forUI


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medscan.database.AppDatabase
import com.example.medscan.database.entity.HealthEntry
import com.example.medscan.screens.AnalysisViewModel

@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: AnalysisViewModel = viewModel(factory = AnalysisViewModelFactory(LocalContext.current)) //ERROR
) {
    val context = LocalContext.current
    val entries by viewModel.entries.collectAsState()

    var temperature by remember { mutableStateOf("") }
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var sugar by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Анализ здоровья") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = temperature,
                onValueChange = { temperature = it },
                label = { Text("Температура (°C)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = systolic,
                onValueChange = { systolic = it },
                label = { Text("Давление верхнее") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = diastolic,
                onValueChange = { diastolic = it },
                label = { Text("Давление нижнее") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sugar,
                onValueChange = { sugar = it },
                label = { Text("Сахар (ммоль/л)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Вес (кг)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                viewModel.addEntry(
                    HealthEntry(
                        temperature = temperature.toFloatOrNull(),
                        systolicPressure = systolic.toIntOrNull(),
                        diastolicPressure = diastolic.toIntOrNull(),
                        bloodSugar = sugar.toFloatOrNull(),
                        weight = weight.toFloatOrNull(),
                        timestamp = System.currentTimeMillis() // 👈 добавь это

                    ) // error
                )
                // Очистка полей
                temperature = ""; systolic = ""; diastolic = ""; sugar = ""; weight = ""
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Графики", style = MaterialTheme.typography.h6)

            // Графики — вынесем в отдельный компонент
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

class AnalysisViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(context).healthEntryDao()
        return AnalysisViewModel(dao) as T
    }
}

