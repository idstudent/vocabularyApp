package com.ljyVoca.vocabularyapp.screen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.QuizProgressSection
import com.ljyVoca.vocabularyapp.components.QuizWordCard
import com.ljyVoca.vocabularyapp.model.QuizType
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

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

    LaunchedEffect(Unit) {
        vocabularyViewModel.startQuiz()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            QuizProgressSection(
                navController = navController,
                currentIndex = currentIndex + 1,
                totalCount = quizWordList.size
            )
            currentQuizWord?.let { word ->
                QuizWordCard(
                    word = word,
                    quizType = filterState.quizType,
                    studyMode = filterState.studyMode,
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
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            TextField(
                value = answer,
                onValueChange = {
                    answer = it.trim()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                maxLines = 2,
                textStyle = AppTypography.fontSize16SemiBold.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if(isQuizCompleted) {
                        navController.popBackStack()
                    }else {
                        // TODO: 여기서 정답체크해서 듀오링고처럼 정답인지, 틀렸는지 확인후 버튼 한번 더눌러야 다음으로 가는방식 아니면 다른방법없나
                        vocabularyViewModel.nextQuizWord()
                    }
                    showAnswer = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 36.dp)
                    .height(48.dp)

            ) {
                Text(
                    if(isQuizCompleted) stringResource(R.string.complete)
                    else stringResource(R.string.next),
                    style = AppTypography.fontSize20Regular
                )
            }
        }
    }
}

