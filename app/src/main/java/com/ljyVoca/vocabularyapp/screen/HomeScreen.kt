package com.ljyVoca.vocabularyapp.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.WordCard
import com.ljyVoca.vocabularyapp.model.FilterState
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.model.StudyMode
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.model.WordFilter
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.roundToInt
import androidx.compose.material3.Slider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val vocabularyViewModel: VocabularyViewModel = hiltViewModel()
    val todayWordList by vocabularyViewModel.todayWordList.collectAsState()

    val weeklyGoal by vocabularyViewModel.weeklyGoal.collectAsState()
    val thisWeekWords by vocabularyViewModel.thisWeekNewWords.collectAsState()

    val filterState by vocabularyViewModel.filterState.collectAsState()
    val filterBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val goalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    var showGoalBottomSheet by remember { mutableStateOf(false) }

    var hasFilterSetting by remember { mutableStateOf(false) }
    var hasGoal by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(vocabularyViewModel::getTodayWords)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("앱이름", style = AppTypography.fontSize20Regular)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
                Divider()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            GoalSection(
                hasGoal = hasGoal,
                thisWeekWords = thisWeekWords,
                goalWords = weeklyGoal,
                onClick =  { showGoalBottomSheet = true }
            )
            TodayWordTitleSection(todayWordList)
            TodayCardSection(todayWordList)
            WordFilterSection(
                filterClick = { showFilterBottomSheet = true },
                filterState = filterState,
                hasFilterSetting = hasFilterSetting
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    // TODO: 단어시작작업
                },
                enabled = hasFilterSetting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.start),
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    color = if(hasFilterSetting) {
                        MaterialTheme.colorScheme.onPrimary
                    }else {
                        MaterialTheme.colorScheme.onSecondary
                    }
                )
            }
        }

        if(showGoalBottomSheet) {
            GoalBottomSheet(
                bottomSheetState = goalBottomSheetState,
                onDismiss = { showGoalBottomSheet = false },
                onGoalChange = {
                    vocabularyViewModel.updateWeeklyGoal(it)
                    hasGoal = true
                }
            )
        }

        if(showFilterBottomSheet) {
            FilterBottomSheet(
                bottomSheetState = filterBottomSheetState,
                filterState = filterState,
                onDismiss = { showFilterBottomSheet = false },
                filterChange = {
                    vocabularyViewModel.updateFilterState(it)
                    hasFilterSetting = true
                }
            )
        }
    }
}

@Composable
private fun GoalSection(
    hasGoal: Boolean,
    thisWeekWords: Int = 0,    // 추가
    goalWords: Int = 0,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "이번주 목표", // 국제화 할 예정
            style = AppTypography.fontSize20SemiBold
        )

        if(hasGoal) {
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .clickable { onClick() },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "새 단어",
                            style = AppTypography.fontSize16SemiBold
                        )
                        Text(
                            text = "$thisWeekWords / $goalWords",
                            style = AppTypography.fontSize16SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val progress = if (goalWords > 0) {
                        (thisWeekWords / goalWords.toFloat()).coerceAtMost(1f)
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "목표를 변경하려면 터치하세요",
                        style = AppTypography.fontSize14Regular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }else {
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    ).clickable { onClick() },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "add",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.goal_setting),
                        style = AppTypography.fontSize16Regular.copy(MaterialTheme.colorScheme.onSecondary)
                    )
                }
            }
        }
    }
}
@Composable
private fun TodayWordTitleSection(todayWordList: List<VocaWord>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(R.string.today_words),
            style = AppTypography.fontSize20SemiBold,
            modifier = Modifier.padding(16.dp)
        )

        if(todayWordList.size > 4) {
            Text(
                stringResource(R.string.more),
                style = AppTypography.fontSize16Regular.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun TodayCardSection(wordList: List<VocaWord>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        wordList.take(4).forEach { word ->
            WordCard(word = word)
        }
    }
}

@Composable
private fun WordFilterSection(
    filterClick: () -> Unit,
    filterState: FilterState,
    hasFilterSetting: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = AppTypography.fontSize20SemiBold
            )
            IconButton(
                onClick = filterClick
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "filter"
                )
            }
        }
        FilterChipSection(filterState, hasFilterSetting)
    }
}

