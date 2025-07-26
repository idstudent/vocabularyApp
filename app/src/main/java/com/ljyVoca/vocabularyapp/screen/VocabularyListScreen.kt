package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.VocabularyFolder
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyListScreen(
    navController: NavHostController,
    vocabularyFolderViewModel: VocabularyFolderViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteVocabularyId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("단어장", style = AppTypography.fontSize20Regular)
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
                    // TODO: 단어장 만드는 화면
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
        val vocabularyFolders by vocabularyFolderViewModel.vocabularyFolders.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            vocabularyFolders.map {
                VocabularyFolder(
                    vocabulary = it,
                    onClick = {
                        // TODO: 단어장 상세
                    },
                    onInsert = {
                        // TODO: 단어장 수정
                    },
                    onDelete = { id ->
                        showDeleteDialog = true
                        deleteVocabularyId = id
                    },
                )
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
                    text = stringResource(R.string.delete_vocabulary),
                    style = AppTypography.fontSize16Regular
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deleteVocabularyId?.let { vocabularyFolderViewModel.deleteVocabulary(it) }
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