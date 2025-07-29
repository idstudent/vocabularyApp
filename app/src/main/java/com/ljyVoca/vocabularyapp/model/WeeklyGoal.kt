package com.ljyVoca.vocabularyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_goals")
data class WeeklyGoal(
    @PrimaryKey val weekStart: Long,
    val goalCount: Int,
    val createdDate: Long = System.currentTimeMillis()
)