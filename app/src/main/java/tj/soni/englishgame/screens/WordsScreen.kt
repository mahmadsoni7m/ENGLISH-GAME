package tj.soni.englishgame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tj.soni.englishgame.data.Category
import tj.soni.englishgame.data.Word

@Composable
fun WordsScreen(
    category: Category,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Бозгашт")
        }

        Text(
            text = "${category.emoji} ${category.title}",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "${category.words.size} калима",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        androidx.compose.foundation.lazy.LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(category.words.size) { index ->
                val word = category.words[index]
                WordCard(word = word)
            }
        }
    }
}

@Composable
private fun WordCard(word: Word) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = word.english,
                style = MaterialTheme.typography.titleLarge
            )

            Text("🇹🇯 ${word.tajik}")

            Text(
                text = "Талаффуз: ${word.pronunciation}",
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider()

            Text("🇬🇧 ${word.sentenceEn}")
            Text("🇹🇯 ${word.sentenceTg}")
        }
    }
}
