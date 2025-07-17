package com.example.medscan.screens.forUI

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.medscan.database.entity.MedicalDocument
import com.example.medscan.screens.home.HomeViewModel
import com.example.medscan.utils.copyTrainedDataIfNeeded
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.InputStream
import com.example.medscan.network.AIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ContextCastToActivity")
@Composable
fun UploadScreen(navController: NavController) {
    val context = LocalContext.current
    val vm: HomeViewModel = viewModel(context as ComponentActivity)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var extractedText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Добавлены две кнопки в одну строку
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { pickImageLauncher.launch("image/*") }) {
                Text("Выбрать изображение")
            }

            Button(onClick = { /* Заглушка */ }) {
                Text("Скачать PDF")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let { uri ->
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Preview",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isLoading = true
                        coroutineScope.launch {
                            extractedText = withContext(Dispatchers.IO) {
                                extractTextFromBitmap(context, it)
                            }
                            isLoading = false
                        }
                    },
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Извлечь текст")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                ) {
                    item {
                        Text(text = extractedText)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (extractedText.isNotBlank()) {
            Button(onClick = {
                val doc = MedicalDocument(
                    name = "Документ",
                    filePath = imageUri.toString(),
                    date = System.currentTimeMillis(),
                    extractedText = extractedText,
                )

                vm.addDocument(doc)

                coroutineScope.launch {
                    val analysis = AIService.analyzeText(extractedText)
                    vm.updateAiAnalysis(doc.id, analysis)
                    navController.navigate("home")
                }
            }) {
                Text("Сохранить и отправить в ИИ")
            }
        }
    }
}

fun extractTextFromBitmap(context: android.content.Context, bitmap: android.graphics.Bitmap): String {
    copyTrainedDataIfNeeded(context, "rus")

    val tessBaseApi = TessBaseAPI()
    tessBaseApi.init(context.filesDir.absolutePath, "rus")
    tessBaseApi.setImage(bitmap)
    val text = tessBaseApi.utF8Text
    tessBaseApi.end()
    return text ?: "Не удалось извлечь текст"
}
