package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ljyVoca.vocabularyapp.screen.HandWriteModeScreen
import com.ljyVoca.vocabularyapp.screen.HomeScreen
import com.ljyVoca.vocabularyapp.screen.QuizModeScreen
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    val vocabularyViewModel: VocabularyViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NaviItem.Home.route,
    ) {
        composable(route = NaviItem.Home.route) {
            HomeScreen(
                navController = navController,
                vocabularyViewModel = vocabularyViewModel
            )
        }
        composable(route = AppRoutes.HANDLE_WRITE_MODE_SCREEN) {
            HandWriteModeScreen(
                navController = navController,
                vocabularyViewModel = vocabularyViewModel
            )
        }

        composable(route = AppRoutes.QUIZ_MODE_SCREEN) {
            QuizModeScreen(
                navController = navController,
                vocabularyViewModel = vocabularyViewModel
            )
        }

        composable(route = NaviItem.Search.route) {
            HomeScreen(navController = navController,
                vocabularyViewModel = vocabularyViewModel)
        }
        composable(route = NaviItem.Cart.route) {
            HomeScreen(navController = navController,
                vocabularyViewModel = vocabularyViewModel)
        }
        composable(route = NaviItem.Profile.route) {
            HomeScreen(navController = navController,
                vocabularyViewModel = vocabularyViewModel)
        }
    }

}