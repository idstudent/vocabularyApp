package com.ljyVoca.vocabularyapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color3D68D2,
    onBackground = Black,
    onPrimary = Color2D2D2D,
    secondary = ColorC4C7C5,
    onSecondary = Color868686,
    primaryContainer = Color4CAF50,
    secondaryContainer = ColorFF5722,
    tertiaryContainer = ColorF6447C,
    tertiary = ColorFFC107,
    onSurface = White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color3D68D2,
    onBackground = White,
    onPrimary = White,
    secondary = Black,
    onSecondary = Color868686,
    primaryContainer = Color4CAF50,
    secondaryContainer = ColorFF5722,
    tertiaryContainer = ColorF6447C,
    tertiary = ColorFFC107,
    onSurface = Black,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LjyVocaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}