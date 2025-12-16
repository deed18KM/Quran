package com.example.quranmemorizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quranmemorizer.data.AppPrefs

@Composable
fun SettingsScreen(
    prefsState: AppPrefs,
    onBack: () -> Unit,
    onPickStartDate: () -> Unit,
    onPickTime: () -> Unit,
    onEnableReminder: () -> Unit,
    onDisableReminder: () -> Unit,
    onToggleDark: (Boolean) -> Unit,
    onOpenReview: () -> Unit,
    onOpenStats: () -> Unit,
    onExportPdf: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الإعدادات") },
                navigationIcon = { TextButton(onClick = onBack) { Text("رجوع") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("تاريخ البدء", style = MaterialTheme.typography.titleMedium)
                    Text(if (prefsState.startDateIso.isBlank()) "غير محدد" else prefsState.startDateIso)
                    Button(onClick = onPickStartDate, modifier = Modifier.fillMaxWidth()) { Text("اختيار تاريخ البدء") }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("وقت التنبيه", style = MaterialTheme.typography.titleMedium)
                    Text(String.format("%02d:%02d", prefsState.reminderHour, prefsState.reminderMinute))
                    Button(onClick = onPickTime, modifier = Modifier.fillMaxWidth()) { Text("تغيير وقت التنبيه") }

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = onEnableReminder, modifier = Modifier.weight(1f)) { Text("تفعيل") }
                        OutlinedButton(onClick = onDisableReminder, modifier = Modifier.weight(1f)) { Text("إيقاف") }
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("الوضع الليلي", style = MaterialTheme.typography.titleMedium)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (prefsState.darkMode) "مفعل" else "غير مفعل")
                        Switch(checked = prefsState.darkMode, onCheckedChange = onToggleDark)
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ميزات", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = onOpenReview, modifier = Modifier.fillMaxWidth()) { Text("وضع المراجعة") }
                    Button(onClick = onOpenStats, modifier = Modifier.fillMaxWidth()) { Text("الإحصائيات") }
                    Button(onClick = onExportPdf, modifier = Modifier.fillMaxWidth()) { Text("تصدير التقدم PDF") }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ملاحظة ملفات المصحف", style = MaterialTheme.typography.titleMedium)
                    Text("ضع ملفات الصفحات داخل: assets/mushaf/page-001.json ... page-604.json")
                }
            }
        }
    }
}
