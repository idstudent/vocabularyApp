package com.ljyVoca.vocabularyapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.Color
import com.ljyVoca.vocabularyapp.ui.theme.Color868686

@Composable
fun Divider(
    colorCode: Color = Color868686
) {
    HorizontalDivider(
        color = colorCode,
        thickness = 0.5.dp
    )
}