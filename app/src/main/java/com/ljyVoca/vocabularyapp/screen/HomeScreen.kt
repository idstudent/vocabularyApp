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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.WordCard
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val vocabularyViewModel: VocabularyViewModel = hiltViewModel()
    val wordList by vocabularyViewModel.wordList.collectAsState()

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
            TodayWordTitleSection()
            TodayCardSection(wordList)
        }
    }
}

@Composable
private fun TodayWordTitleSection() {
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

        // TODO: 단어없으면 안보이게 처리작업 필요
        Text(
            stringResource(R.string.more),
            style = AppTypography.fontSize16Regular.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(16.dp)
        )
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