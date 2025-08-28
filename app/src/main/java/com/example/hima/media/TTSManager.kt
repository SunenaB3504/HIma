package com.example.hima.media

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // prefer Hindi locale; fallback to default
            val locale = Locale("hi")
            val res = tts?.setLanguage(locale)
            Log.d("TTS", "setLanguage result=$res")
            ready = true
        }
    }

    fun speak(text: String) {
        if (!ready) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
