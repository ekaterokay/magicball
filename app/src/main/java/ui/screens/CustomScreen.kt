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
import ui.Mode

@Composable
fun CustomScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var variantsText by remember { mutableStateOf("Да\nНет\nВозможно") }
    var answer by remember { mutableStateOf("Введи варианты и нажми кнопку") }
    val isHint = answer == "Введи варианты и нажми кнопку"

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

        MagicBallHero(size = 150.dp)

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
                // ✅ чуть меньше общий gap, чтобы кнопки были ближе к полю
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
                    // ✅ фиксируем одинаковый размер текста и для ввода, и для подсказки
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(
                            text = "Да\nНет\nВозможно",
                            // ✅ при очистке не “прыгает”: размер тот же, меняется только прозрачность
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

                // ✅ кнопки ближе к полю (убрали лишние “воздухи”)
                Column(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PrimaryButton(text = "Выбрать случайно") {
                        val list = variantsText
                            .lines()
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        val newAnswer =
                            if (list.isEmpty()) "Добавь хотя бы один вариант" else list.random()

                        answer = newAnswer
                        if (list.isNotEmpty()) HistoryStore.add(Mode.CUSTOM, newAnswer)
                    }

                    SecondaryButton(text = "Очистить") {
                        variantsText = ""
                        answer = "Введи варианты и нажми кнопку"
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
                Text(
                    text = answer,
                    color = if (isHint) TextTertiary.copy(alpha = 0.55f) else TextPrimary,
                    style = if (isHint) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleLarge,
                    fontWeight = if (isHint) FontWeight.Normal else FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.weight(1f))
    }
}