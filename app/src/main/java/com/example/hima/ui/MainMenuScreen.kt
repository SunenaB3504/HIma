package com.example.hima.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainMenuScreen(navController: NavController) {
    HimaScaffold(title = "Hima — Main Menu", showBack = false, onBack = null) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("What would you like to do?", fontSize = 26.sp, color = HimaPrimary)

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                GradientCard(title = "अ", subtitle = "Learn the Alphabet", colorStart = Color(0xFF42a5f5), colorEnd = Color(0xFF1e88e5), width = 180.dp, height = 180.dp) {
                    navController.navigate("alphabet")
                }
                GradientCard(title = "📚", subtitle = "Read Stories", colorStart = Color(0xFF26a69a), colorEnd = Color(0xFF00bfa5), width = 180.dp, height = 180.dp) {
                    navController.navigate("stories")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GradientCard(title = "⚙️", subtitle = "Settings", colorStart = Color(0xFF8e8e8e), colorEnd = Color(0xFF5e5e5e), width = 120.dp, height = 120.dp) {
                    navController.navigate("settings")
                }
            }
        }
    }
}
