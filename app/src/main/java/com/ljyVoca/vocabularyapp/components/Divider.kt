package com.ljyVoca.vocabularyapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.Color868686
import androidx.compose.material3.HorizontalDivider

@Composable
fun Divider() {
    HorizontalDivider(
        color = Color868686,
        thickness = 0.5.dp
    )
}