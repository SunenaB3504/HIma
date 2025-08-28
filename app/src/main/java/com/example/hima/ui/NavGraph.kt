package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.hima.data.AssetLoader
import com.example.hima.media.AudioPlayer
import com.example.hima.media.TTSManager
import com.example.hima.data.ProgressStore
import com.example.hima.data.SettingsStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    val context = LocalContext.current
    // lightweight instances for playback and progress; DI can be used later
    val assetLoader = remember { AssetLoader(context) }
    val audioPlayer = remember { AudioPlayer(context) }
    val ttsManager = remember { TTSManager(context) }
    val progressStore = remember { ProgressStore(context) }
    val settingsStore = remember { SettingsStore(context) }

    // simple playful background for the entire app
    Surface(modifier = Modifier.fillMaxSize().background(
        Brush.verticalGradient(listOf(Color(0xFFe6f7ff), Color(0xFFffffff)))
    )) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("home") {
                // simple home that leads to onboarding or main
                HomeScreen(
                    onStart = { navController.navigate("onboarding") },
                    onSkip = { navController.navigate("main") }
                )
            }
            composable("onboarding") {
                OnboardingScreen(onLanguageSelected = { lang ->
                    // persist or pass language; navigate to main menu
                    navController.navigate("main") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }, onBackHome = { navController.popBackStack() })
            }
            composable("main") {
                MainMenuScreen(navController = navController)
            }
            composable("alphabet") {
                AlphabetScreen(onLetterSelected = { letter ->
                    navController.navigate("letter/$letter")
                }, onBack = { navController.popBackStack() })
            }
            composable("letter/{letter}") { backStack ->
                val letter = backStack.arguments?.getString("letter") ?: "अ"
                LetterPracticeScreen(letter = letter, onBack = { navController.popBackStack() }, onListen = { l ->
                    // if user prefers device TTS, speak directly
                    if (settingsStore.useTTS()) {
                        ttsManager.speak(l)
                    } else {
                        // try known assets first, then fallback to JSON-driven path, then TTS
                        val candidate = "audio/letters/${l}.mp3"
                        val played = try { audioPlayer.playAsset(candidate) } catch (e: Exception) { false }
                        if (!played) {
                            val json = try { assetLoader.loadJsonFromAssets("letter_${l}.json") } catch (e: Exception) { null }
                            val path = json?.let { assetLoader.getLetterAudioPath(it) }
                            val ok = if (path != null) try { audioPlayer.playAsset(path) } catch (e: Exception) { false } else false
                            if (!ok) ttsManager.speak(l)
                        }
                    }
                }, progressStore = progressStore)
            }
            composable("combined") {
                CombinedSoundsScreen(onBack = { navController.popBackStack() }, onPlay = { phoneme ->
                    // Attempt to play combined phoneme audio, fallback to TTS
                    val safe = phoneme.replace(" ", "_")
                    val candidate = "audio/combined/${safe}.mp3"
                    val played = try { audioPlayer.playAsset(candidate) } catch (e: Exception) { false }
                    if (!played) {
                        ttsManager.speak(phoneme)
                    }
                })
            }
            composable("settings") {
                SettingsScreen(settingsStore = settingsStore, onBack = { navController.popBackStack() })
            }
        }
    }
}
