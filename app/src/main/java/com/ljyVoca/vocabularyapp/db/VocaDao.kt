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
}