package com.example.hima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hima.data.SettingsStore

@Composable
fun SettingsScreen(settingsStore: SettingsStore, onBack: () -> Unit) {
    var useTts by remember { mutableStateOf(settingsStore.useTTS()) }
    HimaScaffold(title = "Settings", showBack = true, onBack = onBack) { _ ->
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Settings", style = MaterialTheme.typography.headlineSmall, color = HimaPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Use device TTS", modifier = Modifier.weight(1f), color = Color(0xFF1976D2))
                Switch(checked = useTts, onCheckedChange = {
                    useTts = it
                    settingsStore.setUseTTS(it)
                })
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = onBack) { Text("Back to Menu", color = HimaPrimary) }
        }
    }
}
