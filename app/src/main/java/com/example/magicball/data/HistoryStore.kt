package com.example.magicball.data

import ui.Mode

object HistoryStore {
    private val history: MutableMap<Mode, MutableList<String>> = mutableMapOf(
        Mode.CLASSIC to mutableListOf(),
        Mode.CUSTOM to mutableListOf(),
        Mode.DAILY to mutableListOf()
    )

    fun add(mode: Mode, value: String) {
        history.getValue(mode).add(value)
    }

    fun get(mode: Mode): List<String> = history[mode].orEmpty()

    fun randomClassic(): String {
        val list = listOf(
            "Да", "Нет", "Скорее да", "Скорее нет",
            "Не знаю", "Попробуй позже", "Определённо!", "Сомнительно"
        )
        return list.random()
    }

    fun randomDaily(): String {
        val list = listOf(
            "Сделай маленький шаг — он важнее идеального плана.",
            "Не спорь с собой — начни и разберёшься по пути.",
            "Сегодня хороший день, чтобы закончить одну мелочь.",
            "Пей воду и не забывай отдыхать 😄"
        )
        return list.random()
    }
}
