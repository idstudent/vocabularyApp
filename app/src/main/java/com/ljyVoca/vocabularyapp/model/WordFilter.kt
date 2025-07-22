package com.ljyVoca.vocabularyapp.model

enum class StudyMode {
    HANDWRITING, INPUT
}

enum class QuizType {
    WORD_TO_MEANING, MEANING_TO_WORD
}

enum class WordFilter {
    ALL_WORDS, FREQUENTLY_WRONG
}

data class FilterState(
    val studyMode: StudyMode = StudyMode.HANDWRITING,
    val quizType: QuizType = QuizType.WORD_TO_MEANING,
    val wordFilter: WordFilter = WordFilter.ALL_WORDS
)