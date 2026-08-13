package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryTeal,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryGold,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = AppBackground,
    onBackground = OnSurface,
    surface = AppSurface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariant,
    outline = AppOutline,
    outlineVariant = AppOutlineVariant,
    error = AppError,
    errorContainer = AppErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = OnPrimaryContainer,
    onPrimary = PrimaryNavy,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimary,
    secondary = SecondaryContainer,
    onSecondary = SecondaryTeal,
    secondaryContainer = OnSecondaryContainer,
    onSecondaryContainer = SecondaryContainer,
    background = OnSurface,
    onBackground = AppBackground,
    surface = OnSurface,
    onSurface = AppBackground,
    surfaceVariant = OnSurfaceVariant,
    onSurfaceVariant = SurfaceContainerHighest,
    outline = AppOutlineVariant,
    outlineVariant = AppOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

