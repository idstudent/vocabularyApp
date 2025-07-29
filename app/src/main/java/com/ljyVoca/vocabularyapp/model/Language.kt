package com.ljyVoca.vocabularyapp.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.TypeConverter
import com.ljyVoca.vocabularyapp.R

enum class Language(val code: String, val displayNameRes: Int) {
    ENGLISH("en", R.string.language_english),
    KOREAN("ko", R.string.language_korean),
    JAPANESE("ja", R.string.language_japanese),
    CHINESE("zh", R.string.language_chinese),
    SPANISH("es", R.string.language_spanish);

    @Composable
    fun displayName(): String = stringResource(displayNameRes)
}

class Converters {
    @TypeConverter
    fun fromLanguage(language: Language): String {
        return language.code
    }

    @TypeConverter
    fun toLanguage(code: String): Language {
        return Language.values().find { it.code == code } ?: Language.ENGLISH
    }
}