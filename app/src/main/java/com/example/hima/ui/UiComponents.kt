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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A flexible gradient card used across the main menu and onboarding.
 * Accepts a title and optional subtitle and an icon. Size is configurable.
 */
@Composable
fun GradientCard(
    title: String,
    subtitle: String? = null,
    colorStart: Color,
    colorEnd: Color,
    icon: ImageVector? = Icons.Default.Star,
    width: Dp = 160.dp,
    height: Dp = 160.dp,
    textColor: Color? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(width = width, height = height),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(colorStart, colorEnd)))
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            // choose sensible text color if not provided: prefer white on dark gradients, dark on light
            val avgLuminance = (colorStart.luminance() + colorEnd.luminance()) / 2f
            val useColor = textColor ?: if (avgLuminance < 0.5f) Color.White else Color(0xFF0F172A)

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFFFF176),
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = title,
                    color = useColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.15f),
                            blurRadius = 6f
                        )
                    )
                )

                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        color = useColor.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }
    }
}
