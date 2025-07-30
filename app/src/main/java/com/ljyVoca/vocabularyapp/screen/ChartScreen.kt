package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljyVoca.vocabularyapp.ui.theme.AppTypography
import com.ljyVoca.vocabularyapp.viewmodel.ChartViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget
import kotlin.math.roundToInt

@Composable
fun ChartScreen(
    chartViewModel: ChartViewModel
) {
    val dailyWordCounts by chartViewModel.dailyWordCounts.collectAsState()
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        chartViewModel.loadDailyWordCounts()
    }

    LaunchedEffect(dailyWordCounts) {
        if (dailyWordCounts.isNotEmpty() && dailyWordCounts.any { it.second > 0 }) {
            modelProducer.runTransaction {
                lineSeries {
                    series(dailyWordCounts.map { it.second.toFloat() })
                }
            }
        } else {
            // 데이터가 없거나 모든 값이 0일 때는 기본 데이터 넣기, 안쓰면 죽는데 바로 값을 꽂지 않나봄
            modelProducer.runTransaction {
                lineSeries {
                    series(listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)) // 7일치 0 데이터
                }
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "학습 그래프",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(16.dp)
            )
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, value, _ ->
                            val index = value.roundToInt().coerceIn(0, dailyWordCounts.size - 1)
                            dailyWordCounts.getOrNull(index)?.first ?: ""
                        }
                    ),
                    marker = rememberDefaultCartesianMarker(
                        label = rememberTextComponent()
                    )
                ),
                modelProducer = modelProducer,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}