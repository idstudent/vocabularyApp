package com.ljyVoca.vocabularyapp.di

import android.app.Application
import androidx.room.Room
import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
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
}