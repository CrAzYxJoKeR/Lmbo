package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MissedTrade
import com.example.data.model.Trade
import com.example.ui.components.TargetVsActualProfitCard
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
import com.example.ui.viewmodel.GatekeeperViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JournalScreen(
    viewModel: GatekeeperViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val trades by viewModel.allTrades.collectAsState()
    val last50Trades by viewModel.last50Trades.collectAsState()
    val missedTrades by viewModel.allMissedTrades.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()

    var filterOutcome by remember { mutableStateOf("ALL") }
    var showMissedTradeDialog by remember { mutableStateOf(false) }
    var selectedTradeDetail by remember { mutableStateOf<Trade?>(null) }

    // Metrics Calculations
    val sampleTotal = last50Trades.size
    val validSampleTrades = last50Trades.filter { it.outcome != "NO_TRADE" }
    val winCount = validSampleTrades.count { it.outcome == "WIN" }
    val winRate = if (validSampleTrades.isNotEmpty()) (winCount.toDouble() / validSampleTrades.size) * 100.0 else 0.0

    val disciplinedTrades = last50Trades.count { it.disciplineRespected }
    val processScore = if (sampleTotal > 0) (disciplinedTrades.toDouble() / sampleTotal) * 100.0 else 100.0

    val totalNetProfit = trades.sumOf { it.profitOrLoss }

    val filteredTrades = trades.filter {
        when (filterOutcome) {
            "WIN" -> it.outcome == "WIN"
            "LOSS" -> it.outcome == "CLEAN_LOSS" || it.outcome == "NEAR_MISS_LOSS"
            "NEAR_MISS" -> it.outcome == "NEAR_MISS_LOSS"
            "NO_TRADE" -> it.outcome == "NO_TRADE"
            else -> true
        }
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMuted = MaterialTheme.colorScheme.onSurfaceVariant

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Modern Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "THE REVOLUT / TRADINGVIEW LAW",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricIndigo,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "\"Judge the Decision — Not Only the Result.\"",
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "A losing trade executed with strict 2% risk on a confirmed A+ setup is a high-level success. An unverified impulse win is a severe failure.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textMuted
                    )
                }
            }
        }

        // Target vs Actual Profit Session Component
        item {
            TargetVsActualProfitCard(session = activeSession)
        }

        // Two Key Modern Score Cards (Process vs Win Rate)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Score 1: Process / Discipline Score
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = MintEmerald)
                        .clip(RoundedCornerShape(20.dp))
                        .background(surfaceColor)
                        .border(1.dp, MintBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                        .testTag("process_score_card")
                ) {
                    Text(
                        text = "PROCESS SCORE",
                        style = MaterialTheme.typography.labelSmall,
                        color = textMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = String.format(Locale.US, "%.1f%%", processScore),
                        style = MaterialTheme.typography.headlineLarge,
                        color = DeepEmerald,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftMintTint)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "JUDGE THIS",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Score 2: Market Win Rate
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = WarmAmber)
                        .clip(RoundedCornerShape(20.dp))
                        .background(surfaceColor)
                        .border(1.dp, AmberBorder, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                        .testTag("win_rate_card")
                ) {
                    Text(
                        text = "WIN RATE (LAST 50)",
                        style = MaterialTheme.typography.labelSmall,
                        color = textMuted,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = String.format(Locale.US, "%.1f%%", winRate),
                        style = MaterialTheme.typography.headlineLarge,
                        color = DeepAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftAmberTint)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "VARIANCE NOISE",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 50-Trade Sample Tracker Modern Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                    .padding(18.dp)
                    .testTag("50_trade_grid")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "50-Trade Sample Block",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Combats Recency Bias over 50 trials",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${last50Trades.size}/50 logged",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (last50Trades.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No trades logged yet. Start a session to populate sample.",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        last50Trades.reversed().forEach { trade ->
                            val (pillBg, pillBorder, pillText, textColorPill) = when (trade.outcome) {
                                "WIN" -> Tuple4(SoftMintTint, MintBorder, "W", DeepEmerald)
                                "CLEAN_LOSS" -> Tuple4(SoftRoseTint, RoseBorder, "L", DeepRose)
                                "NEAR_MISS_LOSS" -> Tuple4(SoftAmberTint, AmberBorder, "NM", DeepAmber)
                                "NO_TRADE" -> Tuple4(SoftIndigoTint, IndigoBorder, "NT", ElectricIndigo)
                                else -> Tuple4(MaterialTheme.colorScheme.surfaceVariant, borderColor, "?", textMuted)
                            }

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(pillBg)
                                    .border(1.dp, pillBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedTradeDetail = trade },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pillText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = textColorPill,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Action Buttons: Log Missed Trade, Export CSV & JSON
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showMissedTradeDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1.3f).testTag("log_missed_trade_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Missed Trade", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val csv = viewModel.exportJournalCsv()
                        copyToClipboard(context, "LMBO_Journal.csv", csv)
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CSV", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        val json = viewModel.exportJournalJson()
                        copyToClipboard(context, "LMBO_Journal.json", json)
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("JSON", fontSize = 11.sp)
                }
            }
        }

        // Trade Log Header & Modern Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trade History (${filteredTrades.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format(Locale.US, "NET: %s$%.2f", if (totalNetProfit >= 0) "+" else "", totalNetProfit),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (totalNetProfit >= 0) DeepEmerald else DeepRose,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("ALL", "WIN", "LOSS", "NEAR_MISS", "NO_TRADE").forEach { f ->
                    val isSelected = filterOutcome == f
                    FilterChip(
                        selected = isSelected,
                        onClick = { filterOutcome = f },
                        label = { Text(f, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftIndigoTint,
                            selectedLabelColor = ElectricIndigo,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = textMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        if (filteredTrades.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No records match filter criteria.", color = textMuted)
                }
            }
        } else {
            items(filteredTrades, key = { it.id }) { trade ->
                SwipeableTradeItem(
                    trade = trade,
                    onDelete = { id -> viewModel.deleteTrade(id) }
                )
            }
        }

        // Missed Trades Mini-Journal
        if (missedTrades.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Missed Trades Mini-Journal (${missedTrades.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }

            items(missedTrades, key = { it.id }) { missed ->
                ModernMissedTradeCard(missed = missed)
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    if (showMissedTradeDialog) {
        ModernMissedTradeDialog(
            onDismiss = { showMissedTradeDialog = false },
            onSubmit = { asset, dir, reason, reflection ->
                viewModel.logMissedTrade(asset, dir, reason, reflection)
                showMissedTradeDialog = false
            }
        )
    }

    if (selectedTradeDetail != null) {
        ModernTradeDetailDialog(
            trade = selectedTradeDetail!!,
            onDismiss = { selectedTradeDetail = null }
        )
    }
}

@Composable
private fun ModernTradeItemCard(trade: Trade) {
    var expanded by remember { mutableStateOf(false) }
    val sdf = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.US) }

    val (outcomeColor, outcomeBg, outcomeText) = when (trade.outcome) {
        "WIN" -> Triple(DeepEmerald, SoftMintTint, "WIN")
        "CLEAN_LOSS" -> Triple(DeepRose, SoftRoseTint, "LOSS")
        "NEAR_MISS_LOSS" -> Triple(DeepAmber, SoftAmberTint, "NEAR-MISS")
        "NO_TRADE" -> Triple(ElectricIndigo, SoftIndigoTint, "NO-TRADE")
        else -> Triple(TextSlate600, MaterialTheme.colorScheme.surfaceVariant, trade.outcome)
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0F0F172A))
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { expanded = !expanded }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(outcomeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = outcomeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = outcomeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${trade.asset} • ${trade.direction}",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = String.format(Locale.US, "%s$%.2f", if (trade.profitOrLoss >= 0) "+" else "", trade.profitOrLoss),
                style = MaterialTheme.typography.titleMedium,
                color = outcomeColor,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${sdf.format(Date(trade.timestamp))} • ${trade.marketType} • ${trade.payoutPct.toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = trade.emotionalStateTag,
                style = MaterialTheme.typography.labelSmall,
                color = ElectricIndigo,
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "SETUP EXPLANATION:",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricIndigo,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = trade.oneSentenceExplanation.ifBlank { "None recorded." },
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "DEVIL'S ADVOCATE (INVALIDATION):",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = trade.invalidationArgument.ifBlank { "None recorded." },
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor
                        )

                        if (trade.lessonLearned.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "LESSON LEARNED:",
                                style = MaterialTheme.typography.labelSmall,
                                color = DeepEmerald,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = trade.lessonLearned,
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableTradeItem(
    trade: Trade,
    onDelete: (Long) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                onDelete(trade.id)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> DeepEmerald
                SwipeToDismissBoxValue.EndToStart -> DeepRose
                SwipeToDismissBoxValue.Settled -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    else -> Alignment.CenterEnd
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                    Text(
                        text = "Delete Trade",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        ModernTradeItemCard(trade = trade)
    }
}

@Composable
private fun ModernMissedTradeCard(missed: MissedTrade) {
    val sdf = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.US) }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), spotColor = Color(0x0F0F172A))
            .clip(RoundedCornerShape(18.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${missed.asset} ${missed.direction}",
                style = MaterialTheme.typography.titleSmall,
                color = DeepAmber,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sdf.format(Date(missed.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Reason: ${missed.reasonMissed}",
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "\"${missed.reflection}\"",
            style = MaterialTheme.typography.bodySmall,
            color = DeepEmerald,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ModernMissedTradeDialog(
    onDismiss: () -> Unit,
    onSubmit: (asset: String, dir: String, reason: String, reflection: String) -> Unit
) {
    var asset by remember { mutableStateOf("EUR/USD") }
    var direction by remember { mutableStateOf("CALL") }
    var reason by remember { mutableStateOf("") }
    var reflection by remember { mutableStateOf("A missed trade is not a loss. Chasing it usually is. Journal it and wait for a fresh setup.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Missed Setup",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Acknowledge and detach. Document why it passed so you don't chase.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = asset, onValueChange = { asset = it }, label = { Text("Asset") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Reason Missed") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = reflection, onValueChange = { reflection = it }, label = { Text("Mental Reframing") }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { if (reason.isNotBlank()) onSubmit(asset, direction, reason, reflection) },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("LOG & DETACH", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) { Text("CANCEL") }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun ModernTradeDetailDialog(trade: Trade, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${trade.asset} (${trade.direction})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(text = "Outcome: ${trade.outcome} • Stake: $${trade.stakeAmount}", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Explanation:", style = MaterialTheme.typography.labelSmall, color = ElectricIndigo)
                Text(text = trade.oneSentenceExplanation, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Devil's Advocate:", style = MaterialTheme.typography.labelSmall, color = DeepAmber)
                Text(text = trade.invalidationArgument, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Lesson:", style = MaterialTheme.typography.labelSmall, color = DeepEmerald)
                Text(text = trade.lessonLearned.ifBlank { "None recorded" }, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) { Text("CLOSE") }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp)
    )
}

private fun copyToClipboard(context: Context, label: String, content: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, content)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
}

private data class Tuple4<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
