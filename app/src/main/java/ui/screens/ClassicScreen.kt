package com.example.magicball.ui.screens

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
import com.example.magicball.data.HistoryStore
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.MagicBallHero
import com.example.magicball.ui.PrimaryButton
import com.example.magicball.ui.theme.*
import kotlinx.coroutines.delay
import ui.Mode

@Composable
fun ClassicScreen(
    @Suppress("UNUSED_PARAMETER") onBack: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onOpenHistory: () -> Unit
) {
    val initialHint = "Нажми кнопку и получишь ответ"
    var answer by remember { mutableStateOf(initialHint) }
    var askedOnce by remember { mutableStateOf(false) }
    var isShaking by remember { mutableStateOf(false) }
    var dotsCount by remember { mutableStateOf(3) }
    var generatedAnswer by remember { mutableStateOf("") }

    val isHint = !askedOnce && answer == initialHint
    val isGenerating = isShaking

    // Логика циклического уменьшения точек
    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            while (isGenerating) {
                for (dots in 3 downTo 1) {
                    dotsCount = dots
                    delay(300) // время между изменением точек
                }
            }
        }
    }

    // Логика выдачи ответа через 1 секунду
    LaunchedEffect(isShaking) {
        if (isShaking) {
            delay(1000) // ждем 1 секунду (время тряски)
            answer = generatedAnswer
            isShaking = false
            dotsCount = 3
        }
    }

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

        MagicBallHero(size = 180.dp, isShaking = isShaking)

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(horizontal = 6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Surface1),
            border = BorderStroke(1.dp, StrokeGlow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Ответ шара",
                    color = TextSecondary.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating) {
                        // Показываем "Генерация ответа..." с циклическими точками
                        Text(
                            text = "Генерация ответа" + ".".repeat(dotsCount),
                            textAlign = TextAlign.Center,
                            color = TextTertiary.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        // Показываем реальный ответ или hint
                        Text(
                            text = answer,
                            textAlign = TextAlign.Center,
                            color = if (isHint) TextTertiary.copy(alpha = 0.55f) else TextPrimary,
                            style = if (isHint)
                                MaterialTheme.typography.bodyLarge
                            else
                                MaterialTheme.typography.titleLarge,
                            fontWeight = if (isHint)
                                FontWeight.Medium
                            else
                                FontWeight.SemiBold
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
                text = if (askedOnce) "Спросить снова" else "Спросить шар",
                enabled = !isShaking
            ) {
                generatedAnswer = HistoryStore.randomClassic()
                askedOnce = true
                isShaking = true
                dotsCount = 3
                HistoryStore.add(Mode.CLASSIC, generatedAnswer)
            }
        }

        Spacer(Modifier.weight(1f))
    }
}
