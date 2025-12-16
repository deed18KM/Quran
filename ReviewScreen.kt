package com.example.quranmemorizer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quranmemorizer.data.AppPrefs
import com.example.quranmemorizer.quran.QuranPagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ReviewScreen(
    prefsState: AppPrefs,
    repo: QuranPagesRepository,
    onBack: () -> Unit
) {
    val maxDone = prefsState.lastCompletedPage
    val candidates = remember(maxDone) {
        if (maxDone <= 0) emptyList()
        else (maxOf(1, maxDone - 6)..maxDone).toList().reversed()
    }

    var selected by remember { mutableStateOf(candidates.firstOrNull() ?: 1) }
    var lines by remember { mutableStateOf<List<String>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(selected) {
        if (candidates.isEmpty()) return@LaunchedEffect
        loading = true
        lines = withContext(Dispatchers.IO) { repo.getPageLines(selected) }
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("المراجعة") },
                navigationIcon = { TextButton(onClick = onBack) { Text("رجوع") } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (candidates.isEmpty()) {
                Card(Modifier.fillMaxWidth()) { Text("لا توجد صفحات منجزة بعد.", Modifier.padding(16.dp)) }
                return@Column
            }

            Text("اختر صفحة:", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                candidates.take(6).forEach { p ->
                    AssistChip(onClick = { selected = p }, label = { Text("ص $p") })
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    if (loading) Text("تحميل...")
                    else lines.forEach { Text(it, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 4.dp)) }
                }
            }
        }
    }
}
