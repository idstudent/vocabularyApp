package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class ChartRepository(
    private val vocabularyDatabase: VocabularyDatabase
) {
    suspend fun getWordsFromDate(startDate: Long): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getWordsFromDate(startDate)
    }
}