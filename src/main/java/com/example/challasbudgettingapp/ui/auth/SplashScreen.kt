
package com.example.challasbudgettingapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.data.auth.AuthRepository
import com.example.challasbudgettingapp.data.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoggedIn: (Int) -> Unit,
    onLoggedOut: () -> Unit
) {
    LaunchedEffect(Unit) {
        try {
            delay(1200)

            // Initialise repositories inside the protected block.
            val authRepository = AuthRepository()
            val userRepository = UserRepository()

            // Check whether a Firebase user is signed in.
            val firebaseUser = authRepository.getCurrentUser()

            if (firebaseUser == null) {
                onLoggedOut()
            } else {
                val firebaseEmail = firebaseUser.email

                if (firebaseEmail.isNullOrBlank()) {
                    onLoggedOut()
                } else {
                    // Retrieve the user's SQL Server ID.
                    val databaseUser =
                        userRepository.getUserByEmail(firebaseEmail)

                    onLoggedIn(databaseUser.userID)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e(
                "BudgetQuestSplash",
                "Startup authentication or database lookup failed",
                e
            )

            onLoggedOut()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Challas Budget",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Take Control of Every Rand")

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator()
    }
}
