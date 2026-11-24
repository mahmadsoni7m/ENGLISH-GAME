package tj.soni.englishgame

import tj.soni.englishgame.screens.FindWordScreen

import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import java.util.Locale

private val Bg = Color(0xFF0C0B16)
private val Panel = Color(0xFF19172A)
private val Purple = Color(0xFF9B72FF)
private val Mint = Color(0xFF54E3B3)
private val White = Color(0xFFF8F6FF)
private val Muted = Color(0xFFAAA5BE)

data class Word(
    val en: String,
    val tg: String,
    val pronunciation: String,
    val example: String,
    val exampleTg: String
)

data class LessonData(
    val title: String,
    val description: String,
    val words: List<Word>
)

data class QuizQuestion(
    val word: Word,
    val options: List<String>
)

private val lessonsData = listOf(
    LessonData("Салом ва саломдиҳӣ", "Greetings", listOf(
        Word("Hello", "Салом", "Ҳелоу", "Hello, my friend!", "Салом, дӯсти ман!"),
        Word("Hi", "Ассалом", "Ҳай", "Hi, Ali!", "Ассалом, Алӣ!"),
        Word("Good", "Хуб", "Гуд", "I am good.", "Ман хуб ҳастам."),
        Word("Morning", "Субҳ", "Морнинг", "Good morning!", "Субҳ ба хайр!"),
        Word("Evening", "Бегоҳ", "Ивнинг", "Good evening!", "Шом ба хайр!"),
        Word("Night", "Шаб", "Найт", "Good night!", "Шаб ба хайр!"),
        Word("Welcome", "Хуш омадед", "Уэлкам", "Welcome home!", "Ба хона хуш омадед!"),
        Word("Please", "Лутфан", "Плиз", "Please, sit down.", "Лутфан, бишинед."),
        Word("Thanks", "Ташаккур", "Сэнкс", "Thanks a lot!", "Ташаккури зиёд!"),
        Word("Goodbye", "Хайр", "Гудбай", "Goodbye, my friend.", "Хайр, дӯсти ман.")
    )),
    LessonData("Шиносоӣ", "Introduction", listOf(
        Word("Name", "Ном", "Нейм", "My name is Ali.", "Номи ман Алӣ аст."),
        Word("My", "Мани / аз они ман", "Май", "My book is here.", "Китоби ман инҷост."),
        Word("Your", "Ту / аз они ту", "Ёр", "What is your name?", "Номи ту чист?"),
        Word("I", "Ман", "Ай", "I am a student.", "Ман донишҷӯ ҳастам."),
        Word("You", "Ту / шумо", "Ю", "You are my friend.", "Ту дӯсти ман ҳастӣ."),
        Word("Am", "Ҳастам", "Эм", "I am happy.", "Ман хушҳол ҳастам."),
        Word("Are", "Ҳастӣ / ҳастед", "Ар", "You are kind.", "Ту меҳрубон ҳастӣ."),
        Word("Friend", "Дӯст", "Френд", "He is my friend.", "Ӯ дӯсти ман аст."),
        Word("Student", "Донишҷӯ", "Стюдент", "I am a student.", "Ман донишҷӯ ҳастам."),
        Word("Teacher", "Муаллим", "Тичер", "My teacher is good.", "Муаллими ман хуб аст.")
    )),
    LessonData("Рақамҳо", "Numbers", listOf(
        Word("One", "Як", "Уан", "I have one book.", "Ман як китоб дорам."),
        Word("Two", "Ду", "Ту", "I have two pens.", "Ман ду қалам дорам."),
        Word("Three", "Се", "Сри", "Three apples.", "Се себ."),
        Word("Four", "Чор", "Фор", "Four students.", "Чор донишҷӯ."),
        Word("Five", "Панҷ", "Файв", "Five days.", "Панҷ рӯз."),
        Word("Six", "Шаш", "Сикс", "Six books.", "Шаш китоб."),
        Word("Seven", "Ҳафт", "Севен", "Seven days.", "Ҳафт рӯз."),
        Word("Eight", "Ҳашт", "Эйт", "Eight apples.", "Ҳашт себ."),
        Word("Nine", "Нуҳ", "Найн", "Nine people.", "Нуҳ нафар."),
        Word("Ten", "Даҳ", "Тен", "Ten students.", "Даҳ донишҷӯ.")
    )),
    LessonData("Оила", "Family", listOf(
        Word("Family", "Оила", "Фэмили", "I love my family.", "Ман оилаи худро дӯст медорам."),
        Word("Mother", "Модар", "Мазер", "My mother is kind.", "Модари ман меҳрубон аст."),
        Word("Father", "Падар", "Фазер", "My father is strong.", "Падари ман қавӣ аст."),
        Word("Brother", "Бародар", "Бразер", "I have a brother.", "Ман бародар дорам."),
        Word("Sister", "Хоҳар", "Систер", "My sister is happy.", "Хоҳари ман хушҳол аст."),
        Word("Son", "Писар", "Сан", "He is my son.", "Ӯ писари ман аст."),
        Word("Daughter", "Духтар", "Дотер", "She is my daughter.", "Ӯ духтари ман аст."),
        Word("Grandmother", "Бибӣ", "Грэндмазер", "My grandmother is old.", "Бибии ман калонсол аст."),
        Word("Grandfather", "Бобо", "Грэндфазер", "My grandfather is wise.", "Бобои ман доно аст."),
        Word("Baby", "Кӯдак", "Бейби", "The baby is sleeping.", "Кӯдак хоб аст.")
    )),
    LessonData("Хӯрок ва нӯшокӣ", "Food and drinks", listOf(
        Word("Apple", "Себ", "Эпл", "I eat an apple.", "Ман себ мехӯрам."),
        Word("Bread", "Нон", "Бред", "I like bread.", "Ман нонро дӯст медорам."),
        Word("Water", "Об", "Уотер", "I drink water.", "Ман об менӯшам."),
        Word("Milk", "Шир", "Милк", "Milk is white.", "Шир сафед аст."),
        Word("Tea", "Чой", "Ти", "I drink tea.", "Ман чой менӯшам."),
        Word("Rice", "Биринҷ", "Райс", "Rice is good.", "Биринҷ хуб аст."),
        Word("Meat", "Гӯшт", "Мит", "I eat meat.", "Ман гӯшт мехӯрам."),
        Word("Egg", "Тухм", "Эг", "I have an egg.", "Ман як тухм дорам."),
        Word("Sugar", "Шакар", "Шугар", "Tea with sugar.", "Чой бо шакар."),
        Word("Fruit", "Мева", "Фрут", "Fruit is healthy.", "Мева фоидаовар аст.")
    ))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GameApp() }
    }
}

