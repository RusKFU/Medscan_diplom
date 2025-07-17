package com.example.medscan.data


import android.content.Context
import com.example.medscan.database.entity.MedicalDocument
import kotlinx.coroutines.flow.Flow

class DocumentRepository(context: Context) {
    private val dao = DatabaseProvider.getDatabase(context).documentDao()

    suspend fun insertDocument(doc: MedicalDocument) = dao.insert(doc)
    fun getAllDocuments(): Flow<List<MedicalDocument>> = dao.getAllDocuments()

    // Для однократного получения

    // Для наблюдения за изменениями
    fun observeDocument(id: Long) = dao.getDocumentByIdFlow(id)





    fun getDocumentById(id: Long): Flow<MedicalDocument?> {
        return dao.getDocumentByIdFlow(id)
    }

    // Or if you want a one-time fetch:
    suspend fun getDocumentByIdOnce(id: Long): Flow<MedicalDocument?> {
        return dao.getDocumentById(id)
    }
}
