package com.emtrix.spendex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF059669), onPrimary = Color.White,
    background = Color(0xFFFAFAFA), surface = Color.White,
    onBackground = Color(0xFF18181B), onSurface = Color(0xFF18181B),
    surfaceVariant = Color(0xFFF4F4F5), outline = Color(0xFFE5E7EB)
)
private val Dark = darkColorScheme(
    primary = Color(0xFF10B981), onPrimary = Color(0xFF052E22),
    background = Color(0xFF09090B), surface = Color(0xFF121214),
    onBackground = Color(0xFFF4F4F5), onSurface = Color(0xFFF4F4F5),
    surfaceVariant = Color(0xFF1C1C1F), outline = Color(0xFF27272A)
)

@Composable
fun SpendeXTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, typography = AppTypography, content = content)
}
