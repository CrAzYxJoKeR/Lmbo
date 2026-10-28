package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.StakeLadderDialog
import com.example.ui.components.TopEnforcementBar
import com.example.ui.screens.CoachScreen
import com.example.ui.screens.GatekeeperScreen
import com.example.ui.screens.JournalScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SoftIndigoTint
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate600
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.GatekeeperViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainAppScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleDarkTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@Composable
fun MainAppScreen(
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    viewModel: GatekeeperViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showStakeLadderDialog by remember { mutableStateOf(false) }

    val activeSession by viewModel.activeSession.collectAsState()
    val cooldownSeconds by viewModel.cooldownSecondsRemaining.collectAsState()

    // Handle system back navigation
    BackHandler(enabled = selectedTab != 0) {
        selectedTab = 0
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopEnforcementBar(
                session = activeSession,
                cooldownSecondsRemaining = cooldownSeconds,
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = onToggleDarkTheme,
                onSkipSessionClick = { viewModel.logNoTradeDecision() },
                onShowStakeLadder = { showStakeLadderDialog = true }
            )
        },
        bottomBar = {
            // Modern Floating Glassmorphism Pill Dock
            FloatingGlassBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> GatekeeperScreen(
                    viewModel = viewModel,
                    onNavigateToReview = { selectedTab = 3 },
                    onShowStakeLadder = { showStakeLadderDialog = true }
                )
                1 -> JournalScreen(
                    viewModel = viewModel
                )
                2 -> CoachScreen(
                    viewModel = viewModel
                )
                3 -> ReviewScreen(
                    viewModel = viewModel,
                    onSessionReset = { selectedTab = 0 }
                )
            }
        }
    }

    if (showStakeLadderDialog) {
        StakeLadderDialog(
            onDismiss = { showStakeLadderDialog = false }
        )
    }
}

@Composable
private fun FloatingGlassBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val navSurface = if (isDarkTheme) Color(0xFF131B2E).copy(alpha = 0.95f) else Color.White.copy(alpha = 0.96f)
    val navBorder = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(32.dp),
                spotColor = Color(0x1F0F172A)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(navSurface)
            .border(1.dp, navBorder, RoundedCornerShape(32.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .testTag("main_bottom_nav")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingNavItem(
                icon = Icons.Default.Shield,
                label = "Gatekeeper",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                testTag = "nav_gatekeeper"
            )

            FloatingNavItem(
                icon = Icons.Default.BarChart,
                label = "Journal",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                testTag = "nav_journal"
            )

            FloatingNavItem(
                icon = Icons.Default.Psychology,
                label = "Coach & Tilt",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                testTag = "nav_coach"
            )

            FloatingNavItem(
                icon = Icons.Default.RateReview,
                label = "Review",
                isSelected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                testTag = "nav_review"
            )
        }
    }
}

@Composable
private fun FloatingNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val animBg by animateColorAsState(
        targetValue = if (isSelected) SoftIndigoTint else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "navAnimBg"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(animBg)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) ElectricIndigo else TextSlate400,
                modifier = Modifier.size(20.dp)
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricIndigo,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