@Composable
fun GameApp() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("game_data", android.content.Context.MODE_PRIVATE)
    }

    var xp by remember { mutableIntStateOf(prefs.getInt("xp", 0)) }
    var tab by remember { mutableIntStateOf(0) }
    var completed by remember {
        mutableStateOf(prefs.getStringSet("completed_lessons", emptySet())?.toSet() ?: emptySet())
    }

    var selectedLesson by remember { mutableIntStateOf(0) }
    var stage by remember { mutableStateOf("list") }
    var quizIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        tts = engine
        onDispose {
            engine.stop()
            engine.shutdown()
            tts = null
        }
    }

    fun speak(word: String) {
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "english_word")
    }

    fun finishLesson() {
        val id = selectedLesson.toString()
        if (id !in completed) {
            val newCompleted = completed + id
            completed = newCompleted
            xp += 20 + quizScore * 5
            prefs.edit()
                .putInt("xp", xp)
                .putStringSet("completed_lessons", newCompleted)
                .apply()
        }
        stage = "result"
    }

    val lesson = lessonsData[selectedLesson]
    val questions = remember(selectedLesson) {
        lesson.words.take(5).map { word ->
            QuizQuestion(
                word,
                (listOf(word.en) +
                    lesson.words.filter { it.en != word.en }.shuffled().take(3).map { it.en }
                ).shuffled()
            )
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            secondary = Mint,
            background = Bg,
            surface = Panel
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    listOf("Асосӣ", "Дарсҳо", "Бозӣ", "Профил").forEachIndexed { i, name ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = {
                                tab = i
                                if (i == 1) stage = "list"
                            },
                            icon = {
                                Text(listOf("⌂", "📚", "⚡", "👤")[i], fontSize = 19.sp)
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
                            Text("10 калима омӯз ва дониши худро санҷ!", color = Muted)
                            Text("⭐ $xp XP     📚 ${completed.size} дарс", color = Mint)
                            Button(onClick = { tab = 1; stage = "list" }, modifier = Modifier.fillMaxWidth()) {
                                Text("Оғози омӯзиш →")
                            }
                        }

                        Text("Бахшҳои барнома", color = White, fontSize = 20.sp)
                        InfoCard("📖", "Калимаҳои нав", "Дарсҳои 10-калимаӣ")
                        InfoCard("🎯", "Тестҳо", "Баъди ҳар дарс 5 савол")
                        InfoCard("🎧", "Талаффуз", "Калимаҳоро гӯш кун")
                        InfoCard("🏆", "Дастовардҳо", "XP ва сатҳҳо")
                    }

                    1 -> {
                        when (stage) {
                            "list" -> {
                                Text("Дарсҳои англисӣ 📚", color = White, fontSize = 25.sp)
                                Text("Ҳар дарс 10 калима ва 5 савол дорад.", color = Muted)

                                lessonsData.forEachIndexed { index, item ->
                                    GameCard {
                                        Text("ДАРС ${index + 1}", color = Purple, fontWeight = FontWeight.Bold)
                                        Text(item.title, color = White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                                        Text(item.description, color = Muted)
                                        Text("10 калима • Тест аз 5 савол", color = Mint)
                                        Button(
                                            onClick = {
                                                selectedLesson = index
                                                quizIndex = 0
                                                quizScore = 0
                                                selectedAnswer = null
                                                stage = "learn"
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(if (index.toString() in completed) "Такрори дарс ↻" else "Оғози дарс →")
                                        }
                                        if (index.toString() in completed) {
                                            Text("Иҷро шуд ✓", color = Mint)
                                        }
                                    }
                                }
                            }

                            "learn" -> {
                                Text("Дарси ${selectedLesson + 1}", color = Purple, fontWeight = FontWeight.Bold)
                                Text(lesson.title, color = White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                                Text("Омӯзиши 10 калима", color = Muted)

                                lesson.words.forEachIndexed { index, word ->
                                    GameCard {
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${index + 1}. ${word.en}", color = White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                                            OutlinedButton(onClick = { speak(word.en) }) {
                                                Text("🔊")
                                            }
                                        }
                                        Text("Тарҷума: ${word.tg}", color = Mint, fontSize = 17.sp)
                                        Text("Талаффуз: ${word.pronunciation}", color = Muted)
                                        Divider(color = Purple.copy(alpha = 0.3f))
                                        Text("Мисол: ${word.example}", color = White)
                                        Text(word.exampleTg, color = Muted)
                                    }
                                }

                                Button(
                                    onClick = {
                                        quizIndex = 0
                                        quizScore = 0
                                        selectedAnswer = null
                                        stage = "quiz"
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Оғози тест →")
                                }
                            }

                            "quiz" -> {
                                val question = questions[quizIndex]
                                Text("ТЕСТ 🎯", color = Purple, fontWeight = FontWeight.Bold)
                                Text("Саволи ${quizIndex + 1} аз 5", color = White, fontSize = 23.sp)
                                LinearProgressIndicator(
                                    progress = { (quizIndex + 1) / 5f },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                GameCard {
                                    Text("«${question.word.tg}» ба англисӣ чӣ мешавад?", color = White, fontSize = 20.sp)
                                    Text("Калимаи дурустро интихоб кун.", color = Muted)
                                }

                                question.options.forEach { option ->
                                    OutlinedButton(
                                        onClick = {
                                            if (selectedAnswer == null) {
                                                selectedAnswer = option
                                                if (option == question.word.en) quizScore++
                                            }
                                        },
                                        enabled = selectedAnswer == null,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(option, fontSize = 17.sp)
                                    }
                                }

                                if (selectedAnswer != null) {
                                    val correct = selectedAnswer == question.word.en
                                    Text(
                                        if (correct) "Офарин! Ҷавоби дуруст! 🎉"
                                        else "Ҷавоби дуруст: ${question.word.en}",
                                        color = if (correct) Mint else Color(0xFFFF8A8A),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Button(
                                        onClick = {
                                            if (quizIndex < 4) {
                                                quizIndex++
                                                selectedAnswer = null
                                            } else {
                                                finishLesson()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(if (quizIndex < 4) "Саволи навбатӣ →" else "Натиҷаи тест")
                                    }
                                }
                            }

                            "result" -> {
                                Text("Офарин! 🏆", color = Mint, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                GameCard {
                                    Text("Дарс ба анҷом расид!", color = White, fontSize = 21.sp)
                                    Text("Натиҷа: $quizScore аз 5 ҷавоби дуруст", color = Mint, fontSize = 18.sp)
                                    Text("Мукофот: ${20 + quizScore * 5} XP", color = Purple, fontSize = 20.sp)
                                    Text("Ҳамагӣ: $xp XP", color = White)
                                }
                                Button(
                                    onClick = { stage = "list" },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Бозгашт ба дарсҳо")
                                }
                            }
                        }
                    }

                    2 -> {
                        FindWordScreen(
                            onBack = { tab = 0 }
                        )
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
                            Text("${completed.size} дарс анҷом ёфт", color = Muted)
                        }
                        Text("ENGLISH GAME • VERSION 1.1", color = Muted)
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
