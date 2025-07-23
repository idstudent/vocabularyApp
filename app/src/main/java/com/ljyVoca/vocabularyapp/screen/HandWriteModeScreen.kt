package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.QuizWordCard
import com.ljyVoca.vocabularyapp.model.VocaWord
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

    LaunchedEffect(Unit) {
        vocabularyViewModel.startQuiz()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ProgressSection()
            currentQuizWord?.let { word ->
                QuizWordCard(
                    word = word,
                    quizType = filterState.quizType
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    vocabularyViewModel.nextQuizWord()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    }
}

@Composable
private fun ProgressSection() {
    val progress = 100f // TODO: 수정해야됨

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        Text(
            text = "10",  // TODO: 수정해야됨
            style = AppTypography.fontSize16Regular
        )
        Text(
            text = "/ 100", // TOOD 수정해야됨
            style = AppTypography.fontSize16Regular
        )
    }
}
