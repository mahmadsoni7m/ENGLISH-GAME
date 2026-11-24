package tj.soni.englishgame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tj.soni.englishgame.data.Word
import tj.soni.englishgame.games.FindWordGame
import tj.soni.englishgame.games.GameWordBank

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
    var mistakes by remember { mutableStateOf(0) }

    val finished = currentIndex >= questions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Бозгашт")
        }

        Text(
            text = "🎮 Калимаи дурустро ёб",
            style = MaterialTheme.typography.headlineSmall
        )

        if (finished) {
            Text(
                text = "🏆 Бозӣ анҷом ёфт!",
                style = MaterialTheme.typography.headlineMedium
            )

            Text("Ҷавобҳои дуруст: $score аз ${questions.size}")
            Text("Холҳои XP: $xp")
            Text("Ҷавобҳои нодуруст: $mistakes")

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ба меню баргаштан")
            }
        } else {
            val question = questions[currentIndex]

            Text("Саволи ${currentIndex + 1} аз ${questions.size}")

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / questions.size },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "⭐ XP: $xp",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (question.englishToTajik)
                    "Тарҷумаи ин калимаро ёб:"
                else
                    "Калимаи англисии инро ёб:",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = question.question,
                style = MaterialTheme.typography.headlineLarge
            )

            question.options.forEach { option ->
                val isCorrect = option == question.correctAnswer
                val isSelected = option == selectedAnswer

                Button(
                    onClick = {
                        if (selectedAnswer == null) {
                            selectedAnswer = option

                            if (isCorrect) {
                                score++
                                xp += 10
                            } else {
                                mistakes++
                                onWrongAnswer(question.word)
                            }
                        }
                    },
                    enabled = selectedAnswer == null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            selectedAnswer != null && isCorrect ->
                                MaterialTheme.colorScheme.primary
                            selectedAnswer != null && isSelected ->
                                MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        },
                        contentColor = when {
                            selectedAnswer != null && (isCorrect || isSelected) ->
                                MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                ) {
                    Text(option)
                }
            }

            if (selectedAnswer != null) {
                Text(
                    text = if (selectedAnswer == question.correctAnswer)
                        "✅ Офарин! Ҷавоби дуруст!"
                    else
                        "❌ Ҷавоби дуруст: ${question.correctAnswer}"
                )

                Button(
                    onClick = {
                        currentIndex++
                        selectedAnswer = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (currentIndex == questions.lastIndex)
                            "Натиҷа"
                        else
                            "Саволи навбатӣ →"
                    )
                }
            }
        }
    }
}
