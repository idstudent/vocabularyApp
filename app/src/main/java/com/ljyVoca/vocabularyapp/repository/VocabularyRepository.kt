package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class VocabularyRepository(
    private val vocabularyDatabase: VocabularyDatabase
) {
    suspend fun getAllWord(): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getAllWords()
    }
}