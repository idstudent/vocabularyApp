package com.ljyVoca.vocabularyapp.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.LanguageSelectBottomSheet
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.model.Vocabulary
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateVocabularyFolderScreen(
    navController: NavHostController,
    vocabularyFolderViewModel: VocabularyFolderViewModel
) {
    val selectLanguageBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showSelectLanguageBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val toastLanguageMsg = stringResource(R.string.toast_select_language)
    val toastTitleMsg = stringResource(R.string.toast_input_title)
    val toastCompleted = stringResource(R.string.toast_update_folder_complete)

    val vocabulary = navController.previousBackStackEntry?.savedStateHandle?.get<Vocabulary>("vocabulary")
    var titleTextFieldValue by remember { mutableStateOf(vocabulary?.title ?: "") }
    var descriptionTextFieldValue by remember { mutableStateOf(vocabulary?.description ?: "") }
    var selectedLanguage by remember {
        mutableStateOf(
            vocabulary?.category?.let { categoryCode ->
                Language.entries.find { it.code == categoryCode }
            }
        )
    }
    val defaultLanguageText = stringResource(R.string.select_language)


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
            Text(
                text = stringResource(R.string.title),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
            )
            TextField(
                value = titleTextFieldValue,
                onValueChange = {
                    titleTextFieldValue = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,     // 포커스된 밑줄
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,   // 일반 밑줄
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,     // 포커스된 배경
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,   // 일반 배경
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary // 비활성 배경
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = AppTypography.fontSize16Regular
            )

            Spacer(Modifier.height(36.dp))
            Text(
                text = stringResource(R.string.title_description),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextField(
                value = descriptionTextFieldValue,
                onValueChange = {
                    descriptionTextFieldValue = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,     // 포커스된 밑줄
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,   // 일반 밑줄
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,     // 포커스된 배경
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,   // 일반 배경
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary // 비활성 배경
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = AppTypography.fontSize16Regular
            )
            Spacer(Modifier.height(36.dp))
            Text(
                text = stringResource(R.string.select_language),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Text(
                    text = selectedLanguage?.displayName() ?: defaultLanguageText,
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            showSelectLanguageBottomSheet = true
                        }
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                   if(titleTextFieldValue == "") {
                       Toast.makeText(context, toastTitleMsg, Toast.LENGTH_SHORT).show()
                    } else if(selectedLanguage == null) {
                        Toast.makeText(context, toastLanguageMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        vocabulary?.let {
                            vocabularyFolderViewModel.updateVocabulary(
                                vocabulary = it.copy(
                                    title = titleTextFieldValue,
                                    description = descriptionTextFieldValue,
                                    category = selectedLanguage!!.code
                                ),
                                onComplete = {
                                    navController.popBackStack()
                                    Toast.makeText(context, toastCompleted, Toast.LENGTH_SHORT).show()
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
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = AppTypography.fontSize16Regular,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }

        if(showSelectLanguageBottomSheet) {
            LanguageSelectBottomSheet(
                bottomSheetState = selectLanguageBottomSheetState,
                onDismiss = { showSelectLanguageBottomSheet = false },
                onSelect = {
                    selectedLanguage = it
                }
            )
        }
    }
}