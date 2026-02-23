package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ui.screens.ClassicScreen
import ui.screens.CustomScreen
import ui.screens.DailyAdviceScreen
import ui.screens.HistoryScreen
import ui.screens.HomeScreen
import ui.screens.ScreenScaffold

enum class Mode { CLASSIC, CUSTOM, DAILY }



sealed class Screen {
    data object Home : Screen()
    data class ModeScreen(val mode: Mode) : Screen()
    data class History(val mode: Mode) : Screen()
}

@Composable
fun AppRoot(
    onExitApp: () -> Unit
) {
    val screenState = remember { mutableStateOf<Screen>(Screen.Home) }
    val currentModeState = remember { mutableStateOf(Mode.CLASSIC) }

    fun openMode(mode: Mode) {
        currentModeState.value = mode
        screenState.value = Screen.ModeScreen(mode)
    }

    fun openHistoryForCurrentMode() {
        screenState.value = Screen.History(currentModeState.value)
    }

    fun goBack() {
        screenState.value = when (val s = screenState.value) {
            Screen.Home -> Screen.Home
            is Screen.ModeScreen -> Screen.Home
            is Screen.History -> Screen.ModeScreen(s.mode)
        }
    }

    // синхронизируем текущий режим
    when (val s = screenState.value) {
        is Screen.ModeScreen -> currentModeState.value = s.mode
        is Screen.History -> currentModeState.value = s.mode
        Screen.Home -> Unit
    }

    val title = when (val s = screenState.value) {
        Screen.Home -> "MagicBall"
        is Screen.ModeScreen -> when (s.mode) {
            Mode.CLASSIC -> "Классический"
            Mode.CUSTOM -> "Свои варианты"
            Mode.DAILY -> "Совет на день"
        }
        is Screen.History -> "История"
    }

    val isHome = screenState.value is Screen.Home

    ScreenScaffold(
        title = title,
        showBack = !isHome,
        onBack = { goBack() },
        onOpenHistory = if (!isHome && screenState.value !is Screen.History)
            ({ openHistoryForCurrentMode() })
        else null,
        onExit = if (isHome) onExitApp else null
    ) { _ ->

        when (val s = screenState.value) {

            Screen.Home -> HomeScreen(
                onOpenClassic = { openMode(Mode.CLASSIC) },
                onOpenCustom = { openMode(Mode.CUSTOM) },
                onOpenDaily = { openMode(Mode.DAILY) }
            )

            is Screen.ModeScreen -> when (s.mode) {
                Mode.CLASSIC -> ClassicScreen(
                    onBack = { goBack() },
                    onOpenHistory = { openHistoryForCurrentMode() }
                )

                Mode.CUSTOM -> CustomScreen(
                    onBack = { goBack() },
                    onOpenHistory = { openHistoryForCurrentMode() }
                )

                Mode.DAILY -> DailyAdviceScreen(
                    onBack = { goBack() },
                    onOpenHistory = { openHistoryForCurrentMode() }
                )
            }

            is Screen.History -> HistoryScreen(
                mode = s.mode,
                onBack = { screenState.value = Screen.ModeScreen(s.mode) }
            )
        }
    }
}