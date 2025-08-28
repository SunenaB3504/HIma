package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(onLearnAlphabet: () -> Unit, onReadStories: () -> Unit, onHome: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(listOf(Color(0xFFe8f7ff), Color(0xFFFFFFFF)))
        )
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("What would you like to do?", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GradientCard(text = "अ\nLearn the Alphabet", colorStart = Color(0xFF42a5f5), colorEnd = Color(0xFF1e88e5)) {
                onLearnAlphabet()
            }
            GradientCard(text = "📚\nRead Textbook Stories", colorStart = Color(0xFF26a69a), colorEnd = Color(0xFF00bfa5)) {
                onReadStories()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onHome) {
            Text("← Home", color = Color(0xFF0077cc))
        }
    }
}
