package com.loki.chatapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────
//  PREMIUM COLOR PALETTE
// ─────────────────────────────────────────────
//
//  DARK THEME
//  Background        #0D0F14   "Midnight Void"
//  Surface           #161A23   "Abyss Blue"
//  Surface Variant   #1E2330   "Storm Slate"
//  Primary           #6C8EFF   "Periwinkle Arc"
//  Primary Container #1C274D   "Deep Sapphire"
//  Secondary         #A78BFA   "Soft Amethyst"
//  On Background     #E8EAF0   "Pale Frost"
//  On Surface        #C5CAD6   "Silver Mist"
//  On Primary        #FFFFFF   "Pure White"
//  Outline           #2E3448   "Twilight Border"
//
//  LIGHT THEME
//  Background        #F4F6FB   "Ghost White"
//  Surface           #FFFFFF   "Pure White"
//  Surface Variant   #EAEDf5   "Lavender Fog"
//  Primary           #3A5BF0   "Cobalt Spark"
//  Primary Container #DDE4FF   "Iris Mist"
//  Secondary         #7C5CBF   "Royal Plum"
//  On Background     #0D0F14   "Midnight Void"
//  On Surface        #2A2D3A   "Graphite Ink"
//  On Primary        #FFFFFF   "Pure White"
//  Outline           #BEC4D9   "Perle Gray"
// ─────────────────────────────────────────────

// Dark palette
private val DarkBackground       = Color(0xFF0D0F14)
private val DarkSurface          = Color(0xFF161A23)
private val DarkSurfaceVariant   = Color(0xFF1E2330)
private val DarkPrimary          = Color(0xFF6C8EFF)
private val DarkPrimaryContainer = Color(0xFF1C274D)
private val DarkSecondary        = Color(0xFFA78BFA)
private val DarkOnBackground     = Color(0xFFE8EAF0)
private val DarkOnSurface        = Color(0xFFC5CAD6)
private val DarkOnPrimary        = Color(0xFFFFFFFF)
private val DarkOutline          = Color(0xFF2E3448)

// Light palette
private val LightBackground       = Color(0xFFF4F6FB)
private val LightSurface          = Color(0xFFFFFFFF)
private val LightSurfaceVariant   = Color(0xFFEAEDF5)
private val LightPrimary          = Color(0xFF3A5BF0)
private val LightPrimaryContainer = Color(0xFFDDE4FF)
private val LightSecondary        = Color(0xFF7C5CBF)
private val LightOnBackground     = Color(0xFF0D0F14)
private val LightOnSurface        = Color(0xFF2A2D3A)
private val LightOnPrimary        = Color(0xFFFFFFFF)
private val LightOutline          = Color(0xFFBEC4D9)

private val DarkColors = darkColorScheme(
    background       = DarkBackground,
    surface          = DarkSurface,
    surfaceVariant   = DarkSurfaceVariant,
    primary          = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary        = DarkSecondary,
    onBackground     = DarkOnBackground,
    onSurface        = DarkOnSurface,
    onPrimary        = DarkOnPrimary,
    outline          = DarkOutline,
    onSurfaceVariant = DarkOnSurface,
    inversePrimary   = DarkBackground
)

private val LightColors = lightColorScheme(
    background       = LightBackground,
    surface          = LightSurface,
    surfaceVariant   = LightSurfaceVariant,
    primary          = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    secondary        = LightSecondary,
    onBackground     = LightOnBackground,
    onSurface        = LightOnSurface,
    onPrimary        = LightOnPrimary,
    outline          = LightOutline,
    onSurfaceVariant = LightOnSurface,
    inversePrimary   = LightSurface
)

@Composable
fun ChatAppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
