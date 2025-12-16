package com.example.quranmemorizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quranmemorizer.data.AppPrefs
import com.example.quranmemorizer.plan.getTaskForDate
import com.example.quranmemorizer.quran.QuranPagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val ISO = DateTimeFormatter.ISO_LOCAL_DATE

@Composable
fun TodayScreen(
    prefsState: AppPrefs,
    repo: QuranPagesRepository,
    onOpenPlan: () -> Unit,
    onOpenSettings: () -> Unit,
    onMarkDone: (page: Int) -> Unit
) {
    val today = remember { LocalDate.now() }
    val startDate = remember(prefsState.startDateIso) {
        if (prefsState.startDateIso.isBlank()) null else LocalDate.parse(prefsState.startDateIso, ISO)
    }

    val task = remember(startDate, today) { if (startDate == null) null else getTaskForDate(startDate, today) }

    var lines by remember { mutableStateOf<List<String>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(task?.page) {
        if (task == null) return@LaunchedEffect
        loading = true
        lines = withContext(Dispatchers.IO) { repo.getPageLines(task.page) }
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ورد اليوم") },
                actions = {
                    TextButton(onClick = onOpenPlan) { Text("الجدول") }
                    TextButton(onClick = onOpenSettings) { Text("الإعدادات") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (startDate == null || task == null) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("ابدأ من الإعدادات", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("اختر تاريخ البدء ووقت التنبيه.")
                    }
                }
                return@Column
            }

            val done = prefsState.lastCompletedPage >= task.page

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("صفحة ${task.page}", style = MaterialTheme.typography.titleMedium)
                Text(if (done) "✅ منجزة" else "⏳ غير منجزة")
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    if (loading) Text("تحميل الصفحة...")
                    else lines.forEach { line ->
                        Text(line, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }

            Button(
                onClick = { onMarkDone(task.page) },
                enabled = !done,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (done) "تم الحفظ ✅" else "تم حفظ الصفحة") }
        }
    }
}
