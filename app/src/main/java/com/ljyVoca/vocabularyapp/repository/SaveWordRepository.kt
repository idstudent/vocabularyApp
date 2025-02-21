package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class SaveWordRepository(
    private val vocabularyDatabase: VocabularyDatabase
){
    suspend fun insertVoca(voca: VocaWord) {
        return vocabularyDatabase.vocaDao().insertWord(voca)
    }
}