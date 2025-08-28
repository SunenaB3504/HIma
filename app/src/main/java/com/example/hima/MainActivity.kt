package com.example.hima

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import com.example.hima.data.AssetLoader
import com.example.hima.media.AudioPlayer
import com.example.hima.media.TTSManager
import org.json.JSONObject
import com.example.hima.ui.theme.HImaTheme
import com.example.hima.ui.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Surface-level uncaught exception handler to help capture startup crashes in logs
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("HimaCrash", "Uncaught exception in thread ${t.name}", e)
        }
        setContent {
            HImaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        // Hilt will provide these singletons; retrieve via local composition
                        // NavGraph is a @Composable; call it directly and rely on the uncaught
                        // exception handler to capture startup crashes in logcat.
                        NavGraph()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ensure TTS shutdown if used
    }
}

@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    assetLoader: AssetLoader,
    audioPlayer: AudioPlayer,
    tts: TTSManager
) {
    var letterJson by remember { mutableStateOf<JSONObject?>(null) }
    var storyJson by remember { mutableStateOf<JSONObject?>(null) }

    LaunchedEffect(Unit) {
        letterJson = assetLoader.loadJsonFromAssets("letter_h_01.json")
        storyJson = assetLoader.loadJsonFromAssets("story_std1_ch1_page1.json")
    }

    Column(modifier = modifier) {
        Text(text = "Hima — Sample Preview")
        Button(onClick = {
            val audioPath = letterJson?.let { assetLoader.getLetterAudioPath(it) }
            val played = audioPath?.let { audioPlayer.playAsset(it) } ?: false
            if (played == false) {
                // fallback: speak the letter char
                val ch = letterJson?.let { assetLoader.getLetterChar(it) } ?: "अ"
                tts.speak(ch)
            }
        }) {
            Text("Play sample letter audio (or TTS)")
        }

        Button(onClick = {
            val narration = storyJson?.let { assetLoader.getStoryNarrationPath(it) }
            val played = narration?.let { audioPlayer.playAsset(it) } ?: false
            if (played == false) {
                // fallback: speak story text
                val text = storyJson?.let { assetLoader.getStoryText(it) } ?: ""
                if (text.isNotBlank()) tts.speak(text)
            }
        }) {
            Text("Play sample story narration (or TTS)")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HImaTheme {
        Surface {
            Greeting("Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}