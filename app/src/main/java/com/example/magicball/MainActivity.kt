package com.example.magicball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Если у тебя уже есть тема (MagicBallTheme) — можешь обернуть в неё.
            MaterialTheme {
                MagicBallApp()
            }
        }
    }
}

private enum class Tab(val title: String) {
    CLASSIC("Классический"),
    CUSTOM("Свои варианты"),
    DAY("Совет дня")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MagicBallApp() {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.CLASSIC) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шар предсказаний") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == Tab.CLASSIC,
                    onClick = { selectedTab = Tab.CLASSIC },
                    icon = { Icon(Icons.Filled.Help, contentDescription = null) },
                    label = { Text("Classic") }
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.CUSTOM,
                    onClick = { selectedTab = Tab.CUSTOM },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    label = { Text("Custom") }
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.DAY,
                    onClick = { selectedTab = Tab.DAY },
                    icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null) },
                    label = { Text("Day") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                Tab.CLASSIC -> ClassicScreen()
                Tab.CUSTOM -> CustomAnswersScreen()
                Tab.DAY -> DayPredictionScreen()
            }
        }
    }
}

@Composable
private fun ClassicScreen() {
    PlaceholderScreen(
        title = "Классический режим",
        subtitle = "Скоро: кнопка и встряхивание → случайный ответ"
    )
}

@Composable
private fun CustomAnswersScreen() {
    PlaceholderScreen(
        title = "Свои варианты",
        subtitle = "Скоро: ввод вариантов → сохранение в БД → случайный выбор"
    )
}

@Composable
private fun DayPredictionScreen() {
    PlaceholderScreen(
        title = "Совет дня",
        subtitle = "Скоро: загрузка из публичного API + кэш в БД"
    )
}

@Composable
private fun PlaceholderScreen(title: String, subtitle: String) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.large
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
