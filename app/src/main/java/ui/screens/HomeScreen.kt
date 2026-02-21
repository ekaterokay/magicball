package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.MagicBallHero
import com.example.magicball.ui.PrimaryButton
import com.example.magicball.ui.SecondaryButton
import com.example.magicball.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onOpenClassic: () -> Unit,
    onOpenCustom: () -> Unit,
    onOpenDaily: () -> Unit,
) {
    MagicBackground {

        Spacer(modifier = Modifier.height(20.dp))

        // 🔮 Шар + заголовок
        MagicBallHero(
            title = "Шар предсказаний",
            subtitle = "Спроси и узнай свою судьбу!"
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Текст выбора режима
        Text(
            text = "Выбери режим:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // КНОПКИ ПО ЦЕНТРУ
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Классический", onClick = onOpenClassic)
            PrimaryButton(text = "Свои варианты", onClick = onOpenCustom)
            SecondaryButton(text = "Совет на день", onClick = onOpenDaily)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}