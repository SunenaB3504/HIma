package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
// icon imports not required here
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.draw.scale
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
        // animated gradient background and title
        val transition = rememberInfiniteTransition()
        val t by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse)
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(
                colors = listOf(Color(0xFFe3f2fd), Color(0xFFb3e5fc).copy(alpha = 0.9f), Color(0xFF81d4fa)),
                start = Offset(0f + t * 100f, 0f),
                end = Offset(400f - t * 100f, 800f),
                tileMode = TileMode.Clamp
            ))
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // animated title
            val scale by transition.animateFloat(initialValue = 0.98f, targetValue = 1.03f, animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse))
            Text(
                text = "HiMa - Your Fun Hindi and Marathi Language Adventure",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HimaPrimary,
                modifier = Modifier.scale(scale)
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
        }
    }
}

@Composable
fun HomeScreen(onStart: () -> Unit, onSkip: () -> Unit) {
    // Deprecated: keep for compatibility but simplified; onboarding flow shows the animated onboarding screen now.
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("HiMa", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = HimaPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onStart, modifier = Modifier.padding(8.dp)) { Text("Start") }
    }
}
