package com.example.hima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlphabetScreen(onLetterSelected: (String) -> Unit, onBack: () -> Unit) {
    HimaScaffold(title = "Hindi Alphabet", showBack = true, onBack = onBack) { _ ->
        // simple sample alphabet list (Hindi vowels + consonants simplified)
        val letters = listOf("अ","आ","इ","ई","उ","ऊ","क","ख","ग","घ")
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Alphabet", modifier = Modifier.padding(8.dp), fontSize = 20.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("← Back to Menu", color = HimaPrimary) }
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 120.dp), content = {
                items(letters) { letter ->
                    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                        GradientCard(title = letter, subtitle = null, colorStart = HimaPrimary, colorEnd = HimaAccent, width = 120.dp, height = 120.dp) {
                            onLetterSelected(letter)
                        }
                    }
                }
            })
        }
    }
}
