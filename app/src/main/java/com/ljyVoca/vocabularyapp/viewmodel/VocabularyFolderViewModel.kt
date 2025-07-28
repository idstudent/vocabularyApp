package com.ljyVoca.vocabularyapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.Vocabulary
import com.ljyVoca.vocabularyapp.repository.VocabularyFolderRepository
import com.ljyVoca.vocabularyapp.util.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyFolderViewModel @Inject constructor(
    private val vocabularyFolderRepository: VocabularyFolderRepository,
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

    fun speakWord(word: VocaWord) {
        ttsManager.speak(word.word)
    }
    val vocabularyFolders: StateFlow<List<Vocabulary>> = vocabularyFolderRepository.getVocabulary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _words = MutableStateFlow<List<VocaWord>>(emptyList())
    val vocabularyWords: StateFlow<List<VocaWord>> = _words.asStateFlow()

    private val _wordsCount = MutableStateFlow(0)
    val wordsCount: StateFlow<Int> = _wordsCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 0
    private val pageSize = 50
    private var currentVocabularyId: String? = null
    private var hasMoreData = true

    fun clearWords() {
        _wordsCount.value = 0
        _words.value = emptyList()
        _isLoading.value = true
    }

    fun getSaveWordsCount(id: String) {
        viewModelScope.launch {
            val count = vocabularyFolderRepository.getSaveWordsCount(id)
            _wordsCount.value = count
        }
    }
    fun selectVocabularyFolder(id: String) {
        currentVocabularyId = id
        currentPage = 0
        hasMoreData = true

        loadFirstPage()
    }

    private fun loadFirstPage() {
        if (currentVocabularyId == null) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val words = vocabularyFolderRepository.getWordsByPage(
                    currentVocabularyId!!,
                    pageSize,
                    0
                )
                _words.value = words
                hasMoreData = words.size == pageSize
                currentPage = 0
            } catch (e: Exception) {
                Log.e("ljy", "단어 로딩 실패", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreWords() {
        if (_isLoading.value || !hasMoreData || currentVocabularyId == null) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                currentPage++
                val offset = currentPage * pageSize
                val newWords = vocabularyFolderRepository.getWordsByPage(
                    currentVocabularyId!!,
                    pageSize,
                    offset
                )

                _words.value += newWords
                hasMoreData = newWords.size == pageSize
            } catch (e: Exception) {
                currentPage-- // 실패시 페이지 롤백
                Log.e("ljy", "추가 단어 로딩 실패", e)
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun insertVocabulary(vocabulary: Vocabulary) {
        viewModelScope.launch {
            if (vocabularyFolders.value.size < 20) {
                vocabularyFolderRepository.insertVocabulary(vocabulary)
            }
        }
    }

    fun updateVocabulary(vocabulary: Vocabulary, onComplete: () -> Unit) {
        viewModelScope.launch {
            vocabularyFolderRepository.updateVocabulary(vocabulary)
            onComplete()
        }
    }

    fun deleteVocabulary(id: String) {
        viewModelScope.launch {
            vocabularyFolderRepository.deleteWordsByVocabularyId(id)
            vocabularyFolderRepository.deleteVocabularyById(id)
        }
    }
}