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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ButtonDefaults
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
    // Put action buttons into the top app bar by using the Scaffold's navigation area (HimaScaffold still shows back)
    // control visibility state for combinations/exercise at composable scope (moved so actions lambda can access)
    var showCombinations by remember { mutableStateOf(false) }
    var showExercise by remember { mutableStateOf(false) }

    HimaScaffold(title = "Practice: $letter", showBack = true, onBack = onBack, actions = {
        // Top-right action buttons: minimized icons/text for compact layout
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { onListen(letter) }) { Text("🔊", color = Color.White) }
            TextButton(onClick = { showCombinations = !showCombinations }) { Text("Comb", color = Color.White) }
            TextButton(onClick = { showExercise = true }) { Text("Ex", color = Color.White) }
            TextButton(onClick = onBack) { Text("Home", color = Color.White) }
        }
    }) { _ ->
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
    Column(modifier = Modifier.weight(0.60f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = letter, fontSize = 120.sp, color = HimaPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Stars: ${stars.value}", color = HimaPrimary)

            Spacer(modifier = Modifier.height(8.dp))
            // Buttons are moved to the top app bar actions for a cleaner layout. Keep a small inline listen toggle for quick access.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PrimaryButton(text = "Listen") { onListen(letter) }
                PrimaryButton(text = if (!tracing) "Practice" else "Tracing") { tracing = !tracing; if (!tracing) completed = false }
            }

            Spacer(modifier = Modifier.height(12.dp))
            // show combinations inline when requested
            if (showCombinations) {
                // define vowel diacritics to construct combined forms
                val vowelDiacritics = mapOf(
                    "अ" to "",
                    "आ" to "ा",
                    "इ" to "ि",
                    "ई" to "ी",
                    "उ" to "ु",
                    "ऊ" to "ू",
                    "ए" to "े",
                    "ऐ" to "ै",
                    "ओ" to "ो",
                    "औ" to "ौ"
                )
                val vowels = listOf("अ","आ","इ","ई","उ","ऊ","ए","ऐ","ओ","औ")
                val consonants = listOf("क","ख","ग","घ","ङ","च","छ","ज","झ","ञ","ट","ठ","ड","ढ","ण","त","थ","द","ध","न","प","फ","ब","भ","म","य","र","ल","व","श","ष","स","ह")

                val combos = mutableListOf<String>()
                try {
                    // if JSON provides specific combinations, prefer those
                    val arr = examplesJson?.optJSONArray("combinations")
                    if (arr != null) {
                        for (i in 0 until arr.length()) combos.add(arr.getString(i))
                    }
                } catch (_: Exception) {}

                if (combos.isEmpty()) {
                    // build full set depending on whether selected letter is vowel or consonant
                    if (vowels.contains(letter)) {
                        // vowel selected: show consonant + vowel diacritic for all consonants
                        val diac = vowelDiacritics[letter] ?: ""
                        for (c in consonants) combos.add(c + diac)
                    } else {
                        // consonant selected: show consonant combined with every vowel (including base consonant for अ)
                        for (v in vowels) {
                            val diac = vowelDiacritics[v] ?: ""
                            combos.add(letter + diac)
                        }
                    }
                }

                // layout combos in rows; reduce size for many combos
                Column(modifier = Modifier.fillMaxWidth()) {
                    // show combos in a simple wrapping layout using rows of fixed count
                    val perRow = 4
                    combos.chunked(perRow).forEach { rowItems ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (c in rowItems) {
                                GradientCard(title = c, subtitle = null, colorStart = Color(0xFFffd54f), colorEnd = Color(0xFFffb300), width = 72.dp, height = 56.dp) {
                                    onListen("tts:${c}")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
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
                HorizontalDivider()
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
                                    .height(72.dp)
                                    .padding(vertical = 4.dp), shape = RoundedCornerShape(8.dp)) {
                                    Row(modifier = Modifier.fillMaxSize().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = emoji, fontSize = 16.sp)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = word, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HimaPrimary)
                                            }
                                            if (meaning.isNotEmpty()) Text(text = meaning, fontSize = 12.sp, color = Color(0xFF0B8457))
                                            if (sentence.isNotEmpty()) Text(text = sentence, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
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
                                        }, shape = RoundedCornerShape(50), content = { Text("🔊") }, colors = ButtonDefaults.buttonColors(containerColor = HimaPrimary.copy(alpha = 0.08f)))
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
        // Use mutable state so Try New Word can re-select a target
        val pool = remember { examples.toMutableList() }
        var targetIndex by remember { mutableStateOf(0) }
        var target by remember { mutableStateOf<org.json.JSONObject?>(null) }
        val candidates = remember { mutableStateListOf<org.json.JSONObject>() }

        fun pickTargetAndCandidates() {
            if (pool.isEmpty()) return
            // pick a random target from displayedExamples if available, else from pool
            val t = if (displayedExamples.isNotEmpty()) displayedExamples.random() else pool.random()
            target = t
            candidates.clear()
            candidates.add(t)
            val others = (pool.filter { it != t }).shuffled().take(3)
            candidates.addAll(others)
            candidates.shuffle()
        }

        LaunchedEffect(showExercise) {
            pickTargetAndCandidates()
            // play the target audio automatically
            target?.let { tgt ->
                val audio = tgt.optString("audio", "")
                val parts = listOf(tgt.optString("word", ""), tgt.optString("meaning", ""), tgt.optString("sentence", "")).filter { it.isNotBlank() }
                val text = parts.joinToString(". ")
                if (audio.isNotEmpty()) onListen("asset:${audio}|fallback:${text}") else onListen("tts:${text}")
            }
        }

        var selectedIndex by remember { mutableStateOf(-1) }

        AlertDialog(onDismissRequest = { showExercise = false }, title = { Text("Identify the word") }, text = {
            Column {
                Text("Listen to the word and choose the correct option:")
                Spacer(modifier = Modifier.height(12.dp))
                for ((i, c) in candidates.withIndex()) {
                    val w = c.optString("word", "")
                    // highlight selected candidate
                    val isSelected = (i == selectedIndex)
                    val bg = if (isSelected) HimaPrimary.copy(alpha = 0.12f) else Color.Transparent
                    Button(onClick = { selectedIndex = i }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = ButtonDefaults.buttonColors(containerColor = bg)) {
                        Text(w, color = HimaPrimary)
                    }
                }
            }
        }, confirmButton = {
            Column {
                Row {
                    Button(onClick = {
                        if (selectedIndex >= 0) {
                            val chosen = candidates[selectedIndex]
                                if (chosen == target) {
                                // award star and praise
                                progressStore.addStar(letter)
                                Toast.makeText(ctx, "Correct! Star awarded.", Toast.LENGTH_SHORT).show()
                                // localized appreciation pool
                                val lang = settingsStore.getLanguage()
                                // praise pools: Pair(Devanagari, transliteration)
                                val praisePool = if (lang == "marathi") listOf(
                                    Pair("छान!", "Chaan!"),
                                    Pair("खूप छान!", "Khoop chaan!"),
                                    Pair("शाबाश!", "Shabash!"),
                                    Pair("अतिशय छान!", "Atishay chaan!"),
                                    Pair("खूप छान प्रयत्न!", "Khoop chaan prayatn!"),
                                    Pair("उत्तम!", "Uttam!"),
                                    Pair("अभिनंदन!", "Abhinandan!"),
                                    Pair("अगदी बरोबर!", "Agadi barobar!")
                                ) else {
                                    // Hindi praise pool with intensity variants and optional mascot audio triggers
                                    val base = listOf(
                                        Pair("बहुत बढ़िया!", "Bahut badhiya!"),
                                        Pair("शाबाश!", "Shabash!"),
                                        Pair("अच्छा किया!", "Accha kiya!"),
                                        Pair("बहुत अच्छा!", "Bahut accha!")
                                    )
                                    // intensity variants: gentle, normal, excited
                                    val intense = base.map { Pair(it.first + " 🎉", it.second + " (great)") }
                                    val gentle = base.map { Pair(it.first.replace("!", "."), it.second.replace("!", ".")) }
                                    val all = gentle + base + intense
                                    val pick = all.random()
                                    val praise = pick
                                    // optional mascot audio placeholder: call onListen with a mascot asset id (developer can replace with real file)
                                    val mascotAudio = listOf("asset:audio/mascot/cheer1.mp3", "asset:audio/mascot/clap1.mp3")
                                    val useMascot = (0..1).random() == 0
                                    if (useMascot) {
                                        // try to play mascot audio first (NavGraph will fall back to TTS if asset not present)
                                        onListen(mascotAudio.random())
                                    }
                                    onListen("tts:${praise.first}")
                                    Toast.makeText(ctx, praise.second, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(ctx, "Try again.", Toast.LENGTH_SHORT).show()
                                val lang = settingsStore.getLanguage()
                                val tryAgain = if (lang == "marathi") "पुन्हा प्रयत्न करा" else "फिर कोशिश करो"
                                onListen("tts:${tryAgain}")
                            }
                            showExercise = false
                        }
                    }) { Text("Submit") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        // Try a new word: pick new target and replay
                        pickTargetAndCandidates()
                        selectedIndex = -1
                        target?.let { tgt ->
                            val audio = tgt.optString("audio", "")
                            val parts = listOf(tgt.optString("word", ""), tgt.optString("meaning", ""), tgt.optString("sentence", "")).filter { it.isNotBlank() }
                            val text = parts.joinToString(". ")
                            if (audio.isNotEmpty()) onListen("asset:${audio}|fallback:${text}") else onListen("tts:${text}")
                        }
                    }) { Text("Try New Word") }
                }
            }
        }, dismissButton = { Button(onClick = { showExercise = false }) { Text("Cancel") } })
    }
    }
}
