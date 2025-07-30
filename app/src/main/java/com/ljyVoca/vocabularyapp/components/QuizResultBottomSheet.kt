package com.ljyVoca.vocabularyapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import com.ljyVoca.vocabularyapp.model.AnswerResult
import com.ljyVoca.vocabularyapp.ui.theme.Color4CAF50
import com.ljyVoca.vocabularyapp.ui.theme.Color8BC34A
import com.ljyVoca.vocabularyapp.ui.theme.ColorCD4869
import com.ljyVoca.vocabularyapp.ui.theme.ColorF6447C
import com.ljyVoca.vocabularyapp.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizResultBottomSheet(
    bottomSheetState: SheetState,
    answerResult: AnswerResult,
    onDismiss: () -> Unit,
    onNext: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onNext()
            onDismiss()
        },
        sheetState = bottomSheetState,
        containerColor = if(answerResult.isCorrect) {
            Color4CAF50
        } else {
            ColorF6447C
        },
        contentWindowInsets = { WindowInsets(0) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if(answerResult.isCorrect) stringResource(R.string.correct) else stringResource(R.string.incorrect),
                style = AppTypography.fontSize20SemiBold.copy(White),
            )

            if(answerResult.isCorrect) {
                val otherMeans = answerResult.allMeanings.filter {
                    it != answerResult.matchedMeaning
                }

                if (otherMeans.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.other_means),
                        style = AppTypography.fontSize16SemiBold.copy(White)
                    )
                    Text(
                        text = otherMeans.joinToString(", "),
                        style = AppTypography.fontSize16SemiBold.copy(White)
                    )
                }
            }else {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.answer),
                    style = AppTypography.fontSize16Regular.copy(White)
                )
                Text(
                    text = answerResult.allMeanings.joinToString(", "),
                    style = AppTypography.fontSize16SemiBold.copy(White)
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                        onNext()
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(answerResult.isCorrect) Color8BC34A else ColorCD4869,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text= stringResource(R.string.next),
                    style = AppTypography.fontSize20SemiBold
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}