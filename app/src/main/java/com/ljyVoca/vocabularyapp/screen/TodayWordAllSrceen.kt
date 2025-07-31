package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.BannerAdView
import com.ljyVoca.vocabularyapp.components.TodayWordCard
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@Composable
fun TodayWordAllScreen(
    vocabularyViewModel: VocabularyViewModel
) {
    val todayWordList by vocabularyViewModel.todayWordList.collectAsState()

    Scaffold{ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(bottom = 66.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.today_words),
                    style = AppTypography.fontSize20SemiBold.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.padding(16.dp)
                )

                todayWordList.forEach { word ->
                    TodayWordCard(
                        word = word,
                        ttsClick = {
                            vocabularyViewModel.speakOnlyWord(word)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
            BannerAdView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}