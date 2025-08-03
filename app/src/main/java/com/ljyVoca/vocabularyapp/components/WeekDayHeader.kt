package com.ljyVoca.vocabularyapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekDaysHeader(firstDayOfWeek: DayOfWeek) {
    val dayFormatter = remember {
        DateTimeFormatter.ofPattern("E", Locale.getDefault())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(7) { index ->
            val dayOfWeek = firstDayOfWeek.plus(index.toLong())

            val dayName = dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            )

            Text(
                text = dayName,
                style = AppTypography.fontSize14SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}