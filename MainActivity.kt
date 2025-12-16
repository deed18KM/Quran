package com.example.quranmemorizer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.quranmemorizer.data.Prefs
import com.example.quranmemorizer.ui.AppNav

class MainActivity : ComponentActivity() {

    private val reqNotif = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            reqNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            val prefs = remember { Prefs(this) }
            val state by prefs.flow.collectAsState(initial = com.example.quranmemorizer.data.AppPrefs())
            MaterialTheme(colorScheme = if (state.darkMode) darkColorScheme() else lightColorScheme()) {
                Surface { AppNav(context = this) }
            }
        }
    }
}
