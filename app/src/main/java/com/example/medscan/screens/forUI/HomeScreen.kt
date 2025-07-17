package com.example.medscan.screens.forUI

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medscan.database.entity.MedicalDocument
import com.example.medscan.screens.home.HomeViewModel
import com.example.medscan.screens.home.HomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.runtime.collectAsState

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val isLoading by viewModel.isLoading
    val documents by viewModel.documents.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("med.scan", style = MaterialTheme.typography.headlineSmall) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("upload") }) {
                Icon(Icons.Default.Add, contentDescription = "Загрузить документ")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Ваши медицинские документы",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(documents) { doc ->
                    DocumentCard(
                        document = doc,
                        onAnalyze = { viewModel.sendToAI(doc) },
                        onDetails = { navController.navigate("details/${doc.id}") }
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DocumentCard(
    document: MedicalDocument,
    onAnalyze: () -> Unit,
    onDetails: (MedicalDocument) -> Unit = {}
) {
    val dateFormatted = remember(document.date) {
        try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            sdf.format(Date(document.date))
        } catch (e: Exception) {
            "Неизвестная дата"
        }
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)) {

            Text(
                text = "Документ #${document.id}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Дата",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = dateFormatted, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Текст: ${document.extractedText.take(120)}...",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (document.aiAnalysis != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFDFF0D8), shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Анализ завершен",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Анализ выполнен", color = Color(0xFF4CAF50))
                }

                OutlinedButton(
                    onClick = { onDetails(document) },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Подробнее")
                }

            } else {
                Button(
                    onClick = onAnalyze,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.AutoFixHigh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Анализировать")
                }
            }
        }
    }
}