package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magicball.data.HistoryStore
import ui.Mode

@Composable
fun CustomScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var variantsText by remember { mutableStateOf("Да\nНет\nВозможно") }
    var answer by remember { mutableStateOf("Введи варианты (каждый с новой строки) и жми кнопку") }

    ScreenScaffold(
        title = "Свои варианты",
        onBack = onBack,
        onOpenHistory = onOpenHistory
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = variantsText,
                onValueChange = { variantsText = it },
                label = { Text("Варианты") },
                minLines = 4
            )

            Button(onClick = {
                val list = variantsText
                    .lines()
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                val newAnswer = if (list.isEmpty()) "Добавь хотя бы один вариант" else list.random()
                answer = newAnswer
                HistoryStore.add(Mode.CUSTOM, newAnswer)
            }) {
                Text("Выбрать случайно")
            }

            Text(answer)
        }
    }
}
