package com.ljyVoca.vocabularyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voca")
data class VocaWord(
    @PrimaryKey
    val word: String,
    val mean: String
)