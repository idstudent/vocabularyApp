package com.ljyVoca.vocabularyapp.screen


import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
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
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.model.FilterState
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.components.FilterBottomSheet
import com.ljyVoca.vocabularyapp.components.GoalBottomSheet
import com.ljyVoca.vocabularyapp.components.TodayWordCard
import com.ljyVoca.vocabularyapp.components.getQuizTypeDisplayName
import com.ljyVoca.vocabularyapp.components.getStudyModeDisplayName
import com.ljyVoca.vocabularyapp.components.getWordFilterDisplayName
import com.ljyVoca.vocabularyapp.model.StudyMode
import com.ljyVoca.vocabularyapp.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vocabularyViewModel: VocabularyViewModel
) {
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

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(vocabularyViewModel::getTodayWords)

    val availableLanguages by vocabularyViewModel.availableLanguages.collectAsState()
    val hasFrequentlyWrongWords by vocabularyViewModel.hasFrequentlyWrongWords.collectAsState()

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
                hasGoal = weeklyGoal > 0,
                thisWeekWords = thisWeekWords,
                goalWords = weeklyGoal,
                onClick =  { showGoalBottomSheet = true }
            )
            TodayWordTitleSection(navController, todayWordList)
            TodayCardSection(todayWordList, vocabularyViewModel)
            WordFilterSection(
                filterClick = { showFilterBottomSheet = true },
                filterState = filterState,
                hasFilterSetting = hasFilterSetting
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if(filterState.studyMode == StudyMode.HANDWRITING) {
                        navController.navigate(AppRoutes.HANDLE_WRITE_MODE_SCREEN)
                    }else {
                        navController.navigate(AppRoutes.QUIZ_MODE_SCREEN)
                    }

                },
                enabled = hasFilterSetting && availableLanguages.isNotEmpty(),
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
                currentGoal = weeklyGoal,
                onGoalChange = {
                    vocabularyViewModel.updateWeeklyGoal(it)
                }
            )
        }

        if(showFilterBottomSheet) {
            FilterBottomSheet(
                bottomSheetState = filterBottomSheetState,
                filterState = filterState,
                availableLanguages = availableLanguages,
                hasFrequentlyWrongWords = hasFrequentlyWrongWords,
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
    thisWeekWords: Int = 0,
    goalWords: Int = 0,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.weekly_goal),
            style = AppTypography.fontSize20SemiBold
        )

        if(hasGoal) {
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    )
                    .clickable { onClick() },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
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
                            text = stringResource(R.string.new_words),
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
                        modifier = Modifier.fillMaxWidth().height(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.touch_to_change_goal),
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
                    )
                    .clickable { onClick() },
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
private fun TodayWordTitleSection(
    navController: NavHostController,
    todayWordList: List<VocaWord>
) {
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
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate(AppRoutes.TODAY_ALL_WORD_SCREEN)
                    }
            )
        }
    }
}

@Composable
private fun TodayCardSection(wordList: List<VocaWord>, vocabularyViewModel: VocabularyViewModel) {
    if(wordList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            wordList.take(4).forEach { word ->
                TodayWordCard(
                    word = word,
                    ttsClick = {
                        vocabularyViewModel.speakWord(word)
                    },
                    modifier = Modifier
                        .width(240.dp)
                        .height(200.dp)
                )
            }
        }
    }else {
        TodayCardEmptySection()
    }
}

@Composable
private fun TodayCardEmptySection() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.today_word_empty),
                style = AppTypography.fontSize16Regular.copy(MaterialTheme.colorScheme.onSecondary),
            )
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
                .padding(start = 16.dp, end = 16.dp),
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
                .padding(start = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = if (filterState.selectedLanguage != null) {
                            filterState.selectedLanguage.displayName()
                        } else {
                            stringResource(R.string.all_languages)
                        },
                        style = AppTypography.fontSize14Regular
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    labelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                border = null
            )
            
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


