package com.ljyVoca.vocabularyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.repository.SaveWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveWordViewModel @Inject constructor(
    private val wordSaveRepository: SaveWordRepository,
): ViewModel() {
    fun insertWord(word: VocaWord, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = wordSaveRepository.insertWord(word)
            onComplete(success)
        }
    }
    fun updateWord(word: VocaWord, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = wordSaveRepository.updateWord(word)
            onComplete(success)
        }
    }
    fun deleteWord(id: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            wordSaveRepository.deleteWord(id)
            onComplete()
        }
    }

}