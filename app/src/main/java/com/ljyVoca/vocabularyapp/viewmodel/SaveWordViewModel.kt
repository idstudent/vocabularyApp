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
    private val wordSaveRepository: SaveWordRepository
): ViewModel() {
    fun insertVoca(voca: VocaWord) {
        viewModelScope.launch {
            wordSaveRepository.insertVoca(voca)
        }
    }
}