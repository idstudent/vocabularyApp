package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.WordCard
import com.ljyVoca.vocabularyapp.navigation.AppRoutes
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyDetailScreen(
    navController: NavHostController,
    id: String,
    category: String,
    title: String,
    vocabularyFolderViewModel: VocabularyFolderViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(id) {
        vocabularyFolderViewModel.clearWords()
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vocabularyFolderViewModel.selectVocabularyFolder(id)
                vocabularyFolderViewModel.getSaveWordsCount(id)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val words by vocabularyFolderViewModel.vocabularyWords.collectAsState()
    val count by vocabularyFolderViewModel.wordsCount.collectAsState()

    val isLoading by vocabularyFolderViewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    // 스크롤 하단 감지
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 &&
                    lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    // 하단 도달 시 더 로드
    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !isLoading) {
            vocabularyFolderViewModel.loadMoreWords()
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(title, style = AppTypography.fontSize20Regular)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
                Divider()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("${AppRoutes.ADD_WORD_SCREEN}/$category/$id")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add",
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
        ) {
            Text(
                text = "${stringResource(R.string.word_count)} $count",
                style = AppTypography.fontSize16Regular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                textAlign = TextAlign.End
            )
            if (isLoading && words.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(words) {
                        WordCard(
                            word = it,
                            ttsClick = { word ->
                                vocabularyFolderViewModel.speakWord(word)
                            },
                            onUpdate =  {
                                navController.currentBackStackEntry?.savedStateHandle?.set("word", it)
                                navController.navigate(AppRoutes.UPDATE_WORD_SCREEN)
                            },
                            onDelete = {
                                // TODO:  삭제 다이얼로그
                            }
                        )
                    }
                }
            }
        }
    }

}