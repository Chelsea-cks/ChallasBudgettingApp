package com.example.challasbudgettingapp.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = SurfaceLight,

    secondary = BluePrimary,
    onSecondary = SurfaceLight,

    tertiary = GoldAccent,
    onTertiary = BackgroundDark,

    background = BackgroundLight,
    onBackground = BackgroundDark,

    surface = SurfaceLight,
    onSurface = BackgroundDark,

    surfaceVariant = Color(0xFFE7EDE7),
    onSurfaceVariant = Color(0xFF424742),

    error = Color(0xFFBA1A1A),
    onError = SurfaceLight
)

private val DarkColors = darkColorScheme(
    primary = GreenLight,
    onPrimary = BackgroundDark,

    secondary = BlueLight,
    onSecondary = BackgroundDark,

    tertiary = GoldAccent,
    onTertiary = BackgroundDark,

    background = BackgroundDark,
    onBackground = Color(0xFFE6E1E5),

    surface = SurfaceDark,
    onSurface = Color(0xFFE6E1E5),

    surfaceVariant = Color(0xFF414941),
    onSurfaceVariant = Color(0xFFC1C9C0),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun ChallasBudgettingAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background
        ) {
            content()
        }
    }
}
