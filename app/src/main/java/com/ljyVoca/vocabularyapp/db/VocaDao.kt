package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ljyVoca.vocabularyapp.model.VocaWord
import kotlinx.coroutines.flow.Flow

@Dao
interface VocaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocaWord)

    @Query("SELECT * FROM voca")
    suspend fun getAllWords(): List<VocaWord>

    @Query("SELECT * FROM voca WHERE DATE(createdDate/1000, 'unixepoch') = DATE('now') ORDER BY createdDate DESC")
    suspend fun getTodayWords(): List<VocaWord>

    @Query("SELECT COUNT(*) FROM voca WHERE createdDate >= :weekStart")
    fun getThisWeekWordsCount(weekStart: Long): Flow<Int>

    @Query("SELECT DISTINCT category FROM voca ORDER BY category")
    fun getDistinctCategories(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM voca WHERE totalAttempts >= 3 AND (totalAttempts - wrongCount) * 1.0 / totalAttempts < 0.5")
    fun getFrequentlyWrongWordsCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM voca WHERE totalAttempts >= 3 AND (totalAttempts - wrongCount) * 1.0 / totalAttempts < 0.5 LIMIT 1)")
    fun hasFrequentlyWrongWords(): Flow<Boolean>

    @Query("""
    SELECT * FROM voca 
    WHERE (:category IS NULL OR category = :category)
    AND (:onlyFrequentlyWrong = 0 OR (totalAttempts >= 3 AND (totalAttempts - wrongCount) * 1.0 / totalAttempts < 0.5))
    ORDER BY RANDOM()
""")
    suspend fun getQuizWords(
        category: String? = null,
        onlyFrequentlyWrong: Boolean = false
    ): List<VocaWord>

    // 특정 단어장(폴더)의 단어들 조회
    @Query("SELECT * FROM voca WHERE vocabularyId = :vocabularyId ORDER BY createdDate DESC")
    suspend fun getWordsByVocabularyId(vocabularyId: String): List<VocaWord>

    // 단어장 삭제시 해당 단어들도 삭제
    @Query("DELETE FROM voca WHERE vocabularyId = :vocabularyId")
    suspend fun deleteWordsByVocabularyId(vocabularyId: String)

    // 단어장별 단어 개수
    @Query("SELECT COUNT(*) FROM voca WHERE vocabularyId = :vocabularyId")
    suspend fun getWordCountByVocabularyId(vocabularyId: String): Int
}