package com.ljyVoca.vocabularyapp.screen

import androidx.compose.ui.graphics.Color
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.QuizProgressSection
import com.ljyVoca.vocabularyapp.components.QuizWordCard
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@Composable
fun HandWriteModeScreen(
    navController: NavHostController,
    vocabularyViewModel: VocabularyViewModel
) {
    val currentQuizWord by vocabularyViewModel.currentQuizWord.collectAsState()
    val currentIndex by vocabularyViewModel.currentQuizIndex.collectAsState()
    val quizWordList by vocabularyViewModel.quizWordList.collectAsState()
    val isQuizCompleted by vocabularyViewModel.isQuizCompleted.collectAsState()
    val filterState by vocabularyViewModel.filterState.collectAsState()

    var showAnswer by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vocabularyViewModel.startQuiz()
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
                    studyMode= filterState.studyMode,
                    onShowClick = {
                        showAnswer = !showAnswer
                    },
                    ttsClick = {
                        vocabularyViewModel.speakWord(word)
                    }
                )
            }
            if(showAnswer) {
                Text(
                    text = if(filterState.quizType == QuizType.WORD_TO_MEANING)    {
                        "${stringResource(R.string.answer)}${currentQuizWord?.word}"
                    }else {
                        "${stringResource(R.string.answer)}${currentQuizWord?.mean}"
                    },
                    style = AppTypography.fontSize16Regular.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if(isQuizCompleted) {
                        navController.popBackStack()
                    }else {
                        if (currentIndex == quizWordList.size - 1) {
                            navController.popBackStack()
                        } else {
                            vocabularyViewModel.nextQuizWord()
                        }
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
        }
    }
}