package com.ljyVoca.vocabularyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord

@Database(entities = [VocaWord::class], version = 1)
abstract class VocabularyDatabase: RoomDatabase() {
    abstract fun vocaDao(): VocaDao
}