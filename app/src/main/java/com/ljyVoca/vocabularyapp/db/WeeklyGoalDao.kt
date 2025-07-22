package com.ljyVoca.vocabularyapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ljyVoca.vocabularyapp.model.WeeklyGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyGoalDao {
    @Query("SELECT goalCount FROM weekly_goals WHERE weekStart = :weekStart")
    fun getGoalCountForWeek(weekStart: Long): Flow<Int?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: WeeklyGoal)
}