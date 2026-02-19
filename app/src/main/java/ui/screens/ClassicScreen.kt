package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magicball.data.HistoryStore
import ui.Mode

@Composable
fun ClassicScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var answer by remember { mutableStateOf("Нажми кнопку и получишь ответ") }

    ScreenScaffold(
        title = "Классический",
        onBack = onBack,
        onOpenHistory = onOpenHistory
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(answer)

            Button(onClick = {
                val newAnswer = HistoryStore.randomClassic()
                answer = newAnswer
                HistoryStore.add(Mode.CLASSIC, newAnswer)
            }) {
                Text("Спросить шар")
            }
        }
    }
}
