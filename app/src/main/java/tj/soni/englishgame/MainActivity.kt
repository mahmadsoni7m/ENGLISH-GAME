package tj.soni.englishgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF0C0B16)
private val Panel = Color(0xFF19172A)
private val Purple = Color(0xFF9B72FF)
private val Mint = Color(0xFF54E3B3)
private val White = Color(0xFFF8F6FF)
private val Muted = Color(0xFFAAA5BE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GameApp() }
    }
}

@Composable
fun GameApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("game_data", android.content.Context.MODE_PRIVATE)
    }
    var xp by remember { mutableIntStateOf(prefs.getInt("xp", 0)) }
    var lessons by remember { mutableIntStateOf(prefs.getInt("lessons", 0)) }
    var tab by remember { mutableIntStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }

    fun reward(points: Int) {
        xp += points
        lessons += 1
        prefs.edit().putInt("xp", xp).putInt("lessons", lessons).apply()
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Purple,
        secondary = Mint,
        background = Bg,
        surface = Panel
    )) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    listOf("Асосӣ", "Дарсҳо", "Бозӣ", "Профил").forEachIndexed { i, name ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = {
                                Text(
                                    listOf("⌂", "📚", "⚡", "👤")[i],
                                    fontSize = 19.sp
                                )
                            },
                            label = { Text(name, fontSize = 10.sp) }
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier.fillMaxSize()
                    .background(Bg)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (tab) {
                    0 -> {
                        Text("ENGLISH GAME 🇬🇧", color = Purple, fontWeight = FontWeight.Bold)
                        Text("Салом! 👋", color = White, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text("Ҳар рӯз каме англисӣ омӯз!", color = Muted)
                        GameCard {
                            Text("🚀 Ҳадафи имрӯз", color = White, fontSize = 20.sp)
                            Text("5 дақиқа омӯзиш — як қадам ба пеш!", color = Muted)
                            Text("⭐ $xp XP     📚 $lessons дарс", color = Mint)
                            Button(onClick = { tab = 1 }, modifier = Modifier.fillMaxWidth()) {
                                Text("Оғози омӯзиш →")
                            }
                        }
                        Text("Бахшҳои барнома", color = White, fontSize = 20.sp)
                        InfoCard("📖", "Калимаҳои нав", "Калимаҳои ҳаррӯза")
                        InfoCard("🎯", "Тестҳо", "Дониши худро санҷ")
                        InfoCard("🎧", "Гӯш кардан", "Машқи шунавоӣ")
                        InfoCard("🏆", "Дастовардҳо", "XP ва сатҳҳо")
                    }

                    1 -> {
                        Text("Дарсҳои англисӣ 📚", color = White, fontSize = 25.sp)
                        Text("Аз сифр оғоз мекунем.", color = Muted)
                        Lesson("01", "Салом", "Hello — Салом", "Hi — Ассалом", lessons >= 1) { reward(20) }
                        Lesson("02", "Шиносоӣ", "My name — Номи ман", "I am — Ман ҳастам", lessons >= 2) { reward(20) }
                        Lesson("03", "Рақамҳо", "One — Як", "Two — Ду", lessons >= 3) { reward(20) }
                        Lesson("04", "Оила", "Mother — Модар", "Father — Падар", lessons >= 4) { reward(20) }
                        Lesson("05", "Хӯрок", "Apple — Себ", "Bread — Нон", lessons >= 5) { reward(20) }
                    }

                    2 -> {
                        Text("Бозии калимаҳо ⚡", color = White, fontSize = 25.sp)
                        GameCard {
                            Text("🍎", fontSize = 55.sp)
                            Text("«Себ» ба англисӣ чӣ мешавад?", color = White, fontSize = 18.sp)
                        }
                        listOf("Book", "Apple", "Water", "House").forEach { option ->
                            OutlinedButton(
                                onClick = { if (!checked) { answer = option; checked = true } },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !checked
                            ) { Text(option) }
                        }
                        if (checked) {
                            Text(
                                if (answer == "Apple") "Офарин! Ҷавоби дуруст! 🎉"
                                else "Ҷавоби дуруст: Apple",
                                color = Mint
                            )
                            Button(onClick = {
                                if (answer == "Apple") reward(10)
                                answer = ""
                                checked = false
                            }) { Text("Саволи навбатӣ") }
                        }
                    }

                    else -> {
                        Text("Профили ман 👤", color = White, fontSize = 25.sp)
                        GameCard {
                            Text("🧑‍🎓", fontSize = 55.sp)
                            Text("Сатҳи ${xp / 100 + 1}", color = Purple, fontSize = 23.sp)
                            Text("$xp XP", color = Mint, fontSize = 20.sp)
                            LinearProgressIndicator(
                                progress = { (xp % 100) / 100f },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text("$lessons дарс анҷом ёфт", color = Muted)
                        }
                        Text("ENGLISH GAME • VERSION 1.0", color = Muted)
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF282044))
    ) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
fun InfoCard(icon: String, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(17.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 28.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, color = White, fontWeight = FontWeight.Bold)
                Text(description, color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun Lesson(
    number: String,
    title: String,
    line1: String,
    line2: String,
    done: Boolean,
    onComplete: () -> Unit
) {
    GameCard {
        Text("$number  $title", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(line1, color = Mint)
        Text(line2, color = Muted)
        Button(onClick = onComplete, enabled = !done) {
            Text(if (done) "Иҷро шуд ✓" else "Анҷоми дарс +20 XP")
        }
    }
}
