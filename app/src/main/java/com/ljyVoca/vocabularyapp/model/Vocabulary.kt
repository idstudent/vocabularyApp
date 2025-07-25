package com.ljyVoca.vocabularyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "vocabulary")
data class Vocabulary(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val description: String = ""
)