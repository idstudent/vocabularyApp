package com.ljyVoca.vocabularyapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.FilterState
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.repository.VocabularyRepository
import com.ljyVoca.vocabularyapp.util.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val vocabularyRepository: VocabularyRepository,
    @ApplicationContext private val context: Context
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

    private val _quizWordList = MutableStateFlow<List<VocaWord>>(emptyList())
    val quizWordList = _quizWordList.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex = _currentQuizIndex.asStateFlow()

    private val _currentQuizWord = MutableStateFlow<VocaWord?>(null)
    val currentQuizWord = _currentQuizWord.asStateFlow()

    private val _isQuizCompleted = MutableStateFlow(false)
    val isQuizCompleted = _isQuizCompleted.asStateFlow()

    private val ttsManager = TTSManager(context)
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

    val hasFrequentlyWrongWords: StateFlow<Boolean> = vocabularyRepository.hasFrequentlyWrongWords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private var currentIndex = 0

    init {
        // TTS 초기화
        ttsManager.initialize { success ->
            if (success) {
                Log.e("ljy", "TTS 초기화 성공")
            } else {
                Log.e("ljy", "TTS 초기화 실패")
            }
        }
    }
    fun speakOnlyWord(word: VocaWord) {
        ttsManager.speak(word.word)
    }

    fun speakWord(word: VocaWord, quiz: Boolean = true) {
        // 현재 퀴즈 타입에 따라 읽을 텍스트 결정
        val textToSpeak = when(_filterState.value.quizType) {
            QuizType.WORD_TO_MEANING -> word.mean
            QuizType.MEANING_TO_WORD -> word.word
        }

        if(quiz) {
            ttsManager.speak(textToSpeak)
        }else {
            ttsManager.speak(word.word)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }

    fun getTodayWords() {
        viewModelScope.launch {
            _todayWordList.value = vocabularyRepository.getTodayWords()
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
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
    fun startQuiz() {
        viewModelScope.launch {
            try {
                val words = vocabularyRepository.getQuizWords(_filterState.value)

                if (words.isNotEmpty()) {
                    _quizWordList.value = words
                    _currentQuizIndex.value = 0
                    _currentQuizWord.value = words[0]
                    _isQuizCompleted.value = false
                } else {
                    // 단어가 없는 경우
                    _quizWordList.value = emptyList()
                    _currentQuizWord.value = null
                    _isQuizCompleted.value = false
                }

                vocabularyRepository.updateLastStudyDate()
            } catch (e: Exception) {
                Log.e("VocabularyViewModel", "퀴즈 시작 실패", e)
            }
        }
    }

    // 다음 문제
    fun nextQuizWord() {
        val currentIndex = _currentQuizIndex.value
        val wordList = _quizWordList.value

        if (currentIndex < wordList.size - 1) {
            val nextIndex = currentIndex + 1
            _currentQuizIndex.value = nextIndex
            _currentQuizWord.value = wordList[nextIndex]

            if (nextIndex == wordList.size - 1) {
                _isQuizCompleted.value = true
            }
        } else {
            // 퀴즈 완료
            _isQuizCompleted.value = true
        }
    }

    fun processQuizResult(isCorrect: Boolean) {
        viewModelScope.launch {
            _currentQuizWord.value?.let { word ->
                // 시도 횟수 증가
                word.totalAttempts++

                // 틀렸으면 틀린 횟수도 증가
                if (!isCorrect) {
                    word.wrongCount++
                }

                // 마지막 학습 날짜 업데이트
                word.lastStudiedDate = System.currentTimeMillis()

                // DB 업데이트
                vocabularyRepository.updateWord(word)
            }
        }
    }
}