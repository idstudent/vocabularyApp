package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.Vocabulary
import kotlinx.coroutines.flow.Flow

class VocabularyFolderRepository(
    private val vocabularyDatabase: VocabularyDatabase
) {
    fun getVocabulary(): Flow<List<Vocabulary>> {
        return vocabularyDatabase.vocabularyDao().getAllVocabularies()
    }

    suspend fun deleteVocabulary(id: String) {
        vocabularyDatabase.vocabularyDao().deleteVocabularyById(id)
    }
}