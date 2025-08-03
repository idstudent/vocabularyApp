package com.ljyVoca.vocabularyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography

@Composable
fun QuizProgressSection(
    currentIndex: Int,
    totalCount: Int
) {
    val progress = if (totalCount > 0) {
        currentIndex.toFloat() / totalCount.toFloat()
    } else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$currentIndex / $totalCount",
                style = AppTypography.fontSize16Regular.copy(
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }

        CustomProgressBar(
            progress = progress,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                .height(16.dp),
            backgroundColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            progressColor = MaterialTheme.colorScheme.primary,
        )
    }
}