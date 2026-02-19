package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ui.screens.ClassicScreen
import ui.screens.CustomScreen
import ui.screens.DailyAdviceScreen
import ui.screens.HistoryScreen
import ui.screens.HomeScreen

enum class Mode { CLASSIC, CUSTOM, DAILY }

sealed class Screen {
    data object Home : Screen()
    data class ModeScreen(val mode: Mode) : Screen()
    data class History(val mode: Mode) : Screen()
}

@Composable
fun AppRoot() {
    val screenState = remember { mutableStateOf<Screen>(Screen.Home) }

    when (val s = screenState.value) {
        Screen.Home -> HomeScreen(
            onOpenClassic = { screenState.value = Screen.ModeScreen(Mode.CLASSIC) },
            onOpenCustom = { screenState.value = Screen.ModeScreen(Mode.CUSTOM) },
            onOpenDaily = { screenState.value = Screen.ModeScreen(Mode.DAILY) },
        )

        is Screen.ModeScreen -> {
            when (s.mode) {
                Mode.CLASSIC -> ClassicScreen(
                    onBack = { screenState.value = Screen.Home },
                    onOpenHistory = { screenState.value = Screen.History(Mode.CLASSIC) }
                )

                Mode.CUSTOM -> CustomScreen(
                    onBack = { screenState.value = Screen.Home },
                    onOpenHistory = { screenState.value = Screen.History(Mode.CUSTOM) }
                )

                Mode.DAILY -> DailyAdviceScreen(
                    onBack = { screenState.value = Screen.Home },
                    onOpenHistory = { screenState.value = Screen.History(Mode.DAILY) }
                )
            }
        }

        is Screen.History -> HistoryScreen(
            mode = s.mode,
            onBack = { screenState.value = Screen.ModeScreen(s.mode) }
        )
    }
}
