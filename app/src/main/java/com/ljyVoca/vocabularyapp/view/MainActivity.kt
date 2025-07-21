package com.ljyVoca.vocabularyapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ljyVoca.vocabularyapp.screen.MainScreen
import com.ljyVoca.vocabularyapp.ui.theme.LjyVocaTheme


class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LjyVocaTheme {
                MainScreen(navController = rememberNavController())
            }
        }
    }
}