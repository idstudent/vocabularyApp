package com.ljyVoca.vocabularyapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.QuizProgressSection
import com.ljyVoca.vocabularyapp.components.QuizResultBottomSheet
import com.ljyVoca.vocabularyapp.components.QuizWordCard
import com.ljyVoca.vocabularyapp.model.AnswerResult
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizModeScreen(
    navController: NavHostController,
    vocabularyViewModel: VocabularyViewModel
) {
    val currentQuizWord by vocabularyViewModel.currentQuizWord.collectAsState()
    val currentIndex by vocabularyViewModel.currentQuizIndex.collectAsState()
    val quizWordList by vocabularyViewModel.quizWordList.collectAsState()
    val isQuizCompleted by vocabularyViewModel.isQuizCompleted.collectAsState()
    val filterState by vocabularyViewModel.filterState.collectAsState()

    var showAnswer by remember { mutableStateOf(false) }

    var answer by remember { mutableStateOf("") }

    val resultBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showResultBottomSheet by remember { mutableStateOf(false) }
    var answerResult by remember { mutableStateOf<AnswerResult?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }

    // 뒤로가기 버튼 처리
    BackHandler {
        if (showResultBottomSheet) {
            // 바텀시트 열려있으면 닫기
            showResultBottomSheet = false
        } else {
            // 바텀시트 닫혀있으면 종료 다이얼로그
            showExitDialog = true
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.quiz_exit),
                    style = AppTypography.fontSize16SemiBold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.quiz_exit_confirmation),
                    style = AppTypography.fontSize16Regular
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        showResultBottomSheet = false  // 바텀시트 닫기
                        navController.popBackStack()   // 뒤로가기
                    }
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = AppTypography.fontSize16SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = AppTypography.fontSize16Regular,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    LaunchedEffect(Unit) {
        vocabularyViewModel.startQuiz()
    }

    fun checkAnswer(userAnswer:String, correctMean: String): AnswerResult {
        val meanings = correctMean.split(",", "、").map { it.trim() }
        val userTrimmed = userAnswer.trim()

        val matchedMeaning = meanings.find { meaning ->
            userTrimmed.equals(meaning, ignoreCase = true) // ignorecase = true는 대,소문자 무시하고 비교
        }

        return AnswerResult(
            isCorrect = matchedMeaning != null,
            allMeanings = meanings,
            matchedMeaning = matchedMeaning
        )
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            QuizProgressSection(
                currentIndex = currentIndex + 1,
                totalCount = quizWordList.size
            )
            currentQuizWord?.let { word ->
                QuizWordCard(
                    word = word,
                    quizType = filterState.quizType,
                    studyMode = filterState.studyMode,
                    onShowClick = {},
                    ttsClick = {
                        vocabularyViewModel.speakWord(word)
                    },
                )
            }

            TextField(
                value = answer,
                onValueChange = {
                    answer = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,     // 포커스된 밑줄
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,   // 일반 밑줄
                    focusedContainerColor = MaterialTheme.colorScheme.onBackground,     // 포커스된 배경
                    unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,   // 일반 배경
                    disabledContainerColor = MaterialTheme.colorScheme.onBackground // 비활성 배경
                ),
                textStyle = AppTypography.fontSize16SemiBold.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if(isQuizCompleted) {
                        navController.popBackStack()
                    }else {
                        val result = if(filterState.quizType == QuizType.WORD_TO_MEANING) {
                            checkAnswer(answer, currentQuizWord?.word ?: "")
                        } else {
                            checkAnswer(answer, currentQuizWord?.mean ?: "")
                        }

                        vocabularyViewModel.processQuizResult(result.isCorrect)

                        answerResult = result
                        showResultBottomSheet = true
                    }
                    showAnswer = false

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 36.dp)
                    .height(52.dp)

            ) {
                Text(
                    if(isQuizCompleted) stringResource(R.string.complete)
                    else stringResource(R.string.next),
                    style = AppTypography.fontSize20Regular.copy(
                        color = Color.White
                    )
                )
            }

            if(showResultBottomSheet && answerResult != null) {
                QuizResultBottomSheet(
                    bottomSheetState = resultBottomSheetState,
                    answerResult = answerResult!!,
                    onDismiss = { showResultBottomSheet = false },
                    onNext = {
                        if (currentIndex == quizWordList.size - 1) {
                            navController.popBackStack()
                        } else {
                            vocabularyViewModel.nextQuizWord()
                            answer = ""
                            answerResult = null
                        }
                    },
                    isLastQuestion = currentIndex == quizWordList.size - 1
                )
            }
        }
    }
}

