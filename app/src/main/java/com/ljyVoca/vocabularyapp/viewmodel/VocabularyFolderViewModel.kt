package com.ljyVoca.vocabularyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljyVoca.vocabularyapp.model.Vocabulary
import com.ljyVoca.vocabularyapp.repository.VocabularyFolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyFolderViewModel @Inject constructor(
    private val vocabularyFolderRepository: VocabularyFolderRepository
): ViewModel() {
    val vocabularyFolders: StateFlow<List<Vocabulary>> = vocabularyFolderRepository.getVocabulary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteVocabulary(id: String) {
        viewModelScope.launch {
            vocabularyFolderRepository.deleteVocabulary(id)
        }
    }
}