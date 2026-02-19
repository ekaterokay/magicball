package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenClassic: () -> Unit,
    onOpenCustom: () -> Unit,
    onOpenDaily: () -> Unit,
) {
    ScreenScaffold(
        title = "Magic Ball",
        onBack = null,
        onOpenHistory = null
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Выбери режим 🔮",
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = onOpenClassic) { Text("Классический") }
            Button(onClick = onOpenCustom) { Text("Свои варианты") }
            Button(onClick = onOpenDaily) { Text("Совет на день") }
        }
    }
}
