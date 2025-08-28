package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
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
                }, onBackHome = { navController.navigate("home") })
            }
            composable("main") {
                MainMenuScreen(
                    onLearnAlphabet = { /* TODO */ },
                    onReadStories = { /* TODO */ },
                    onHome = { navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    } }
                )
            }
        }
    }
}
