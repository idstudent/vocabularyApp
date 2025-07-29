package com.ljyVoca.vocabularyapp.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.InputTextFieldSection
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.SaveWordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    category: String,
    vocaId: String,
    saveWordViewModel: SaveWordViewModel
) {
    var wordTextFieldValue by remember { mutableStateOf("") }
    var meanTextFieldValue by remember { mutableStateOf("") }
    var descriptionTextFieldValue by remember { mutableStateOf("") }

    val context = LocalContext.current

    val toastEmptyWord = stringResource(R.string.hint_word)
    val toastEmptyMean = stringResource(R.string.hint_mean)
    val toastComplete = stringResource(R.string.toast_save)
    val toastError = stringResource(R.string.toast_word_exists)
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.vocabulary), style = AppTypography.fontSize20Regular)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
                Divider()
            }
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            InputTextFieldSection(
                title = stringResource(R.string.title_word),
                value = wordTextFieldValue,
                onValueChange = { wordTextFieldValue = it },
                placeholder = stringResource(R.string.hint_word)
            )

            Spacer(Modifier.height(36.dp))

            InputTextFieldSection(
                title = stringResource(R.string.title_mean),
                value = meanTextFieldValue,
                onValueChange = { meanTextFieldValue = it },
                placeholder = stringResource(R.string.hint_word)
            )

            Spacer(Modifier.height(36.dp))

            InputTextFieldSection(
                title = stringResource(R.string.title_description),
                value = descriptionTextFieldValue,
                onValueChange = { descriptionTextFieldValue = it },
                placeholder = stringResource(R.string.hint_description),
            )

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = {
                    if(wordTextFieldValue == "") {
                        Toast.makeText(context, toastEmptyWord, Toast.LENGTH_SHORT).show()
                    } else if(meanTextFieldValue == "") {
                        Toast.makeText(context, toastEmptyMean, Toast.LENGTH_SHORT).show()
                    } else {
                        saveWordViewModel.insertWord(
                            VocaWord(
                                word = wordTextFieldValue,
                                mean = meanTextFieldValue,
                                description = descriptionTextFieldValue,
                                category = category,
                                vocabularyId = vocaId
                            ),
                            onComplete = { success ->
                                if (success) {
                                    Toast.makeText(context, toastComplete, Toast.LENGTH_SHORT).show()
                                    wordTextFieldValue = ""
                                    meanTextFieldValue = ""
                                    descriptionTextFieldValue = ""
                                } else {
                                    Toast.makeText(context, toastError, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }

    }
}