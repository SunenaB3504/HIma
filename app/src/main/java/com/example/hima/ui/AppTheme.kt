package com.example.hima.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Centralized colors & common scaffold to give a uniform look
val HimaPrimary = Color(0xFF0077CC)
val HimaAccent = Color(0xFF00BFA5)
val HimaSoftBg = Color(0xFFe6f7ff)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HimaScaffold(
    title: String,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            // Use a top app bar with a left-aligned title so screens can place actions on the right
            TopAppBar(
                title = { Row(verticalAlignment = Alignment.CenterVertically) { Text(text = title, color = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HimaPrimary),
                navigationIcon = {
                    if (showBack && onBack != null) {
                        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White) }
                    }
                }
                , actions = actions
            )
        },
        containerColor = HimaSoftBg
    ) { inner ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(HimaSoftBg, Color.White)))
            .padding(inner)
        ) {
            content(inner)
        }
    }
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = HimaAccent, contentColor = Color.White),
        modifier = Modifier
            .padding(4.dp)
            .heightIn(min = 48.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
