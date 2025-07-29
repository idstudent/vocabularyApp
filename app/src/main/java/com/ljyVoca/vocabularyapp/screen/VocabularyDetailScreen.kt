package com.ljyVoca.vocabularyapp.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.ljyVoca.vocabularyapp.viewmodel.SaveWordViewModel
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyDetailScreen(
    navController: NavHostController,
    id: String,
    category: String,
    title: String,
    saveWordViewModel: SaveWordViewModel,
    vocabularyFolderViewModel: VocabularyFolderViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteWordId by remember { mutableStateOf<String?>(null) }

    val toastDelete = stringResource(R.string.toast_delete_word)

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val words by vocabularyFolderViewModel.vocabularyWords.collectAsState()
    val count by vocabularyFolderViewModel.wordsCount.collectAsState()

    val isLoading by vocabularyFolderViewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }

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

    val filteredWords = remember(words, searchQuery) {
        if (searchQuery.isEmpty()) {
            words
        } else {
            words.filter { word ->
                word.word.contains(searchQuery, ignoreCase = true) ||
                        word.mean.contains(searchQuery, ignoreCase = true)
            }
        }
    }

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
        if (reachedBottom && !isLoading && searchQuery.isEmpty()) {
            vocabularyFolderViewModel.loadMoreWords()
        }
    }

    Scaffold(
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
            Spacer(Modifier.height(24.dp))
            Text(
                text = title,
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.hint_search),
                        style = AppTypography.fontSize16Regular
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,     // 포커스된 밑줄
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,   // 일반 밑줄
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,     // 포커스된 배경
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,   // 일반 배경
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary // 비활성 배경
                )
            )

            if (searchQuery.isNotEmpty()) {
                Text(
                    text = "${stringResource(R.string.search_result)} ${filteredWords.size}",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            } else {
                Text(
                    text = "${stringResource(R.string.word_count)} $count",
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    textAlign = TextAlign.End
                )
            }

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
                    items(filteredWords) {
                        WordCard(
                            word = it,
                            ttsClick = { word ->
                                vocabularyFolderViewModel.speakWord(word)
                            },
                            onUpdate =  {
                                navController.currentBackStackEntry?.savedStateHandle?.set("word", it)
                                navController.navigate(AppRoutes.UPDATE_WORD_SCREEN)
                            },
                            onDelete = { id ->
                                showDeleteDialog = true
                                deleteWordId = id
                            }
                        )
                    }
                }
            }
        }
    }
    if(showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.delete),
                    style = AppTypography.fontSize16SemiBold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_word),
                    style = AppTypography.fontSize16Regular
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deleteWordId?.let {
                            saveWordViewModel.deleteWord(it) {
                                vocabularyFolderViewModel.selectVocabularyFolder(id)
                                vocabularyFolderViewModel.getSaveWordsCount(id)
                                Toast.makeText(context, toastDelete, Toast.LENGTH_SHORT).show()
                            }
                        }
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
                    onClick = { showDeleteDialog = false }
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

}