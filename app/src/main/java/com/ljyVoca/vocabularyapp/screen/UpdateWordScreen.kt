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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.InputTextFieldSection
import com.ljyVoca.vocabularyapp.model.VocaWord
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.SaveWordViewModel

@Composable
fun UpdateWordScreen(
    navController: NavHostController,
    saveWordViewModel: SaveWordViewModel
) {
    val word = navController.previousBackStackEntry?.savedStateHandle?.get<VocaWord>("word")
    var wordTextFieldValue by remember { mutableStateOf(word?.word ?: "") }
    var meanTextFieldValue by remember { mutableStateOf(word?.mean ?: "") }
    var descriptionTextFieldValue by remember { mutableStateOf(word?.description ?: "") }

    val context = LocalContext.current

    val toastEmptyWord = stringResource(R.string.hint_word)
    val toastEmptyMean = stringResource(R.string.hint_mean)
    val toastComplete = stringResource(R.string.toast_save)
    val toastError = stringResource(R.string.toast_word_exists)

    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.vocabulary),
                style = AppTypography.fontSize20Regular.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

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
                placeholder = stringResource(R.string.hint_description)
            )

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = {
                    if(wordTextFieldValue == "") {
                        Toast.makeText(context, toastEmptyWord, Toast.LENGTH_SHORT).show()
                    } else if(meanTextFieldValue == "") {
                        Toast.makeText(context, toastEmptyMean, Toast.LENGTH_SHORT).show()
                    } else {
                        word?.let {
                            saveWordViewModel.updateWord(
                                it.copy(
                                    word = wordTextFieldValue,
                                    mean = meanTextFieldValue,
                                    description = descriptionTextFieldValue
                                ),
                                onComplete = { success ->
                                    if (success) {
                                        Toast.makeText(context, toastComplete, Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, toastError, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
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