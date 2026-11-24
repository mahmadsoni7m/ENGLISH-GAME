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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF0C0B16)
private val Panel = Color(0xFF19172A)
private val Purple = Color(0xFF9B72FF)
private val Mint = Color(0xFF54E3B3)
private val White = Color(0xFFF8F6FF)
private val Muted = Color(0xFFAAA5BE)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EnglishGameApp() }
    }
}

@Composable
fun EnglishGameApp() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("english_game", android.content.Context.MODE_PRIVATE)
    }
    var xp by remember { mutableIntStateOf(prefs.getInt("xp", 0)) }
    var completed by remember { mutableIntStateOf(prefs.getInt("completed", 0)) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var quizAnswer by remember { mutableStateOf("") }
    var answered by remember { mutableStateOf(false) }

    fun earnXP(points: Int) {
        xp += points
        completed += 1
        prefs.edit().putInt("xp", xp).putInt("completed", completed).apply()
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Background,
            surface = Panel,
            primary = Purple,
            secondary = Mint,
            onBackground = White,
            onSurface = White
        )
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    val tabs = listOf("Асосӣ", "Дарсҳо", "Бозӣ", "Профил")
                    val symbols = listOf("⌂", "▤", "⚡", "♙")
                    tabs.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Text(symbols[index], fontSize = 20.sp) },
                            label = { Text(label, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Purple,
                                selectedTextColor = Purple,
                                indicatorColor = Color(0xFF30264B),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeScreen(
                    xp = xp,
                    completed = completed,
                    onStart = { selectedTab = 1 },
                    modifier = Modifier.padding(padding)
                )
                1 -> LessonsScreen(
                    completed = completed,
                    onComplete = { earnXP(20) },
                    modifier = Modifier.padding(padding)
                )
                2 -> GameScreen(
                    answered = answered,
                    answer = quizAnswer,
                    onAnswer = {
                        quizAnswer = it
                        answered = true
                    },
                    onNext = {
                        if (answered && quizAnswer == "Apple") earnXP(10)
                        quizAnswer = ""
                        answered = false
                    },
                    modifier = Modifier.padding(padding)
                )
                else -> ProfileScreen(
                    xp = xp,
                    completed = completed,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun PageColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
fun HomeScreen(xp: Int, completed: Int, onStart: () -> Unit, modifier: Modifier = Modifier) {
    PageColumn(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("ENGLISH GAME", color = Purple, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Салом! 👋", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Имрӯз англисӣ меомӯзем?", color = Muted, fontSize = 14.sp)
            }
            Text("🇬🇧", fontSize = 38.sp)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF282044)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🚀  Ҳадафи имрӯз", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Ҳар рӯз 5 дақиқа омӯз ва ба сатҳи нав бирас!", color = Muted)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐ $xp XP", color = Mint, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(18.dp))
                    Text("📚 $completed дарс", color = White)
                }
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Оғози омӯзиш  →", color = White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text("Бахшҳои омӯзиш", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        FeatureCard("📖", "Калимаҳои нав", "Калимаҳои зарурии ҳаррӯза", "Омӯзиши калимаҳо")
        FeatureCard("🎯", "Тестҳо", "Дониши худро санҷ", "Бозӣ")
        FeatureCard("🎧", "Гӯш кардан", "Фаҳмидани забони англисӣ", "Ба наздикӣ")
        FeatureCard("🏆", "Дастовардҳо", "XP ҷамъ кун ва сатҳатро баланд бар", "Профил")
    }
}

@Composable
fun FeatureCard(emoji: String, title: String, description: String, action: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 30.sp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, color = Muted, fontSize = 12.sp)
            }
            Text(action, color = Mint, fontSize = 11.sp)
        }
    }
}

