package com.ljyVoca.vocabularyapp.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.model.Language
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectBottomSheet(
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    onSelect: (Language) -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ){
            Text(
                text = stringResource(R.string.language_title),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Language.entries.forEach { language ->
                val displayName = language.displayName()

                Text(
                    text = displayName,
                    style = AppTypography.fontSize16SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            onSelect(language)
                            onDismiss()
                        }
                )
            }
        }
    }
}