package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(listOf(Color(0xFFe6f7ff), Color(0xFFFFFFFF)))
        )
        .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hima — Learn & Play",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0077cc)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GradientCard(text = "Hindi 🇮🇳", colorStart = Color(0xFFff8a65), colorEnd = Color(0xFFff7043)) {
                onLanguageSelected("hindi")
            }
            GradientCard(text = "Marathi 🇮🇳", colorStart = Color(0xFF4dd0e1), colorEnd = Color(0xFF00bfa5)) {
                onLanguageSelected("marathi")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onBackHome) {
            Text("← Change Language", color = Color(0xFF0077cc))
        }
    }
}

@Composable
fun GradientCard(text: String, colorStart: Color, colorEnd: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(colorStart, colorEnd)))
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                // small playful icon above the label
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFF176),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            blurRadius = 4f
                        )
                    )
                )
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
