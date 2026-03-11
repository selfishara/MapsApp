package com.example.mapsapp.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkTerracotta,
    onPrimary = DarkBackground,

    secondary = DarkRose,
    onSecondary = DarkBackground,

    tertiary = TerracottaSoft,
    onTertiary = DarkBackground,

    background = DarkBackground,
    onBackground = DarkText,

    surface = DarkSurface,
    onSurface = DarkText,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMutedText,

    outline = DarkMutedText.copy(alpha = 0.35f)
)

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = WarmWhite,

    secondary = RoseMist,
    onSecondary = CocoaText,

    tertiary = TerracottaSoft,
    onTertiary = CocoaText,

    background = Cream,
    onBackground = CocoaText,

    surface = WarmWhite,
    onSurface = CocoaText,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = MutedBrown,

    outline = BorderColor
)

/**
 * Main application theme.
 *
 * This theme defines the visual identity of the app using a warm,
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