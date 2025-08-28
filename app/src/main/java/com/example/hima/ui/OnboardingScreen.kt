package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
// icon imports not required here
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onLanguageSelected: (String) -> Unit, onBackHome: () -> Unit) {
    HimaScaffold(title = "Choose language", showBack = true, onBack = onBackHome) { _ ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hima — Learn & Play",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HimaPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GradientCard(title = "Hindi 🇮🇳", subtitle = null, colorStart = Color(0xFFff8a65), colorEnd = Color(0xFFff7043), width = 160.dp, height = 160.dp) {
                    onLanguageSelected("hindi")
                }
                GradientCard(title = "Marathi 🇮🇳", subtitle = null, colorStart = Color(0xFF4dd0e1), colorEnd = Color(0xFF00bfa5), width = 160.dp, height = 160.dp) {
                    onLanguageSelected("marathi")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onBackHome) {
                Text("← Change Language", color = HimaPrimary)
            }
        }
    }
}

@Composable
fun HomeScreen(onStart: () -> Unit, onSkip: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Hima — Your fun language adventure!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0077cc))
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onStart, modifier = Modifier.padding(8.dp)) { Text("Start") }
        OutlinedButton(onClick = onSkip, modifier = Modifier.padding(8.dp)) { Text("Skip Intro") }
    }
}
