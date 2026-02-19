package com.example.magicball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicball.data.AppDatabase
import com.example.magicball.data.CustomAnswersDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import ui.AppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppRoot()
        }
    }
}


private enum class Screen { Home, Custom }

@Composable
private fun MagicBallApp(db: AppDatabase) {
    var screen by remember { mutableStateOf(Screen.Home) }
    var lastAnswer by remember { mutableStateOf("Задай вопрос и нажми кнопку 🎱") }
    var history by remember { mutableStateOf(listOf<String>()) }

    when (screen) {
        Screen.Home -> HomeScreen(
            answer = lastAnswer,
            history = history,
            onAsk = { generated ->
                lastAnswer = generated
                history = (listOf(generated) + history).take(10)
            },
            onOpenCustom = { screen = Screen.Custom },
            db = db
        )

        Screen.Custom -> CustomAnswersScreen(
            db = db,
            onBack = { screen = Screen.Home }
        )
    }
}

@Composable
private fun HomeScreen(
    answer: String,
    history: List<String>,
    onAsk: (String) -> Unit,
    onOpenCustom: () -> Unit,
    db: AppDatabase
) {
    val scope = rememberCoroutineScope()

    val defaults = remember {
        listOf(
            "Да ✅", "Нет ❌", "Возможно 🤔", "Скорее всего 👍",
            "Сомнительно 😅", "Точно! 💯", "Спроси позже ⏳", "Не уверен 🫠"
        )
    }

    var customAnswers by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            customAnswers = db.customAnswerDao().getAll().map { it.text }
        }
    }

    val pool = remember(customAnswers) { (defaults + customAnswers).ifEmpty { defaults } }

    val bg = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )
    @OptIn(ExperimentalMaterial3Api::class)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MagicBall", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = onOpenCustom) {
                        Icon(Icons.Filled.Add, contentDescription = "Свои ответы")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            Text("Шар предсказаний", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                "Нажми кнопку — получишь ответ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(18.dp))
            MagicBallCard(answer = answer)
            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val newAnswer = pool[Random.nextInt(pool.size)]
                        onAsk(newAnswer)
                    }
                ) {
                    Text("Спросить 🎱")
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onAsk("Задай вопрос и нажми кнопку 🎱") }
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Сброс")
                }
            }

            Spacer(Modifier.height(18.dp))
            HistoryBlock(history = history)
        }
    }
}

@Composable
private fun MagicBallCard(answer: String) {
    val cardBrush = Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        )
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardBrush)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("8", fontSize = 42.sp, fontWeight = FontWeight.Black)
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = answer,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HistoryBlock(history: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("История", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))

            if (history.isEmpty()) {
                Text(
                    "Пока пусто. Нажми «Спросить».",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 220.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                item,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomAnswersScreen(
    db: AppDatabase,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var list by remember { mutableStateOf<List<CustomAnswersDb>>(emptyList()) }
    var text by remember { mutableStateOf("") }

    fun reload() {
        scope.launch(Dispatchers.IO) {
            val data = db.customAnswerDao().getAll()
            withContext(Dispatchers.Main) { list = data }
        }
    }

    LaunchedEffect(Unit) { reload() }
    @OptIn(ExperimentalMaterial3Api::class)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Свои ответы") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Добавь свои варианты — они будут участвовать в ответах шара.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Например: «Пора действовать 🚀»") },
                    singleLine = true
                )
                Button(
                    onClick = {
                        val trimmed = text.trim()
                        if (trimmed.isNotEmpty()) {
                            scope.launch(Dispatchers.IO) {
                                db.customAnswerDao().insert(CustomAnswersDb(text = trimmed))
                                withContext(Dispatchers.Main) { text = "" }
                                reload()
                            }
                        }
                    }
                ) { Text("Добавить") }
            }

            Spacer(Modifier.height(16.dp))

            if (list.isEmpty()) {
                Text("Пока нет своих ответов.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(list) { item ->
                        Card(shape = RoundedCornerShape(18.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.text, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            db.customAnswerDao().delete(item)
                                            reload()
                                        }
                                    }
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
