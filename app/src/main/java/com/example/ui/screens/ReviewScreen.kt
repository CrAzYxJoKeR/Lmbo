package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.SessionReview
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
import com.example.ui.theme.RoseBorder
import com.example.ui.theme.SoftAmberTint
import com.example.ui.theme.SoftIndigoTint
import com.example.ui.theme.SoftMintTint
import com.example.ui.theme.SoftRoseTint
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate600
import com.example.ui.theme.TextSlate900
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.GatekeeperViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ReviewScreen(
    viewModel: GatekeeperViewModel,
    onSessionReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeSession by viewModel.activeSession.collectAsState()
    val allReviews by viewModel.allReviews.collectAsState()

    var followedChecklist by remember { mutableStateOf(true) }
    var respectedLimits by remember { mutableStateOf(true) }
    var emotionDroveDecision by remember { mutableStateOf(false) }
    var emotionNotes by remember { mutableStateOf("") }
    var setupQualityRating by remember { mutableIntStateOf(8) }
    var keyLesson by remember { mutableStateOf("") }

    var nonTradingDays by remember { mutableStateOf(viewModel.repository.getNonTradingDays()) }

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
        // Summary Header Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                    .padding(18.dp)
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
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "End-of-Session Review",
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Answer the 5 mandatory integrity questions before closing",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }
                }

                if (activeSession != null) {
                    val s = activeSession!!
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ModernStatPill("Trades", "${s.totalTrades}/${s.maxTrades}")
                        ModernStatPill("Losses", "${s.losses}/${s.maxLosses}")
                        ModernStatPill("Streak", "${s.winStreak}W")
                        ModernStatPill(
                            "Net P/L",
                            String.format(Locale.US, "%s$%.2f", if (s.currentProfit >= 0) "+" else "", s.currentProfit),
                            if (s.currentProfit >= 0) DeepEmerald else DeepRose
                        )
                    }
                }
            }
        }

        // The 5 Mandatory Questions Form
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                    .padding(20.dp)
                    .testTag("mandatory_review_form")
            ) {
                Text(
                    text = "5 MANDATORY INTEGRITY QUESTIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Question 1: Checklist
                ModernQuestionToggle(
                    number = "1",
                    question = "Did I follow my pre-trade checklist on every trade?",
                    state = followedChecklist,
                    onStateChange = { followedChecklist = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Question 2: Session & loss limits
                ModernQuestionToggle(
                    number = "2",
                    question = "Did I respect my session and loss limits without negotiation?",
                    state = respectedLimits,
                    onStateChange = { respectedLimits = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Question 3: Emotion driving decision
                Column {
                    ModernQuestionToggle(
                        number = "3",
                        question = "Did I let emotion drive any entry, size, or timing decision?",
                        state = emotionDroveDecision,
                        onStateChange = { emotionDroveDecision = it },
                        trueLabel = "YES (Emotion Present)",
                        falseLabel = "NO (Systematic)",
                        invertedColors = true
                    )
                    if (emotionDroveDecision) {
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = emotionNotes,
                            onValueChange = { emotionNotes = it },
                            placeholder = { Text("Describe the emotion: FOMO, revenge, impatience, hesitation...", color = textMuted) },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricIndigo,
                                unfocusedBorderColor = borderColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question 4: Setup Quality (1 to 10)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "4. Setup Execution Quality Rating:",
                            style = MaterialTheme.typography.titleSmall,
                            color = textColor,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (setupQualityRating >= 7) SoftMintTint else SoftAmberTint)
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$setupQualityRating / 10",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (setupQualityRating >= 7) DeepEmerald else DeepAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Slider(
                        value = setupQualityRating.toFloat(),
                        onValueChange = { setupQualityRating = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricIndigo,
                            activeTrackColor = ElectricIndigo,
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question 5: Key Lesson
                Text(
                    text = "5. What is the ONE lesson I take into tomorrow?",
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = keyLesson,
                    onValueChange = { keyLesson = it },
                    placeholder = { Text("e.g. Respect the 5-minute revenge cooldown without touching the mouse.", color = textMuted) },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("key_lesson_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.submitSessionReview(
                            followedChecklist = followedChecklist,
                            respectedLimits = respectedLimits,
                            emotionDroveDecision = emotionDroveDecision,
                            emotionNotes = emotionNotes,
                            setupQualityRating = setupQualityRating,
                            keyLesson = keyLesson
                        )
                        Toast.makeText(context, "Session review completed! Laptop closed.", Toast.LENGTH_SHORT).show()
                        onSessionReset()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_review_button")
                ) {
                    Text(
                        text = "SUBMIT REVIEW & CLOSE SESSION",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Non-Trading Days Configuration Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x140F172A))
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SoftAmberTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = DeepAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Mandatory Rest Days (2 Days / Week)",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Prevents Session Addiction (Topic 20)",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val daysMap = listOf(
                    Pair(Calendar.MONDAY, "Mon"),
                    Pair(Calendar.TUESDAY, "Tue"),
                    Pair(Calendar.WEDNESDAY, "Wed"),
                    Pair(Calendar.THURSDAY, "Thu"),
                    Pair(Calendar.FRIDAY, "Fri"),
                    Pair(Calendar.SATURDAY, "Sat"),
                    Pair(Calendar.SUNDAY, "Sun")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysMap.forEach { (dayInt, label) ->
                        val isSelected = nonTradingDays.contains(dayInt)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) SoftRoseTint else MaterialTheme.colorScheme.surfaceVariant)
                                .border(1.dp, if (isSelected) CoralRose else borderColor, RoundedCornerShape(12.dp))
                                .clickable {
                                    val newSet = nonTradingDays.toMutableSet()
                                    if (isSelected) newSet.remove(dayInt) else newSet.add(dayInt)
                                    nonTradingDays = newSet
                                    viewModel.repository.setNonTradingDays(newSet)
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) DeepRose else textMuted,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Past Reviews History List
        if (allReviews.isNotEmpty()) {
            item {
                Text(
                    text = "Historical Session Reviews (${allReviews.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
            }

            items(allReviews, key = { it.id }) { review ->
                ModernReviewHistoryCard(review = review)
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun ModernStatPill(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ModernQuestionToggle(
    number: String,
    question: String,
    state: Boolean,
    onStateChange: (Boolean) -> Unit,
    trueLabel: String = "YES",
    falseLabel: String = "NO",
    invertedColors: Boolean = false
) {
    val yesColor = if (invertedColors) DeepRose else DeepEmerald
    val yesBg = if (invertedColors) SoftRoseTint else SoftMintTint
    val noColor = if (invertedColors) DeepEmerald else DeepRose
    val noBg = if (invertedColors) SoftMintTint else SoftRoseTint

    Column {
        Text(
            text = "$number. $question",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (state) yesBg else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (state) yesColor else Color.Transparent, RoundedCornerShape(12.dp))
                    .clickable { onStateChange(true) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = trueLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (state) yesColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (!state) noBg else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (!state) noColor else Color.Transparent, RoundedCornerShape(12.dp))
                    .clickable { onStateChange(false) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = falseLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (!state) noColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ModernReviewHistoryCard(review: SessionReview) {
    val sdf = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US) }
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
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = sdf.format(Date(review.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftIndigoTint)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Quality: ${review.setupQualityRating}/10",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Checklist: ${if (review.followedChecklist) "PASS" else "FAIL"}",
                style = MaterialTheme.typography.labelSmall,
                color = if (review.followedChecklist) DeepEmerald else DeepRose,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• Limits: ${if (review.respectedLimits) "RESPECTED" else "VIOLATED"}",
                style = MaterialTheme.typography.labelSmall,
                color = if (review.respectedLimits) DeepEmerald else DeepRose,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "• Emotion: ${if (review.emotionDroveDecision) "DRIVEN" else "NEUTRAL"}",
                style = MaterialTheme.typography.labelSmall,
                color = if (review.emotionDroveDecision) DeepRose else DeepEmerald,
                fontWeight = FontWeight.Bold
            )
        }

        if (review.keyLesson.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Lesson: \"${review.keyLesson}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
