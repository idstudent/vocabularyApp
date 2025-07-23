package com.ljyVoca.vocabularyapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.FilterState
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository
): ViewModel() {
    private val _todayWordList = MutableStateFlow<List<VocaWord>>(emptyList())
    val todayWordList = _todayWordList.asStateFlow()

    private val _wordList = MutableStateFlow<List<VocaWord>>(emptyList())
    val wordList = _wordList.asStateFlow()

    private val _saveWordList = MutableStateFlow<List<VocaWord>>(emptyList())
    val saveWordList = _saveWordList.asStateFlow()

    private val _currentWord = MutableStateFlow<VocaWord?>(null)
    val currentWord = _currentWord.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val currentWeekStart = getStartOfThisWeek()

    // 이번 주 목표
    val weeklyGoal: StateFlow<Int> = vocabularyRepository.getCurrentWeekGoal(currentWeekStart)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // 이번 주 새로운 단어 갯수
    val thisWeekNewWords: StateFlow<Int> = vocabularyRepository.getThisWeekWordsCount(currentWeekStart)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val availableLanguages: StateFlow<List<Language>> = vocabularyRepository.getAvailableLanguages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var currentIndex = 0

/*
    init {
        viewModelScope.launch {
            val words = vocabularyRepository.getAllWord()
            Log.e("ljy", "단어 개수: ${words.size}")

            _wordList.value = vocabularyRepository.getAllWord().shuffled()


            if(_wordList.value.isNotEmpty()) {
                _currentWord.value = _wordList.value[0]
            }
        }
    }
*/

    fun getTodayWords() {
        viewModelScope.launch {
            _todayWordList.value = vocabularyRepository.getTodayWords()
        }
    }
    fun getAllWord() {
        viewModelScope.launch {
            _saveWordList.value = vocabularyRepository.getAllWord()
        }
    }
    fun nextGetWord() {
        currentIndex++
        if(currentIndex < _wordList.value.size) {
            _currentWord.value = _wordList.value[currentIndex]
        }else {
            _currentWord.value?.ending = true
        }
    }

    fun updateFilterState(newState: FilterState) {
        _filterState.value = newState
    }

    fun updateWeeklyGoal(newGoal: Int) {
        viewModelScope.launch {
            vocabularyRepository.setWeeklyGoal(currentWeekStart, newGoal)
        }
    }

    private fun getStartOfThisWeek(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}