package com.example.hima.media

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ready = false
    private val pending = mutableListOf<String>()

    init {
        // use applicationContext to avoid leaking Activity context
        tts = TextToSpeech(context.applicationContext, this)
        Log.d("TTS", "TTSManager initialized, waiting for onInit")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // try preferred Hindi locale; fall back to default if unavailable
            val preferred = Locale("hi")
            var res = tts?.setLanguage(preferred)
            Log.d("TTS", "preferred setLanguage result=$res for locale=$preferred")
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                val fallback = Locale.getDefault()
                res = tts?.setLanguage(fallback)
                Log.w("TTS", "preferred locale not available; fallback setLanguage result=$res for locale=$fallback")
            }
            // even if language is not ideal, mark ready so speak() will attempt
            ready = true

            // flush any pending speak requests
            if (pending.isNotEmpty()) {
                Log.d("TTS", "flushing ${pending.size} pending TTS requests")
                for ((i, txt) in pending.withIndex()) {
                    try {
                        tts?.speak(txt, if (i == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, "tts_$i")
                    } catch (e: Exception) {
                        Log.e("TTS", "Error speaking pending text", e)
                    }
                }
                pending.clear()
            }
        } else {
            Log.e("TTS", "TextToSpeech initialization failed with status=$status")
            // keep ready false; future speak calls will queue but likely won't play
        }
    }

    fun speak(text: String) {
        if (!ready) {
            Log.w("TTS", "TTS not ready yet, queueing text='$text'")
            pending.add(text)
            return
        }
        try {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_now")
        } catch (e: Exception) {
            Log.e("TTS", "speak failed", e)
        }
    }

    /**
     * Speak text after attempting to set a preferred locale.
     * localeTag should be a BCP-47 tag like "mr" or "hi". If null, existing language is used.
     */
    fun speakWithLocale(text: String, localeTag: String?) {
        if (!ready) {
            Log.w("TTS", "TTS not ready yet, queueing text='$text'")
            pending.add(text)
            return
        }
        try {
            if (!localeTag.isNullOrBlank()) {
                try {
                    val locale = Locale(localeTag)
                    val res = tts?.setLanguage(locale)
                    Log.d("TTS", "setLanguage result=$res for locale=$locale")
                } catch (e: Exception) {
                    Log.w("TTS", "failed to set requested locale=$localeTag, using current locale", e)
                }
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_now")
        } catch (e: Exception) {
            Log.e("TTS", "speak failed", e)
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("TTS", "shutdown error", e)
        }
    }
}
