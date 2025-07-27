package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ljyVoca.vocabularyapp.model.Vocabulary
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary")
    fun getAllVocabularies(): Flow<List<Vocabulary>>

    @Insert
    suspend fun insertVocabulary(vocabulary: Vocabulary)

    @Update
    suspend fun updateVocabulary(vocabulary: Vocabulary)

    @Query("DELETE FROM vocabulary WHERE id = :vocabularyId")
    suspend fun deleteVocabularyById(vocabularyId: String)
}