package com.ljyVoca.vocabularyapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    canGoPrevious: Boolean,
    canGoNext: Boolean
) {
    val monthFormatter = remember {
        when (Locale.getDefault().language) {
            "ko" -> DateTimeFormatter.ofPattern("yyyy년 M월")
            else -> DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 달 버튼
        IconButton(
            onClick = onPreviousMonth,
            enabled = canGoPrevious
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "이전 달",
                tint = if (canGoPrevious) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        }

        // 년/월 표시
        Text(
            text = currentMonth.format(monthFormatter),
            style = AppTypography.fontSize20SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // 다음 달 버튼
        IconButton(
            onClick = onNextMonth,
            enabled = canGoNext
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "다음 달",
                tint = if (canGoNext) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        }
    }
}