package com.ljyVoca.vocabularyapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.repository.ChartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val chartRepository: ChartRepository
): ViewModel() {
    private val _dailyWordCounts = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val dailyWordCounts = _dailyWordCounts.asStateFlow()

    private val _dailyAccuracy = MutableStateFlow<List<Pair<String, Float>>>(emptyList())
    val dailyAccuracy = _dailyAccuracy.asStateFlow()

    fun loadDailyWordCounts() {
        viewModelScope.launch {
            val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
            val words = chartRepository.getWordsFromDate(sevenDaysAgo)

            val formatter = SimpleDateFormat("MM.dd", Locale.getDefault())
            val counts = words.groupBy { word ->
                formatter.format(Date(word.createdDate))
            }.mapValues { it.value.size }

            val result = (6 downTo 0).map { daysAgo ->
                val date = Date(System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000L))
                val dateString = formatter.format(date)
                dateString to (counts[dateString] ?: 0)
            }

            _dailyWordCounts.value = result
        }
    }

    fun loadDailyAccuracy() {
        viewModelScope.launch {
            val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
            val words = chartRepository.getWordsFromDate(sevenDaysAgo)

            val formatter = SimpleDateFormat("MM.dd", Locale.getDefault())

            val accuracyByDate = words
                .filter { it.totalAttempts > 0 }
                .groupBy { word ->
                    formatter.format(Date(word.lastStudiedDate)) 
                }
                .mapValues { entry ->
                    val wordsOnDate = entry.value
                    if (wordsOnDate.isNotEmpty()) {
                        wordsOnDate.map { it.accuracy }.average().toFloat()
                    } else 0f
                }

            val result = (6 downTo 0).map { daysAgo ->
                val date = Date(System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000L))
                val dateString = formatter.format(date)
                dateString to (accuracyByDate[dateString] ?: 0f)
            }

            _dailyAccuracy.value = result
        }
    }
}