package com.example.hima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CombinedSoundsScreen(consonant: String = "क", onBack: () -> Unit, onPlay: (String) -> Unit) {
    val vowels = listOf("ा","ि","ी","ु","ू","े","ै")
    var index by remember { mutableStateOf(0) }
    HimaScaffold(title = "Combined Sounds", showBack = true, onBack = onBack) { _ ->
        val current = consonant + vowels[index]
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = current, fontSize = 72.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(text = "Prev") { index = (index - 1 + vowels.size) % vowels.size }
                PrimaryButton(text = "Play") { onPlay(current) }
                PrimaryButton(text = "Next") { index = (index + 1) % vowels.size }
            }
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = onBack) { Text("Back to Menu", color = HimaPrimary) }
        }
    }
}
