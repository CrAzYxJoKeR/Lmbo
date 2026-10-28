package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradingSession
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.CallGradient
import com.example.ui.theme.CoralRose
import com.example.ui.theme.DeepAmber
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepRose
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceVariant
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.PutGradient
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftAmberTint
import com.example.ui.theme.SoftIndigoTint
import com.example.ui.theme.SoftMintTint
import com.example.ui.theme.SoftRoseTint
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate600
import com.example.ui.theme.TextSlate700
import com.example.ui.theme.TextSlate900
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarmAmber
import com.example.ui.theme.WarningGradient
import java.util.Locale

@Composable
fun TopEnforcementBar(
    session: TradingSession?,
    cooldownSecondsRemaining: Long,
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onSkipSessionClick: () -> Unit,
    onShowStakeLadder: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSkipConfirmDialog by remember { mutableStateOf(false) }

    // Pulsing dot animation for live status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMutedColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x140F172A)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(16.dp)
            .testTag("top_enforcement_bar")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Row 1: Header Brand, Status Pill, Theme Toggle, & Skip Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Pill with pulsing live dot
                ModernStatusPill(
                    session = session,
                    cooldownSeconds = cooldownSecondsRemaining,
                    pulseAlpha = pulseAlpha
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Skip session action
                    if (session != null && !session.isLocked && !session.isCompleted) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isDarkTheme) Color(0xFF1E293B) else LightSurfaceVariant)
                                .clickable { showSkipConfirmDialog = true }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("skip_session_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Block,
                                    contentDescription = "No-Trade",
                                    tint = WarmAmber,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "No-Trade",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmAmber,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Light / Dark Glass Mode Toggle
                    IconButton(
                        onClick = onToggleDarkTheme,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color(0xFF1E293B) else LightSurfaceVariant)
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDarkTheme) WarmAmber else ElectricIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (session == null) {
                // Zero-Session Welcome State
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Welcome Trader",
                            style = MaterialTheme.typography.titleSmall,
                            color = textMutedColor
                        )
                        Text(
                            text = "Start Session",
                            style = MaterialTheme.typography.headlineMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(BrandGradient)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "2% Risk Lock Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Active Session Wallet Card Details
                val totalBalance = session.startingBalance + session.currentProfit
                val profitPrefix = if (session.currentProfit >= 0) "+$" else "-$"
                val profitColor = if (session.currentProfit >= 0) MintEmerald else CoralRose

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "ACCOUNT BALANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = textMutedColor,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format(Locale.US, "$%,.2f", totalBalance),
                                style = MaterialTheme.typography.headlineLarge,
                                color = textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Session profit indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format(Locale.US, "%s%.2f session P/L", profitPrefix, kotlin.math.abs(session.currentProfit)),
                                style = MaterialTheme.typography.labelMedium,
                                color = profitColor,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (session.winStreak > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SoftMintTint)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = MintEmerald,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${session.winStreak}W",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DeepEmerald,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Locked 2% Stake Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDarkTheme) Color(0xFF1E293B) else SoftIndigoTint)
                            .border(1.dp, if (isDarkTheme) Color(0xFF334155) else IndigoBorder, RoundedCornerShape(16.dp))
                            .clickable { onShowStakeLadder() }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = ElectricIndigo,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Column {
                                Text(
                                    text = String.format(Locale.US, "$%.2f", session.stakeAmount),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ElectricIndigo,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "2% Fixed Stake",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDarkTheme) TextSlate400 else TextSlate600,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Modern Segmented Progress Bars (Trades 0/5 & Losses 0/2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Segmented Trades (5 segments)
                    SegmentedProgressCard(
                        title = "Trades",
                        current = session.totalTrades,
                        max = session.maxTrades,
                        activeBrush = BrandGradient,
                        inactiveColor = if (isDarkTheme) Color(0xFF222F4C) else LightSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                    // Segmented Losses (2 crimson segments)
                    SegmentedProgressCard(
                        title = "Loss Limit",
                        current = session.losses,
                        max = session.maxLosses,
                        activeBrush = PutGradient,
                        inactiveColor = if (isDarkTheme) Color(0xFF222F4C) else LightSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        isDanger = true
                    )
                }
            }
        }
    }

    if (showSkipConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSkipConfirmDialog = false },
            title = {
                Text(
                    text = "Log No-Trade Decision",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor
                )
            },
            text = {
                Column {
                    Text(
                        text = "\"The no-trade decision is a valid decision.\"",
                        style = MaterialTheme.typography.titleSmall,
                        color = MintEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "If conditions are subpar, preserving 100% of capital is a hallmark of elite trading discipline. This logs a 100% Process Score session.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textMutedColor
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSkipConfirmDialog = false
                        onSkipSessionClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintEmerald),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("confirm_skip_session")
                ) {
                    Text("LOG NO-TRADE VICTORY", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSkipConfirmDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("CANCEL", color = textMutedColor)
                }
            },
            containerColor = surfaceColor,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ModernStatusPill(
    session: TradingSession?,
    cooldownSeconds: Long,
    pulseAlpha: Float
) {
    val (pillBg, pillBorder, pillText, textColor, pulseColor) = when {
        session == null -> {
            Tuple5(SoftIndigoTint, IndigoBorder, "READY TO START", ElectricIndigo, ElectricIndigo)
        }
        session.isLocked -> {
            val text = when (session.lockReason) {
                "MAX_LOSSES" -> "2 LOSSES — LOCKED"
                "MAX_TRADES" -> "5 TRADES — LOCKED"
                "PROFIT_TARGET" -> "TARGET HIT — CLOSED"
                "NON_TRADING_DAY" -> "REST DAY ENFORCED"
                "NO_TRADE_DECISION" -> "NO-TRADE COMPLETED"
                else -> "SESSION LOCKED"
            }
            Tuple5(SoftRoseTint, RoseBorder, text, DeepRose, CoralRose)
        }
        cooldownSeconds > 0 -> {
            val minutes = cooldownSeconds / 60
            val seconds = cooldownSeconds % 60
            val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)
            Tuple5(SoftAmberTint, AmberBorder, "COOLDOWN ($timeFormatted)", DeepAmber, WarmAmber)
        }
        else -> {
            Tuple5(SoftMintTint, MintBorder, "READY TO ANALYZE", DeepEmerald, MintEmerald)
        }
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(pillBg)
            .border(1.dp, pillBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Pulsing dot
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(pulseColor)
                    .alpha(pulseAlpha)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = pillText,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SegmentedProgressCard(
    title: String,
    current: Int,
    max: Int,
    activeBrush: Brush,
    inactiveColor: Color,
    modifier: Modifier = Modifier,
    isDanger: Boolean = false
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (isDanger && current >= max) CoralRose else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$current/$max",
                style = MaterialTheme.typography.labelSmall,
                color = if (isDanger && current >= max) CoralRose else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Segmented pill bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until max) {
                val isFilled = i < current
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .then(
                            if (isFilled) Modifier.background(activeBrush)
                            else Modifier.background(inactiveColor)
                        )
                )
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
