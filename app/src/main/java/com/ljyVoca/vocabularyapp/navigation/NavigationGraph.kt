package com.ljyVoca.vocabularyapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ljyVoca.vocabularyapp.screen.AddVocabularyFolderScreen
import com.ljyVoca.vocabularyapp.screen.HandWriteModeScreen
import com.ljyVoca.vocabularyapp.screen.HomeScreen
import com.ljyVoca.vocabularyapp.screen.QuizModeScreen
import com.ljyVoca.vocabularyapp.screen.UpdateVocabularyFolderScreen
import com.ljyVoca.vocabularyapp.screen.VocabularyDetailScreen
import com.ljyVoca.vocabularyapp.screen.VocabularyListScreen
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    val vocabularyViewModel: VocabularyViewModel = hiltViewModel()
    val vocabularyFolderViewModel: VocabularyFolderViewModel = hiltViewModel()

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

        composable(route = NaviItem.Voca.route) {
            VocabularyListScreen(
                navController = navController,
                vocabularyFolderViewModel = vocabularyFolderViewModel
            )
        }

        composable(route = AppRoutes.ADD_VOCABULARY_FOLDER_SCREEN) {
            AddVocabularyFolderScreen(
                navController = navController,
                vocabularyFolderViewModel = vocabularyFolderViewModel
            )
        }

        composable(route = AppRoutes.UPDATE_VOCABULARY_FOLDER_SCREEN) {
            UpdateVocabularyFolderScreen(
                navController = navController,
                vocabularyFolderViewModel = vocabularyFolderViewModel
            )
        }

        composable(
            route = "${AppRoutes.VOCABULARY_DETAIL_SCREEN}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType },)
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val title = navController.previousBackStackEntry?.savedStateHandle?.get<String>("title") ?: ""

            VocabularyDetailScreen(
                navController = navController,
                id = id,
                title = title,
                vocabularyFolderViewModel = vocabularyFolderViewModel
            )
        }
    }

}