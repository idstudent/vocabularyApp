package com.ljyVoca.vocabularyapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ljyVoca.vocabularyapp.screen.MainScreen
import com.ljyVoca.vocabularyapp.ui.theme.LjyVocaTheme
import com.ljyVoca.vocabularyapp.ui.theme.SetStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LjyVocaTheme {
                SetStatusBarColor()
                MainScreen(navController = rememberNavController())
            }
        }
    }
}

