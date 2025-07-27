package com.ljyVoca.vocabularyapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voca")
data class VocaWord(
    @PrimaryKey
    val word: String,
    val mean: String,
    val phonetic: String = "",           // 발음기호
    val description: String = "",        // 부연설명
    var wrongCount: Int = 0,             // 틀린 횟수
    var totalAttempts: Int = 0,          // 총 시도 횟수
    var lastStudiedDate: Long = 0,       // 마지막 학습 날짜
    var category: String = "en",        // 언어 카테고리
    var vocabularyId: String = "",
    var isBookmarked: Boolean = false,   // 즐겨찾기
    val createdDate: Long = System.currentTimeMillis(),
    var ending: Boolean = false          // 마지막 단어 여부

) {
    // 정답률 계산
    val accuracy: Float get() = if (totalAttempts > 0) {
        (totalAttempts - wrongCount).toFloat() / totalAttempts
    } else 0f

    // 자주 틀린 단어 판정
    val isFrequentlyWrong: Boolean get() = totalAttempts >= 3 && accuracy < 0.5f
}