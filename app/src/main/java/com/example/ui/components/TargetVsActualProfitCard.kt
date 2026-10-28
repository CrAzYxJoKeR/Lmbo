package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.TradingSession
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.CoralRose
import com.example.ui.theme.DeepAmber
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepRose
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.SoftAmberTint
import com.example.ui.theme.SoftIndigoTint
import com.example.ui.theme.SoftMintTint
import com.example.ui.theme.SoftRoseTint
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarmAmber
import java.util.Locale

@Composable
fun TargetVsActualProfitCard(
    session: TradingSession?,
    modifier: Modifier = Modifier
) {
    val target = session?.profitTarget ?: 60.0
    val actual = session?.currentProfit ?: 0.0

    // Deviation percentage calculation relative to target
    val deviationPct = if (target > 0) ((actual - target) / target) * 100.0 else 0.0
    val achievementPct = if (target > 0) (actual / target) * 100.0 else 0.0

    val isAhead = actual >= target
    val cardBg = if (isAhead) SoftMintTint else if (actual >= 0) SoftIndigoTint else SoftRoseTint
    val borderColor = if (isAhead) MintBorder else if (actual >= 0) IndigoBorder else RoseBorder
    val accentColor = if (isAhead) DeepEmerald else if (actual >= 0) ElectricIndigo else DeepRose

    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val textMuted = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
            .clip(RoundedCornerShape(24.dp))
            .background(surfaceColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(18.dp)
            .testTag("target_vs_actual_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Target vs Actual Profit",
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Session Performance Tracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = textMuted
                    )
                }
            }

            // Deviation Badge Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%s%.1f%%", if (deviationPct >= 0) "+" else "", deviationPct),
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Side-by-side Target vs Actual Values
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Target Profit Card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    text = "TARGET PROFIT",
                    style = MaterialTheme.typography.labelSmall,
                    color = textMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = String.format(Locale.US, "$$%.2f", target),
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Actual Profit Card
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "ACTUAL PROFIT",
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = String.format(Locale.US, "%s$%.2f", if (actual >= 0) "$" else "-$", kotlin.math.abs(actual)),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress bar towards target
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Target Achievement",
                    style = MaterialTheme.typography.labelSmall,
                    color = textMuted
                )
                Text(
                    text = String.format(Locale.US, "%.1f%% Completed", achievementPct.coerceIn(0.0, 200.0)),
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val progressFraction = (achievementPct / 100.0).toFloat().coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isAhead) MintEmerald else ElectricIndigo)
                )
            }
        }
    }
}
