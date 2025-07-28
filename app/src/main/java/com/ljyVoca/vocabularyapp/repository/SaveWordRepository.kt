package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class SaveWordRepository(
    private val vocabularyDatabase: VocabularyDatabase
){
    suspend fun insertWord(word: VocaWord) {
        return vocabularyDatabase.vocaDao().insertWord(word)
    }

    suspend fun updateWord(word: VocaWord) {
        return vocabularyDatabase.vocaDao().updateWord(word)
    }
}