package com.ljyVoca.vocabularyapp.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.model.FilterState
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.model.StudyMode
import com.ljyVoca.vocabularyapp.model.WordFilter
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    bottomSheetState: SheetState,
    filterState: FilterState,
    availableLanguages: List<Language> = emptyList(),
    hasFrequentlyWrongWords: Boolean = false,
    onDismiss: () -> Unit,
    filterChange: (FilterState) -> Unit
) {
    val initialLanguage = when {
        availableLanguages.size == 1 -> availableLanguages.first()
        else -> null  // 전체
    }

    var tempFilterState by remember {
        mutableStateOf(
            filterState.copy(
                selectedLanguage = filterState.selectedLanguage ?: initialLanguage
            )
        )
    }
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ){
            Text(
                text = stringResource(R.string.study_settings),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (availableLanguages.isNotEmpty()) {  // 언어가 하나라도 있으면 표시
                FilterSection(
                    title = stringResource(R.string.language_title),
                    options = FilterOptions.getLanguages(availableLanguages),
                    selectedOption = tempFilterState.selectedLanguage,
                    onOptionSelected = { language ->
                        tempFilterState = tempFilterState.copy(selectedLanguage = language)
                    }
                )
            }

            FilterSection(
                title = stringResource(R.string.mode_selection),
                options = FilterOptions.getStudyModes(),
                selectedOption =  tempFilterState.studyMode,
                onOptionSelected = { mode ->
                    tempFilterState = tempFilterState.copy(studyMode = mode)
                }
            )

            FilterSection(
                title = stringResource(R.string.quiz_type),
                options = FilterOptions.getQuizTypes(),
                selectedOption = tempFilterState.quizType,
                onOptionSelected = { type ->
                    tempFilterState = tempFilterState.copy(quizType = type)
                }
            )

            FilterSection(
                title = stringResource(R.string.word_range),
                options = FilterOptions.getWordFilters(),
                selectedOption = tempFilterState.wordFilter,
                disabledOptions = if (!hasFrequentlyWrongWords) setOf(WordFilter.FREQUENTLY_WRONG) else emptySet(),
                onOptionSelected = { filter ->
                    tempFilterState = tempFilterState.copy(wordFilter = filter)
                },
                onDisabledOptionClicked = { filter ->
                    if (filter == WordFilter.FREQUENTLY_WRONG) {
                        Toast.makeText(context, context.getString(R.string.no_frequently_wrong_words), Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Button(
                onClick = {
                    filterChange(tempFilterState)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.ok))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun <T> FilterSection(
    title: String,
    options: List<Pair<T, String>>,
    selectedOption: T,
    disabledOptions: Set<T> = emptySet(),
    onOptionSelected: (T) -> Unit,
    onDisabledOptionClicked: (T) -> Unit = {}
) {
    Column {
        Text(
            text = title,
            style = AppTypography.fontSize16SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEach { (enumValue, displayName) ->
                val isSelected = selectedOption == enumValue
                val isDisabled = enumValue in disabledOptions

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            when {
                                isDisabled -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            }
                        )
                        .clickable {
                            if (isDisabled) {
                                onDisabledOptionClicked(enumValue)  // 비활성화된 옵션 클릭 시
                            } else {
                                onOptionSelected(enumValue)  // 정상 클릭
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = displayName,
                        style = AppTypography.fontSize14Regular,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

object FilterOptions {
    @Composable
    fun getStudyModes() = listOf(
        StudyMode.HANDWRITING to stringResource(R.string.study_mode_handwriting),
        StudyMode.INPUT to stringResource(R.string.study_mode_input)
    )

    @Composable
    fun getQuizTypes() = listOf(
        QuizType.WORD_TO_MEANING to stringResource(R.string.quiz_type_word_to_meaning),
        QuizType.MEANING_TO_WORD to stringResource(R.string.quiz_type_meaning_to_word)
    )

    @Composable
    fun getWordFilters() = listOf(
        WordFilter.ALL_WORDS to stringResource(R.string.word_filter_all),
        WordFilter.FREQUENTLY_WRONG to stringResource(R.string.word_filter_frequently_wrong)
    )

    @Composable
    fun getLanguages(availableLanguages: List<Language>) = buildList {
        if (availableLanguages.size > 1) {
            add(null to stringResource(R.string.all_languages))
        }

        availableLanguages.forEach { language ->
            add(language to language.displayName())
        }
    }

}


@Composable
fun getStudyModeDisplayName(mode: StudyMode): String {
    return when (mode) {
        StudyMode.HANDWRITING -> stringResource(R.string.study_mode_handwriting)
        StudyMode.INPUT -> stringResource(R.string.study_mode_input)
    }
}

@Composable
fun getQuizTypeDisplayName(type: QuizType): String {
    return when (type) {
        QuizType.WORD_TO_MEANING -> stringResource(R.string.quiz_type_word_to_meaning)
        QuizType.MEANING_TO_WORD -> stringResource(R.string.quiz_type_meaning_to_word)
    }
}

@Composable
fun getWordFilterDisplayName(filter: WordFilter): String {
    return when (filter) {
        WordFilter.ALL_WORDS -> stringResource(R.string.word_filter_all)
        WordFilter.FREQUENTLY_WRONG -> stringResource(R.string.word_filter_frequently_wrong)
    }
}