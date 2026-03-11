package com.example.mapsapp.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    onPrimary = DarkBackground,
    secondary = PalePink,
    onSecondary = DarkBackground,
    tertiary = SoftSage,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText
)

private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = WarmWhite,
    secondary = PalePink,
    onSecondary = ForestText,
    tertiary = SoftSage,
    background = Cream,
    onBackground = ForestText,
    surface = WarmWhite,
    onSurface = ForestText
)

/**
 * Main application theme.
 *
 * This theme defines the visual identity of the app using a soft,
 * minimal and map-inspired palette.
 *
 * @param darkTheme Whether dark theme should be used.
 * @param content Composable content wrapped by the theme.
 */
@Composable
fun MapsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}