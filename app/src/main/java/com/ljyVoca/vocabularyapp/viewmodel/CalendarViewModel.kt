package com.ljyVoca.vocabularyapp.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.repository.CalendarRepository
import com.ljyVoca.vocabularyapp.util.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val ttsManager = TTSManager(context)

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

    // 첫 단어 저장 월
    private val _firstWordMonth = MutableStateFlow<YearMonth?>(null)
    val firstWordMonth = _firstWordMonth.asStateFlow()

    // 선택한 달의 단어들 (단어 저장한 날짜 점 표시용으로)
    private val _currentMonthWords = MutableStateFlow<List<VocaWord>>(emptyList())
    val currentMonthWords = _currentMonthWords.asStateFlow()

    // 선택한 달
    private val _currentViewingMonth = MutableStateFlow("")
    val currentViewingMonth = _currentViewingMonth.asStateFlow()

    // 선택된 날짜의 단어들
    private val _selectedDateWords = MutableStateFlow<List<VocaWord>>(emptyList())
    val selectedDateWords = _selectedDateWords.asStateFlow()

    init {
        viewModelScope.launch {
            val firstDate = calendarRepository.getFirstWordDate()
            firstDate?.let { timestamp ->
                val localDate = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                _firstWordMonth.value = YearMonth.from(localDate)
            }
        }
    }

    fun getMonthData(yearMonth: YearMonth) {
        val yearMonthString = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))

        // 같은 월을 보고 있다면 다시 불러오지 않음
        if (_currentViewingMonth.value == yearMonthString) return

        _currentViewingMonth.value = yearMonthString

        viewModelScope.launch {
            try {
                // 해당 월의 단어들 가져오기
                val nowMonthWords = calendarRepository.getWordsByMonth(yearMonthString)
                _currentMonthWords.value = nowMonthWords
            }catch (e: Exception) {
                _currentMonthWords.value = emptyList()
            }
        }
    }

    fun getWordsByDay(date: LocalDate) {
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        viewModelScope.launch {
            try {
                _selectedDateWords.value =  calendarRepository.getWordsByDay(dateString)
            }catch (e: Exception) {
                _selectedDateWords.value = emptyList()
            }
        }
    }

    fun speakWord(word: VocaWord) {
        ttsManager.speak(word.word)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}