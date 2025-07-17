package com.example.medscan.utils


import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun copyTrainedDataIfNeeded(context: Context, languageCode: String = "rus") {
    val tessDataDir = File(context.filesDir, "tessdata")
    if (!tessDataDir.exists()) {
        tessDataDir.mkdirs()
    }

    val trainedDataFile = File(tessDataDir, "$languageCode.traineddata")
    if (!trainedDataFile.exists()) {
        context.assets.open("tessdata/$languageCode.traineddata").use { input ->
            FileOutputStream(trainedDataFile).use { output ->
                input.copyTo(output)
            }
        }
    }
}
