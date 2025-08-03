package com.ljyVoca.vocabularyapp.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ljyVoca.vocabularyapp.navigation.NaviItem
import com.ljyVoca.vocabularyapp.navigation.NavigationGraph
import com.ljyVoca.vocabularyapp.navigation.BottomNavigationBar
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val darkTheme = isSystemInDarkTheme()

    // 시스템바 아이콘 색상 설정
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = !darkTheme
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
            bottomBar = {
                if (NaviItem.showBottomBar(currentRoute)) {
                    BottomNavigationBar(navController = navController, currentRoute = currentRoute)
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(navController = navController)
                }
            }
        )

        // 상태바 배경
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
                .background(if (darkTheme) Color.Black else Color.White)
                .align(Alignment.TopCenter)
        )
    }
}
