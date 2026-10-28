package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PreTradeState
import com.example.data.model.Trade
import com.example.data.model.TradingSession
import com.example.ui.components.BreakEvenCalculatorCard
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.CallGradient
import com.example.ui.theme.CoralRose
import com.example.ui.theme.DeepAmber
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepRose
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.PutGradient
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.SoftAmberTint
import com.example.ui.theme.SoftCyanTint
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
import com.example.ui.viewmodel.GatekeeperViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GatekeeperScreen(
    viewModel: GatekeeperViewModel,
    onNavigateToReview: () -> Unit,
    onShowStakeLadder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSession by viewModel.activeSession.collectAsState()
    val preTradeState by viewModel.preTradeState.collectAsState()
    val activePendingTrade by viewModel.activePendingTrade.collectAsState()
    val approvalCountdown by viewModel.approvalCountdown.collectAsState()
    val isTradeApproved by viewModel.isTradeApproved.collectAsState()
    val cooldownSeconds by viewModel.cooldownSecondsRemaining.collectAsState()
    val sameAssetBlocked by viewModel.sameAssetRevengeBlocked.collectAsState()
    val sameAssetSec by viewModel.sameAssetRemainingSeconds.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            when {
                // State 1: No active session -> Modern Setup screen
                activeSession == null -> {
                    ModernSessionSetupView(
                        onStartSession = { bal, profit, strict ->
                            viewModel.startNewSession(bal, profit, strict)
                        },
                        onShowStakeLadder = onShowStakeLadder
                    )
                }

                // State 2: Session is Locked (Hard Stop, Max Trades, Target Hit, Non-Trading Day, etc.)
                activeSession?.isLocked == true -> {
                    ModernSessionLockedView(
                        session = activeSession!!,
                        onProceedToReview = onNavigateToReview,
                        onResetSession = { viewModel.resetActiveSession() }
                    )
                }

                // State 3: Cooldown timer active (Enforced pause after loss, 3 wins, or 3 trades)
                cooldownSeconds > 0 -> {
                    ModernCooldownActiveView(
                        session = activeSession!!,
                        cooldownSeconds = cooldownSeconds
                    )
                }

                // State 4: Trade approved and awaiting execution outcome
                activePendingTrade != null -> {
                    ModernActiveTradeResolutionView(
                        trade = activePendingTrade!!,
                        onLogOutcome = { outcome, emotion, screenshot, lesson ->
                            viewModel.logTradeOutcome(outcome, emotion, screenshot, lesson)
                        }
                    )
                }

                // State 5: Active Pre-Trade Gatekeeper (Decision Tree: TRADE / WAIT / STOP)
                else -> {
                    ModernPreTradeGatekeeperView(
                        state = preTradeState,
                        session = activeSession!!,
                        sameAssetBlocked = sameAssetBlocked,
                        sameAssetSec = sameAssetSec,
                        approvalCountdown = approvalCountdown,
                        isApproved = isTradeApproved,
                        onStateUpdate = { update -> viewModel.updateChecklist(update) },
                        onAssetSelected = { viewModel.setAsset(it) },
                        onMarketTypeSelected = { viewModel.setMarketType(it) },
                        onDirectionSelected = { viewModel.setDirection(it) },
                        onPayoutChanged = { viewModel.setPayoutPct(it) },
                        onExplanationChanged = { viewModel.setExplanation(it) },
                        onInvalidationChanged = { viewModel.setInvalidation(it) },
                        onStartApprovalCountdown = { viewModel.startApprovalCountdown() },
                        onCancelApprovalCountdown = { viewModel.cancelApprovalCountdown() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

// -------------------------------------------------------------
// MODULE 1: MODERN FINTECH SESSION SETUP CARD
// -------------------------------------------------------------
@Composable
private fun ModernSessionSetupView(
    onStartSession: (balance: Double, profitTarget: Double, strictCooldown: Boolean) -> Unit,
    onShowStakeLadder: () -> Unit
) {
    var balanceInput by remember { mutableStateOf("1000") }
    var profitTargetInput by remember { mutableStateOf("60") }
    var strictCooldown by remember { mutableStateOf(false) }

    val balance = balanceInput.toDoubleOrNull() ?: 1000.0
    val lockedStake = balance * 0.02

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMuted = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
            .testTag("session_setup_card")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BrandGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Launch Daily Session",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Automated risk locks prevent drift & tilt",
                    style = MaterialTheme.typography.bodySmall,
                    color = textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Strict Rule Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SoftRoseTint)
                .border(1.dp, RoseBorder, RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = CoralRose,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FIXED 2% STAKE. NO MARTINGALE.",
                    style = MaterialTheme.typography.labelMedium,
                    color = DeepRose,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Account Balance Input
        Text(
            text = "ACCOUNT BALANCE ($)",
            style = MaterialTheme.typography.labelSmall,
            color = textMuted,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = balanceInput,
            onValueChange = { balanceInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricIndigo,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("balance_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Locked Stake Display Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SoftIndigoTint)
                .border(1.dp, IndigoBorder, RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LOCKED 2% FIXED STAKE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.US, "$%.2f per trade", lockedStake),
                    style = MaterialTheme.typography.headlineSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedButton(
                onClick = onShowStakeLadder,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricIndigo),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = BrandGradient),
                modifier = Modifier.testTag("stake_ladder_button")
            ) {
                Text("Why 2%?", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profit Target Input
        Text(
            text = "SESSION PROFIT CEILING ($)",
            style = MaterialTheme.typography.labelSmall,
            color = textMuted,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = profitTargetInput,
            onValueChange = { profitTargetInput = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricIndigo,
                unfocusedBorderColor = borderColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profit_target_input")
        )
        Text(
            text = "Locks terminal when reached to prevent Profit Giveback.",
            style = MaterialTheme.typography.labelSmall,
            color = textMuted,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Strict Cooldown Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (strictCooldown) SoftAmberTint else MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, if (strictCooldown) AmberBorder else borderColor, RoundedCornerShape(14.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Strict 15-Minute Cooldown Mode",
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (strictCooldown) "15:00 min mandatory pause after loss" else "05:00 min standard pause after loss",
                    style = MaterialTheme.typography.bodySmall,
                    color = textMuted
                )
            }
            Switch(
                checked = strictCooldown,
                onCheckedChange = { strictCooldown = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = WarmAmber
                ),
                modifier = Modifier.testTag("strict_cooldown_switch")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val pTarget = profitTargetInput.toDoubleOrNull() ?: 60.0
                onStartSession(balance, pTarget, strictCooldown)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_session_button")
        ) {
            Text(
                text = "START DISCIPLINED SESSION",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// -------------------------------------------------------------
// MODULE 2: MODERN PRE-TRADE GATEKEEPER VIEW
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernPreTradeGatekeeperView(
    state: PreTradeState,
    session: TradingSession,
    sameAssetBlocked: Boolean,
    sameAssetSec: Long,
    approvalCountdown: Int,
    isApproved: Boolean,
    onStateUpdate: ((PreTradeState) -> PreTradeState) -> Unit,
    onAssetSelected: (String) -> Unit,
    onMarketTypeSelected: (String) -> Unit,
    onDirectionSelected: (String) -> Unit,
    onPayoutChanged: (Double) -> Unit,
    onExplanationChanged: (String) -> Unit,
    onInvalidationChanged: (String) -> Unit,
    onStartApprovalCountdown: () -> Unit,
    onCancelApprovalCountdown: () -> Unit
) {
    val watchlistItems = listOf(
        Pair("EUR/USD", "🇪🇺🇺🇸"),
        Pair("GBP/USD", "🇬🇧🇺🇸"),
        Pair("USD/JPY", "🇺🇸🇯🇵"),
        Pair("Gold XAU/USD", "🥇"),
        Pair("BTC/USD", "₿"),
        Pair("AUD/USD", "🇦🇺🇺🇸"),
        Pair("USD/CAD", "🇺🇸🇨🇦"),
        Pair("EUR/JPY", "🇪🇺🇯🇵")
    )

    // Fast Binary 1-Tap Mode Toggle
    var fastBinaryMode by remember { mutableStateOf(true) }

    // Tab state for 2-Step Checklist Wizard (Branch A vs Branch B)
    var selectedWizardTab by remember { mutableIntStateOf(0) }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMuted = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = Modifier.fillMaxWidth()) {
        // Same-Asset Revenge Alert if triggered
        if (sameAssetBlocked) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SoftRoseTint)
                    .border(1.dp, RoseBorder, RoundedCornerShape(18.dp))
                    .padding(14.dp)
                    .testTag("revenge_blocker_alert")
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PutGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Same-Asset Revenge Blocker",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepRose,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Expiry loss on ${state.asset} within the last 5 minutes. Locked for ${sameAssetSec}s. Switch pair or wait.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CoralRose
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Section 1: Modern Asset Selector Carousel & Parameters
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                .clip(RoundedCornerShape(24.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT ASSET & ORDER",
                    style = MaterialTheme.typography.labelSmall,
                    color = textMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                // Fast Binary Mode Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (fastBinaryMode) SoftCyanTint else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { fastBinaryMode = !fastBinaryMode }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = if (fastBinaryMode) ElectricIndigo else textMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "1-Tap Fast Mode",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (fastBinaryMode) ElectricIndigo else textMuted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Swipeable Horizontal Carousel of Asset Pills
            val carouselScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(carouselScroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                watchlistItems.forEach { (assetName, flag) ->
                    val isSelected = state.asset == assetName
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .then(
                                if (isSelected) Modifier.background(BrandGradient)
                                else Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.Transparent else borderColor,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onAssetSelected(assetName) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = flag, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = assetName,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) TextWhite else textColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // iOS-Style Sliding Segmented Control for Market Type (Standard | OTC)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (state.marketType == "Standard") surfaceColor else Color.Transparent)
                        .clickable { onMarketTypeSelected("Standard") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Standard Market",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (state.marketType == "Standard") textColor else textMuted,
                        fontWeight = if (state.marketType == "Standard") FontWeight.Bold else FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (state.marketType == "OTC") SoftAmberTint else Color.Transparent)
                        .clickable { onMarketTypeSelected("OTC") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "OTC (Broker Sim)",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (state.marketType == "OTC") DeepAmber else textMuted,
                        fontWeight = if (state.marketType == "OTC") FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tactile 3D-Feel CALL and PUT Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CALL CARD
                val isCall = state.direction == "CALL"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .shadow(if (isCall) 8.dp else 2.dp, RoundedCornerShape(18.dp), spotColor = MintEmerald)
                        .clip(RoundedCornerShape(18.dp))
                        .then(
                            if (isCall) Modifier.background(CallGradient)
                            else Modifier.background(SoftMintTint).border(1.dp, MintBorder, RoundedCornerShape(18.dp))
                        )
                        .clickable { onDirectionSelected("CALL") }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isCall) Color.White.copy(alpha = 0.25f) else MintEmerald),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "CALL",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CALL / BUY",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isCall) TextWhite else DeepEmerald,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bullish Setup",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isCall) TextWhite.copy(alpha = 0.85f) else MintEmerald
                            )
                        }
                    }
                }

                // PUT CARD
                val isPut = state.direction == "PUT"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .shadow(if (isPut) 8.dp else 2.dp, RoundedCornerShape(18.dp), spotColor = CoralRose)
                        .clip(RoundedCornerShape(18.dp))
                        .then(
                            if (isPut) Modifier.background(PutGradient)
                            else Modifier.background(SoftRoseTint).border(1.dp, RoseBorder, RoundedCornerShape(18.dp))
                        )
                        .clickable { onDirectionSelected("PUT") }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isPut) Color.White.copy(alpha = 0.25f) else CoralRose),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "PUT",
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PUT / SELL",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isPut) TextWhite else DeepRose,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bearish Setup",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isPut) TextWhite.copy(alpha = 0.85f) else CoralRose
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Broker Payout Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Broker Payout",
                    style = MaterialTheme.typography.labelMedium,
                    color = textMuted,
                    fontWeight = FontWeight.SemiBold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(75.0, 80.0, 85.0, 90.0, 95.0).forEach { p ->
                        val isSelected = state.payoutPct == p
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .then(
                                    if (isSelected) Modifier.background(BrandGradient)
                                    else Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                                )
                                .clickable { onPayoutChanged(p) }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${p.toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) TextWhite else textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BreakEvenCalculatorCard(payoutPct = state.payoutPct)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 2: Interactive 2-Step Checklist Wizard (Branch A & Branch B)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                .clip(RoundedCornerShape(24.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                .padding(18.dp)
        ) {
            // Wizard Step Selector Tabs
            TabRow(
                selectedTabIndex = selectedWizardTab,
                containerColor = Color.Transparent,
                contentColor = ElectricIndigo,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedWizardTab]),
                        color = ElectricIndigo
                    )
                }
            ) {
                Tab(
                    selected = selectedWizardTab == 0,
                    onClick = { selectedWizardTab = 0 },
                    text = {
                        val countA = listOf(
                            state.a1Watchlist, state.a2HigherTimeframeTrend, state.a3EntryTriggerConfirmed,
                            state.a4PayoutAndBreakEvenUnderstood, state.a5StakeMatchesFixed2Pct,
                            state.a6ExpiryTimeframeDefined, state.a7NewsCalendarChecked
                        ).count { it }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("1. Setup Quality", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (countA == 7) SoftMintTint else MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "$countA/7",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (countA == 7) DeepEmerald else textMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )

                Tab(
                    selected = selectedWizardTab == 1,
                    onClick = { selectedWizardTab = 1 },
                    text = {
                        val countB = listOf(
                            state.b1WithinTwoLossLimit, state.b2EmotionalNeutrality10Min, state.b3PhysicalEnergyAndSleep,
                            state.b4NoLossRecoveryIntent, state.b5NoExternalOrCountdownPressure,
                            state.b6NoBlindSignalFollowing, state.b7CanExplainInOneSentence
                        ).count { it }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("2. Internal State", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (countB == 7) SoftMintTint else MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "$countB/7",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (countB == 7) DeepEmerald else textMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (selectedWizardTab == 0) {
                // Branch A Tappable Cards
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernTapCard(
                        title = "Asset Watchlist",
                        subtitle = "Asset is on my pre-approved watchlist",
                        isChecked = state.a1Watchlist,
                        onToggle = { onStateUpdate { s -> s.copy(a1Watchlist = it) } }
                    )
                    ModernTapCard(
                        title = "HTF Trend Direction",
                        subtitle = "Higher timeframe trend is clean and aligned",
                        isChecked = state.a2HigherTimeframeTrend,
                        onToggle = { onStateUpdate { s -> s.copy(a2HigherTimeframeTrend = it) } }
                    )
                    ModernTapCard(
                        title = "Entry Trigger Confirmed",
                        subtitle = "Candle closed at trigger level — zero front-running",
                        isChecked = state.a3EntryTriggerConfirmed,
                        onToggle = { onStateUpdate { s -> s.copy(a3EntryTriggerConfirmed = it) } }
                    )
                    ModernTapCard(
                        title = "Break-Even Win Rate",
                        subtitle = "Required break-even win rate calculated & verified",
                        isChecked = state.a4PayoutAndBreakEvenUnderstood,
                        onToggle = { onStateUpdate { s -> s.copy(a4PayoutAndBreakEvenUnderstood = it) } }
                    )
                    ModernTapCard(
                        title = "Fixed 2% Stake",
                        subtitle = "Matches locked $$session.stakeAmount stake plan",
                        isChecked = state.a5StakeMatchesFixed2Pct,
                        onToggle = { onStateUpdate { s -> s.copy(a5StakeMatchesFixed2Pct = it) } }
                    )
                    ModernTapCard(
                        title = "Expiry Timeframe Defined",
                        subtitle = "Expiry duration accurately matches technical setup",
                        isChecked = state.a6ExpiryTimeframeDefined,
                        onToggle = { onStateUpdate { s -> s.copy(a6ExpiryTimeframeDefined = it) } }
                    )
                    ModernTapCard(
                        title = "News Calendar Clear",
                        subtitle = "No high-impact red news scheduled near entry",
                        isChecked = state.a7NewsCalendarChecked,
                        onToggle = { onStateUpdate { s -> s.copy(a7NewsCalendarChecked = it) } }
                    )
                }
            } else {
                // Branch B Tappable Cards
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModernTapCard(
                        title = "Within 2-Loss Session Limit",
                        subtitle = "I have not reached or exceeded the 2-loss stop",
                        isChecked = state.b1WithinTwoLossLimit,
                        onToggle = { onStateUpdate { s -> s.copy(b1WithinTwoLossLimit = it) } }
                    )
                    ModernTapCard(
                        title = "Emotional Neutrality (10 min)",
                        subtitle = "Zero anger, anxiety, or euphoria in past 10 minutes",
                        isChecked = state.b2EmotionalNeutrality10Min,
                        onToggle = { onStateUpdate { s -> s.copy(b2EmotionalNeutrality10Min = it) } }
                    )
                    ModernTapCard(
                        title = "Physical Energy & Sleep",
                        subtitle = "Rested, hydrated, fed — no cognitive fatigue",
                        isChecked = state.b3PhysicalEnergyAndSleep,
                        onToggle = { onStateUpdate { s -> s.copy(b3PhysicalEnergyAndSleep = it) } }
                    )
                    ModernTapCard(
                        title = "No Recovery Obsession",
                        subtitle = "Not attempting to win back a prior loss",
                        isChecked = state.b4NoLossRecoveryIntent,
                        onToggle = { onStateUpdate { s -> s.copy(b4NoLossRecoveryIntent = it) } }
                    )
                    ModernTapCard(
                        title = "No Countdown Pressure",
                        subtitle = "Unpressured by platform timers, family, or work",
                        isChecked = state.b5NoExternalOrCountdownPressure,
                        onToggle = { onStateUpdate { s -> s.copy(b5NoExternalOrCountdownPressure = it) } }
                    )
                    ModernTapCard(
                        title = "Independent Verification",
                        subtitle = "Not blindly following Telegram, bot, or signal calls",
                        isChecked = state.b6NoBlindSignalFollowing,
                        onToggle = { onStateUpdate { s -> s.copy(b6NoBlindSignalFollowing = it) } }
                    )
                    ModernTapCard(
                        title = "1-Sentence Clarity",
                        subtitle = "I can articulate why this trade makes sense in one line",
                        isChecked = state.b7CanExplainInOneSentence,
                        onToggle = { onStateUpdate { s -> s.copy(b7CanExplainInOneSentence = it) } }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 3: Anti-Confirmation Bias & Fast 1-Tap Chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                .clip(RoundedCornerShape(24.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                .padding(18.dp)
        ) {
            Text(
                text = "ANTI-CONFIRMATION BIAS GATE",
                style = MaterialTheme.typography.labelSmall,
                color = textMuted,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation Section
            Text(
                text = "1. Explain Setup in One Sentence:",
                style = MaterialTheme.typography.titleSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )

            // Fast Binary Mode 1-Tap Chips for Reason
            if (fastBinaryMode) {
                Spacer(modifier = Modifier.height(6.dp))
                val setupTags = listOf("S/R Rejection", "Trend Pullback", "Breakout Retest", "Liquidity Sweep", "HTF Alignment")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    setupTags.forEach { tag ->
                        val isSelected = state.oneSentenceExplanation.contains(tag)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftIndigoTint else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (isSelected) ElectricIndigo else borderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    val current = state.oneSentenceExplanation
                                    if (current.isBlank()) {
                                        onExplanationChanged(tag + " with confirmed candle close.")
                                    } else if (!current.contains(tag)) {
                                        onExplanationChanged("$current $tag")
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) ElectricIndigo else textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.oneSentenceExplanation,
                onValueChange = onExplanationChanged,
                placeholder = { Text("e.g. Trend pullback bounced off 50% EMA with confirmed engulfing candle.", color = textMuted) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricIndigo,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("explanation_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Devil's Advocate Section
            Text(
                text = "2. Devil's Advocate (Argue Against Yourself):",
                style = MaterialTheme.typography.titleSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )

            // Fast Binary Mode 1-Tap Chips for Invalidation
            if (fastBinaryMode) {
                Spacer(modifier = Modifier.height(6.dp))
                val invalidationTags = listOf("Close Beyond Level", "News Spike Near", "Low Volatility / Doji", "Fighting Strong Momentum")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    invalidationTags.forEach { tag ->
                        val isSelected = state.invalidationArgument.contains(tag)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftAmberTint else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (isSelected) WarmAmber else borderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    val current = state.invalidationArgument
                                    if (current.isBlank()) {
                                        onInvalidationChanged(tag + " would immediately invalidate this bias.")
                                    } else if (!current.contains(tag)) {
                                        onInvalidationChanged("$current $tag")
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) DeepAmber else textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = state.invalidationArgument,
                onValueChange = onInvalidationChanged,
                placeholder = { Text("e.g. Higher timeframe resistance 3 pips above; break of key level invalidates.", color = textMuted) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WarmAmber,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("invalidation_input")
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Dynamic Verdict Banner & Animated Sweep 3-Second Approval Button
        val canApprove = state.allConditionsPass && !sameAssetBlocked

        if (!canApprove) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(SoftAmberTint)
                    .border(1.dp, AmberBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
                    .testTag("verdict_wait_banner")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(WarningGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "WAIT — CONDITIONS NOT READY",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${state.checkedCount}/14 checklist criteria verified. Both sentences required.",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmAmber
                        )
                    }
                }
            }
        } else {
            // All conditions pass! Show Floating 3-Second Pill Button with sweeping fill bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = MintEmerald)
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, MintBorder, RoundedCornerShape(24.dp))
                    .padding(18.dp)
                    .testTag("verdict_pass_banner")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CallGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ALL CONDITIONS PASS",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepEmerald,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hold 3-second breathing countdown to verify execution.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MintEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (approvalCountdown > 0) {
                    // Animated sweeping fill-bar button
                    val fillProgress = (4 - approvalCountdown) / 3f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = fillProgress)
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(CallGradient)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Breathe & Verify: $approvalCountdown...",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedButton(
                                onClick = onCancelApprovalCountdown,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralRose)
                            ) {
                                Text("CANCEL", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = onStartApprovalCountdown,
                        colors = ButtonDefaults.buttonColors(containerColor = MintEmerald),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("approve_trade_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "COUNT 3 SECONDS TO APPROVE TRADE",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAP CARD COMPONENT (Replaces boring HTML checkboxes)
// -------------------------------------------------------------
@Composable
private fun ModernTapCard(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val animatedBg by animateColorAsState(
        targetValue = if (isChecked) SoftMintTint else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(200),
        label = "tapCardBg"
    )
    val animatedBorder by animateColorAsState(
        targetValue = if (isChecked) MintBorder else MaterialTheme.colorScheme.outline,
        animationSpec = tween(200),
        label = "tapCardBorder"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(14.dp))
            .clickable { onToggle(!isChecked) }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isChecked) DeepEmerald else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isChecked) DeepEmerald.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Circular Checkmark Badge
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isChecked) MintEmerald else MaterialTheme.colorScheme.surface)
                    .border(
                        1.dp,
                        if (isChecked) MintEmerald else MaterialTheme.colorScheme.outline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checked",
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// MODULE 3: ACTIVE RESOLUTION VIEW (Modern Fintech)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ModernActiveTradeResolutionView(
    trade: Trade,
    onLogOutcome: (outcome: String, emotion: String, screenshot: String, lesson: String) -> Unit
) {
    var selectedOutcome by remember { mutableStateOf("WIN") }
    var selectedEmotion by remember { mutableStateOf("Focused") }
    var screenshotUrl by remember { mutableStateOf("") }
    var lessonLearned by remember { mutableStateOf("") }

    val emotions = listOf("Calm", "Focused", "Hesitant", "FOMO", "Urgent")
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = ElectricIndigo)
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, IndigoBorder, RoundedCornerShape(24.dp))
            .padding(20.dp)
            .testTag("active_resolution_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE EXECUTION RESOLUTION",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${trade.asset} • ${trade.direction}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (trade.direction == "CALL") MintEmerald else CoralRose,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftIndigoTint)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Stake: $${trade.stakeAmount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Setup: \"${trade.oneSentenceExplanation}\"",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "RECORD OUTCOME:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // WIN
            val isWin = selectedOutcome == "WIN"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isWin) SoftMintTint else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (isWin) MintEmerald else Color.Transparent, RoundedCornerShape(14.dp))
                    .clickable { selectedOutcome = "WIN" }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = MintEmerald)
                    Text("WIN", style = MaterialTheme.typography.labelLarge, color = DeepEmerald, fontWeight = FontWeight.Bold)
                }
            }

            // CLEAN LOSS
            val isClean = selectedOutcome == "CLEAN_LOSS"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isClean) SoftRoseTint else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (isClean) CoralRose else Color.Transparent, RoundedCornerShape(14.dp))
                    .clickable { selectedOutcome = "CLEAN_LOSS" }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = CoralRose)
                    Text("CLEAN LOSS", style = MaterialTheme.typography.labelLarge, color = DeepRose, fontWeight = FontWeight.Bold)
                }
            }

            // NEAR-MISS LOSS
            val isNearMiss = selectedOutcome == "NEAR_MISS_LOSS"
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isNearMiss) SoftAmberTint else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (isNearMiss) WarmAmber else Color.Transparent, RoundedCornerShape(14.dp))
                    .clickable { selectedOutcome = "NEAR_MISS_LOSS" }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = WarmAmber)
                    Text("NEAR-MISS", style = MaterialTheme.typography.labelLarge, color = DeepAmber, fontWeight = FontWeight.Bold)
                    Text("Last 10s flip", style = MaterialTheme.typography.labelSmall, color = WarmAmber, fontSize = 9.sp)
                }
            }
        }

        if (selectedOutcome == "NEAR_MISS_LOSS") {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftAmberTint)
                    .border(1.dp, AmberBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "\"Right for 59 seconds is not right. Only the expiry counts. A near-miss is a probability event, not a personal injustice.\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepAmber,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "EMOTIONAL STATE BEFORE ENTRY:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            emotions.forEach { em ->
                val isSelected = selectedEmotion == em
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedEmotion = em },
                    label = { Text(em, style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (em in listOf("FOMO", "Urgent")) SoftRoseTint else SoftIndigoTint,
                        selectedLabelColor = if (em in listOf("FOMO", "Urgent")) DeepRose else ElectricIndigo,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "ONE LESSON LEARNED:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = lessonLearned,
            onValueChange = { lessonLearned = it },
            placeholder = { Text("What did I execute well, and what could be sharpened?", color = TextSlate400) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().testTag("lesson_input")
        )

        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = { onLogOutcome(selectedOutcome, selectedEmotion, screenshotUrl, lessonLearned) },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("log_trade_outcome_button")
        ) {
            Text("LOG TRADE & ENFORCE DISCIPLINE", color = TextWhite, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// MODULE 3: MODERN COOLDOWN VIEW
// -------------------------------------------------------------
@Composable
private fun ModernCooldownActiveView(
    session: TradingSession,
    cooldownSeconds: Long
) {
    val minutes = cooldownSeconds / 60
    val seconds = cooldownSeconds % 60
    val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = WarmAmber)
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, AmberBorder, RoundedCornerShape(24.dp))
            .padding(24.dp)
            .testTag("cooldown_active_card"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(WarningGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        val title = when (session.cooldownType) {
            "WIN_STREAK_EUPHORIA" -> "Euphoria Pause (3 Wins)"
            "MID_SESSION_BREAK" -> "15-Minute Mid-Session Reset"
            else -> "5-Minute Expiry Revenge Cooldown"
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = DeepAmber,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Big Modern Countdown Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(SoftAmberTint)
                .border(1.dp, AmberBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 32.dp, vertical = 14.dp)
        ) {
            Text(
                text = timeFormatted,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 42.sp),
                color = DeepAmber,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val reframingMessage = when (session.cooldownType) {
            "WIN_STREAK_EUPHORIA" ->
                "\"Pause after every 3 wins. A win streak is a statistical run, not a new strategy. The market has no memory. Re-confirm the next setup.\""
            "MID_SESSION_BREAK" ->
                "\"3 trades completed. Step away from the screen for 15 minutes to reset your cognitive focus and prevent fatigue slippage.\""
            else ->
                "\"Right for 59 seconds is not right. Only the expiry counts. A near-miss is a probability event, not a personal injustice. Step back and breathe.\""
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(14.dp)
        ) {
            Text(
                text = reframingMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// -------------------------------------------------------------
// MODULE 1 & 3: MODERN SESSION LOCKED SCREEN
// -------------------------------------------------------------
@Composable
private fun ModernSessionLockedView(
    session: TradingSession,
    onProceedToReview: () -> Unit,
    onResetSession: () -> Unit
) {
    val isMaxLosses = session.lockReason == "MAX_LOSSES"
    val isProfitTarget = session.lockReason == "PROFIT_TARGET"
    val isMaxTrades = session.lockReason == "MAX_TRADES"
    val isNonTradingDay = session.lockReason == "NON_TRADING_DAY"
    val isNoTrade = session.lockReason == "NO_TRADE_DECISION"

    val headerColor = when {
        isProfitTarget || isNoTrade -> MintEmerald
        else -> CoralRose
    }

    val headerGradient = when {
        isProfitTarget || isNoTrade -> CallGradient
        else -> PutGradient
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = headerColor)
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, if (isProfitTarget || isNoTrade) MintBorder else RoseBorder, RoundedCornerShape(24.dp))
            .padding(24.dp)
            .testTag("session_locked_card"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(headerGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        val title = when {
            isMaxLosses -> "2 Losses = Session Over"
            isProfitTarget -> "Profit Target Hit — Close Laptop"
            isMaxTrades -> "Maximum 5 Trades Reached"
            isNonTradingDay -> "Non-Trading Day Enforced"
            isNoTrade -> "Disciplined No-Trade Session"
            else -> "Session Locked"
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        val message = when {
            isMaxLosses ->
                "\"AFTER 2 LOSSES, CLOSE THE LAPTOP. The market does not owe you. You owe yourself discipline.\"\n\nStopping now protects tomorrow's capital. Your prefrontal cortex is vulnerable."
            isProfitTarget ->
                "\"Target achieved ($${String.format(Locale.US, "%.2f", session.profitTarget)} profit).\n\nDo not give it back to the broker. Close the laptop and bank the win.\""
            isMaxTrades ->
                "\"Maximum 5 trades reached. Trade less. Wait more. The market will be here tomorrow.\""
            isNonTradingDay ->
                "\"TWO NON-TRADING DAYS PER WEEK — NON-NEGOTIABLE.\n\nHonor scheduled neural recovery to prevent burnout and session addiction.\""
            isNoTrade ->
                "\"The no-trade decision is a valid decision.\n\nCapital preserved in choppy conditions is edge retained.\""
            else ->
                "Session limits have been activated."
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(if (isProfitTarget || isNoTrade) SoftMintTint else SoftRoseTint)
                .padding(14.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onProceedToReview,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isProfitTarget || isNoTrade) MintEmerald else CoralRose
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("proceed_review_button")
        ) {
            Text(
                text = "POST-SESSION REVIEW",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onResetSession,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("RESET SESSION", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
