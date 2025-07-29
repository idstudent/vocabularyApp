package com.ljyVoca.vocabularyapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ljyVoca.vocabularyapp.screen.AddVocabularyFolderScreen
import com.ljyVoca.vocabularyapp.screen.AddWordScreen
import com.ljyVoca.vocabularyapp.screen.CalendarScreen
import com.ljyVoca.vocabularyapp.screen.HandWriteModeScreen
import com.ljyVoca.vocabularyapp.screen.HomeScreen
import com.ljyVoca.vocabularyapp.screen.QuizModeScreen
import com.ljyVoca.vocabularyapp.screen.TodayWordAllScreen
import com.ljyVoca.vocabularyapp.screen.UpdateVocabularyFolderScreen
import com.ljyVoca.vocabularyapp.screen.UpdateWordScreen
import com.ljyVoca.vocabularyapp.screen.VocabularyDetailScreen
import com.ljyVoca.vocabularyapp.screen.VocabularyListScreen
import com.ljyVoca.vocabularyapp.viewmodel.CalendarViewModel
import com.ljyVoca.vocabularyapp.viewmodel.SaveWordViewModel
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyFolderViewModel
import com.ljyVoca.vocabularyapp.viewmodel.VocabularyViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController) {
    val vocabularyViewModel: VocabularyViewModel = hiltViewModel()
    val vocabularyFolderViewModel: VocabularyFolderViewModel = hiltViewModel()
    val saveWordViewModel: SaveWordViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()

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
            route = "${AppRoutes.VOCABULARY_DETAIL_SCREEN}/{id}/{category}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: "en"
            val title = navController.previousBackStackEntry?.savedStateHandle?.get<String>("title") ?: ""

            VocabularyDetailScreen(
                navController = navController,
                id = id,
                category = category,
                title = title,
                saveWordViewModel = saveWordViewModel,
                vocabularyFolderViewModel = vocabularyFolderViewModel
            )
        }

        composable(
            route = "${AppRoutes.ADD_WORD_SCREEN}/{category}/{vocaId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("vocaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "en"
            val vocaId = backStackEntry.arguments?.getString("vocaId") ?: ""

            AddWordScreen(
                category = category,
                vocaId = vocaId,
                saveWordViewModel = saveWordViewModel
            )
        }

        composable(route = AppRoutes.UPDATE_WORD_SCREEN) {
            UpdateWordScreen(
                navController = navController,
                saveWordViewModel = saveWordViewModel
            )
        }

        composable(route = AppRoutes.TODAY_ALL_WORD_SCREEN) {
            TodayWordAllScreen(
                vocabularyViewModel = vocabularyViewModel
            )
        }

        composable(route = NaviItem.Calendar.route) {
            CalendarScreen(
                navController = navController,
                calendarViewModel =  calendarViewModel
            )
        }
    }

}