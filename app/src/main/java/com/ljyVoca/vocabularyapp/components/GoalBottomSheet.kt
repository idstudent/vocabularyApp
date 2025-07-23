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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import kotlin.math.roundToInt


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
                text = "주간 학습 목표",
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
                    OutlinedTextField(
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
                        textStyle = AppTypography.fontSize24ExtraBold.copy(
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = "${tempGoal}개",
                        style = AppTypography.fontSize24ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (!isEditingText) {
                Text(
                    text = "숫자를 터치하면 직접 입력할 수 있어요",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    tempGoal = roundedValue.coerceIn(5, 300)
                    isEditingText = false // 슬라이더 조작시 텍스트 편집 모드 해제
                },
                valueRange = 5f..300f,
                steps = 58, // (300-5)/5 = 59단계, steps = 단계수-1
                modifier = Modifier.fillMaxWidth()
            )

            // 범위 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "5개",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "300개",
                    style = AppTypography.fontSize14Regular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    onGoalChange(tempGoal)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("설정 완료")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}