package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magicball.data.HistoryStore
import ui.Mode

@Composable
fun HistoryScreen(
    mode: Mode,
    onBack: () -> Unit
) {
    val items = HistoryStore.get(mode)

    ScreenScaffold(
        title = "История",
        onBack = onBack,
        onOpenHistory = null
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (items.isEmpty()) {
                Text("История пустая")
            } else {
                items.asReversed().forEachIndexed { index, item ->
                    Text("${index + 1}. $item")
                }
            }
        }
    }
}
