package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ljyVoca.vocabularyapp.model.VocaWord
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

    // 단어장 삭제
    @Query("DELETE FROM vocabulary WHERE id = :vocabularyId")
    suspend fun deleteVocabularyById(vocabularyId: String)

    // 특정 단어장(폴더)의 단어들 조회
    @Query("SELECT * FROM voca WHERE vocabularyId = :vocabularyId ORDER BY createdDate DESC LIMIT :limit OFFSET :offset")
    suspend fun getWordsByPage(vocabularyId: String, limit: Int, offset: Int): List<VocaWord>

    // 단어장 삭제시 해당 단어들도 삭제
    @Query("DELETE FROM voca WHERE vocabularyId = :vocabularyId")
    suspend fun deleteWordsByVocabularyId(vocabularyId: String)

    // 단어장별 단어 개수
    @Query("SELECT COUNT(*) FROM voca WHERE vocabularyId = :vocabularyId")
    suspend fun getWordCountByVocabularyId(vocabularyId: String): Int

    // 단어 하나만 삭제
    @Query("DELETE FROM voca WHERE id = :wordId")
    suspend fun deleteWordById(wordId: String)
}