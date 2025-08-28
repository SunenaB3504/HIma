package com.example.hima.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.IOException

class AudioPlayer(private val context: Context) {
    private var player: MediaPlayer? = null

    fun playAsset(path: String): Boolean {
        // path is a logical path like "audio/letters/h/अ.mp3" stored in assets
        return try {
            player?.release()
            player = MediaPlayer()
            val afd = context.assets.openFd(path)
            player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            player?.prepare()
            player?.start()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}
