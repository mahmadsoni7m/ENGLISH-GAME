package tj.soni.englishgame.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import tj.soni.englishgame.games.FindWordGame
import tj.soni.englishgame.games.GameWordBank

private val ArcadeBg = Color(0xFF090D35)
private val ArcadeBlue = Color(0xFF1769FF)
private val ArcadePurple = Color(0xFF713BFF)
private val ArcadeGreen = Color(0xFF00D957)
private val ArcadeRed = Color(0xFFFF2855)
private val ArcadeYellow = Color(0xFFFFD52A)

@Composable
fun FindWordScreen(
    onBack: () -> Unit,
    onWrongAnswer: (Word) -> Unit = {}
) {
    val questions = remember {
        FindWordGame.createQuestions(GameWordBank.getAllWords(), 10)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var xp by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    val correctWords = remember { mutableStateListOf<Word>() }
    val wrongWords = remember { mutableStateListOf<Word>() }

    val finished = currentIndex >= questions.size

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer != null) {
            delay(1000)
            currentIndex++
            selectedAnswer = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArcadeBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ArcadeButton("←", onBack)
            Text(
                "🎮 КАЛИМАЁБАКА",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Black
            )
            Text("⭐ $xp", color = ArcadeYellow, fontWeight = FontWeight.Bold)
        }

        if (finished) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "🏆 НАТИҶАИ БОЗӢ!",
                        color = ArcadeYellow,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )
                    ArcadeCard(ArcadePurple) {
                        Text(
                            "🎯 ${score * 10} XP",
                            color = ArcadeYellow,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "Дуруст: ${correctWords.size}  •  Нодуруст: ${wrongWords.size}",
                            color = Color.White,
                            fontSize = 17.sp
                        )
                        Text(
                            "Натиҷа: ${score * 10}%",
                            color = ArcadeGreen,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text("✅ Ҷавобҳои дуруст", color = ArcadeGreen, fontSize = 20.sp)
                }

                items(correctWords) { word ->
                    ResultWordCard(word, true)
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    Text("❌ Ҷавобҳои нодуруст", color = ArcadeRed, fontSize = 20.sp)
                }

                items(wrongWords) { word ->
                    ResultWordCard(word, false)
                }

                item {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ArcadePurple
                        )
                    ) {
                        Text("🏠 Ба меню баргаштан")
                    }
                }
            }
        } else {
            val question = questions[currentIndex]

            Text(
                "САВОЛИ ${currentIndex + 1} / ${questions.size}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size },
                modifier = Modifier.fillMaxWidth(),
                color = ArcadeYellow,
                trackColor = Color(0xFF26336B)
            )

            ArcadeCard(ArcadePurple) {
                Text(
                    if (question.englishToTajik)
                        "ТАРҶУМАИ КАЛИМАРО ЁБ"
                    else
                        "КАЛИМАИ АНГЛИСИРО ЁБ",
                    color = ArcadeYellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    question.question,
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )
            }

            question.options.forEach { option ->
                val isCorrect = option == question.correctAnswer
                val isSelected = option == selectedAnswer

                val buttonColor = when {
                    selectedAnswer == null -> ArcadeBlue
                    isCorrect -> ArcadeGreen
                    isSelected -> ArcadeRed
                    else -> Color(0xFF18245C)
                }

                Button(
                    onClick = {
                        if (selectedAnswer == null) {
                            selectedAnswer = option

                            if (isCorrect) {
                                score++
                                xp += 10
                                correctWords.add(question.word)
                            } else {
                                wrongWords.add(question.word)
                                onWrongAnswer(question.word)
                            }
                        }
                    },
                    enabled = selectedAnswer == null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        disabledContainerColor = buttonColor,
                        contentColor = Color.White,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(
                        option + when {
                            selectedAnswer != null && isCorrect -> "  ✓"
                            isSelected && !isCorrect -> "  ✕"
                            else -> ""
                        },
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (selectedAnswer != null) {
                val correct = selectedAnswer == question.correctAnswer

                ArcadeCard(if (correct) ArcadeGreen else ArcadeRed) {
                    Text(
                        if (correct) "✅ ОФАРИН! ДУРУСТ!"
                        else "❌ ХАТО! Ҷавоби дуруст: ${question.correctAnswer}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "Калимаи навбатӣ ба таври худкор...",
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                "⭐ $xp XP     ✅ $score дуруст     ❌ ${wrongWords.size} хато",
                color = ArcadeYellow,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ArcadeCard(
    color: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.30f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun ArcadeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ArcadePurple),
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 8.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
private fun ResultWordCard(
    word: Word,
    correct: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (correct)
                Color(0xFF075B36)
            else
                Color(0xFF70152F)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "${if (correct) "✓" else "✕"} ${word.english} — ${word.tajik}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                word.sentenceEn,
                color = Color.White.copy(alpha = 0.85f)
            )
            Text(
                word.sentenceTg,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}
