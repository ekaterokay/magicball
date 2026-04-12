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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ui.Mode

// Объект для сохранения состояния между переходами
object DailyAdviceState {
    var cachedAdvice: String? = null
    var cachedLastSaved: String? = null
}

@Composable
fun DailyAdviceScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var isShaking by remember { mutableStateOf(false) }
    var dotsCount by remember { mutableStateOf(3) }
    var generatedAnswer by remember { mutableStateOf("") }
    var askedOnce by remember { mutableStateOf(false) }

    var advice by remember { mutableStateOf(DailyAdviceState.cachedAdvice) }
    var error by remember { mutableStateOf<String?>(null) }

    var reloadKey by remember { mutableIntStateOf(0) }

    val isHint = !askedOnce && advice == null
    val isGenerating = isShaking

    // Логика циклического уменьшения точек
    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            while (isGenerating) {
                for (dots in 3 downTo 1) {
                    dotsCount = dots
                    delay(300)
                }
            }
        }
    }

    // Логика выдачи ответа через 1 секунду
    LaunchedEffect(isShaking) {
        if (isShaking) {
            delay(1000)
            advice = generatedAnswer
            DailyAdviceState.cachedAdvice = generatedAnswer
            isShaking = false
            dotsCount = 3
        }
    }

    // Загрузка совета
    LaunchedEffect(reloadKey) {
        if (askedOnce && isShaking) {
            try {
                error = null
                val res = withContext(Dispatchers.IO) { AdviceService.api.getQuote() }
                val text = res.quoteText?.trim().orEmpty()
                val finalText = if (text.isNotEmpty()) text else "Не удалось получить совет 😅"
                generatedAnswer = finalText

                // сохраняем в историю только реальный совет и только если он новый
                if (text.isNotEmpty() && text != DailyAdviceState.cachedLastSaved) {
                    HistoryStore.add(Mode.DAILY, text)
                    DailyAdviceState.cachedLastSaved = text
                }
            } catch (t: Throwable) {
                error = t.message ?: "Ошибка сети"
                isShaking = false
            }
        }
    }

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

        MagicBallHero(size = 170.dp, isShaking = isShaking)

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

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isGenerating -> Text(
                            text = "Генерация ответа" + ".".repeat(dotsCount),
                            textAlign = TextAlign.Center,
                            color = TextTertiary.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )

                        error != null -> Text(
                            text = "Ошибка: $error",
                            color = Error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        else -> Text(
                            text = advice ?: "Нажми кнопку и получишь совет",
                            color = if (isHint) TextTertiary.copy(alpha = 0.55f) else TextPrimary,
                            style = if (isHint) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleLarge,
                            fontWeight = if (isHint) FontWeight.Medium else FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                text = if (askedOnce) "Получить другой совет" else "Получить совет",
                enabled = !isShaking
            ) {
                generatedAnswer = ""
                askedOnce = true
                isShaking = true
                dotsCount = 3
                reloadKey++
            }
        }

        Spacer(Modifier.weight(1f))
    }
}
