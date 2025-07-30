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
import com.ljyVoca.vocabularyapp.components.BannerAdView
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
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ChartScreen(
    chartViewModel: ChartViewModel
) {
    val dailyWordCounts by chartViewModel.dailyWordCounts.collectAsState()
    val dailyAccuracy by chartViewModel.dailyAccuracy.collectAsState()

    val modelProducer = remember { CartesianChartModelProducer() }
    val accuracyModelProducer = remember { CartesianChartModelProducer() }

    val dateFormatter = remember { SimpleDateFormat("MM.dd", Locale.getDefault()) }

    val defaultDateLabels = remember {
        (6 downTo 0).map { daysAgo ->
            val date = Date(System.currentTimeMillis() - (daysAgo * 24 * 60 * 60 * 1000L))
            dateFormatter.format(date)
        }
    }

    LaunchedEffect(Unit) {
        chartViewModel.loadDailyWordCounts()
        chartViewModel.loadDailyAccuracy()
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

    LaunchedEffect(dailyAccuracy) {
        if (dailyAccuracy.isNotEmpty()) {
            accuracyModelProducer.runTransaction {
                lineSeries {
                    // Y축 범위를 0~100으로 고정하기 위해 데이터도 0~100 범위에 맞춤
                    series(
                        x = (0 until 7).map { it.toFloat() },
                        y = dailyAccuracy.map { (it.second * 100f).coerceIn(0f, 100f) }
                    )
                }
            }
        } else {
            accuracyModelProducer.runTransaction {
                lineSeries {
                    series(
                        x = (0 until 7).map { it.toFloat() },
                        y = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
                    )
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
                text = "최근 1주일간 저장한 단어",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, value, _ ->
                            if (dailyWordCounts.isNotEmpty()) {
                                val index = value.roundToInt().coerceIn(0, dailyWordCounts.size - 1)
                                dailyWordCounts.getOrNull(index)?.first ?: defaultDateLabels.getOrNull(value.roundToInt()) ?: "Day ${value.roundToInt() + 1}"
                            } else {
                                defaultDateLabels.getOrNull(value.roundToInt()) ?: "Day ${value.roundToInt() + 1}"
                            }
                        }
                    ),
                    marker = rememberDefaultCartesianMarker(
                        label = rememberTextComponent()
                    )
                ),
                modelProducer = modelProducer,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))

            BannerAdView(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "최근 1주일간 정답률",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.padding(16.dp)
            )

            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer( // y축 라벨값 고정 0% ~ 100%까지
                        rangeProvider = CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = 100.0)
                    ),
                    startAxis = VerticalAxis.rememberStart(
                        valueFormatter = { _, value, _ ->
                            "${value.roundToInt()}%"
                        },
                        itemPlacer = VerticalAxis.ItemPlacer.step(step = { 10.0 })
                    ),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = { _, value, _ ->
                            if (dailyAccuracy.isNotEmpty()) {
                                val index = value.roundToInt().coerceIn(0, dailyAccuracy.size - 1)
                                dailyAccuracy.getOrNull(index)?.first ?: defaultDateLabels.getOrNull(index) ?: "Day ${index + 1}"
                            } else {
                                defaultDateLabels.getOrNull(value.roundToInt()) ?: "Day ${value.roundToInt() + 1}"
                            }
                        }
                    ),
                    marker = rememberDefaultCartesianMarker(
                        label = rememberTextComponent()
                    )
                ),
                modelProducer = accuracyModelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}