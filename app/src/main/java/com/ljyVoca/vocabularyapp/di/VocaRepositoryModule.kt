package com.ljyVoca.vocabularyapp.di

import android.content.Context
import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.db.WeeklyGoalDatabase
import com.ljyVoca.vocabularyapp.repository.SaveWordRepository
import com.ljyVoca.vocabularyapp.repository.VocabularyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VocaRepositoryModule {
    @Singleton
    @Provides
    fun providesSaveWordRepository(vocaDatabase: VocabularyDatabase): SaveWordRepository {
        return SaveWordRepository(vocaDatabase)
    }

    @Singleton
    @Provides
    fun provideVocabularyRepository(vocaDatabase: VocabularyDatabase, weeklyGoalDatabase: WeeklyGoalDatabase, @ApplicationContext context: Context): VocabularyRepository {
        return VocabularyRepository(vocaDatabase, weeklyGoalDatabase, context)
    }
}