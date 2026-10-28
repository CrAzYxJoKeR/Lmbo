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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepRose
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.IndigoBorder
import com.example.ui.theme.MintBorder
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.PutGradient
import com.example.ui.theme.RoseBorder
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

@Composable
fun StakeLadderDialog(
    onDismiss: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PutGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Stake Escalation Danger",
                        style = MaterialTheme.typography.titleLarge,
                        color = onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "The Psychological Drift Ladder",
                        style = MaterialTheme.typography.labelSmall,
                        color = onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column {
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
                            style = MaterialTheme.typography.labelLarge,
                            color = DeepRose,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Why stake field locks mid-session:",
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // The 5 Steps
                ModernLadderStep(
                    amount = "$5",
                    state = "Calm",
                    desc = "Strict 2% plan, emotional baseline intact.",
                    bgTint = SoftMintTint,
                    borderColor = MintBorder,
                    textColor = DeepEmerald
                )
                Spacer(modifier = Modifier.height(6.dp))
                ModernLadderStep(
                    amount = "$10",
                    state = "Confident",
                    desc = "First win streak; subconscious urge to accelerate.",
                    bgTint = SoftMintTint.copy(alpha = 0.6f),
                    borderColor = MintBorder,
                    textColor = MintEmerald
                )
                Spacer(modifier = Modifier.height(6.dp))
                ModernLadderStep(
                    amount = "$25",
                    state = "Greedy",
                    desc = "Trying to win back earlier draw or hit round target.",
                    bgTint = SoftAmberTint,
                    borderColor = AmberBorder,
                    textColor = DeepAmber
                )
                Spacer(modifier = Modifier.height(6.dp))
                ModernLadderStep(
                    amount = "$60",
                    state = "Reckless",
                    desc = "Prefrontal cortex hijacked; revenge double-down.",
                    bgTint = SoftRoseTint,
                    borderColor = RoseBorder,
                    textColor = CoralRose
                )
                Spacer(modifier = Modifier.height(6.dp))
                ModernLadderStep(
                    amount = "$150+",
                    state = "Account Wipeout",
                    desc = "Total capital destruction within 60 seconds.",
                    bgTint = SoftRoseTint,
                    borderColor = RoseBorder,
                    textColor = DeepRose
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "A fixed 2% stake guarantees surviving 50 consecutive losses before ruin. Martingale wipes out the entire balance on just 5 losses in a row.",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("UNDERSTOOD & COMMITTED", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = surfaceColor,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun ModernLadderStep(
    amount: String,
    state: String,
    desc: String,
    bgTint: Color,
    borderColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgTint)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp)
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
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = amount,
                        style = MaterialTheme.typography.labelLarge,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = state,
                        style = MaterialTheme.typography.titleSmall,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate600,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
