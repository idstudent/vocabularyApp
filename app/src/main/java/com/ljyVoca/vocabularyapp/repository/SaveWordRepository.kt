package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

class SaveWordRepository(
    private val vocabularyDatabase: VocabularyDatabase
){
    suspend fun insertWord(word: VocaWord): Boolean {
        val exists = vocabularyDatabase.vocaDao().isWordExists(word.word)
        println("존재 여부: $exists, 단어: '${word.word}'") // 디버깅
        return if (exists) {
            false // 중복이면 저장 안 함
        } else {
            vocabularyDatabase.vocaDao().insertWord(word)
            true // 저장 성공
        }
    }

    suspend fun updateWord(word: VocaWord): Boolean {
        val exists = vocabularyDatabase.vocaDao().isWordExistsExcluding(word.word, word.id)
        println("수정 시 중복 체크: $exists, 단어: '${word.word}', ID: '${word.id}'")
        return if (exists) {
            false // 다른 단어와 중복이면 수정 안 함
        } else {
            try {
                vocabularyDatabase.vocaDao().updateWord(word)
                true // 수정 성공
            } catch (e: Exception) {
                println("단어 수정 실패: ${e.message}")
                false // 수정 실패
            }
        }
    }

    suspend fun deleteWord(id: String) {
        vocabularyDatabase.vocabularyDao().deleteWordById(id)
    }
}