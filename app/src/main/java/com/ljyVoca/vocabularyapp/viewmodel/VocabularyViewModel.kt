package com.ljyVoca.vocabularyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository
): ViewModel() {
    private val _wordList = MutableStateFlow<List<VocaWord>>(emptyList())
    val wordList = _wordList.asStateFlow()

    private val _currentWord = MutableStateFlow<VocaWord?>(null)
    val currentWord = _currentWord.asStateFlow()

    private var currentIndex = 0

    init {
        viewModelScope.launch {
            _wordList.value = vocabularyRepository.getAllWord().shuffled()

            if(_wordList.value.isNotEmpty()) {
                _currentWord.value = _wordList.value[0]
            }
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

}