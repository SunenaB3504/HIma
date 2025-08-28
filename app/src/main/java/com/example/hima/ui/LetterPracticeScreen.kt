package com.example.hima.ui

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hima.data.ProgressStore


@Composable
fun TracingCanvas(
    letter: String,
    modifier: Modifier = Modifier,
    strokeColor: Color = HimaPrimary,
    clearSignal: Int = 0,
    onChanged: (hasStrokes: Boolean) -> Unit = {}
) {
    val paths = remember { mutableStateListOf<Path>() }
    // watch clearSignal to clear paths when it changes
    var lastClear by remember { mutableStateOf(clearSignal) }
    if (clearSignal != lastClear) {
        paths.clear()
        lastClear = clearSignal
        onChanged(false)
    }

    Box(modifier = modifier
        .background(Color(0xFFF6F9FF), RoundedCornerShape(8.dp))
        .border(width = 2.dp, color = HimaPrimary.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
    ) {
        // faint guide outline of the letter
        Text(
            text = letter,
            fontSize = 160.sp,
            color = HimaPrimary.copy(alpha = 0.08f),
            modifier = Modifier.align(Alignment.Center)
        )

        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val p = Path().apply { moveTo(offset.x, offset.y) }
                        paths.add(p)
                        onChanged(true)
                    },
                    onDrag = { change, _ ->
                        val last = paths.lastOrNull()
                        if (last != null) {
                            last.lineTo(change.position.x, change.position.y)
                        }
                    }
                )
            }
        ) {
            for (p in paths) {
                drawPath(path = p, color = strokeColor, style = Stroke(width = 14f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
}

@Composable
fun LetterPracticeScreen(
    letter: String,
    onBack: () -> Unit,
    onListen: (String) -> Unit,
    progressStore: ProgressStore
) {
    HimaScaffold(title = "Practice: $letter", showBack = true, onBack = onBack) { _ ->
        var tracing by remember { mutableStateOf(false) }
        var completed by remember { mutableStateOf(false) }

        val stars = remember { derivedStateOf { progressStore.getStars(letter) } }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(12.dp))
                Text(text = letter, fontSize = 96.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "⭐️ ${stars.value}", color = HimaPrimary)

            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(text = "Listen") { onListen(letter) }

            Spacer(modifier = Modifier.height(12.dp))

            if (!tracing) {
                PrimaryButton(text = "Trace") { tracing = true; completed = false }
            } else {
                // interactive tracing area — finger/digit tracing
                var hasStrokes by remember { mutableStateOf(false) }
                var clearSignal by remember { mutableStateOf(0) }
                val ctx = LocalContext.current

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(8.dp)) {
                    TracingCanvas(letter = letter, modifier = Modifier.fillMaxSize(), strokeColor = HimaPrimary, clearSignal = clearSignal) { has ->
                        hasStrokes = has
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryButton(text = "Mark Complete") {
                        if (!completed && hasStrokes) {
                            progressStore.addStar(letter)
                            completed = true
                            // toast + log
                            Toast.makeText(ctx, "Star awarded! ⭐️", Toast.LENGTH_SHORT).show()
                            android.util.Log.d("ProgressStore", "addStar $letter")
                            // replay the letter using existing onListen handler
                            onListen(letter)
                        }
                        tracing = false
                    }
                    Button(onClick = {
                        // clear the canvas
                        clearSignal += 1
                        hasStrokes = false
                        completed = false
                    }) { Text("Clear", color = HimaPrimary) }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            // rely on the top app bar back action; provide a quieter navigation hint
            TextButton(onClick = onBack) { Text("Back to Alphabet", color = HimaPrimary) }
        }
    }
}
