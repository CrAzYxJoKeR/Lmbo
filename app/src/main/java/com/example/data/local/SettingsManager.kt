package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lmbo_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NON_TRADING_DAYS = "non_trading_days" // Comma-separated Calendar day-of-week ints
        private const val KEY_STRICT_COOLDOWN = "strict_cooldown"
        private const val KEY_DEFAULT_BALANCE = "default_balance"
        private const val KEY_DEFAULT_PROFIT_TARGET = "default_profit_target"
    }

    // Default to Saturday (7) and Sunday (1)
    fun getNonTradingDays(): Set<Int> {
        val raw = prefs.getString(KEY_NON_TRADING_DAYS, "${Calendar.SATURDAY},${Calendar.SUNDAY}") ?: ""
        return raw.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
    }

    fun setNonTradingDays(days: Set<Int>) {
        prefs.edit().putString(KEY_NON_TRADING_DAYS, days.joinToString(",")).apply()
    }

    fun isTodayNonTradingDay(): Boolean {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return getNonTradingDays().contains(today)
    }

    fun isStrictCooldown(): Boolean {
        return prefs.getBoolean(KEY_STRICT_COOLDOWN, false)
    }

    fun setStrictCooldown(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_STRICT_COOLDOWN, enabled).apply()
    }

    fun getDefaultBalance(): Double {
        return prefs.getFloat(KEY_DEFAULT_BALANCE, 1000f).toDouble()
    }

    fun setDefaultBalance(balance: Double) {
        prefs.edit().putFloat(KEY_DEFAULT_BALANCE, balance.toFloat()).apply()
    }

    fun getDefaultProfitTarget(): Double {
        return prefs.getFloat(KEY_DEFAULT_PROFIT_TARGET, 60f).toDouble()
    }

    fun setDefaultProfitTarget(target: Double) {
        prefs.edit().putFloat(KEY_DEFAULT_PROFIT_TARGET, target.toFloat()).apply()
    }
}
