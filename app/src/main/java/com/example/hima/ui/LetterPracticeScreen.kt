package com.example.hima.ui

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.CardDefaults
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
import com.example.hima.data.AssetLoader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight


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
    progressStore: ProgressStore,
    settingsStore: com.example.hima.data.SettingsStore
) {
    HimaScaffold(title = "Practice: $letter", showBack = true, onBack = onBack) { _ ->
        var tracing by remember { mutableStateOf(false) }
        var completed by remember { mutableStateOf(false) }

        val stars = remember { derivedStateOf { progressStore.getStars(letter) } }

    val ctx = LocalContext.current
    val assetLoader = remember { AssetLoader(ctx) }

    // load examples for the selected letter
    var examplesJson = remember { assetLoader.loadJsonFromAssets("letter_${letter}.json") }
    if (examplesJson == null) {
        // fallback: some assets use transliterated filenames; try to find by `char` field
        examplesJson = remember { assetLoader.findLetterJsonByChar(letter) }
    }
    val examples = remember(examplesJson) { assetLoader.getLetterExamples(examplesJson ?: org.json.JSONObject()) }

    // displayedExamples is mutable state used to show shuffled/new sets without reloading asset
    val displayedExamples = remember { mutableStateListOf<org.json.JSONObject>() }
    LaunchedEffect(examplesJson) {
        // load a static set of up to 3 words mapped to this letter when it's selected
        displayedExamples.clear()
        if (examples.isNotEmpty()) {
            val firstThree = examples.take(3)
            displayedExamples.addAll(firstThree)
        }
    }

    // Two-column layout: left = letter + tracing, right = examples list
    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Left column
    Column(modifier = Modifier.weight(0.62f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = letter, fontSize = 120.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "⭐️ ${stars.value}", color = HimaPrimary)

            Spacer(modifier = Modifier.height(12.dp))
            // control row: Listen | Practice | Combination | Exercise
            var showCombinations by remember { mutableStateOf(false) }
            var showExercise by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PrimaryButton(text = "Listen") { onListen(letter) }
                PrimaryButton(text = if (!tracing) "Practice" else "Tracing") { tracing = !tracing; if (!tracing) completed = false }
                PrimaryButton(text = "Combination") { showCombinations = !showCombinations }
                PrimaryButton(text = "Exercise") { showExercise = true }
            }

            Spacer(modifier = Modifier.height(12.dp))
            // show combinations inline when requested
            if (showCombinations) {
                val combos = mutableListOf<String>()
                // try to read combos from the JSON if present
                try {
                    val arr = examplesJson?.optJSONArray("combinations")
                    if (arr != null) {
                        for (i in 0 until arr.length()) combos.add(arr.getString(i))
                    }
                } catch (_: Exception) {}
                if (combos.isEmpty()) {
                    combos.add("${letter}ा")
                    combos.add("${letter}ि")
                    combos.add("${letter}ु")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (c in combos) {
                        GradientCard(title = c, subtitle = null, colorStart = Color(0xFFffd54f), colorEnd = Color(0xFFffb300), width = 100.dp, height = 60.dp) {
                            // when user taps a combination, show its practice screen (reuse onLetterSelected by calling onListen with the string)
                            onListen("tts:${c}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Tracing area
            if (tracing) {
                var hasStrokes by remember { mutableStateOf(false) }
                var clearSignal by remember { mutableStateOf(0) }
                val ctx2 = LocalContext.current

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
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
                            Toast.makeText(ctx2, "Star awarded! ⭐️", Toast.LENGTH_SHORT).show()
                            android.util.Log.d("ProgressStore", "addStar $letter")
                            onListen(letter)
                        }
                        tracing = false
                    }
                    Button(onClick = {
                        clearSignal += 1
                        hasStrokes = false
                        completed = false
                    }) { Text("Clear", color = HimaPrimary) }
                }

            } else {
                // show a faint guide box when not tracing
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .padding(8.dp)
                    .border(width = 2.dp, color = HimaPrimary.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Text(text = letter, fontSize = 120.sp, color = HimaPrimary.copy(alpha = 0.08f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onBack) { Text("← Back to Alphabet", color = HimaPrimary) }
        }

    Spacer(modifier = Modifier.width(12.dp))

        // Right column: examples list
    Card(modifier = Modifier.weight(0.38f).fillMaxHeight(), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                Text(text = "Words that start with $letter", fontSize = 18.sp, color = HimaPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
                    items(displayedExamples) { ex ->
                        val word = ex.optString("word", "")
                        val emoji = ex.optString("emoji", "")
                        val audio = ex.optString("audio", "")
                        val meaning = ex.optString("meaning", "")
                        // prefer language-specific sentence when instruction language is Marathi
                        val lang = settingsStore.getLanguage()
                        val sentence = if (lang == "marathi") ex.optString("sentence_mr", ex.optString("sentence", "")) else ex.optString("sentence", "")

                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp), shape = RoundedCornerShape(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = emoji, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = word, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HimaPrimary)
                                    }
                                    if (meaning.isNotEmpty()) Text(text = meaning, fontSize = 13.sp, color = Color(0xFF0B8457))
                                    if (sentence.isNotEmpty()) Text(text = sentence, fontSize = 13.sp, color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                // listen button: request either asset playback or TTS of word+meaning+sentence
                                Button(onClick = {
                                    val parts = listOf(word, meaning, sentence).filter { it.isNotBlank() }
                                    val text = parts.joinToString(separator = ". ")
                                    if (audio.isNotEmpty()) {
                                        // include a readable fallback text so TTS can speak something sensible if asset play fails
                                        onListen("asset:${audio}|fallback:${text}")
                                    } else {
                                        onListen("tts:${text}")
                                    }
                                }, shape = RoundedCornerShape(50), content = { Text("🔊") })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Get New Words") {
                    // This app uses a static mapping of 3 words per letter.
                    // Do nothing other than inform the user.
                    Toast.makeText(ctx, "Only 3 words are shown per letter", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Identify Word exercise dialog
    if (showExercise) {
        // Build candidates: pick one target from displayedExamples and up to 3 distractors from examples
        val target = displayedExamples.firstOrNull() ?: examples.firstOrNull()
        if (target != null) {
            val candidates = remember { mutableStateListOf<org.json.JSONObject>() }
            LaunchedEffect(target) {
                candidates.clear()
                candidates.add(target)
                // add distractors
                val others = (examples - target).shuffled().take(3)
                candidates.addAll(others)
                candidates.shuffle()
                // play the target audio automatically
                val audio = target.optString("audio", "")
                val parts = listOf(target.optString("word", ""), target.optString("meaning", ""), target.optString("sentence", "")).filter { it.isNotBlank() }
                val text = parts.joinToString(". ")
                if (audio.isNotEmpty()) onListen("asset:${audio}|fallback:${text}") else onListen("tts:${text}")
            }

            var selectedIndex by remember { mutableStateOf(-1) }
            AlertDialog(onDismissRequest = { showExercise = false }, title = { Text("Identify the word") }, text = {
                Column {
                    Text("Listen to the word and choose the correct option:")
                    Spacer(modifier = Modifier.height(12.dp))
                    for ((i, c) in candidates.withIndex()) {
                        val w = c.optString("word", "")
                        Button(onClick = { selectedIndex = i }) { Text(w) }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }, confirmButton = {
                Button(onClick = {
                    if (selectedIndex >= 0) {
                        val chosen = candidates[selectedIndex]
                        if (chosen == target) {
                            progressStore.addStar(letter)
                            Toast.makeText(ctx, "Correct! Star awarded.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(ctx, "Try again.", Toast.LENGTH_SHORT).show()
                        }
                        showExercise = false
                    }
                }) { Text("Submit") }
            }, dismissButton = { Button(onClick = { showExercise = false }) { Text("Cancel") } })
        } else {
            // nothing to exercise on
            LaunchedEffect(Unit) { Toast.makeText(ctx, "Not enough words to run exercise", Toast.LENGTH_SHORT).show(); showExercise = false }
        }
    }
    }
}
