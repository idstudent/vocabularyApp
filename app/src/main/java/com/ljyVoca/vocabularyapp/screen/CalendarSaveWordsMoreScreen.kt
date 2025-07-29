package com.ljyVoca.vocabularyapp.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.TodayWordCard
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.CalendarViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarSaveWordsMoreScreen(
    calendarViewModel: CalendarViewModel
) {
    val selectedDateWords by calendarViewModel.selectedDateWords.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.vocabulary),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            selectedDateWords.forEach { word ->
                TodayWordCard(
                    word = word,
                    ttsClick =  {
                        calendarViewModel.speakWord(word)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}