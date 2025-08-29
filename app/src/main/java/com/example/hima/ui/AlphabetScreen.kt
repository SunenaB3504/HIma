package com.example.hima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlphabetScreen(onLetterSelected: (String) -> Unit, onBack: () -> Unit) {
    HimaScaffold(title = "HiMa — Alphabet", showBack = true, onBack = onBack) { _ ->
        val vowels = listOf("अ","आ","इ","ई","उ","ऊ","ए","ऐ","ओ","औ")
        val consonants = listOf("क","ख","ग","घ","ङ","च","छ","ज","झ","ञ","ट","ठ","ड","ढ","ण","त","थ","द","ध","न","प","फ","ब","भ","म","य","र","ल","व","श","ष","स","ह")
        // simple combinations map for demo
    // combinations are shown from the Practice page now
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Alphabet", modifier = Modifier.padding(8.dp), fontSize = 20.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("← Back to Menu", color = HimaPrimary) }
            Spacer(modifier = Modifier.height(12.dp))

            // Vowels row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (v in vowels) {
                    GradientCard(title = v, subtitle = null, colorStart = HimaAccent, colorEnd = HimaPrimary, width = 100.dp, height = 80.dp) {
                        onLetterSelected(v)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Consonants grid (scrollable simplified)
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 88.dp), modifier = Modifier.weight(1f)) {
                items(consonants) { c ->
                    Box(modifier = Modifier.padding(6.dp)) {
                        GradientCard(title = c, subtitle = null, colorStart = HimaPrimary, colorEnd = HimaAccent, width = 88.dp, height = 88.dp) {
                            selectedConsonant = c
                            onLetterSelected(c)
                        }
                    }
                }
            }

            // combinations are handled on the Practice screen
        }
    }
}