@Composable
private fun FilterChipSection(filterState: FilterState, hasFilterSetting: Boolean) {
    if(hasFilterSetting) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = getStudyModeDisplayName(filterState.studyMode),
                        style = AppTypography.fontSize14Regular
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                border = null
            )

            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = getQuizTypeDisplayName(filterState.quizType),
                        style = AppTypography.fontSize14Regular
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                border = null
            )

            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = getWordFilterDisplayName(filterState.wordFilter),
                        style = AppTypography.fontSize14Regular
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                border = null
            )
        }
    }else {
        Text(
            text = stringResource(R.string.empty_filter),
            style = AppTypography.fontSize16SemiBold.copy(MaterialTheme.colorScheme.onSecondary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalBottomSheet(
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    currentGoal: Int = 20,
    onGoalChange: (Int) -> Unit = {}
) {
    var tempGoal by remember { mutableIntStateOf(currentGoal) }
    var isEditingText by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "주간 학습 목표",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 현재 목표 표시 (터치하면 직접 입력)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isEditingText = true
                        textFieldValue = tempGoal.toString()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isEditingText) {
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            // 숫자만 입력 가능, 최대 3자리
                            if (newValue.all { char -> char.isDigit() } && newValue.length <= 3) {
                                textFieldValue = newValue
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val newGoal = textFieldValue.toIntOrNull() ?: tempGoal
                                tempGoal = newGoal.coerceIn(1, 300) // 1~300 범위로 제한
                                isEditingText = false
                            }
                        ),
                        modifier = Modifier.width(120.dp),
                        textStyle = AppTypography.fontSize24ExtraBold.copy(
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = "${tempGoal}개",
                        style = AppTypography.fontSize24ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (!isEditingText) {
                Text(
                    text = "숫자를 터치하면 직접 입력할 수 있어요",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 슬라이더 (5~300, 5간격)
            Slider(
                value = tempGoal.toFloat(),
                onValueChange = { newValue ->
                    // 5의 배수로 반올림
                    val roundedValue = (newValue / 5f).roundToInt() * 5
                    tempGoal = roundedValue.coerceIn(5, 300)
                    isEditingText = false // 슬라이더 조작시 텍스트 편집 모드 해제
                },
                valueRange = 5f..300f,
                steps = 58, // (300-5)/5 = 59단계, steps = 단계수-1
                modifier = Modifier.fillMaxWidth()
            )

            // 범위 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "5개",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "300개",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    onGoalChange(tempGoal)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("설정 완료")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    bottomSheetState: SheetState,
    filterState: FilterState,
    onDismiss: () -> Unit,
    filterChange: (FilterState) -> Unit
) {
    var tempFilterState by remember { mutableStateOf(filterState) }

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
                onOptionSelected = { filter ->
                    tempFilterState = tempFilterState.copy(wordFilter = filter)
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
    onOptionSelected: (T) -> Unit
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

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        .clickable { onOptionSelected(enumValue) }
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
}


@Composable
private fun getStudyModeDisplayName(mode: StudyMode): String {
    return when (mode) {
        StudyMode.HANDWRITING -> stringResource(R.string.study_mode_handwriting)
        StudyMode.INPUT -> stringResource(R.string.study_mode_input)
    }
}

@Composable
private fun getQuizTypeDisplayName(type: QuizType): String {
    return when (type) {
        QuizType.WORD_TO_MEANING -> stringResource(R.string.quiz_type_word_to_meaning)
        QuizType.MEANING_TO_WORD -> stringResource(R.string.quiz_type_meaning_to_word)
    }
}

@Composable
private fun getWordFilterDisplayName(filter: WordFilter): String {
    return when (filter) {
        WordFilter.ALL_WORDS -> stringResource(R.string.word_filter_all)
        WordFilter.FREQUENTLY_WRONG -> stringResource(R.string.word_filter_frequently_wrong)
    }
}