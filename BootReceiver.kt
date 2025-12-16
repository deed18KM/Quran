package com.example.quranmemorizer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.quranmemorizer.data.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.intent.action.BOOT_COMPLETED") return

        CoroutineScope(Dispatchers.IO).launch {
            val prefs = Prefs(context).flow.first()
            if (prefs.startDateIso.isNotBlank()) {
                AlarmScheduler.scheduleDaily(context, prefs.reminderHour, prefs.reminderMinute)
            }
        }
    }
}
