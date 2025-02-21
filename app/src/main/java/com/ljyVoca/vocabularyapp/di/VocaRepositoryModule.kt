package com.ljyVoca.vocabularyapp.di

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.repository.SaveWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

}