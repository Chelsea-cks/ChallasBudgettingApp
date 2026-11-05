
package com.example.challasbudgettingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.challasbudgettingapp.ui.navigation.ChallasNavGraph
import com.example.challasbudgettingapp.ui.theme.ChallasBudgettingAppTheme
import com.example.challasbudgettingapp.util.SettingsStore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BudgetQuestFCM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var darkMode by rememberSaveable {
                mutableStateOf(SettingsStore.isDarkMode(this@MainActivity))
            }

            val permissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->
                    Log.d(TAG, "Notification permission granted: $granted")
                }

            LaunchedEffect(Unit) {
                Log.d(TAG, "MainActivity started")

                if (Build.VERSION.SDK_INT >= 33) {
                    val permissionGranted =
                        ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                    if (!permissionGranted) {
                        permissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                }

                try {
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    TAG,
                                    "Firebase messaging token retrieved successfully"
                                )
                            } else {
                                Log.e(
                                    TAG,
                                    "Failed to retrieve Firebase messaging token",
                                    task.exception
                                )
                            }
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "Firebase messaging error", e)
                }
            }

            ChallasBudgettingAppTheme(darkTheme = darkMode) {
                ChallasNavGraph(
                    darkMode = darkMode,
                    onDarkModeChanged = { enabled ->
                        darkMode = enabled
                        SettingsStore.setDarkMode(this@MainActivity, enabled)
                    }
                )
            }
        }
    }
}
