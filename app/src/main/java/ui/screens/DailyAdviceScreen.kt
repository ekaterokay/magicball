package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.magicball.data.AdviceService
import com.example.magicball.data.HistoryStore
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.MagicBallHero
import com.example.magicball.ui.PrimaryButton
import com.example.magicball.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.Mode

@Composable
fun DailyAdviceScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    // onBack и onOpenHistory обрабатываются общим ScreenScaffold в AppRoot

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var advice by remember { mutableStateOf<String?>(null) }

    var reloadKey by remember { mutableIntStateOf(0) }
    var lastSaved by remember { mutableStateOf<String?>(null) }

    suspend fun loadAdvice() {
        loading = true
        error = null
        try {
            val res = withContext(Dispatchers.IO) { AdviceService.api.getQuote() }
            val text = res.quoteText?.trim().orEmpty()
            val finalText = if (text.isNotEmpty()) text else "Не удалось получить совет 😅"
            advice = finalText

            // сохраняем в историю только реальный совет и только если он новый
            if (text.isNotEmpty() && text != lastSaved) {
                HistoryStore.add(Mode.DAILY, text)
                lastSaved = text
            }
        } catch (t: Throwable) {
            error = t.message ?: "Ошибка сети"
        } finally {
            loading = false
        }
    }

    LaunchedEffect(reloadKey) { loadAdvice() }

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp)) // отступ от общего topBar

        MagicBallHero(size = 170.dp)

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Surface1),
            border = BorderStroke(1.dp, StrokeGlow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Твой совет",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(10.dp))

                when {
                    loading -> CircularProgressIndicator(color = AccentLavender)

                    error != null -> Text(
                        text = "Ошибка: $error",
                        color = Error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    else -> Text(
                        text = advice.orEmpty(),
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                text = if (loading) "Загружаю..." else "Другой совет",
                enabled = !loading
            ) { reloadKey++ }
        }

        Spacer(Modifier.weight(1f))
    }
}