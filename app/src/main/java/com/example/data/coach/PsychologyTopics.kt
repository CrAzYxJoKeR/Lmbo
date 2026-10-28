package com.example.data.coach

import com.example.data.model.CoachTopic

object PsychologyTopics {
    val allTopics: List<CoachTopic> = listOf(
        // Part 1: The Emotional Enemies
        CoachTopic(
            id = 1,
            numberStr = "01",
            title = "Revenge Trading",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Urgent desire to re-enter immediately after a loss; increasing stake to win back lost capital; feeling wronged by the market.",
            cognitiveError = "Believing the market owes you your money back or that the previous loss was a personal slight rather than an edge probability outcome.",
            enforceableRule = "Mandatory 5-minute cooldown timer after ANY loss. Two losses end the session instantly. Zero re-entries on the same asset within 5 minutes.",
            mentalReframing = "The market has no memory of your loss. Taking an emotional trade is donating another stake to the broker.",
            loopBreakerAction = "STOP -> COOL DOWN (5:00) -> REVIEW CHECKLIST -> WAIT FOR FRESH SETUP"
        ),
        CoachTopic(
            id = 2,
            numberStr = "02",
            title = "Overtrading",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Clicking multiple setups in quick succession; trading low-conviction secondary setups; ignoring time between entries.",
            cognitiveError = "Equating activity with productivity. Believing more trades yield more profit rather than higher broker fee drag and edge decay.",
            enforceableRule = "Strict cap of 5 trades per session maximum. Trade less. Wait more. The market will be here tomorrow.",
            mentalReframing = "High-probability setups are rare anomalies. A sniper fires once or twice; a machine gunner wastes ammunition.",
            loopBreakerAction = "ENFORCE HARD 5-TRADE CAP -> CLOSE TERMINAL"
        ),
        CoachTopic(
            id = 3,
            numberStr = "03",
            title = "FOMO (Fear Of Missing Out)",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Entering late as price runs away; panic buying at candle extremes; feeling regret when observing a missed green candle.",
            cognitiveError = "Assuming that missing a winning setup means lost profit that was rightfully yours.",
            enforceableRule = "If price has already moved past the planned trigger level, the trade is dead. Never chase an expired entry.",
            mentalReframing = "There are thousands of setups every week. Missing a move costs $0.00. Entering late carries 100% downside risk.",
            loopBreakerAction = "LOG TO MISSED TRADE JOURNAL -> WAIT FOR FRESH RETRACEMENT"
        ),
        CoachTopic(
            id = 4,
            numberStr = "04",
            title = "Greed",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Pushing beyond the pre-set profit target; raising stake size during a winning run; dreaming of unrealistic daily multiples.",
            cognitiveError = "Extrapolating current winning conditions indefinitely and ignoring statistical variance.",
            enforceableRule = "Mandatory session stop immediately when the profit target is hit. Close the laptop. Secure the win.",
            mentalReframing = "Unrealized profits belong to the market until you close the session. Greed turns winning days into red days.",
            loopBreakerAction = "LOCK SESSION AT PROFIT TARGET -> WALK AWAY"
        ),
        CoachTopic(
            id = 5,
            numberStr = "05",
            title = "Fear of Loss",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Hesitating on valid A+ setups; freezing when the trigger occurs; closing positions prematurely out of dread.",
            cognitiveError = "Viewing every individual loss as failure rather than the cost of doing business in a probabilistic distribution.",
            enforceableRule = "Limit risk to exactly 2% of capital so any single loss is mathematically negligible to your survival.",
            mentalReframing = "Losses are routine inventory costs, like a grocer buying apples. If the setup was valid, the decision was correct regardless of outcome.",
            loopBreakerAction = "VERIFY FIXED 2% RISK -> TRUST THE SAMPLE SIZE"
        ),
        CoachTopic(
            id = 6,
            numberStr = "06",
            title = "Overconfidence",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Skipping pre-trade checklists after a 3-trade win streak; feeling invincible; calling market tops and bottoms without confirmation.",
            cognitiveError = "Attributing random market variance and short-term runs exclusively to personal brilliance.",
            enforceableRule = "Mandatory 5-minute pause after 3 consecutive wins. Re-confirm every item on the pre-trade checklist.",
            mentalReframing = "A win streak is just a statistical cluster in a random distribution. The next trade has exactly the same base odds.",
            loopBreakerAction = "WIN-STREAK EUPHORIA PAUSE (5:00) -> RESET EGO"
        ),
        CoachTopic(
            id = 7,
            numberStr = "07",
            title = "Loss Aversion",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Experiencing twice as much pain from a loss than pleasure from an equivalent win; refusing to accept session end.",
            cognitiveError = "Psychological asymmetry where avoiding loss feels more urgent than executing systematic edge.",
            enforceableRule = "Two losses terminate the session unconditionally. No negotiation, no 'just one more'.",
            mentalReframing = "A disciplined loss preserves capital for tomorrow's pristine conditions. Accept the red session as proof of discipline.",
            loopBreakerAction = "CLOSE SESSION AT 2 LOSSES -> TAKE POST-SESSION REVIEW"
        ),
        CoachTopic(
            id = 8,
            numberStr = "08",
            title = "Recency Bias",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Judging your entire trading ability or strategy edge based exclusively on the last 1 or 2 trades.",
            cognitiveError = "Weighting recent events heavily while ignoring long-term statistical reality.",
            enforceableRule = "Evaluate your edge over 50-trade sample blocks. Never change rules based on fewer than 50 trades.",
            mentalReframing = "One trade is 2% of a sample. You cannot assess a coin's bias in 2 flips. Look at the 50-Trade Sample Tracker.",
            loopBreakerAction = "OPEN 50-TRADE SAMPLE TRACKER -> INSPECT DISCIPLINE SCORE"
        ),
        CoachTopic(
            id = 9,
            numberStr = "09",
            title = "Confirmation Bias",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Looking only for indicators that support your preferred direction; ignoring contradicting higher-timeframe resistance.",
            cognitiveError = "Seeking evidence that confirms pre-existing desire while actively filtering out warning signs.",
            enforceableRule = "Mandatory Devil's Advocate text field: Write what would invalidate this trade before the click is unlocked.",
            mentalReframing = "Professional traders actively look for reasons NOT to take a trade. Argue against yourself first.",
            loopBreakerAction = "COMPLETE INVALUATION ARGUMENT -> VERIFY OPPOSING SIGNALS"
        ),
        CoachTopic(
            id = 10,
            numberStr = "10",
            title = "Gambler's Fallacy",
            part = 1,
            partName = "Part 1: The Emotional Enemies",
            symptoms = "Thinking '5 red candles in a row, the next MUST be green!' or 'I've had 2 losses, the next one is due to win!'.",
            cognitiveError = "Assuming past independent outcomes alter the probability of future random events.",
            enforceableRule = "Every candle and trade is an independent trial. Never trade against momentum solely because of streak length.",
            mentalReframing = "A coin has no memory. The market does not 'owe' a green candle. Trade confirmed price structure, not streak exhaustion.",
            loopBreakerAction = "WAIT FOR STRUCTURE BREAK -> NO EXHAUSTION GUESSWORK"
        ),

        // Part 2: When Control Slips
        CoachTopic(
            id = 11,
            numberStr = "11",
            title = "Hot-Hand Fallacy",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Feeling that you 'cannot lose today' after a series of quick wins; increasing position size mid-session.",
            cognitiveError = "Believing a person is in a magical hot state where edge is artificially heightened.",
            enforceableRule = "Fixed stake remains 2% regardless of win streak. The stake size field is permanently locked during an active session.",
            mentalReframing = "The cards don't know who is holding them. Stay humble or the market will humble your balance.",
            loopBreakerAction = "ENFORCE 2% FIXED STAKE -> RE-VERIFY SYSTEM RULES"
        ),
        CoachTopic(
            id = 12,
            numberStr = "12",
            title = "Chasing Missed Trades",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Jumping in with poor risk-reward after price leaves your planned entry zone; clicking just as the candle expires.",
            cognitiveError = "Believing that an entry anywhere in the direction of the trend is as good as the precise trigger price.",
            enforceableRule = "If the entry is missed by even 2 pips/points, stand down. Log it to the Missed Trade Mini-Journal.",
            mentalReframing = "Your edge resides in the exact entry price and timing. Chasing destroys your risk-to-reward ratio.",
            loopBreakerAction = "LOG TO MISSED TRADE JOURNAL -> BREATHE 3 DEEP CYCLES"
        ),
        CoachTopic(
            id = 13,
            numberStr = "13",
            title = "Boredom Trading",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Taking low-quality trades because nothing has happened for 20 minutes; browsing untracked pairs just to click.",
            cognitiveError = "Viewing trading as entertainment or an adrenaline source rather than disciplined capital allocation.",
            enforceableRule = "If no A+ setup appears, the 'No-Trade Decision' is a valid, winning decision. Log a No-Trade session proudly.",
            mentalReframing = "If the market is boring — that is a signal to wait, not to trade. Sitting on your hands pays a high hourly wage in saved capital.",
            loopBreakerAction = "LOG 'NO-TRADE DECISION' -> CLOSE LAPTOP"
        ),
        CoachTopic(
            id = 14,
            numberStr = "14",
            title = "Impatience",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Entering 15 seconds before the candle closes; jumping ahead of confirmation because 'it looks like it will close above'.",
            cognitiveError = "Believing 5 seconds of patience won't change the outcome.",
            enforceableRule = "Entry trigger must be fully confirmed (e.g. candle fully closed above key level). No front-running.",
            mentalReframing = "Candles frequently wick back in the last 3 seconds. The only truth is the candle close.",
            loopBreakerAction = "WAIT FOR FULL CANDLE CLOSE -> CONFIRM KEY LEVEL"
        ),
        CoachTopic(
            id = 15,
            numberStr = "15",
            title = "Tilt",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Racing heart, heat in the face, clenched jaw, frantic clicking, ignoring all rules, feeling intense rage at the broker.",
            cognitiveError = "Complete emotional hijack of the prefrontal cortex; trading from survival/fight-or-flight mode.",
            enforceableRule = "Emergency Lockout: Step away from screen immediately. Physical distance is the only antidote to cortisol.",
            mentalReframing = "Tilt will burn months of profit in 15 minutes. Stepping away right now is the most profitable action you can take.",
            loopBreakerAction = "EMERGENCY HARD STOP -> WALK OUTSIDE 15 MINUTES"
        ),
        CoachTopic(
            id = 16,
            numberStr = "16",
            title = "Stake Escalation",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Drifting from $5 to $10, then $25, then $60 after a loss or win streak; justifying it as 'scaling up'.",
            cognitiveError = "Believing that increasing stake size speeds up recovery rather than multiplying ruin probability.",
            enforceableRule = "STRICT NO MARTINGALE. Fixed 2% maximum. Stake field is locked for the entire session.",
            mentalReframing = "Stake escalation is the #1 killer of retail trading accounts. Protect capital first; profits compound over time.",
            loopBreakerAction = "REVIEW STAKE ESCALATION LADDER -> LOCK STAKE AT 2%"
        ),
        CoachTopic(
            id = 17,
            numberStr = "17",
            title = "One-More-Trade Syndrome",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Reaching your 5-trade limit or profit target and rationalizing: 'Just one more to make it a round $100'.",
            cognitiveError = "Believing arbitrary round numbers in your head dictate market opportunities.",
            enforceableRule = "Hard system lock at 5 trades or target hit. Terminal prohibits additional entries.",
            mentalReframing = "'One more trade' is almost always the trade that erases the entire day's discipline. Say goodbye to the market.",
            loopBreakerAction = "ENFORCE LOCKOUT SCREEN -> INITIATE POST-SESSION REVIEW"
        ),
        CoachTopic(
            id = 18,
            numberStr = "18",
            title = "Profit Giveback",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Starting the session up +$80 and ending down -$40 because you kept trading until you gave it all back.",
            cognitiveError = "Treating earned session profits as 'house money' that can be gambled with less rigor.",
            enforceableRule = "Profit target is a hard ceiling. Once hit, trading is locked. No trading into the evening session.",
            mentalReframing = "It is not house money. It is YOUR money the moment it enters your balance. Defend it with your life.",
            loopBreakerAction = "TRIGGER PROFIT TARGET MODAL -> CELEBRATE GREEN FINISH"
        ),
        CoachTopic(
            id = 19,
            numberStr = "19",
            title = "Recovery Obsession",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Inability to close the session while down; staying up late searching OTC charts to reach break-even.",
            cognitiveError = "Needing every single calendar day to end green to preserve personal pride.",
            enforceableRule = "Daily loss limit of 2 losses. Accept red days as the price of long-term profitability.",
            mentalReframing = "Red days are normal and expected in every professional hedge fund. Your job is not to avoid red days, but to keep them small.",
            loopBreakerAction = "ACCEPT 2-LOSS LIMIT -> RECORD LESSONS LEARNED"
        ),
        CoachTopic(
            id = 20,
            numberStr = "20",
            title = "Session Addiction",
            part = 2,
            partName = "Part 2: When Control Slips",
            symptoms = "Trading 7 days a week, day and night; checking charts while with family; inability to take scheduled days off.",
            cognitiveError = "Believing that constant exposure makes you a better trader rather than causing cognitive fatigue.",
            enforceableRule = "Two mandatory Non-Trading Days per week. Gatekeeper strictly locks on designated rest days.",
            mentalReframing = "Trading is cognitive athletics. Top athletes recover intentionally. Two non-trading days per week is non-negotiable.",
            loopBreakerAction = "ENFORCE NON-TRADING DAY LOCKOUT -> REST YOUR MIND"
        ),

        // Part 3: Binary & Format-Specific Psychology
        CoachTopic(
            id = 21,
            numberStr = "21",
            title = "Near-Miss Frustration",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Losing in the final 5–10 seconds of a binary option expiry and screaming 'The broker manipulated it!'.",
            cognitiveError = "Believing being in profit for 55 seconds of a 60-second trade makes it an 'almost win' that deserves compensation.",
            enforceableRule = "Mandatory Near-Miss Reframing card: Read aloud before taking another trade.",
            mentalReframing = "Right for 59 seconds is not right. Only the expiry counts. A near-miss is a probability event, not a personal injustice.",
            loopBreakerAction = "READ NEAR-MISS REFRAMING -> OBSERVE 5-MIN COOLDOWN"
        ),
        CoachTopic(
            id = 22,
            numberStr = "22",
            title = "Expiry Revenge",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Doubling down on the exact same asset with a 1-minute expiry immediately following an expiry loss.",
            cognitiveError = "Trying to prove your directional prediction right on the same asset within seconds.",
            enforceableRule = "Same-Asset Revenge Blocker: The app locks out the same asset for 5 minutes after an expiry loss.",
            mentalReframing = "If you got the timing wrong once, market noise is high right now. Forcing another entry guarantees a second loss.",
            loopBreakerAction = "SAME-ASSET LOCK ACTIVATED (5:00) -> SWITCH ASSET OR WAIT"
        ),
        CoachTopic(
            id = 23,
            numberStr = "23",
            title = "Payout Chasing",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Trading sketchy OTC pairs or volatile assets purely because the broker is advertising 92%-98% payout.",
            cognitiveError = "Looking only at payout percentage while ignoring erratic spreads, wick spikes, and algorithmic manipulation.",
            enforceableRule = "Only trade pre-approved watchlist assets with steady volume. Verify break-even win rate requirements.",
            mentalReframing = "High payouts are broker bait during erratic conditions. 85% payout on a clean setup beats 95% on an erratic casino wick.",
            loopBreakerAction = "CHECK BREAK-EVEN CALCULATOR -> STICK TO WATCHLIST"
        ),
        CoachTopic(
            id = 24,
            numberStr = "24",
            title = "Fast-Click Syndrome",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Rapidly clicking Call/Put buttons without checking checklist; double-clicking due to excitement or panic.",
            cognitiveError = "Impulsive motor action bypassing conscious decision verification.",
            enforceableRule = "Mandatory 3-Second Countdown verification button. Trade approval requires holding and counting 3 seconds.",
            mentalReframing = "Slowness is power. 3 seconds of pause separates systematic execution from gambling addiction.",
            loopBreakerAction = "3-SECOND BREATHE & VERIFY COUNTDOWN ACTIVE"
        ),
        CoachTopic(
            id = 25,
            numberStr = "25",
            title = "Countdown Pressure",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Watching broker countdown timers (e.g. 00:08 to purchase) and rushing an entry to beat the clock.",
            cognitiveError = "Confusing artificial platform countdown timers with genuine market structure signals.",
            enforceableRule = "If the countdown causes haste, skip the candle. Only enter when market conditions are pristine.",
            mentalReframing = "The broker's countdown clock is designed to trigger urgency. You are not obligated to catch this candle.",
            loopBreakerAction = "LET THE CANDLE PASS -> RESET BREATHING"
        ),
        CoachTopic(
            id = 26,
            numberStr = "26",
            title = "Win-Streak Euphoria",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Dopamine rush after 3 consecutive wins leading to loose checklists and laughing off risk parameters.",
            cognitiveError = "Dopamine surge blinding the brain to downside risk; feeling that the market is effortless.",
            enforceableRule = "Win-Streak Euphoria Pause: Mandatory 5-minute break triggered upon reaching 3 consecutive wins.",
            mentalReframing = "Pause after every 3 wins. A win streak is a statistical run, not a new strategy. The market has no memory.",
            loopBreakerAction = "5-MINUTE EUPHORIA PAUSE -> COOL DOWN DOPAMINE"
        ),
        CoachTopic(
            id = 27,
            numberStr = "27",
            title = "Loss-Streak Panic",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Sweating, feeling that your edge has vanished forever, spiraling into despair after back-to-back losses.",
            cognitiveError = "Catastrophizing expected variance into existential strategy failure.",
            enforceableRule = "At 2 losses, the session ends automatically. Step away. Review your last 50 trades in the journal.",
            mentalReframing = "Even a 65% win-rate strategy will experience two consecutive losses in 12% of trials. Stick to the law of large numbers.",
            loopBreakerAction = "AUTO-LOCKOUT TRIGGERED -> CHECK 50-TRADE SAMPLE"
        ),
        CoachTopic(
            id = 28,
            numberStr = "28",
            title = "Missed-Trade Anxiety",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Staring in agony at a green candle you didn't click; mentally counting how much money you 'lost' by not entering.",
            cognitiveError = "Treating unearned hypothetical gains as actual financial losses.",
            enforceableRule = "Open the 'Log Missed Trade' mini-journal. Document setup, state why it passed, and release it.",
            mentalReframing = "A missed trade is not a loss. Chasing it usually is. Journal it and wait for a fresh setup.",
            loopBreakerAction = "SUBMIT MISSED TRADE MINI-JOURNAL -> DETACH MENTALLY"
        ),
        CoachTopic(
            id = 29,
            numberStr = "29",
            title = "Signal/Telegram Herding",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Blindly entering trades dictated by an online group, bot, or guru without performing personal checklist checks.",
            cognitiveError = "Outsourcing personal accountability to external authority figures to avoid guilt.",
            enforceableRule = "Rule 6 on internal state check: Never enter a trade without independent technical validation.",
            mentalReframing = "A signal is information — not a substitute for your rules. If you cannot explain the trade, do not click.",
            loopBreakerAction = "INDEPENDENT CHECKLIST MANDATORY -> VERIFY TECHNICALS"
        ),
        CoachTopic(
            id = 30,
            numberStr = "30",
            title = "The No-Trade Discipline",
            part = 3,
            partName = "Part 3: Binary & Format-Specific Psychology",
            symptoms = "Feeling empty or like a failure because no trade met your standards and you closed with 0 trades taken.",
            cognitiveError = "Equating non-action with laziness or wasted time.",
            enforceableRule = "Officially log a 'No-Trade Decision'. Celebrate it as a 100% Process / Discipline victory.",
            mentalReframing = "The no-trade decision is a valid decision. Protecting your capital in unfavorable markets is the hallmark of elite trading.",
            loopBreakerAction = "CLICK 'NO-TRADE / SKIP SESSION' -> LOG DISCIPLINED WIN"
        )
    )

    fun getTopicsForPart(part: Int): List<CoachTopic> {
        return allTopics.filter { it.part == part }
    }

    fun getTopicById(id: Int): CoachTopic? {
        return allTopics.find { it.id == id }
    }
}
