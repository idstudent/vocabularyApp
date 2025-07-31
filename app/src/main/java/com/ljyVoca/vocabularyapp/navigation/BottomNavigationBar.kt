package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val items = listOf(NaviItem.Home, NaviItem.Voca, NaviItem.Calendar, NaviItem.Chart)
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }

    var tabWidths by remember { mutableStateOf(List(items.size) { 0.dp }) }
    var tabPositions by remember { mutableStateOf(List(items.size) { 0.dp }) }
    val density = LocalDensity.current

    val animatedOffset by animateFloatAsState(
        targetValue = if (selectedIndex >= 0) tabPositions[selectedIndex].value else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedWidth by animateFloatAsState(
        targetValue = if (selectedIndex >= 0) tabWidths[selectedIndex].value else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Column {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 0.5.dp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = currentRoute == item.route

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .onGloballyPositioned { coordinates ->
                                with(density) {
                                    // 양옆 패딩 16dp씩해서 32dp 추가
                                    val totalWidth = coordinates.size.width.toDp() + 32.dp
                                    tabWidths = tabWidths.toMutableList().apply {
                                        this[index] = totalWidth
                                    }

                                    /*  positionInParent는 패딩적용 후 컨텐츠의 포지션을 줌 그래서 위에서 패딩 준 값부터
                                    시작해야되서 -16 해준거임
                                    24는 row 패딩
                                 */
                                    val absoluteX = coordinates.positionInParent().x - 16.dp.toPx()
                                    tabPositions = tabPositions.toMutableList().apply {
                                        this[index] = (absoluteX + 24.dp.toPx()).toDp()
                                    }
                                }
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                            )

                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(item.title),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            if (selectedIndex >= 0) {
                Box(
                    modifier = Modifier
                        .offset(x = animatedOffset.dp, y = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .size(width = animatedWidth.dp, height = 48.dp)
                )
            }
        }
    }
}