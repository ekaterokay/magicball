package ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.magicball.ui.MagicBackground
import com.example.magicball.ui.MagicBallHero
import com.example.magicball.ui.PrimaryButton
import com.example.magicball.ui.theme.*

@Composable
fun DailyAdviceScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit,
    advice: String?,
    loading: Boolean,
    error: String?,
    onEnsureLoaded: () -> Unit,
    onReload: () -> Unit
) {
    // ✅ загружаем только если совета ещё нет (AppRoot решает, надо ли)
    LaunchedEffect(Unit) {
        onEnsureLoaded()
    }

    MagicBackground(contentPadding = PaddingValues(16.dp)) {

        Spacer(Modifier.height(12.dp))

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
            ) {
                onReload()
            }
        }

        Spacer(Modifier.weight(1f))
    }
}