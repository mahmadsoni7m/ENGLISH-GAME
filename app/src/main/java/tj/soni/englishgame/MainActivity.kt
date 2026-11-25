package tj.soni.englishgame
import tj.soni.englishgame.data.LearnedWordsStore
import tj.soni.englishgame.data.WordCatalog
import tj.soni.englishgame.screens.LessonsScreen
import tj.soni.englishgame.screens.WordsScreen
import tj.soni.englishgame.data.Category

import tj.soni.englishgame.screens.FindWordScreen
import tj.soni.englishgame.screens.BuildWordScreen
import tj.soni.englishgame.screens.WordHuntScreen

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
    var selectedGame by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var completed by remember {
        mutableStateOf(prefs.getStringSet("completed_lessons", emptySet())?.toSet() ?: emptySet())
    }

    var selectedLesson by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var stage by remember { mutableStateOf("list") }
    var quizIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    var lastBackPress by remember { mutableLongStateOf(0L) }

    BackHandler {
        when {
            selectedGame != 0 -> {
                selectedGame = 0
            }
            selectedCategory != null -> {
                selectedCategory = null
            }
            stage != "list" -> {
                stage = "list"
            }
            tab != 0 -> {
                tab = 0
            }
            else -> {
                val now = System.currentTimeMillis()
                if (now - lastBackPress < 2000L) {
                    (context as? Activity)?.finish()
                } else {
                    lastBackPress = now
                    Toast.makeText(
                        context,
                        "Барои баромадан бори дигар Назад-ро пахш кун",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                    listOf("Асосӣ", "Дарсҳо", "Бозиҳо", "Рейтинг").forEachIndexed { i, name ->
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
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (tab) {
                    0 -> {
                        Text("ZABONIQ 🇹🇯", color = Purple, fontWeight = FontWeight.Bold)
                        Text("Салом! 👋", color = White, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                        Text("Ҳар рӯз каме англисӣ омӯз!", color = Muted)

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Ҷустуҷӯи калима ё ҷумла...") },
                            singleLine = true
                        )

                        if (searchQuery.isNotBlank()) {
                            val query = searchQuery.trim()
                            val results = WordCatalog.categories.flatMap { category ->
                                category.words.filter { word ->
                                    listOf(
                                        word.english,
                                        word.tajik,
                                        word.pronunciation,
                                        word.sentenceEn,
                                        word.sentenceTg
                                    ).any { it.contains(query, ignoreCase = true) }
                                }.map { word -> category to word }
                            }

                            Text("Натиҷаҳо: ${results.size}", color = Mint)

                            results.forEach { (category, word) ->
                                GameCard {
                                    Text("${word.english} — ${word.tajik}", color = White, fontSize = 18.sp)
                                    Text("Талаффуз: ${word.pronunciation}", color = Muted)
                                    Text(word.sentenceEn, color = White)
                                    Text(word.sentenceTg, color = Mint)
                                    Text("${category.emoji} ${category.title}", color = Purple)
                                    TextButton(onClick = {
                                        selectedCategory = category
                                        tab = 1
                                        searchQuery = ""
                                    }) {
                                        Text("Кушодани категория →")
                                    }
                                }
                            }
                        }

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
                        InfoCard("🏆", "Дастовардҳо", "XP ва сатҳҳо")
                    }

                    1 -> {
                        val category = selectedCategory
                        if (category == null) {
                            LessonsScreen(
                                onCategoryClick = { selectedCategory = it }
                            )
                        } else {
                            WordsScreen(
                                category = category,
                                onBack = { selectedCategory = null }
                            )
                        }
                    }

                    2 -> {
                        when (selectedGame) {
                            1 -> FindWordScreen(
                                onBack = { selectedGame = 0 }
                            )

                            2 -> BuildWordScreen(
                                onBack = { selectedGame = 0 }
                            )
                            3 -> WordHuntScreen(
                                onBack = { selectedGame = 0 },
                                onXp = { earned ->
                                    xp += earned
                                    prefs.edit().putInt("xp", xp).apply()
                                }
                            )

                            else -> {
                                Text(
                                    "🎮 МАРКАЗИ БОЗИҲО",
                                    color = White,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    "Бозии дӯстдоштаатро интихоб кун!",
                                    color = Muted
                                )

                                GameCard {
                                    Text("🔍", fontSize = 42.sp)
                                    Text(
                                        "Калимаёбака",
                                        color = White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Тарҷумаи дурусти калимаро ёб!",
                                        color = Muted
                                    )
                                    Button(
                                        onClick = { selectedGame = 1 },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("▶ Бозиро оғоз кун")
                                    }
                                }

                                GameCard {
                                    Text("🧩", fontSize = 42.sp)
                                    Text(
                                        "Ҳарфҳоро ҷамъ кун",
                                        color = White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Аз ҳарфҳои пароканда калима соз!",
                                        color = Muted
                                    )
                                    Button(
                                        onClick = { selectedGame = 2 },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Purple
                                        )
                                    ) {
                                        Text("▶ Бозиро оғоз кун")
                                    }
                                }

                                GameCard {
                                    Text("⚡", fontSize = 42.sp)
                                    Text("Калимашикор", color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Text("Дар 60 сония ҳарфҳоро ҷамъ кун!", color = Muted)
                                    Button(
                                        onClick = { selectedGame = 3 },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("▶ Бозиро оғоз кун")
                                    }
                                }

                            }
                        }
                    }

                    else -> {
                        val learnedCount = LearnedWordsStore.getLearnedIds(context).size
                        val totalWords = WordCatalog.allWords.size
                        val remaining = (totalWords - learnedCount).coerceAtLeast(0)

                        Text("🏆 Рейтинги ман", color = White, fontSize = 25.sp)
                        GameCard {
                            Text("🧑‍🎓", fontSize = 55.sp)
                            Text("Сатҳи ${xp / 100 + 1}", color = Purple, fontSize = 25.sp)
                            Text("$xp XP", color = Mint, fontSize = 22.sp)
                            LinearProgressIndicator(
                                progress = { (xp % 100) / 100f },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text("${xp % 100} / 100 XP то сатҳи нав", color = Muted)
                        }

                        GameCard {
                            Text("📚 Пешрафти калимаҳо", color = White, fontSize = 19.sp)
                            Text("✓ Азёдшуда: $learnedCount", color = Mint)
                            Text("📖 Боқимонда: $remaining", color = Muted)
                            Text("Ҳамагӣ: $totalWords калима", color = White)
                            LinearProgressIndicator(
                                progress = {
                                    if (totalWords == 0) 0f
                                    else learnedCount.toFloat().div(totalWords).coerceIn(0f, 1f)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        GameCard {
                            Text("🎯 Дарсҳо", color = White, fontSize = 19.sp)
                            Text("${completed.size} дарси кӯҳна анҷом ёфт", color = Muted)
                            Text("19 категорияи омӯзишӣ", color = Mint)
                        }

                        Text("ZABONIQ • VERSION 2.1", color = Muted)
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
