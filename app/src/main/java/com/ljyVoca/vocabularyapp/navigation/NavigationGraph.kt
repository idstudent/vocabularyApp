package com.exam.compose_clone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.exam.compose_clone.screen.HomeScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NaviItem.Home.route,
    ) {
        composable(route = NaviItem.Home.route) {
            HomeScreen()
        }
        composable(route = NaviItem.Search.route) {
            HomeScreen()
        }
        composable(route = NaviItem.Cart.route) {
            HomeScreen()
        }
        composable(route = NaviItem.Profile.route) {
            HomeScreen()
        }
    }

}