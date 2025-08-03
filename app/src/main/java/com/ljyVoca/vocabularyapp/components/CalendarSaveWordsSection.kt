package com.ljyVoca.vocabularyapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.navigation.AppRoutes
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.CalendarViewModel

@Composable
fun CalendarSaveWordSection(
    navController: NavHostController,
    vocaWord: List<VocaWord>,
    calendarViewModel: CalendarViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "저장한 단어",
                style = AppTypography.fontSize20SemiBold.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            if(vocaWord.size > 4) {
                Text(
                    text = stringResource(R.string.more),
                    style = AppTypography.fontSize16Regular.copy(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(AppRoutes.CALENDAR_SAVE_WORDS_MORE_SCREEN)
                        }
                        .padding(8.dp)
                )
            }
        }
        HorizontalCardSection(wordList = vocaWord, calendarViewModel = calendarViewModel)

    }
}

@Composable
private fun HorizontalCardSection(wordList: List<VocaWord>, calendarViewModel: CalendarViewModel) {
    if(wordList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            wordList.take(4).forEach { word ->
                TodayWordCard(
                    word = word,
                    ttsClick = {
                        calendarViewModel.speakWord(word)
                    },
                    modifier = Modifier
                        .width(240.dp)
                        .height(200.dp)
                )
            }
        }
    }else {
        SelectDayCardEmptySection()
    }
}

@Composable
private fun SelectDayCardEmptySection() {
    Card(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(120.dp)
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
                text = stringResource(R.string.empty_save_word),
                style = AppTypography.fontSize16Regular.copy(MaterialTheme.colorScheme.onSecondary),
            )
        }
    }
}