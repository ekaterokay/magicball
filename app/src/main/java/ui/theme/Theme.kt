package com.example.magicball.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    // Main brand
    primary = AccentLavender,
    onPrimary = Color(0xFF0B0611),
    primaryContainer = Color(0xFF2A1650),
    onPrimaryContainer = TextPrimary,

    secondary = AccentViolet,
    onSecondary = Color(0xFF0B0611),
    secondaryContainer = Color(0xFF261246),
    onSecondaryContainer = TextPrimary,

    // Optional “magic highlight”
    tertiary = AccentCyan,
    onTertiary = Color(0xFF061014),
    tertiaryContainer = Color(0xFF10212B),
    onTertiaryContainer = TextPrimary,

    // Backgrounds & surfaces
    background = Bg0,
    onBackground = TextPrimary,

    surface = Surface0,
    onSurface = TextPrimary,

    surfaceVariant = Surface1,
    onSurfaceVariant = TextSecondary,

    surfaceTint = AccentLavender,

    // Outline / dividers
    outline = StrokeSoft,
    outlineVariant = Color(0x1AFFFFFF),

    // Status
    error = Error,
    onError = Color(0xFF16060B),
    errorContainer = Color(0xFF2A0D18),
    onErrorContainer = TextPrimary
)

@Composable
fun MagicBallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}