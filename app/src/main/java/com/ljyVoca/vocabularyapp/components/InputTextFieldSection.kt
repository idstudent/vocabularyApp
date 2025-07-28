package com.ljyVoca.vocabularyapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography

@Composable
fun InputTextFieldSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int = 1
) {
    Text(
        text = title,
        style = AppTypography.fontSize20SemiBold,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
    )
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
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
        textStyle = AppTypography.fontSize16Regular,
        placeholder = {
            Text(
                text = placeholder,
                style = AppTypography.fontSize16Regular
            )
        },
        maxLines = maxLines
    )
}