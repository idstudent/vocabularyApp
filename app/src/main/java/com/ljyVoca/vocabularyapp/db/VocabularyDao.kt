package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ljyVoca.vocabularyapp.model.Vocabulary

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary ORDER BY title")
    suspend fun getAllVocabularies(): List<Vocabulary>

    @Insert
    suspend fun insertVocabulary(vocabulary: Vocabulary)

    @Query("DELETE FROM vocabulary WHERE id = :vocabularyId")
    suspend fun deleteVocabularyById(vocabularyId: String)
}