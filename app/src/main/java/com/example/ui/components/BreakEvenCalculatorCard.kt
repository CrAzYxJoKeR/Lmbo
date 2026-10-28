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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.CoralRose
import com.example.ui.theme.DeepAmber
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftAmberTint
import com.example.ui.theme.SoftIndigoTint
import com.example.ui.theme.SoftRoseTint
import com.example.ui.theme.TextSlate600
import com.example.ui.theme.TextSlate900
import com.example.ui.theme.WarmAmber
import java.util.Locale

@Composable
fun BreakEvenCalculatorCard(
    payoutPct: Double,
    modifier: Modifier = Modifier
) {
    val breakEvenWinRate = if (payoutPct > 0) (100.0 / (100.0 + payoutPct)) * 100.0 else 100.0
    val isHighPayoutTrap = payoutPct >= 92.0
    val isPoorPayout = payoutPct < 75.0

    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SoftIndigoTint)
            .border(1.dp, IndigoBorder, RoundedCornerShape(18.dp))
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
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Break-Even Win Rate",
                        style = MaterialTheme.typography.titleSmall,
                        color = ElectricIndigo,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Formula: 100 / (100 + ${payoutPct.toInt()}%)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSlate600
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, IndigoBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f%% MIN", breakEvenWinRate),
                    style = MaterialTheme.typography.labelLarge,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Mini progress bar comparing break-even to 100%
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
        ) {
            val progressFraction = (breakEvenWinRate / 100.0).toFloat().coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressFraction)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BrandGradient)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "50% Coin Toss",
                style = MaterialTheme.typography.labelSmall,
                color = TextSlate600
            )
            Text(
                text = String.format(Locale.US, "Must win %.2f%% to retain capital", breakEvenWinRate),
                style = MaterialTheme.typography.labelSmall,
                color = ElectricIndigo,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (isHighPayoutTrap) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftAmberTint)
                    .border(1.dp, AmberBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = DeepAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Topic 23 (Payout Chasing Alert): 92%+ payouts frequently lure traders into high-spread or OTC algorithm wicks. Do not trade erratic candles for payout vanity.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepAmber,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (isPoorPayout) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftRoseTint)
                    .border(1.dp, RoseBorder, RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = CoralRose,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Low Payout Warning: Below 75% requires over 57.1% win rate just to break even. The mathematical friction is excessively steep.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CoralRose,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
