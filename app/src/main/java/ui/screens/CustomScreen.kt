package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.magicball.data.HistoryStore
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.MagicBallHero
import com.example.magicball.ui.PrimaryButton
import com.example.magicball.ui.SecondaryButton
import com.example.magicball.ui.theme.*
import kotlinx.coroutines.delay
import ui.Mode

@Composable
fun CustomScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var variantsText by remember { mutableStateOf("Да\nНет\nВозможно") }
    var answer by remember { mutableStateOf("Введи варианты и нажми кнопку") }
    var isShaking by remember { mutableStateOf(false) }
    var dotsCount by remember { mutableStateOf(3) }
    var generatedAnswer by remember { mutableStateOf("") }
    var askedOnce by remember { mutableStateOf(false) }

    val isHint = answer == "Введи варианты и нажми кнопку"
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
            answer = generatedAnswer
            isShaking = false
            dotsCount = 3
        }
    }

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

        MagicBallHero(size = 150.dp, isShaking = isShaking)

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Surface1),
            border = BorderStroke(1.dp, StrokeSoft)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Варианты (каждый с новой строки)",
                    color = TextSecondary.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelLarge
                )

                OutlinedTextField(
                    value = variantsText,
                    onValueChange = { variantsText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(
                            text = "Да\nНет\nВозможно",
                            color = TextPrimary.copy(alpha = 0.35f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    minLines = 4,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface0,
                        unfocusedContainerColor = Surface0,
                        disabledContainerColor = Surface0,
                        focusedBorderColor = AccentLavender,
                        unfocusedBorderColor = StrokeSoft,
                        cursorColor = AccentNeon,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedPlaceholderColor = TextPrimary.copy(alpha = 0.35f),
                        unfocusedPlaceholderColor = TextPrimary.copy(alpha = 0.35f)
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PrimaryButton(text = "Выбрать случайно", enabled = !isShaking) {
                        val list = variantsText
                            .lines()
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        when {
                            list.isEmpty() -> {
                                answer = "Добавь хотя бы два варианта"
                            }
                            list.size < 2 -> {
                                answer = "Нужно минимум два варианта"
                            }
                            else -> {
                                generatedAnswer = list.random()
                                askedOnce = true
                                isShaking = true
                                dotsCount = 3
                                HistoryStore.add(Mode.CUSTOM, generatedAnswer)
                            }
                        }
                    }


                    SecondaryButton(text = "Очистить") {
                        variantsText = ""
                        answer = "Введи варианты и нажми кнопку"
                        askedOnce = false
                        isShaking = false
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

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
                    text = "Результат",
                    color = TextSecondary.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating) {
                        Text(
                            text = "Генерация ответа" + ".".repeat(dotsCount),
                            color = TextTertiary.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = answer,
                            color = if (isHint) TextTertiary.copy(alpha = 0.55f) else TextPrimary,
                            style = if (isHint) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleLarge,
                            fontWeight = if (isHint) FontWeight.Normal else FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
    }
}
