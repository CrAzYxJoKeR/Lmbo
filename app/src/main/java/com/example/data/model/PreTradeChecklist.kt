package com.example.data.model

data class PreTradeState(
    // Branch A: Setup-Quality Checklist (7 items)
    val a1Watchlist: Boolean = false,
    val a2HigherTimeframeTrend: Boolean = false,
    val a3EntryTriggerConfirmed: Boolean = false,
    val a4PayoutAndBreakEvenUnderstood: Boolean = false,
    val a5StakeMatchesFixed2Pct: Boolean = false,
    val a6ExpiryTimeframeDefined: Boolean = false,
    val a7NewsCalendarChecked: Boolean = false,

    // Branch B: Internal State Check (7 items)
    val b1WithinTwoLossLimit: Boolean = false,
    val b2EmotionalNeutrality10Min: Boolean = false,
    val b3PhysicalEnergyAndSleep: Boolean = false,
    val b4NoLossRecoveryIntent: Boolean = false,
    val b5NoExternalOrCountdownPressure: Boolean = false,
    val b6NoBlindSignalFollowing: Boolean = false,
    val b7CanExplainInOneSentence: Boolean = false,

    // Mandatory Text Inputs
    val oneSentenceExplanation: String = "",
    val invalidationArgument: String = "",

    // Asset & Parameters
    val asset: String = "EUR/USD",
    val marketType: String = "Standard", // "Standard", "OTC"
    val direction: String = "CALL", // "CALL" or "PUT"
    val payoutPct: Double = 85.0
) {
    val branchAPassed: Boolean
        get() = a1Watchlist && a2HigherTimeframeTrend && a3EntryTriggerConfirmed &&
                a4PayoutAndBreakEvenUnderstood && a5StakeMatchesFixed2Pct &&
                a6ExpiryTimeframeDefined && a7NewsCalendarChecked

    val branchBPassed: Boolean
        get() = b1WithinTwoLossLimit && b2EmotionalNeutrality10Min && b3PhysicalEnergyAndSleep &&
                b4NoLossRecoveryIntent && b5NoExternalOrCountdownPressure &&
                b6NoBlindSignalFollowing && b7CanExplainInOneSentence

    val textInputsValid: Boolean
        get() = oneSentenceExplanation.trim().length >= 8 && invalidationArgument.trim().length >= 8

    val allConditionsPass: Boolean
        get() = branchAPassed && branchBPassed && textInputsValid

    val breakEvenWinRate: Double
        get() = if (payoutPct > 0) (100.0 / (100.0 + payoutPct)) * 100.0 else 100.0

    val checkedCount: Int
        get() = listOf(
            a1Watchlist, a2HigherTimeframeTrend, a3EntryTriggerConfirmed,
            a4PayoutAndBreakEvenUnderstood, a5StakeMatchesFixed2Pct,
            a6ExpiryTimeframeDefined, a7NewsCalendarChecked,
            b1WithinTwoLossLimit, b2EmotionalNeutrality10Min, b3PhysicalEnergyAndSleep,
            b4NoLossRecoveryIntent, b5NoExternalOrCountdownPressure,
            b6NoBlindSignalFollowing, b7CanExplainInOneSentence
        ).count { it }
}
