package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magicball.data.AdviceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyAdviceScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var advice by remember { mutableStateOf<String?>(null) }

    // триггер для “Другой совет”
    var reloadKey by remember { mutableIntStateOf(0) }

    suspend fun loadAdvice() {
        loading = true
        error = null
        try {
            val res = withContext(Dispatchers.IO) { AdviceService.api.getQuote() }
            val text = res.quoteText?.trim().orEmpty()
            advice = if (text.isNotEmpty()) text else "Не удалось получить совет 😅"
        } catch (t: Throwable) {
            error = t.message ?: "Ошибка сети"
        } finally {
            loading = false
        }
    }

    LaunchedEffect(reloadKey) { loadAdvice() }

    // Если у тебя есть свой ScreenScaffold — подставь его, ниже просто Material3 Scaffold
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Совет на день") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                },
                actions = {
                    TextButton(onClick = onOpenHistory) { Text("История") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                loading -> CircularProgressIndicator()
                error != null -> Text("Ошибка: $error")
                else -> Text(
                    advice ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { reloadKey++ },
                    enabled = !loading
                ) {
                    Text("Другой совет")
                }

                OutlinedButton(
                    onClick = { reloadKey = reloadKey }, // без действий, просто пример
                    enabled = false
                ) {
                    Text(" ")
                }
            }
        }
    }
}
