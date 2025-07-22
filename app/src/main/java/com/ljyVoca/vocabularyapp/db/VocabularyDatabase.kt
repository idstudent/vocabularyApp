package com.ljyVoca.vocabularyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.WeeklyGoal

@Database(entities = [VocaWord::class], version = 2, exportSchema = true)
abstract class VocabularyDatabase: RoomDatabase() {
    abstract fun vocaDao(): VocaDao
}

@Database(entities = [WeeklyGoal::class], version = 1, exportSchema = true)
abstract class WeeklyGoalDatabase : RoomDatabase() {
    abstract fun weeklyGoalDao(): WeeklyGoalDao
}