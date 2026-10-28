package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Modern Bright Fintech Palette (Light First)
val LightBg = Color(0xFFF4F7FB)             // Soft Alabaster / Cool Pearl Gray
val LightSurface = Color(0xFFFFFFFF)        // Crisp White Card Surface
val LightSurfaceSubtle = Color(0xFFF8FAFC)  // Subtle Slate 50
val LightSurfaceVariant = Color(0xFFF1F5F9) // Slate 100
val LightBorder = Color(0xFFE2E8F0)         // Slate 200
val LightBorderSubtle = Color(0xFFEDF2F7)

// Dark Glass Palette
val DarkBg = Color(0xFF0A0F1D)              // Obsidian Navy
val DarkSurface = Color(0xFF131B2E)         // Slate Card Surface
val DarkSurfaceSubtle = Color(0xFF1A243B)
val DarkSurfaceVariant = Color(0xFF222F4C)
val DarkBorder = Color(0xFF1E293B)
val DarkBorderSubtle = Color(0xFF334155)

// Typography Palette
val TextSlate900 = Color(0xFF0F172A)
val TextSlate700 = Color(0xFF334155)
val TextSlate600 = Color(0xFF475569)
val TextSlate400 = Color(0xFF94A3B8)
val TextWhite = Color(0xFFFFFFFF)

// Modern Brand Accents (Gradients & Solid)
val ElectricIndigo = Color(0xFF4F46E5)
val RoyalBlue = Color(0xFF2563EB)
val SoftIndigoTint = Color(0xFFEEF2FF)
val IndigoBorder = Color(0xFFC7D2FE)

val BrandGradient = Brush.horizontalGradient(
    colors = listOf(ElectricIndigo, RoyalBlue)
)

// CALL / Trade Approved / Mint Emerald
val MintEmerald = Color(0xFF10B981)
val DeepEmerald = Color(0xFF059669)
val SoftMintTint = Color(0xFFECFDF5)
val MintBorder = Color(0xFFA7F3D0)

val CallGradient = Brush.horizontalGradient(
    colors = listOf(MintEmerald, DeepEmerald)
)

// PUT / Danger / Stop / Coral Rose
val CoralRose = Color(0xFFF43F5E)
val DeepRose = Color(0xFFE11D48)
val SoftRoseTint = Color(0xFFFFF1F2)
val RoseBorder = Color(0xFFFECDD3)

val PutGradient = Brush.horizontalGradient(
    colors = listOf(CoralRose, DeepRose)
)

// Warning / Cooldown / Warm Amber
val WarmAmber = Color(0xFFF59E0B)
val DeepAmber = Color(0xFFD97706)
val SoftAmberTint = Color(0xFFFFFBEB)
val AmberBorder = Color(0xFFFDE68A)

val WarningGradient = Brush.horizontalGradient(
    colors = listOf(WarmAmber, DeepAmber)
)

// Cyan & Violet Accents
val CyanAccent = Color(0xFF06B6D4)
val SoftCyanTint = Color(0xFFECFEFF)
val VioletAccent = Color(0xFF8B5CF6)
