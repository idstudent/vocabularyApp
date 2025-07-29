package com.ljyVoca.vocabularyapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.db.WeeklyGoalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideVocaDatabase(app: Application) =
        Room.databaseBuilder(app, VocabularyDatabase::class.java, "voca_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideWeeklyGoalDatabase(app: Application) =
        Room.databaseBuilder(app, WeeklyGoalDatabase::class.java, "weekly_goal_db")
            .build()
}