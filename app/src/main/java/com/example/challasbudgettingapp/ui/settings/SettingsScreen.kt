package com.example.challasbudgettingapp.ui.settings

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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var notificationsEnabled by rememberSaveable {
        mutableStateOf(true)
    }

    var currency by rememberSaveable {
        mutableStateOf("ZAR (R)")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Personalise your Challas Budget experience."
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
                    text = "Appearance",
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
                            text = "Dark Mode",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (darkMode) {
                                "Dark theme is enabled"
                            } else {
                                "Light theme is enabled"
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


        // CURRENCY


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Currency",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Current currency: $currency"
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
                    }
                ) {
                    Text("Change Currency")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // NOTIFICATIONS


        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (notificationsEnabled) {
                            "Budget notifications are enabled"
                        } else {
                            "Budget notifications are disabled"
                        }
                    )
                }

                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { enabled ->
                        notificationsEnabled = enabled
                    }
                )
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
                    text = "About Challas Budget",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Challas Budget helps users track income, " +
                            "expenses and monthly financial progress."
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Version 1.0"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Take Control of Every Rand."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // LOGOUT


        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}
