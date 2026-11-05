package com.example.challasbudgettingapp.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.challasbudgettingapp.util.SettingsStore
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import com.example.challasbudgettingapp.R
import com.example.challasbudgettingapp.notifications.NotificationHelper

@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    var notificationsEnabled by rememberSaveable {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= 33) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationsEnabled = isGranted
    }

    val settingsContext = androidx.compose.ui.platform.LocalContext.current
    var currency by rememberSaveable {
        mutableStateOf(SettingsStore.getCurrency(settingsContext))
    }

    var selectedLanguage by rememberSaveable {
        mutableStateOf("English")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.settings_subtitle)
        )

        Spacer(modifier = Modifier.height(24.dp))


        // APPEARANCE


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.appearance),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = stringResource(R.string.dark_mode),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (darkMode) {
                                stringResource(R.string.dark_mode_enabled)
                            } else {
                                stringResource(R.string.light_mode_enabled)
                            }
                        )
                    }

                    Switch(
                        checked = darkMode,
                        onCheckedChange = { enabled ->
                            onDarkModeChanged(enabled)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // LANGUAGE (SOUTH AFRICAN LANGUAGES)


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.current_language, selectedLanguage)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            selectedLanguage = "English"
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("en")
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.english))
                    }

                    Button(
                        onClick = {
                            selectedLanguage = "isiZulu"
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("zu")
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.isizulu))
                    }

                    Button(
                        onClick = {
                            selectedLanguage = "Afrikaans"
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("af")
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.afrikaans))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // CURRENCY


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.currency),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.current_currency, currency)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        currency =
                            if (currency == "ZAR (R)") {
                                "USD ($)"
                            } else {
                                "ZAR (R)"
                            }
                        SettingsStore.setCurrency(settingsContext, currency)
                    }
                ) {
                    Text(stringResource(R.string.change_currency))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // NOTIFICATIONS


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = stringResource(R.string.notifications),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (notificationsEnabled) {
                                stringResource(R.string.notifications_enabled)
                            } else {
                                stringResource(R.string.notifications_disabled)
                            }
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled && Build.VERSION.SDK_INT >= 33) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED

                                if (!hasPermission) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    notificationsEnabled = true
                                    NotificationHelper.showNotification(
                                        context,
                                        "Notifications Enabled",
                                        "You will receive budget updates and financial reminders."
                                    )
                                }
                            } else {
                                notificationsEnabled = enabled
                                if (enabled) {
                                    NotificationHelper.showNotification(
                                        context,
                                        "Notifications Enabled",
                                        "You will receive budget updates and financial reminders."
                                    )
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            NotificationHelper.showNotification(
                                context,
                                "Challas Budget Alert",
                                "Real-time Notification: Your monthly balance was updated!"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send Test Push Notification")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // ABOUT


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.about_app),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.about_desc)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.version)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.motto)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // LOGOUT


        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.logout))
        }
    }
}
