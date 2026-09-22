package com.example.challasbudgettingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.challasbudgettingapp.ui.navigation.ChallasNavGraph
import com.example.challasbudgettingapp.ui.theme.ChallasBudgettingAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            var darkMode by rememberSaveable {
                mutableStateOf(false)
            }

            ChallasBudgettingAppTheme(
                darkTheme = darkMode
            ) {
                ChallasNavGraph(
                    darkMode = darkMode,
                    onDarkModeChanged = { enabled ->
                        darkMode = enabled
                    }
                )
            }
        }
    }
}