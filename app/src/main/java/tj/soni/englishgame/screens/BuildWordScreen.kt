package tj.soni.englishgame.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import tj.soni.englishgame.data.Word
import tj.soni.englishgame.games.BuildWordGame
import tj.soni.englishgame.games.GameWordBank

private val BuildBg = Color(0xFF090D35)
private val BuildPurple = Color(0xFF713BFF)
private val BuildBlue = Color(0xFF1769FF)
private val BuildGreen = Color(0xFF00D957)
private val BuildRed = Color(0xFFFF2855)
private val BuildYellow = Color(0xFFFFD52A)

@Composable
fun BuildWordScreen(onBack: () -> Unit) {
    val questions = remember {
        BuildWordGame.createQuestions(GameWordBank.getAllWords(), 10)
    }

    var index by remember { mutableIntStateOf(0) }
    var xp by remember { mutableIntStateOf(0) }
    var status by remember { mutableStateOf<Boolean?>(null) }

    val selectedIndices = remember { mutableStateListOf<Int>() }
    val correctWords = remember { mutableStateListOf<Word>() }
    val wrongWords = remember { mutableStateListOf<Word>() }

    val finished = index >= questions.size

    LaunchedEffect(status) {
        if (status != null) {
            delay(1100)
            index++
            selectedIndices.clear()
            status = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = BuildPurple)
            ) {
                Text("← Бозгашт")
            }

            Text(
                "🧩 ҲАРФҲОРО ҶАМЪ КУН",
                color = Color.White,
                fontWeight = FontWeight.Black
            )

            Text("⭐ $xp", color = BuildYellow, fontWeight = FontWeight.Bold)
        }

        if (finished) {
            Text(
                "🏆 НАТИҶАИ БОЗӢ",
                color = BuildYellow,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = BuildPurple.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "⭐ $xp XP",
                        color = BuildYellow,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text("✅ Дуруст: ${correctWords.size}", color = BuildGreen, fontSize = 19.sp)
                    Text("❌ Нодуруст: ${wrongWords.size}", color = BuildRed, fontSize = 19.sp)
                    Text(
                        "🎯 Натиҷа: ${if (questions.isEmpty()) 0 else correctWords.size * 100 / questions.size}%",
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
            }

            Text("✅ Калимаҳои дуруст", color = BuildGreen, fontSize = 20.sp)
            correctWords.forEach {
                Text("${it.english} — ${it.tajik}", color = Color.White)
            }

            Text("❌ Калимаҳои нодуруст", color = BuildRed, fontSize = 20.sp)
            wrongWords.forEach {
                Text("${it.english} — ${it.tajik}", color = Color.White)
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🏠 Ба бозиҳо баргаштан")
            }
        } else {
            val question = questions[index]
            val answer = question.word.english.uppercase()

            val selectedAnswer = selectedIndices.joinToString("") { letterIndex ->
                question.letters[letterIndex].toString()
            }

            fun chooseLetter(letterIndex: Int) {
                if (status != null || letterIndex in selectedIndices) return

                selectedIndices.add(letterIndex)

                val newAnswer = selectedIndices.joinToString("") {
                    question.letters[it].toString()
                }

                if (selectedIndices.size == answer.length) {
                    val isCorrect = newAnswer == answer
                    status = isCorrect

                    if (isCorrect) {
                        xp += 10
                        correctWords.add(question.word)
                    } else {
                        wrongWords.add(question.word)
                    }
                }
            }

            Text(
                "САВОЛИ ${index + 1} / ${questions.size}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { (index + 1).toFloat() / questions.size.coerceAtLeast(1) },
                modifier = Modifier.fillMaxWidth(),
                color = BuildYellow
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = BuildPurple.copy(alpha = 0.35f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "КАЛИМАИ АНГЛИСИРО СОЗ",
                        color = BuildYellow,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        question.word.tajik,
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "Тарҷумаи англисиро аз ҳарфҳо соз",
                        color = Color.LightGray
                    )
                }
            }

            Text("ҶАВОБИ ТУ", color = Color.White, fontWeight = FontWeight.Bold)

            Text(
                selectedAnswer.toCharArray().joinToString(" ").ifEmpty {
                    "＿ ".repeat(answer.length)
                },
                modifier = Modifier.fillMaxWidth(),
                color = when (status) {
                    true -> BuildGreen
                    false -> BuildRed
                    null -> BuildYellow
                },
                fontSize = 27.sp,
                fontWeight = FontWeight.Black
            )

            val letterColors = listOf(
                BuildBlue,
                BuildPurple,
                Color(0xFFB33BFF),
                Color(0xFF087F9C)
            )

            // Ҳар тугма бо индекси дақиқи ҳарфи худаш пайваст аст.
            question.letters.withIndex().toList().chunked(4).forEach { letterRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    letterRow.forEach { entry ->
                        val letterIndex = entry.index
                        val letter = entry.value
                        val isUsed = letterIndex in selectedIndices

                        Button(
                            onClick = { chooseLetter(letterIndex) },
                            enabled = status == null && !isUsed,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = letterColors[letterIndex % letterColors.size],
                                disabledContainerColor = if (isUsed) {
                                    Color.DarkGray
                                } else {
                                    letterColors[letterIndex % letterColors.size]
                                }
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                letter.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    repeat(4 - letterRow.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { selectedIndices.clear() },
                    enabled = status == null && selectedIndices.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("↶ Пок кардан")
                }

                Button(
                    onClick = { selectedIndices.clear() },
                    enabled = status == null && selectedIndices.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BuildBlue)
                ) {
                    Text("Тоза кардан")
                }
            }

            if (status != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (status == true) BuildGreen else BuildRed
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            if (status == true) {
                                "🎉 ОФАРИН! +10 XP"
                            } else {
                                "❌ ХАТО! Ҷавоби дуруст: $answer"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            "Саволи навбатӣ худкор оғоз мешавад...",
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                "⭐ $xp XP     ✅ ${correctWords.size}     ❌ ${wrongWords.size}",
                color = BuildYellow,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
