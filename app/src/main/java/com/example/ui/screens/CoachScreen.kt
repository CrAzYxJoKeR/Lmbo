package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.coach.PsychologyTopics
import com.example.data.model.CoachTopic
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoachScreen(
    viewModel: GatekeeperViewModel,
    modifier: Modifier = Modifier
) {
    val diagnosticReport by viewModel.diagnosticReport.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedPartFilter by remember { mutableIntStateOf(0) }
    var activeModalTopic by remember { mutableStateOf<CoachTopic?>(null) }
    var activeModalSecondaryTopic by remember { mutableStateOf<CoachTopic?>(null) }

    val allTopics = PsychologyTopics.allTopics

    val filteredTopics = allTopics.filter { topic ->
        val matchesPart = selectedPartFilter == 0 || topic.part == selectedPartFilter
        val matchesQuery = searchQuery.isBlank() ||
                topic.title.contains(searchQuery, ignoreCase = true) ||
                topic.symptoms.contains(searchQuery, ignoreCase = true) ||
                topic.cognitiveError.contains(searchQuery, ignoreCase = true) ||
                topic.enforceableRule.contains(searchQuery, ignoreCase = true)
        matchesPart && matchesQuery
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
        // Emergency Panic Intervention Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = CoralRose)
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, RoseBorder, RoundedCornerShape(24.dp))
                    .padding(18.dp)
                    .testTag("emergency_panic_card")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(PutGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Emergency Tilt Circuit-Breaker",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepRose,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap your current feeling for instant neurological reset:",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                ModernPanicButton(
                    text = "• \"I want to win back my last loss immediately\"",
                    bgTint = SoftRoseTint,
                    borderColor = RoseBorder,
                    textColor = DeepRose
                ) {
                    activeModalTopic = PsychologyTopics.getTopicById(1)
                    activeModalSecondaryTopic = PsychologyTopics.getTopicById(22)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ModernPanicButton(
                    text = "• \"Price already moved / I missed a winning setup!\"",
                    bgTint = SoftAmberTint,
                    borderColor = AmberBorder,
                    textColor = DeepAmber
                ) {
                    activeModalTopic = PsychologyTopics.getTopicById(3)
                    activeModalSecondaryTopic = PsychologyTopics.getTopicById(28)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ModernPanicButton(
                    text = "• \"5 red candles in a row, next MUST be green!\"",
                    bgTint = SoftAmberTint,
                    borderColor = AmberBorder,
                    textColor = DeepAmber
                ) {
                    activeModalTopic = PsychologyTopics.getTopicById(10)
                    activeModalSecondaryTopic = PsychologyTopics.getTopicById(8)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ModernPanicButton(
                    text = "• \"Nothing is happening for 20 minutes, I'm bored\"",
                    bgTint = SoftIndigoTint,
                    borderColor = IndigoBorder,
                    textColor = ElectricIndigo
                ) {
                    activeModalTopic = PsychologyTopics.getTopicById(13)
                    activeModalSecondaryTopic = PsychologyTopics.getTopicById(30)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ModernPanicButton(
                    text = "• \"Telegram group just posted a 95% payout OTC signal!\"",
                    bgTint = SoftAmberTint,
                    borderColor = AmberBorder,
                    textColor = DeepAmber
                ) {
                    activeModalTopic = PsychologyTopics.getTopicById(23)
                    activeModalSecondaryTopic = PsychologyTopics.getTopicById(29)
                }
            }
        }

        // Gemini AI Psychological Coach Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = ElectricIndigo)
                    .clip(RoundedCornerShape(24.dp))
                    .background(surfaceColor)
                    .border(1.dp, IndigoBorder, RoundedCornerShape(24.dp))
                    .padding(18.dp)
                    .testTag("ai_diagnostic_card")
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
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Gemini AI Psychological Coach",
                            style = MaterialTheme.typography.titleMedium,
                            color = ElectricIndigo,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Scans trade journals, emotional tags, and variance",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { viewModel.runDiagnostic() },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("run_ai_diagnostic_button")
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = TextWhite)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RUN PSYCHOLOGICAL DIAGNOSTIC", color = TextWhite, fontWeight = FontWeight.Bold)
                }

                if (diagnosticReport != null) {
                    val report = diagnosticReport!!
                    Spacer(modifier = Modifier.height(14.dp))

                    val (riskBg, riskBorder, riskText) = when (report.riskLevel) {
                        "CRITICAL" -> Triple(SoftRoseTint, RoseBorder, DeepRose)
                        "MODERATE" -> Triple(SoftAmberTint, AmberBorder, DeepAmber)
                        else -> Triple(SoftMintTint, MintBorder, DeepEmerald)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(riskBg)
                            .border(1.dp, riskBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DIAGNOSTIC REPORT",
                                style = MaterialTheme.typography.labelSmall,
                                color = riskText,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "RISK: ${report.riskLevel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = riskText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = report.summary,
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = report.behavioralObservation,
                            style = MaterialTheme.typography.bodySmall,
                            color = textMuted
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Prescription: ${report.prescription}",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepEmerald,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { activeModalTopic = report.primaryThreat },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("STUDY TOPIC ${report.primaryThreat.numberStr}: ${report.primaryThreat.title}", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 30-Topic Reference Section
        item {
            Column {
                Text(
                    text = "30-Topic Masterclass Library",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Complete psychology handbook for binary options & forex",
                    style = MaterialTheme.typography.bodySmall,
                    color = textMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search topics, cognitive traps, rules...", color = textMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textMuted) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricIndigo,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedPartFilter == 0,
                        onClick = { selectedPartFilter = 0 },
                        label = { Text("All (30)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftIndigoTint,
                            selectedLabelColor = ElectricIndigo,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = textMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedPartFilter == 1,
                        onClick = { selectedPartFilter = 1 },
                        label = { Text("Part 1: Emotional (10)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftRoseTint,
                            selectedLabelColor = DeepRose,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = textMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedPartFilter == 2,
                        onClick = { selectedPartFilter = 2 },
                        label = { Text("Part 2: Control (10)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftAmberTint,
                            selectedLabelColor = DeepAmber,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = textMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = selectedPartFilter == 3,
                        onClick = { selectedPartFilter = 3 },
                        label = { Text("Part 3: Formats (10)", style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SoftMintTint,
                            selectedLabelColor = DeepEmerald,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = textMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Topics Accordion Cards
        items(filteredTopics, key = { it.id }) { topic ->
            ModernTopicCard(topic = topic)
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    if (activeModalTopic != null) {
        ModernTopicModalDialog(
            topic = activeModalTopic!!,
            secondaryTopic = activeModalSecondaryTopic,
            onDismiss = {
                activeModalTopic = null
                activeModalSecondaryTopic = null
            }
        )
    }
}

@Composable
private fun ModernPanicButton(
    text: String,
    bgTint: Color,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgTint)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ModernTopicCard(topic: CoachTopic) {
    var expanded by remember { mutableStateOf(false) }

    val (partColor, partBg) = when (topic.part) {
        1 -> Pair(DeepRose, SoftRoseTint)
        2 -> Pair(DeepAmber, SoftAmberTint)
        else -> Pair(DeepEmerald, SoftMintTint)
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
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(partBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = topic.numberStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = partColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = topic.partName,
                        style = MaterialTheme.typography.labelSmall,
                        color = partColor
                    )
                }
            }

            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(text = "Trigger Symptoms:", style = MaterialTheme.typography.labelSmall, color = TextSlate400, fontWeight = FontWeight.Bold)
                Text(text = topic.symptoms, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Cognitive Trap:", style = MaterialTheme.typography.labelSmall, color = DeepAmber, fontWeight = FontWeight.Bold)
                Text(text = topic.cognitiveError, style = MaterialTheme.typography.bodySmall, color = textColor)

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Enforceable Rule:", style = MaterialTheme.typography.labelSmall, color = DeepRose, fontWeight = FontWeight.Bold)
                Text(text = topic.enforceableRule, style = MaterialTheme.typography.bodySmall, color = DeepRose, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Mental Reframing:", style = MaterialTheme.typography.labelSmall, color = DeepEmerald, fontWeight = FontWeight.Bold)
                Text(text = "\"${topic.mentalReframing}\"", style = MaterialTheme.typography.bodySmall, color = DeepEmerald, fontWeight = FontWeight.Medium)

                if (topic.loopBreakerAction != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SoftIndigoTint)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "ACTION: ${topic.loopBreakerAction}",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricIndigo,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernTopicModalDialog(
    topic: CoachTopic,
    secondaryTopic: CoachTopic?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Topic ${topic.numberStr}: ${topic.title}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = topic.partName,
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricIndigo
                )
            }
        },
        text = {
            Column {
                Text(text = "Symptoms:", style = MaterialTheme.typography.labelSmall, color = TextSlate400)
                Text(text = topic.symptoms, style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Cognitive Error:", style = MaterialTheme.typography.labelSmall, color = DeepAmber)
                Text(text = topic.cognitiveError, style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Enforceable Rule:", style = MaterialTheme.typography.labelSmall, color = DeepRose)
                Text(text = topic.enforceableRule, style = MaterialTheme.typography.bodySmall, color = DeepRose, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Mental Reframing:", style = MaterialTheme.typography.labelSmall, color = DeepEmerald)
                Text(text = "\"${topic.mentalReframing}\"", style = MaterialTheme.typography.bodySmall, color = DeepEmerald, fontWeight = FontWeight.Medium)

                if (secondaryTopic != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Related Counter-Topic (${secondaryTopic.title}):",
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepAmber
                    )
                    Text(text = "\"${secondaryTopic.mentalReframing}\"", style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("COMMIT TO RULE", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp)
    )
}
