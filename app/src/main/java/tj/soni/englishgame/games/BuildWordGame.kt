package tj.soni.englishgame.games

import tj.soni.englishgame.data.Word

data class BuildWordQuestion(
    val word: Word,
    val letters: List<Char>
)

object BuildWordGame {

    fun createQuestions(
        words: List<Word>,
        count: Int = 10
    ): List<BuildWordQuestion> {
        return words
            .filter { it.english.length in 2..12 }
            .distinctBy { it.id }
            .shuffled()
            .take(count)
            .map { word ->
                BuildWordQuestion(
                    word = word,
                    letters = word.english.uppercase().toList().shuffled()
                )
            }
    }
}
