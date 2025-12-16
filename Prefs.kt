package com.example.quranmemorizer.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.ds by preferencesDataStore(name = "quran_prefs")

data class AppPrefs(
    val startDateIso: String = "",
    val reminderHour: Int = 19,
    val reminderMinute: Int = 0,
    val lastCompletedPage: Int = 0,
    val darkMode: Boolean = false
)

class Prefs(private val context: Context) {

    private val K_START = stringPreferencesKey("startDateIso")
    private val K_HOUR = intPreferencesKey("reminderHour")
    private val K_MIN  = intPreferencesKey("reminderMinute")
    private val K_DONE = intPreferencesKey("lastCompletedPage")
    private val K_DARK = booleanPreferencesKey("darkMode")

    val flow: Flow<AppPrefs> = context.ds.data.map { p ->
        AppPrefs(
            startDateIso = p[K_START] ?: "",
            reminderHour = p[K_HOUR] ?: 19,
            reminderMinute = p[K_MIN] ?: 0,
            lastCompletedPage = p[K_DONE] ?: 0,
            darkMode = p[K_DARK] ?: false
        )
    }

    suspend fun setStartDate(iso: String) { context.ds.edit { it[K_START] = iso } }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.ds.edit { it[K_HOUR] = hour; it[K_MIN] = minute }
    }

    suspend fun setLastCompletedPage(page: Int) { context.ds.edit { it[K_DONE] = page } }

    suspend fun setDarkMode(enabled: Boolean) { context.ds.edit { it[K_DARK] = enabled } }
}
