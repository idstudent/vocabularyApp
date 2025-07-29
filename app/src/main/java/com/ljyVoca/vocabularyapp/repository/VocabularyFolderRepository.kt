package com.ljyVoca.vocabularyapp.repository

import androidx.room.util.copyAndClose
import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.Vocabulary
import kotlinx.coroutines.flow.Flow

class VocabularyFolderRepository(
    private val vocabularyDatabase: VocabularyDatabase
) {
    fun getVocabulary(): Flow<List<Vocabulary>> {
        return vocabularyDatabase.vocabularyDao().getAllVocabularies()
    }

    suspend fun getSaveWordsCount(id: String): Int {
        return vocabularyDatabase.vocabularyDao().getWordCountByVocabularyId(id)
    }
    suspend fun insertVocabulary(vocabulary: Vocabulary) {
        vocabularyDatabase.vocabularyDao().insertVocabulary(vocabulary)
    }

    suspend fun updateVocabulary(vocabulary: Vocabulary) {
        vocabularyDatabase.vocabularyDao().updateVocabulary(vocabulary)
    }

    suspend fun deleteWordsByVocabularyId(id: String) {
        vocabularyDatabase.vocabularyDao().deleteWordsByVocabularyId(id)
    }
    suspend fun deleteVocabularyById(id: String) {
        vocabularyDatabase.vocabularyDao().deleteVocabularyById(id)
    }

    suspend fun getWordsByPage(vocabularyId: String, limit: Int, offset: Int): List<VocaWord> {
        return vocabularyDatabase.vocabularyDao().getWordsByPage(vocabularyId, limit, offset)
    }
}