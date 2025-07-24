package com.ljyVoca.vocabularyapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import kotlin.math.roundToInt
import com.ljyVoca.vocabularyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalBottomSheet(
    bottomSheetState: SheetState,
    onDismiss: () -> Unit,
    currentGoal: Int = 20,
    onGoalChange: (Int) -> Unit = {}
) {
    var tempGoal by remember { mutableIntStateOf(currentGoal) }
    var isEditingText by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    LaunchedEffect(currentGoal) {
        tempGoal = currentGoal
        textFieldValue = currentGoal.toString()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.weekly_learning_goal),
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isEditingText = true
                        textFieldValue = tempGoal.toString()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isEditingText) {
                    TextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            if (newValue.all { char -> char.isDigit() } && newValue.length <= 3) {
                                textFieldValue = newValue
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                val newGoal = textFieldValue.toIntOrNull() ?: tempGoal
                                tempGoal = newGoal.coerceIn(1, 300) // 1~300 범위로 제한
                                isEditingText = false
                            }
                        ),
                        modifier = Modifier.width(120.dp),
                        textStyle = AppTypography.fontSize20ExtraBold.copy(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,     // 포커스된 밑줄
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,   // 일반 밑줄
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,     // 포커스된 배경
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,   // 일반 배경
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimary // 비활성 배경
                        )
                    )
                } else {
                    Text(
                        text = stringResource(R.string.word_count_format, tempGoal),
                        style = AppTypography.fontSize24ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (!isEditingText) {
                Text(
                    text = stringResource(R.string.edit_goal),
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 슬라이더 (5~300, 5간격)
            Slider(
                value = tempGoal.toFloat(),
                onValueChange = { newValue ->
                    // 5의 배수로 반올림
                    val roundedValue = (newValue / 5f).roundToInt() * 5
                    tempGoal = roundedValue.coerceIn(0, 300)
                    isEditingText = false // 슬라이더 조작시 텍스트 편집 모드 해제
                },
                valueRange = 0f..300f,
                steps = 60, // (300-5)/5 = 59단계, steps = 단계수-1
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors().copy(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSecondary,
                    disabledThumbColor = MaterialTheme.colorScheme.primary,
                    disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                    disabledInactiveTrackColor = MaterialTheme.colorScheme.onSecondary,
                    activeTickColor = MaterialTheme.colorScheme.primary,
                    inactiveTickColor = MaterialTheme.colorScheme.onSecondary,
                    disabledActiveTickColor = MaterialTheme.colorScheme.primary,
                    disabledInactiveTickColor = MaterialTheme.colorScheme.onSecondary
                )
            )

            // 범위 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.word_count_format, 0),
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.word_count_format, 300),
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isEditingText) {
                        val newGoal = textFieldValue.toIntOrNull() ?: tempGoal
                        tempGoal = newGoal.coerceIn(1, 300)
                    }
                    onGoalChange(tempGoal)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.complete_setting), style = AppTypography.fontSize16Regular)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}