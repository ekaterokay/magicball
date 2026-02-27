package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.magicball.data.AdviceService
import com.example.magicball.data.HistoryStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val scope = rememberCoroutineScope()

    // ✅ берём сохранённый совет из SharedPreferences (переживает перезапуск)
    val dailyAdvice = remember { mutableStateOf(HistoryStore.getCurrentDailyAdvice()) }
    val dailyLoading = remember { mutableStateOf(false) }
    val dailyError = remember { mutableStateOf<String?>(null) }

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

    // держим текущий режим актуальным
    when (val s = screenState.value) {
        is Screen.ModeScreen -> currentModeState.value = s.mode
        is Screen.History -> currentModeState.value = s.mode
        Screen.Home -> Unit
    }

    fun loadDailyAdvice(force: Boolean) {
        // ✅ если совет уже есть, не меняем его (только по кнопке)
        if (!force && dailyAdvice.value != null) return

        scope.launch {
            dailyLoading.value = true
            dailyError.value = null
            try {
                val res = withContext(Dispatchers.IO) { AdviceService.api.getQuote() }
                val text = res.quoteText?.trim().orEmpty()
                val finalText = if (text.isNotEmpty()) text else "Не удалось получить совет 😅"

                val prev = HistoryStore.getCurrentDailyAdvice()

                // ✅ сохраняем текущий совет (переживает перезапуск)
                dailyAdvice.value = finalText
                HistoryStore.setCurrentDailyAdvice(finalText)

                // ✅ в историю пишем только реальный текст и только если он отличается
                if (text.isNotEmpty() && text != prev) {
                    HistoryStore.add(Mode.DAILY, text)
                }
            } catch (t: Throwable) {
                dailyError.value = t.message ?: "Ошибка сети"
            } finally {
                dailyLoading.value = false
            }
        }
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
                    onOpenHistory = { openHistoryForCurrentMode() },
                    advice = dailyAdvice.value,
                    loading = dailyLoading.value,
                    error = dailyError.value,
                    onEnsureLoaded = { loadDailyAdvice(force = false) },
                    onReload = { loadDailyAdvice(force = true) }
                )
            }

            is Screen.History -> HistoryScreen(
                mode = s.mode,
                onBack = { screenState.value = Screen.ModeScreen(s.mode) }
            )
        }
    }
}