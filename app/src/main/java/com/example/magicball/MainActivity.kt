package com.example.magicball.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.magicball.data.AppDatabase
import com.example.magicball.data.CustomAnswerEntity
import com.example.magicball.net.QuoteApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagicBallTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MagicBallScreen()
                }
            }
        }
    }
}

@Composable
private fun MagicBallScreen() {
    val ctx = LocalContext.current
    val db = remember { AppDatabase.get(ctx) }
    val dao = remember { db.customAnswerDao() }
    val scope = rememberCoroutineScope()

    val api = remember { QuoteApi.create() }

    val customAnswers by dao.getAll().collectAsState(initial = emptyList())

    var question by remember { mutableStateOf("") }
    var newAnswer by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Нажми кнопку и я отвечу 🙂") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("MagicBall", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Твой вопрос") }
        )

        ElevatedButton(
            onClick = {
                scope.launch {
                    loading = true
                    try {
                        // если есть кастомные ответы — иногда берем из них
                        if (customAnswers.isNotEmpty() && (0..1).random() == 0) {
                            result = customAnswers.random().text
                        } else {
                            val q = api.randomQuote()
                            result = "“${q.content}” — ${q.author}"
                        }
                    } catch (e: Exception) {
                        result = "Ошибка сети: ${e.message}"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "Думаю..." else "Спросить шар")
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Ответ:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(result)
            }
        }

        Divider()

        Text("Свои ответы (Room):", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = newAnswer,
                onValueChange = { newAnswer = it },
                modifier = Modifier.weight(1f),
                label = { Text("Добавить ответ") }
            )
            Button(
                onClick = {
                    val text = newAnswer.trim()
                    if (text.isNotEmpty()) {
                        scope.launch { dao.insert(CustomAnswerEntity(text = text)) }
                        newAnswer = ""
                    }
                }
            ) { Text("Добавить") }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(customAnswers, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.text, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { scope.launch { dao.delete(item) } }) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    }
}
