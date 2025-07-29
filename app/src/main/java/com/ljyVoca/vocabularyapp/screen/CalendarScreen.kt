package com.ljyVoca.vocabularyapp.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.ljyVoca.vocabularyapp.R
import com.ljyVoca.vocabularyapp.components.CalendarDay
import com.ljyVoca.vocabularyapp.components.CalendarHeader
import com.ljyVoca.vocabularyapp.components.Divider
import com.ljyVoca.vocabularyapp.components.WeekDaysHeader
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel
) {
    val firstWordMonth by calendarViewModel.firstWordMonth.collectAsState()
    val currentMonthWords by calendarViewModel.currentMonthWords.collectAsState()
    val selectedDateWords by calendarViewModel.selectedDateWords.collectAsState()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember(firstWordMonth) {
        firstWordMonth ?: currentMonth
    }
    val endMonth = remember { currentMonth }

    var selectDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // 한국은 1주시작이 일요일부터, 외국은 월요일부터 시작함

    val coroutineScope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    // 현재 보이는 월이 변경될 때마다 가져옴
    LaunchedEffect(state.firstVisibleMonth) {
        calendarViewModel.getMonthData(state.firstVisibleMonth.yearMonth)
    }

    LaunchedEffect(Unit) {
        selectDate?.let {
            calendarViewModel.getWordsByDay(it)
        }
    }

    // 날짜별 단어 개수 계산
    val wordCountsByDate = remember(currentMonthWords) {
        currentMonthWords.groupBy { word ->
            Instant.ofEpochMilli(word.createdDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }.mapValues { it.value.size }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.calendar),
                            style = AppTypography.fontSize20Regular
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
                Divider()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            CalendarHeader(
                currentMonth = state.firstVisibleMonth.yearMonth,
                onPreviousMonth = {
                    val previousMonth = state.firstVisibleMonth.yearMonth.minusMonths(1)
                    if (previousMonth >= startMonth) {
                        coroutineScope.launch {
                            state.animateScrollToMonth(previousMonth)
                        }
                    }
                },
                onNextMonth = {
                    val nextMonth = state.firstVisibleMonth.yearMonth.plusMonths(1)
                    if (nextMonth <= endMonth) {
                        coroutineScope.launch {
                            state.animateScrollToMonth(nextMonth)
                        }
                    }
                },
                canGoPrevious = state.firstVisibleMonth.yearMonth > startMonth,
                canGoNext = state.firstVisibleMonth.yearMonth < endMonth
            )

            WeekDaysHeader(firstDayOfWeek = firstDayOfWeek)

            HorizontalCalendar(
                state = state,
                dayContent = {
                    val dateString = it.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val wordCount = wordCountsByDate[dateString] ?: 0

                    CalendarDay(
                        day = it,
                        isSelected = selectDate == it.date,
                        hasWords = wordCount > 0,
                        wordCount = wordCount,
                        onClick = {
                            if (it.position == DayPosition.MonthDate) {
                                selectDate = if (selectDate == it.date) null else it.date
                                selectDate?.let { date ->
                                    calendarViewModel.getWordsByDay(date)
                                }
                            }

                        }
                    )
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}