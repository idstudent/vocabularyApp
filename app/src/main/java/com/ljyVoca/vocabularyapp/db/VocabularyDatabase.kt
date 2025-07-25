package com.ljyVoca.vocabularyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ljyVoca.vocabularyapp.model.Converters
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.Vocabulary
import com.ljyVoca.vocabularyapp.model.WeeklyGoal

@Database(entities = [VocaWord::class, Vocabulary::class], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class VocabularyDatabase: RoomDatabase() {
    abstract fun vocaDao(): VocaDao
    abstract fun vocabularyDao(): VocabularyDao  // 추가
}

@Database(entities = [WeeklyGoal::class], version = 1, exportSchema = true)
abstract class WeeklyGoalDatabase : RoomDatabase() {
    abstract fun weeklyGoalDao(): WeeklyGoalDao
}