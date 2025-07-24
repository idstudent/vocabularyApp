package com.ljyVoca.vocabularyapp.model

data class AnswerResult(
    val isCorrect: Boolean,
    val allMeanings: List<String>,
    val matchedMeaning: String?
)