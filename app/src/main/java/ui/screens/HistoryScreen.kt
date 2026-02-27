package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.magicball.ui.SecondaryButton
import com.example.magicball.ui.theme.*
import ui.Mode

@Composable
fun HistoryScreen(
    mode: Mode,
    onBack: () -> Unit
) {
    // чтобы обновлять UI после очистки
    var refreshKey by remember { mutableIntStateOf(0) }
    val items = remember(refreshKey, mode) { HistoryStore.get(mode).asReversed() } // новые сверху

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

        MagicBallHero(size = 130.dp)

        Spacer(Modifier.height(14.dp))

        Text(
            text = when (mode) {
                Mode.CLASSIC -> "Классический режим"
                Mode.CUSTOM -> "Свои варианты"
                Mode.DAILY -> "Совет на день"
            },
            color = TextSecondary,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(12.dp))

        if (items.isEmpty()) {
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
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "История пустая",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Сделай пару предсказаний — и они появятся здесь ✨",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        } else {
            // список занимает доступное место
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(top = 4.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface1),
                        border = BorderStroke(1.dp, StrokeGlow)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "№ ${items.size - index}", // от большего к меньшему
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = item,
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // кнопка внизу
            SecondaryButton(text = "Очистить историю") {
                HistoryStore.clear(mode)
                refreshKey++
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}