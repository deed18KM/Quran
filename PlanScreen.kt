package com.example.quranmemorizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quranmemorizer.data.AppPrefs
import com.example.quranmemorizer.plan.getTaskForDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val ISO = DateTimeFormatter.ISO_LOCAL_DATE

@Composable
fun PlanScreen(
    prefsState: AppPrefs,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val startDate = remember(prefsState.startDateIso) {
        if (prefsState.startDateIso.isBlank()) null else LocalDate.parse(prefsState.startDateIso, ISO)
    }
    val daysToShow = 365

    val list = remember(startDate) {
        if (startDate == null) emptyList()
        else (0 until daysToShow).mapNotNull { i ->
            val d = startDate.plusDays(i.toLong())
            getTaskForDate(startDate, d)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الجدول السنوي") },
                navigationIcon = { TextButton(onClick = onBack) { Text("رجوع") } },
                actions = { TextButton(onClick = onOpenSettings) { Text("الإعدادات") } }
            )
        }
    ) { padding ->
        if (startDate == null) {
            Box(Modifier.padding(padding).padding(16.dp)) { Text("لا يوجد تاريخ بدء. اذهب إلى الإعدادات أولاً.") }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { task ->
                val done = prefsState.lastCompletedPage >= task.page
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(task.date.toString())
                        Text("ص ${task.page}  ${if (done) "✅" else ""}")
                    }
                }
            }
        }
    }
}
