package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AlphabetScreen(onLetterSelected: (String) -> Unit, onBack: () -> Unit) {
    HimaScaffold(title = "HiMa — Alphabet", showBack = true, onBack = onBack) { _ ->
        val vowels = listOf("अ","आ","इ","ई","उ","ऊ","ए","ऐ","ओ","औ")

        // consonant groups aligned as in the supplied image (each inner list will be rendered as a row)
        val consonantGroups = listOf(
            listOf("क","ख","ग","घ","ङ"),
            listOf("च","छ","ज","झ","ञ"),
            listOf("ट","ठ","ड","ढ","ण"),
            listOf("त","थ","द","ध","न"),
            listOf("प","फ","ब","भ","म"),
            listOf("य","र","ल","व","श"),
            listOf("ष","स","ह","क्ष","त्र")
        )

        // responsive sizing
        val config = LocalConfiguration.current
        val screenWidth = config.screenWidthDp.dp
        // compute tile size to fit 6 items per row with spacing
        val targetPerRow = 6
    val totalSpacing = (8.dp * (targetPerRow - 1)) + 24.dp // inter-item + padding
    val tileSize = ((screenWidth - totalSpacing) / targetPerRow).coerceAtLeast(56.dp)

        Row(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            // left colored labels column
            Column(modifier = Modifier.width(56.dp).fillMaxHeight(), verticalArrangement = Arrangement.Top) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(Color(0xFF6a1b9a)), contentAlignment = Alignment.Center) {
                    Text(text = "स्वर", color = Color.White, modifier = Modifier.graphicsLayer(rotationZ = -90f))
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFc62828)), contentAlignment = Alignment.Center) {
                    Text(text = "व्यंजन", color = Color.White, modifier = Modifier.graphicsLayer(rotationZ = -90f))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text("Alphabet", modifier = Modifier.padding(8.dp), fontSize = 20.sp, color = HimaPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onBack) { Text("← Back to Menu", color = HimaPrimary) }
                Spacer(modifier = Modifier.height(12.dp))

                // Vowels heading
                Text("स्वर", fontSize = 16.sp, color = HimaPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (v in vowels) {
                        GradientCard(title = v, subtitle = null, colorStart = HimaAccent, colorEnd = HimaPrimary, width = 80.dp, height = 64.dp) {
                            onLetterSelected(v)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Consonants heading
                Text("व्यंजन", fontSize = 16.sp, color = HimaPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                // Render consonant groups with responsive tile size
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    for (group in consonantGroups) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (c in group) {
                                GradientCard(title = c, subtitle = null, colorStart = HimaPrimary, colorEnd = HimaAccent, width = tileSize, height = tileSize) {
                                    onLetterSelected(c)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

