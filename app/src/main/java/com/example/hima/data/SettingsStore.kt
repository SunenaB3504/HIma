package com.example.hima.data

import android.content.Context

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("hima_settings", Context.MODE_PRIVATE)

    fun useTTS(): Boolean = prefs.getBoolean("use_tts", true)

    fun setUseTTS(value: Boolean) {
        prefs.edit().putBoolean("use_tts", value).apply()
    }
}
