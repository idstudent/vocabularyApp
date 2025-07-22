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
            .addMigrations(MIGRATION_1_2)
            .build()

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE voca ADD COLUMN phonetic TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE voca ADD COLUMN description TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE voca ADD COLUMN wrongCount INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN totalAttempts INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN lastStudiedDate INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN category TEXT NOT NULL DEFAULT '영어'")
                database.execSQL("ALTER TABLE voca ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN createdDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
            }
        }
    }

    @Singleton
    @Provides
    fun provideWeeklyGoalDatabase(app: Application) =
        Room.databaseBuilder(app, WeeklyGoalDatabase::class.java, "weekly_goal_db")
            .build()
}