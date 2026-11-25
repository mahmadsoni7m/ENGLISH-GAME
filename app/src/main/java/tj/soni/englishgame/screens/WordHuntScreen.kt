package tj.soni.englishgame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import tj.soni.englishgame.games.GameWordBank

@Composable
fun WordHuntScreen(
    onBack: () -> Unit,
    onXp: (Int) -> Unit
) {
    val words = remember {
        GameWordBank.getAllWords()
            .filter { it.english.length in 3..10 && it.english.all(Char::isLetter) }
            .distinctBy { it.id }
            .shuffled()
    }

    var index by remember { mutableIntStateOf(0) }
    var seconds by remember { mutableIntStateOf(60) }
    var score by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf(listOf<Int>()) }
    var solved by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    val word = words.getOrNull(index)
    val letters = remember(index) {
        word?.english?.uppercase()?.toList()?.shuffled() ?: emptyList()
    }
    val answer = selected.mapNotNull { letters.getOrNull(it) }.joinToString("")

    LaunchedEffect(finished) {
        while (!finished && seconds > 0) {
            delay(1000)
            seconds--
        }
        if (seconds == 0) finished = true
    }

    val bg = Color(0xFF0C0B16)
    val purple = Color(0xFF9B72FF)
    val mint = Color(0xFF54E3B3)
    val white = Color(0xFFF8F6FF)

    Column(
        Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Бозгашт") }
            Text("⚡ КАЛИМАШИКОР", color = purple, fontWeight = FontWeight.Black)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("⏱ $seconds сония", color = if (seconds <= 10) Color.Red else white)
            Text("⭐ $score XP", color = mint)
        }

        LinearProgressIndicator(
            progress = { seconds / 60f },
            modifier = Modifier.fillMaxWidth(),
            color = purple
        )

        if (finished || word == null) {
            Spacer(Modifier.height(35.dp))
            Text("🏁 Бозӣ анҷом ёфт!", color = white, fontSize = 27.sp)
            Text("Холи ту: $score XP", color = mint, fontSize = 23.sp)
            Button(onClick = {
                index = 0
                seconds = 60
                score = 0
                selected = emptyList()
                solved = false
                finished = false
            }) { Text("Боз аз нав") }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF282044)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Калимаи тоҷикӣ:", color = Color.LightGray)
                    Text(word.tajik, color = white, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text("Тарҷумаи англисиро ҷамъ кун!", color = Color.LightGray)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF19172A)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(
                    Modifier.fillMaxWidth().padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (answer.isEmpty()) "Калимаи худро соз..." else answer,
                        color = if (solved) mint else white,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(letters) { i, letter ->
                    Button(
                        onClick = {
                            if (!solved && i !in selected) selected = selected + i
                        },
                        enabled = !solved && i !in selected,
                        modifier = Modifier.height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purple,
                            disabledContainerColor = Color(0xFF39334A)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(letter.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (solved) {
                Text("Офарин! +10 XP 🎉", color = mint, fontSize = 20.sp)
                Button(
                    onClick = {
                        index++
                        selected = emptyList()
                        solved = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Калимаи нав →") }
            } else {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = { selected = emptyList() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Пок кардан") }

                    Button(
                        onClick = {
                            if (answer.equals(word.english, ignoreCase = true)) {
                                solved = true
                                score += 10
                                onXp(10)
                            } else {
                                selected = emptyList()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selected.isNotEmpty()
                    ) { Text("Санҷидан") }
                }
            }
        }
    }
}
