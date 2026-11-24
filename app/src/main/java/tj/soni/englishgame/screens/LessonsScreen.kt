package tj.soni.englishgame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tj.soni.englishgame.data.Category
import tj.soni.englishgame.data.WordCatalog

@Composable
fun LessonsScreen(
    onCategoryClick: (Category) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "📚 Дарсҳои англисӣ",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "${WordCatalog.categories.size} категория • ${WordCatalog.allWords.size} калима",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(WordCatalog.categories) { category ->
                Card(
                    onClick = { onCategoryClick(category) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = category.emoji,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = category.englishTitle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${category.words.size} калима",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
