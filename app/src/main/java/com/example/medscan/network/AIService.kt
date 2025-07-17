package com.example.medscan.network


import kotlinx.coroutines.delay

import com.google.ai.client.generativeai.GenerativeModel
import com.googlecode.tesseract.android.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AIService {
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        //apiKey = "wkgPR85IbwwK6MoRgngw2uyf7az6AiwbBZ46MlE"
        apiKey = "AIzaSyCmT3IC83ymxeeSnQBEsZWvphwpV1yDZX8"
    )

    suspend fun analyzeText(text: String): String = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent("Сделай краткий анализ медецинских анализов" + text)
            response.text ?: "Нет ответа от ИИ"
        } catch (e: Exception) {
            "Ошибка при анализе: ${e.message}"
        }
    }

    suspend fun analyzeSymptoms(symptoms: String): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Проанализируйте следующие симптомы пациента и предоставьте:
                1. Возможные диагнозы (от наиболее до наименее вероятных)
                2. Рекомендации по дальнейшим действиям
                3. Срочность обращения к врачу
                
                Симптомы:
                $symptoms
                
                Ответ предоставьте в структурированном виде на русском языке.
            """.trimIndent()

            val response = model.generateContent(prompt)
            response.text ?: "Не удалось сгенерировать анализ симптомов"
        } catch (e: Exception) {
            "Ошибка при анализе симптомов: ${e.localizedMessage}"
        }
    }
}


