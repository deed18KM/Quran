package com.example.quranmemorizer.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.quranmemorizer.alarm.AlarmScheduler
import com.example.quranmemorizer.data.Prefs
import com.example.quranmemorizer.export.PdfExporter
import com.example.quranmemorizer.quran.QuranPagesRepository
import com.example.quranmemorizer.ui.screens.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

private val ISO = DateTimeFormatter.ISO_LOCAL_DATE

@Composable
fun AppNav(context: Context) {
    val nav = rememberNavController()
    val prefs = remember { Prefs(context) }
    val repo = remember { QuranPagesRepository(context) }
    val scope = rememberCoroutineScope()
    val state by prefs.flow.collectAsState(initial = com.example.quranmemorizer.data.AppPrefs())

    NavHost(navController = nav, startDestination = "today") {
        composable("today") {
            TodayScreen(
                prefsState = state,
                repo = repo,
                onOpenPlan = { nav.navigate("plan") },
                onOpenSettings = { nav.navigate("settings") },
                onMarkDone = { page -> scope.launch { prefs.setLastCompletedPage(page) } }
            )
        }
        composable("plan") {
            PlanScreen(prefsState = state, onBack = { nav.popBackStack() }, onOpenSettings = { nav.navigate("settings") })
        }
        composable("review") {
            ReviewScreen(prefsState = state, repo = repo, onBack = { nav.popBackStack() })
        }
        composable("stats") {
            StatsScreen(prefsState = state, onBack = { nav.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(
                prefsState = state,
                onBack = { nav.popBackStack() },
                onPickStartDate = {
                    showDatePicker(context) { picked -> scope.launch { prefs.setStartDate(picked.format(ISO)) } }
                },
                onPickTime = {
                    showTimePicker(context, state.reminderHour, state.reminderMinute) { h, m ->
                        scope.launch { prefs.setReminderTime(h, m) }
                        AlarmScheduler.scheduleDaily(context, h, m)
                    }
                },
                onEnableReminder = {
                    if (state.startDateIso.isNotBlank()) AlarmScheduler.scheduleDaily(context, state.reminderHour, state.reminderMinute)
                },
                onDisableReminder = { AlarmScheduler.cancel(context) },
                onToggleDark = { enabled -> scope.launch { prefs.setDarkMode(enabled) } },
                onOpenReview = { nav.navigate("review") },
                onOpenStats = { nav.navigate("stats") },
                onExportPdf = {
                    val uri = PdfExporter.exportProgress(context, state)
                    Toast.makeText(context, if (uri != null) "تم حفظ PDF في Downloads ✅" else "فشل التصدير", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

private fun showDatePicker(context: Context, onPicked: (LocalDate) -> Unit) {
    val c = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, y, m, d -> onPicked(LocalDate.of(y, m + 1, d)) },
        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
    ).show()
}

private fun showTimePicker(context: Context, hour: Int, minute: Int, onPicked: (Int, Int) -> Unit) {
    TimePickerDialog(context, { _, h, m -> onPicked(h, m) }, hour, minute, true).show()
}
