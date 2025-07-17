package com.example.medscan.screens.forUI

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medscan.data.DocumentRepository
import com.example.medscan.database.entity.MedicalDocument
import com.example.medscan.screens.home.HomeViewModel
import com.example.medscan.screens.home.HomeViewModelFactory

import androidx.compose.ui.platform.LocalContext
import com.example.medscan.screens.DetailsViewModel
import com.example.medscan.screens.DetailsViewModelFactory

// Composable for DetailsScreen
@Composable
fun DetailsScreen(
    documentId: Long,
    repository: DocumentRepository,
    navController: NavController
) {
    val detailsViewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(repository)
    )

    // Load document details once when screen is first shown
    LaunchedEffect(documentId) {
        detailsViewModel.getDocumentDetails(documentId)
    }
    val context = LocalContext.current.applicationContext as Application

    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))

    val documents by viewModel.documents.collectAsState(initial = emptyList())
    val document = documents.find { it.id.toLong() == documentId }

    val isLoading by detailsViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Document Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colors.primary)
                    }
                }
                document == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Document not found.",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
                else -> {
                    DocumentDetailsContent(document = document!!)
                }
            }
        }
    }
}

@Composable
fun DocumentDetailsContent(document: MedicalDocument) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        Text(
            text = "Date: ${document.date}",
            style = MaterialTheme.typography.body1
        )

        Text(
            text = "Extracted Text:",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = document.extractedText,
            style = MaterialTheme.typography.body2
        )

        if (!document.aiAnalysis.isNullOrEmpty()) {
            Text(
                text = "AI Analysis:",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = document.aiAnalysis,
                style = MaterialTheme.typography.body2
            )
        }
    }
}