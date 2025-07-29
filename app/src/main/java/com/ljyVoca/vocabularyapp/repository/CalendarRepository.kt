package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class CalendarRepository(
    private val vocabularyDatabase: VocabularyDatabase
) {
    suspend fun getWordsByMonth(yearMonth: String): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getWordsByMonth(yearMonth)
    }

    suspend fun getWordsByDay(targetDate: String): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getWordsByDay(targetDate)
    }

    suspend fun getFirstWordDate(): Long? {
        return vocabularyDatabase.vocaDao().getFirstWordDate()
    }
}