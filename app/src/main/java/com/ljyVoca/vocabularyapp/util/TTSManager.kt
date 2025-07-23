package com.ljyVoca.vocabularyapp.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import java.util.Locale

class TTSManager(private val context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    // Lingua 감지기
    private val detector = LanguageDetectorBuilder.fromLanguages(
        Language.ENGLISH,
        Language.SPANISH,
        Language.JAPANESE,
        Language.CHINESE,
        Language.KOREAN
    ).build()

    fun initialize(onInitComplete: (Boolean) -> Unit = {}) {
        textToSpeech = TextToSpeech(context) { status ->
            isInitialized = status == TextToSpeech.SUCCESS
            onInitComplete(isInitialized)
        }
    }

    fun speak(text: String) {
        if (!isInitialized) {
            Log.w("TTSManager", "TTS not initialized")
            return
        }

        val detectedLanguage = detector.detectLanguageOf(text)
        val languageCode = when(detectedLanguage) {
            Language.KOREAN -> "ko"
            Language.JAPANESE -> "ja"
            Language.CHINESE -> "zh"
            Language.SPANISH -> "es"
            Language.ENGLISH -> "en"
            else -> "en"
        }

        setLanguage(languageCode)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setLanguage(languageCode: String) {
        val locale = LANGUAGE_LOCALES[languageCode] ?: Locale.US
        val result = textToSpeech?.setLanguage(locale)

        when (result) {
            TextToSpeech.LANG_MISSING_DATA -> {
                return
            }
            TextToSpeech.LANG_NOT_SUPPORTED -> {
                return
            }
        }
    }

    fun stop() {
        textToSpeech?.stop()
    }

    // README에 명시된 정확한 메모리 해제 방법
    fun shutdown() {
        // TTS 리소스 해제
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false

        // Lingua 리소스 해제 (README 9.1.7 섹션)
        detector.unloadLanguageModels()
        // 참고: 이렇게 해도 thread pool은 계속 실행됨 (README에 명시됨)
    }

    companion object {
        private val LANGUAGE_LOCALES = mapOf(
            "ko" to Locale.KOREAN,
            "en" to Locale.US,
            "ja" to Locale.JAPANESE,
            "zh" to Locale.CHINESE,
            "es" to Locale("es", "ES")
        )
    }
}