package com.example.magicball.data

import android.content.Context
import org.json.JSONArray
import ui.Mode
import kotlin.random.Random

object HistoryStore {

    private const val PREFS_NAME = "magicball_history"
    private const val KEY_CLASSIC = "history_classic"
    private const val KEY_CUSTOM = "history_custom"
    private const val KEY_DAILY = "history_daily"

    // ✅ текущий “совет на день”, который должен жить между перезапусками
    private const val KEY_DAILY_CURRENT = "daily_current_advice"

    private lateinit var appContext: Context

    // вызвать один раз при старте приложения
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun prefs() = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun keyFor(mode: Mode): String = when (mode) {
        Mode.CLASSIC -> KEY_CLASSIC
        Mode.CUSTOM -> KEY_CUSTOM
        Mode.DAILY -> KEY_DAILY
    }

    fun get(mode: Mode): List<String> {
        ensureInit()
        val raw = prefs().getString(keyFor(mode), null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            buildList {
                for (i in 0 until arr.length()) add(arr.optString(i))
            }.filter { it.isNotBlank() }
        } catch (_: Throwable) {
            emptyList()
        }
    }

    fun add(mode: Mode, value: String) {
        ensureInit()
        val text = value.trim()
        if (text.isEmpty()) return

        val list = get(mode).toMutableList()
        list.add(text)

        val arr = JSONArray()
        list.forEach { arr.put(it) }

        prefs().edit().putString(keyFor(mode), arr.toString()).apply()
    }

    fun clear(mode: Mode) {
        ensureInit()
        prefs().edit().remove(keyFor(mode)).apply()
    }

    // ✅ текущий совет на день (переживает перезапуск)
    fun getCurrentDailyAdvice(): String? {
        ensureInit()
        return prefs().getString(KEY_DAILY_CURRENT, null)
    }

    fun setCurrentDailyAdvice(value: String) {
        ensureInit()
        prefs().edit().putString(KEY_DAILY_CURRENT, value).apply()
    }

    fun randomClassic(): String {
        val defaults = listOf(
            "Да",
            "Нет",
            "Возможно",
            "Скорее да",
            "Скорее нет",
            "Спроси позже",
            "Определённо",
            "Скорее всего"
        )
        return defaults[Random.nextInt(defaults.size)]
    }

    private fun ensureInit() {
        check(::appContext.isInitialized) {
            "HistoryStore не инициализирован. Вызови HistoryStore.init(context) в MainActivity.onCreate()."
        }
    }
}