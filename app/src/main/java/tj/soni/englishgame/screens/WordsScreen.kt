package tj.soni.englishgame.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import tj.soni.englishgame.data.Category
import tj.soni.englishgame.data.Word
import tj.soni.englishgame.data.LearnedWordsStore

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun WordsScreen(
    category: Category,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var learnedIds by remember(category.id) {
        mutableStateOf(LearnedWordsStore.getLearnedIds(context))
    }

    val orderedWords = remember(category, learnedIds) {
        category.words.sortedBy { it.id in learnedIds }
    }

    val learnedCount = category.words.count { it.id in learnedIds }
    val remainingCount = category.words.size - learnedCount

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
            text = "Азёдшуда: $learnedCount • Боқимонда: $remainingCount",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orderedWords, key = { it.id }) { word ->
                val isLearned = word.id in learnedIds

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {},
                            onDoubleClick = {
                                learnedIds = LearnedWordsStore.toggle(
                                    context,
                                    word.id
                                )
                            }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isLearned)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    WordCard(
                        word = word,
                        isLearned = isLearned
                    )
                }
            }
        }
    }
}

@Composable
private fun WordCard(
    word: Word,
    isLearned: Boolean
) {
    val decoration = if (isLearned)
        TextDecoration.LineThrough
    else
        TextDecoration.None

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = word.english,
            style = MaterialTheme.typography.titleLarge,
            textDecoration = decoration
        )

        Text(
            text = "🇹🇯 ${word.tajik}",
            textDecoration = decoration
        )

        Text(
            text = "Талаффуз: ${word.pronunciation}",
            style = MaterialTheme.typography.bodyMedium,
            textDecoration = decoration
        )

        HorizontalDivider()

        Text(
            text = "🇬🇧 ${word.sentenceEn}",
            textDecoration = decoration
        )

        Text(
            text = "🇹🇯 ${word.sentenceTg}",
            textDecoration = decoration
        )

        if (isLearned) {
            Text(
                text = "✓ Азёд шудааст",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            Text(
                text = "Ду бор пахш кун — азёд шуд",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
