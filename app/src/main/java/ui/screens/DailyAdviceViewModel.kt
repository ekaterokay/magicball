package com.example.magicball.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DailyAdviceUiState(
    val loading: Boolean = false,
    val advice: String? = null,
    val error: String? = null
)

class DailyAdviceViewModel(
    private val repo: AdviceRepository = AdviceRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(DailyAdviceUiState())
    val state: StateFlow<DailyAdviceUiState> = _state

    fun load() {
        viewModelScope.launch {
            _state.value = DailyAdviceUiState(loading = true)
            try {
                val advice = repo.fetchAdvice()
                _state.value = DailyAdviceUiState(advice = advice)
            } catch (t: Throwable) {
                _state.value = DailyAdviceUiState(error = (t.message ?: "Network error"))
            }
        }
    }
}
