package tj.soni.englishgame.screens

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
import tj.soni.englishgame.games.FindWordGame
import tj.soni.englishgame.games.GameWordBank

@Composable
fun EscapeRoomScreen(onBack: () -> Unit, onXp: (Int) -> Unit) {
    val questions = remember {
        FindWordGame.createQuestions(GameWordBank.getAllWords(), 5)
    }
    var door by remember { mutableIntStateOf(0) }
    var solved by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val purple = Color(0xFF9B72FF)
    val white = Color(0xFFF8F6FF)
    val mint = Color(0xFF54E3B3)

    Column(
        Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onBack) { Text("← Бозгашт") }
            Text("🚪 ФИРОР АЗ ҲУҶРА", color = purple, fontWeight = FontWeight.Black)
        }

        Text("⭐ $score XP", color = mint, fontSize = 18.sp)

        if (finished || questions.isEmpty()) {
            Spacer(Modifier.height(35.dp))
            Text("🏆 ТУ ФИРОР КАРДӢ!", color = mint, fontSize = 27.sp, fontWeight = FontWeight.Black)
            Text("Ҳамаи 5 дарро кушодӣ!", color = white, fontSize = 20.sp)
            Button(onClick = onBack) { Text("Ба маркази бозиҳо") }
        } else {
            Text("🚪 Дари ${door + 1} аз 5", color = white, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { door / 5f },
                modifier = Modifier.fillMaxWidth(),
                color = purple
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF282044)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("🔐 Барои кушодани дар ҷавоб деҳ:", color = Color.LightGray)
                    Text(
                        questions[door].question,
                        color = white,
                        fontSize = 29.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            questions[door].options.forEach { option ->
                Button(
                    onClick = {
                        if (!solved && !failed) {
                            if (option == questions[door].correctAnswer) {
                                solved = true
                                score += 15
                                onXp(15)
                            } else {
                                failed = true
                            }
                        }
                    },
                    enabled = !solved && !failed,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34294F))
                ) {
                    Text(option, color = white, fontSize = 18.sp)
                }
            }

            if (solved) {
                Text("🔓 ДАР КУШОДА ШУД! +15 XP", color = mint, fontSize = 19.sp)
                Button(
                    onClick = {
                        door++
                        solved = false
                        failed = false
                        if (door >= questions.size) finished = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (door == 4) "Фирор аз ҳуҷра!" else "Дари нав →")
                }
            }

            if (failed) {
                Text("❌ Ҷавоб нодуруст! Бори дигар кӯшиш кун.", color = Color.Red)
                Button(onClick = { failed = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("Аз нав кӯшиш кун")
                }
            }
        }
    }
}