@Composable
fun LessonsScreen(completed: Int, onComplete: () -> Unit, modifier: Modifier = Modifier) {
    PageColumn(modifier) {
        Text("Дарсҳо 📚", color = White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Аз сифр оғоз мекунем. Ҳар дарс кӯтоҳ ва осон аст.", color = Muted)
        LessonCard("01", "Салом ва шиносоӣ", "Hello • Hi • Good morning", "Салом • Ассалом • Субҳ ба хайр", completed >= 1, onComplete)
        LessonCard("02", "Одамон", "I • You • He • She", "Ман • Ту • Ӯ • Ӯ", completed >= 2, onComplete)
        LessonCard("03", "Рақамҳо", "One • Two • Three • Four", "Як • Ду • Се • Чор", completed >= 3, onComplete)
        LessonCard("04", "Оила", "Family • Mother • Father", "Оила • Модар • Падар", completed >= 4, onComplete)
        LessonCard("05", "Хӯрок", "Water • Bread • Apple", "Об • Нон • Себ", completed >= 5, onComplete)
    }
}

@Composable
fun LessonCard(
    number: String,
    title: String,
    words: String,
    translation: String,
    done: Boolean,
    onComplete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(number, color = Purple, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(10.dp))
                Text(title, color = White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.weight(1f))
                if (done) Text("✓", color = Mint, fontSize = 20.sp)
            }
            Text(words, color = Mint, fontSize = 14.sp)
            Text(translation, color = Muted, fontSize = 13.sp)
            Button(
                onClick = onComplete,
                enabled = !done,
                colors = ButtonDefaults.buttonColors(containerColor = Purple, disabledContainerColor = Color(0xFF343044)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (done) "Иҷро шуд ✓" else "Гузаштани дарс  +20 XP")
            }
        }
    }
}

@Composable
fun GameScreen(
    answered: Boolean,
    answer: String,
    onAnswer: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    PageColumn(modifier) {
        Text("Бозии имрӯз ⚡", color = White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Калимаи англисиро ёб ва хол гир!", color = Muted)
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF282044)), shape = RoundedCornerShape(22.dp)) {
            Column(
                Modifier.fillMaxWidth().padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("СЕБ", color = Muted, fontSize = 14.sp)
                Text("🍎", fontSize = 64.sp)
                Text("«Себ» ба англисӣ чӣ мешавад?", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        listOf("Book", "Apple", "Water", "House").forEach { option ->
            OutlinedButton(
                onClick = { if (!answered) onAnswer(option) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !answered,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (answered && option == "Apple") Mint else White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(option, fontSize = 17.sp)
            }
        }
        if (answered) {
            Text(
                if (answer == "Apple") "Офарин! Ҷавоби дуруст аст! 🎉" else "Ҷавоби дуруст: Apple 🍎",
                color = if (answer == "Apple") Mint else Color(0xFFFFB86B),
                fontWeight = FontWeight.Bold
            )
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                Text("Саволи навбатӣ")
            }
        }
    }
}

@Composable
fun ProfileScreen(xp: Int, completed: Int, modifier: Modifier = Modifier) {
    PageColumn(modifier) {
        Text("Профили ман 👤", color = White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(22.dp)) {
            Column(
                Modifier.fillMaxWidth().padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("🧑‍🎓", fontSize = 60.sp)
                Text("Омӯзандаи англисӣ", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Сатҳи ${xp / 100 + 1}", color = Purple, fontSize = 18.sp)
                LinearProgressIndicator(
                    progress = (xp % 100) / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = Mint,
                    trackColor = Color(0xFF38334A)
                )
                Text("$xp / ${((xp / 100) + 1) * 100} XP то сатҳи нав", color = Muted)
            }
        }
        FeatureCard("⭐", "Холҳо", "$xp XP ҷамъ шудааст", "XP")
        FeatureCard("📚", "Дарсҳои анҷомшуда", "$completed дарс", "Дарс")
        Text("ENGLISH GAME • VERSION 1.0", color = Muted, fontSize = 11.sp)
    }
}
