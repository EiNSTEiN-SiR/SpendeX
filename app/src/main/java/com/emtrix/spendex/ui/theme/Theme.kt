package com.emtrix.spendex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF047857), onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5), onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF0F766E), onSecondary = Color.White,
    background = Color(0xFFF5F8F6), surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF14231D), onSurface = Color(0xFF14231D),
    surfaceVariant = Color(0xFFEAF1ED), outline = Color(0xFFDCE6DF),
    error = Color(0xFFDC2626)
)
private val Dark = darkColorScheme(
    primary = Color(0xFF34D399), onPrimary = Color(0xFF052E22),
    primaryContainer = Color(0xFF064E3B), onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF5EEAD4), onSecondary = Color(0xFF042F2E),
    background = Color(0xFF08130F), surface = Color(0xFF101D17),
    onBackground = Color(0xFFE8F3EC), onSurface = Color(0xFFE8F3EC),
    surfaceVariant = Color(0xFF1B2B23), outline = Color(0xFF2A4034),
    error = Color(0xFFF87171)
)

@Composable
fun SpendeXTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, typography = AppTypography, content = content)
}
