package com.example.magicball.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * MagicBall premium dark palette
 * - Deep violet base
 * - Neon lavender accent
 * - Soft cyan highlight (for “magic” feel)
 * - Glow + gradients for cards/buttons/ball
 */

// --- Base background (near-black violet)
val Bg0 = Color(0xFF07040C)     // almost black
val Bg1 = Color(0xFF0D0616)     // deep violet-black
val Bg2 = Color(0xFF140A22)     // your current mid
val Bg3 = Color(0xFF1E0F33)     // deep purple
val Bg4 = Color(0xFF120A1C)     // your current bottom

// --- Surfaces (cards, sheets)
val Surface0 = Color(0xFF120A1C)
val Surface1 = Color(0xFF1A0F2B)
val Surface2 = Color(0xFF24133A)

// --- Text
val TextPrimary = Color(0xFFEFE7FF)   // warm lavender-white
val TextSecondary = Color(0xFFBDA9E8) // muted lavender
val TextTertiary = Color(0xFF8A78B5)  // dim label

// --- Accents (magic)
val AccentLavender = Color(0xFFB388FF) // your Purple1
val AccentViolet = Color(0xFF7C4DFF)   // your Purple2
val AccentNeon = Color(0xFFD7B8FF)     // brighter lavender
val AccentCyan = Color(0xFF63E6FF)     // subtle “mystic tech” highlight

// --- States
val Success = Color(0xFF5CF2C2)
val Warning = Color(0xFFFFD36E)
val Error = Color(0xFFFF6B8B)

// --- Dividers / strokes
val StrokeSoft = Color(0x26FFFFFF)     // ~15% white
val StrokeGlow = Color(0x33B388FF)     // lavender glow stroke

// =========================
// Brushes / Gradients
// =========================

// Main app background gradient (deep magical)
val GradBg = Brush.verticalGradient(
    colors = listOf(
        Bg0,
        Bg1,
        Bg2,
        Bg3,
        Bg4
    )
)

// Card background gradient (subtle depth)
val GradCard = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1B1030),
        Color(0xFF140A22)
    )
)

// Primary button gradient (premium violet)
val GradPrimary = Brush.horizontalGradient(
    colors = listOf(
        AccentLavender,
        AccentViolet
    )
)

// Soft glow overlay (use as background for glow rings)
val GradGlowLavender = Brush.radialGradient(
    colors = listOf(
        Color(0x66B388FF), // center glow
        Color(0x00B388FF)  // transparent edge
    )
)

val GradGlowCyan = Brush.radialGradient(
    colors = listOf(
        Color(0x4063E6FF),
        Color(0x0063E6FF)
    )
)

// “Magic Ball” gradient (inside the sphere)
val GradBall = Brush.radialGradient(
    colors = listOf(
        Color(0xFFE9D7FF), // bright core
        Color(0xFFB388FF), // lavender
        Color(0xFF5A2EA6), // deep violet
        Color(0xFF140A22)  // dark edge
    )
)