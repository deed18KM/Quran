package com.example.quranmemorizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quranmemorizer.data.AppPrefs
import com.example.quranmemorizer.plan.TOTAL_PAGES
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val ISO = DateTimeFormatter.ISO_LOCAL_DATE

@Composable
fun StatsScreen(
    prefsState: AppPrefs,
    onBack: () -> Unit
) {
    val start = remember(prefsState.startDateIso) {
        if (prefsState.startDateIso.isBlank()) null else LocalDate.parse(prefsState.startDateIso, ISO)
    }
    val done = prefsState.lastCompletedPage
    val remaining = (TOTAL_PAGES - done).coerceAtLeast(0)
    val percent = if (TOTAL_PAGES == 0) 0 else (done * 100 / TOTAL_PAGES)
    val expectedFinish = remember(start) { start?.plusDays((TOTAL_PAGES - 1).toLong()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الإحصائيات") },
                navigationIcon = { TextButton(onClick = onBack) { Text("رجوع") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("المنجز: $done / $TOTAL_PAGES ($percent%)", style = MaterialTheme.typography.titleMedium)
                    Text("المتبقي: $remaining صفحة")
                    Text("وقت التنبيه: ${String.format("%02d:%02d", prefsState.reminderHour, prefsState.reminderMinute)}")
                    Text("تاريخ البدء: ${if (prefsState.startDateIso.isBlank()) "غير محدد" else prefsState.startDateIso}")
                    Text("تاريخ متوقع للختم: ${expectedFinish?.toString() ?: "—"}")
                }
            }
        }
    }
}
