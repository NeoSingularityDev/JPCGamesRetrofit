package com.example.gamesretrofit.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// PALETA LIGHT
private val LightColors = lightColorScheme(
    primary = CyanPrimaryLight,
    onPrimary = Color.White,
    secondary = CyanSecondary,
    onSecondary = Color.Black,
    tertiary = CyanTertiary,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onSurface = Color.Black
)

// PALETA DARK
private val DarkColors = darkColorScheme(
    primary = CyanPrimaryDark,
    onPrimary = Color.Black,
    secondary = CyanSecondary,
    onSecondary = Color.Black,
    tertiary = CyanTertiary,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White
)

@Composable
fun GamesRetrofitTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}