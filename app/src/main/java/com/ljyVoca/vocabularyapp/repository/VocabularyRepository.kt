package com.ljyVoca.vocabularyapp.repository

import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.db.WeeklyGoalDatabase
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.WeeklyGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VocabularyRepository(
    private val vocabularyDatabase: VocabularyDatabase,
    private val weeklyGoalDatabase: WeeklyGoalDatabase
) {
    suspend fun getAllWord(): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getAllWords()
    }

    suspend fun getTodayWords(): List<VocaWord> {
        return vocabularyDatabase.vocaDao().getTodayWords()
    }

    // 이번주 실제 저장한 단어 갯수 (VocaWord에서)
    fun getThisWeekWordsCount(weekStart: Long): Flow<Int> {
        return vocabularyDatabase.vocaDao().getThisWeekWordsCount(weekStart)
    }

    // 이번주 목표 단어 갯수 (WeeklyGoal에서)
    fun getCurrentWeekGoal(weekStart: Long): Flow<Int> {
        return weeklyGoalDatabase.weeklyGoalDao().getGoalCountForWeek(weekStart)
            .map { it ?: 0 }
    }

    // 목표 저장
    suspend fun setWeeklyGoal(weekStart: Long, goalCount: Int) {
        weeklyGoalDatabase.weeklyGoalDao().insertGoal(
            WeeklyGoal(weekStart, goalCount)
        )
    }
}