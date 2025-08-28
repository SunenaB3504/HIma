package com.example.hima.data

import android.content.Context

class ProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences("hima_progress", Context.MODE_PRIVATE)

    fun getStars(letter: String): Int = prefs.getInt("stars_$letter", 0)

    fun addStar(letter: String) {
        val cur = getStars(letter)
        prefs.edit().putInt("stars_$letter", cur + 1).apply()
    }
}
