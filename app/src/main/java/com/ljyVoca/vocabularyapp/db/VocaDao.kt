package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ljyVoca.vocabularyapp.model.VocaWord

@Dao
interface VocaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocaWord)

    @Query("SELECT * FROM voca")
    suspend fun getAllWords(): List<VocaWord>
}