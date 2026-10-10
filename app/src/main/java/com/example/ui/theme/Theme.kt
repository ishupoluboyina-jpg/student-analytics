package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = DarkPrimary,
  onPrimary = Color.Black,
  primaryContainer = RoyalBlueDark,
  onPrimaryContainer = Color.White,
  secondary = DarkSecondary,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF004D40),
  onSecondaryContainer = Color.White,
  tertiary = AccentAmber,
  background = DarkBackground,
  surface = DarkSurface,
  onBackground = Color(0xFFF1F5F9),
  onSurface = Color(0xFFF1F5F9),
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
  primary = RoyalBluePrimary,
  onPrimary = Color.White,
  primaryContainer = RoyalBlueLight,
  onPrimaryContainer = RoyalBlueDark,
  secondary = TealSecondary,
  onSecondary = Color.White,
  secondaryContainer = TealContainer,
  onSecondaryContainer = Color(0xFF00382E),
  tertiary = AccentAmber,
  background = BackgroundLight,
  surface = SurfaceLight,
  onBackground = SlateDark,
  onSurface = SlateDark,
  surfaceVariant = SlateLight,
  onSurfaceVariant = SlateMedium,
  outline = SlateBorder
)

@Composable
fun CareerBridgeTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
