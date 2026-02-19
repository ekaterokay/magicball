package com.example.magicball.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Purple1,
    secondary = Purple2,
    background = Purple4,
    surface = Purple3,
    onPrimary = Color(0xFF0B0611),
    onSecondary = Color.White,
    onBackground = Color(0xFFEFE7FF),
    onSurface = Color(0xFFEFE7FF),
)

@Composable
fun MagicBallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}
