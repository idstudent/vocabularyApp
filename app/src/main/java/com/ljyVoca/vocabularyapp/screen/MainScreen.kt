package com.ljyVoca.vocabularyapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exam.compose_clone.navigation.NaviItem
import com.ljyVoca.vocabularyapp.navigation.NavigationGraph
import com.ljyVoca.vocabularyapp.navigation.BottomNavigationBar

@Composable
fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if(NaviItem.showBottomBar(currentRoute)) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute)
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavigationGraph(navController = navController)
            }
        }
    )
}