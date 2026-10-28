package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// CompositionLocal to allow toggling theme from any screen
val LocalThemeIsDark = compositionLocalOf { mutableStateOf(false) }

private val ModernLightColorScheme = lightColorScheme(
    primary = ElectricIndigo,
    onPrimary = TextWhite,
    primaryContainer = SoftIndigoTint,
    onPrimaryContainer = ElectricIndigo,

    secondary = RoyalBlue,
    onSecondary = TextWhite,
    secondaryContainer = SoftCyanTint,
    onSecondaryContainer = RoyalBlue,

    tertiary = MintEmerald,
    onTertiary = TextWhite,
    tertiaryContainer = SoftMintTint,
    onTertiaryContainer = DeepEmerald,

    error = CoralRose,
    onError = TextWhite,
    errorContainer = SoftRoseTint,
    onErrorContainer = DeepRose,

    background = LightBg,
    onBackground = TextSlate900,

    surface = LightSurface,
    onSurface = TextSlate900,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSlate600,

    outline = LightBorder,
    outlineVariant = LightBorderSubtle
)

private val ModernDarkColorScheme = darkColorScheme(
    primary = RoyalBlue,
    onPrimary = TextWhite,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = SoftIndigoTint,

    secondary = ElectricIndigo,
    onSecondary = TextWhite,
    secondaryContainer = DarkSurfaceSubtle,
    onSecondaryContainer = SoftIndigoTint,

    tertiary = MintEmerald,
    onTertiary = TextWhite,
    tertiaryContainer = DarkSurfaceVariant,
    onTertiaryContainer = MintEmerald,

    error = CoralRose,
    onError = TextWhite,
    errorContainer = DarkSurfaceVariant,
    onErrorContainer = CoralRose,

    background = DarkBg,
    onBackground = TextWhite,

    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSlate400,

    outline = DarkBorder,
    outlineVariant = DarkBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ModernDarkColorScheme else ModernLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                val statusBarColor = if (darkTheme) DarkBg else LightBg
                val navBarColor = if (darkTheme) DarkSurface else LightSurface
                it.statusBarColor = statusBarColor.toArgb()
                it.navigationBarColor = navBarColor.toArgb()
                val controller = WindowCompat.getInsetsController(it, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
