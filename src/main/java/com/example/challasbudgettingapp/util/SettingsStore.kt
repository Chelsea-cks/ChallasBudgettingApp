package com.example.challasbudgettingapp.util

import android.content.Context
import androidx.core.content.edit

/** Persists user settings so they survive app restarts. */
object SettingsStore {
    private const val PREFS = "challas_settings"

    private fun prefs(c: Context) = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isDarkMode(c: Context) = prefs(c).getBoolean("dark_mode", false)
    fun setDarkMode(c: Context, v: Boolean) = prefs(c).edit { putBoolean("dark_mode", v) }

    fun getCurrency(c: Context) = prefs(c).getString("currency", "ZAR (R)") ?: "ZAR (R)"
    fun setCurrency(c: Context, v: String) = prefs(c).edit { putString("currency", v) }
}
