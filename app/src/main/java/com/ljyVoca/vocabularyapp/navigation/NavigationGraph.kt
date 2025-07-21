package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ljyVoca.vocabularyapp.screen.HomeScreen

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