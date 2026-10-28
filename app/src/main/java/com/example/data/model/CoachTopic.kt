package com.example.data.model

data class CoachTopic(
    val id: Int,
    val numberStr: String, // e.g. "01"
    val title: String,
    val part: Int, // 1, 2, or 3
    val partName: String, // "Part 1: The Emotional Enemies", "Part 2: When Control Slips", "Part 3: Binary & Format-Specific Psychology"
    val symptoms: String,
    val cognitiveError: String,
    val enforceableRule: String,
    val mentalReframing: String,
    val loopBreakerAction: String? = null
)
