package com.example.data.coach

import com.example.data.model.CoachTopic
import com.example.data.model.Trade

data class DiagnosticReport(
    val summary: String,
    val primaryThreat: CoachTopic,
    val secondaryThreats: List<CoachTopic>,
    val riskLevel: String, // "LOW", "MODERATE", "CRITICAL"
    val behavioralObservation: String,
    val prescription: String
)

object CoachDiagnosticEngine {

    fun analyzeTrades(trades: List<Trade>): DiagnosticReport {
        if (trades.isEmpty()) {
            val topic30 = PsychologyTopics.getTopicById(30)!!
            return DiagnosticReport(
                summary = "Clean Slate: No trades recorded yet.",
                primaryThreat = topic30,
                secondaryThreats = listOf(PsychologyTopics.getTopicById(14)!!, PsychologyTopics.getTopicById(24)!!),
                riskLevel = "LOW",
                behavioralObservation = "You are currently flat with no active market exposure. The primary challenge right now is patience — waiting for a confirmed A+ setup without boredom clicking.",
                prescription = "Wait for your pre-approved watchlist setups. Remember: 'The no-trade decision is a valid decision'."
            )
        }

        val recent = trades.take(10)
        val nearMissCount = recent.count { it.outcome == "NEAR_MISS_LOSS" }
        val losses = recent.count { it.outcome == "CLEAN_LOSS" || it.outcome == "NEAR_MISS_LOSS" }
        val wins = recent.count { it.outcome == "WIN" }
        val fomoOrUrgent = recent.count { it.emotionalStateTag.equals("FOMO", ignoreCase = true) || it.emotionalStateTag.equals("Urgent", ignoreCase = true) }
        val otcCount = recent.count { it.marketType.equals("OTC", ignoreCase = true) }
        val highPayoutCount = recent.count { it.payoutPct >= 92.0 }

        // Determine primary and secondary threat
        val primaryTopic: CoachTopic
        val secondary: MutableList<CoachTopic> = mutableListOf()
        val riskLevel: String
        val observation: StringBuilder = StringBuilder()
        val prescription: StringBuilder = StringBuilder()

        when {
            losses >= 2 && nearMissCount >= 1 -> {
                primaryTopic = PsychologyTopics.getTopicById(21)!! // Near-Miss Frustration
                secondary.add(PsychologyTopics.getTopicById(1)!!) // Revenge Trading
                secondary.add(PsychologyTopics.getTopicById(22)!!) // Expiry Revenge
                riskLevel = "CRITICAL"
                observation.append("You experienced near-miss expiry losses. Brain chemistry treats 'almost winning' as an injustice, triggering aggressive adrenaline surges.")
                prescription.append("Step away from the screen. Right for 59 seconds is not right. The session is closed or requires strict cooldown. Do not click.")
            }
            losses >= 2 -> {
                primaryTopic = PsychologyTopics.getTopicById(1)!! // Revenge Trading
                secondary.add(PsychologyTopics.getTopicById(19)!!) // Recovery Obsession
                secondary.add(PsychologyTopics.getTopicById(7)!!) // Loss Aversion
                riskLevel = "CRITICAL"
                observation.append("Session hit the 2-loss threshold. There is an immediate subconscious urge to recover lost capital before ending the day.")
                prescription.append("Close the laptop. The market does not owe you. Red days are normal inventory costs in trading. Protect your bankroll.")
            }
            wins >= 3 -> {
                primaryTopic = PsychologyTopics.getTopicById(26)!! // Win-Streak Euphoria
                secondary.add(PsychologyTopics.getTopicById(6)!!) // Overconfidence
                secondary.add(PsychologyTopics.getTopicById(11)!!) // Hot-Hand Fallacy
                riskLevel = "MODERATE"
                observation.append("Win streak of 3+ detected. Dopamine is elevated, making risk feel lower than it actually is.")
                prescription.append("Observe the 5-minute pause. A win streak is a statistical run, not an indication to raise stake or loosen checklist criteria.")
            }
            fomoOrUrgent >= 1 -> {
                primaryTopic = PsychologyTopics.getTopicById(3)!! // FOMO
                secondary.add(PsychologyTopics.getTopicById(12)!!) // Chasing Missed Trades
                secondary.add(PsychologyTopics.getTopicById(24)!!) // Fast-Click Syndrome
                riskLevel = "MODERATE"
                observation.append("You flagged emotional tags of FOMO or Urgency. This indicates you felt hurried by platform candles or price movement.")
                prescription.append("Enforce the 3-second hold countdown. If price moves before you verify your checklist, let it go. Missing a move costs $0.")
            }
            highPayoutCount >= 2 || otcCount >= 3 -> {
                primaryTopic = PsychologyTopics.getTopicById(23)!! // Payout Chasing
                secondary.add(PsychologyTopics.getTopicById(29)!!) // Signal Herding
                secondary.add(PsychologyTopics.getTopicById(13)!!) // Boredom Trading
                riskLevel = "MODERATE"
                observation.append("Multiple OTC or ultra-high payout (>92%) entries logged. High broker payouts frequently mask manipulated spreads and erratic wicks.")
                prescription.append("Check the Break-Even calculator. Stick to major pairs with steady volume. Do not trade OTC wicks for payout vanity.")
            }
            else -> {
                primaryTopic = PsychologyTopics.getTopicById(8)!! // Recency Bias
                secondary.add(PsychologyTopics.getTopicById(5)!!) // Fear of Loss
                secondary.add(PsychologyTopics.getTopicById(16)!!) // Stake Escalation
                riskLevel = "LOW"
                observation.append("Your recent entries show disciplined execution. Keep monitoring your 50-trade sample block rather than judging trade-by-trade.")
                prescription.append("Maintain strictly 2% fixed stake per trade. Judge the decision process, not only the financial result.")
            }
        }

        return DiagnosticReport(
            summary = "Psychological Diagnosis: Watch for ${primaryTopic.title} (Topic ${primaryTopic.numberStr})",
            primaryThreat = primaryTopic,
            secondaryThreats = secondary,
            riskLevel = riskLevel,
            behavioralObservation = observation.toString(),
            prescription = prescription.toString()
        )
    }
}
