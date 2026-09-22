package com.example.challasbudgettingapp.ui.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.ui.data.auth.AuthRepository

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {

    val authRepository = AuthRepository()

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var message by rememberSaveable {
        mutableStateOf("")
    }

    var errorMessage by rememberSaveable {
        mutableStateOf("")
    }

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Enter your email address and we will send you a password reset link."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
                message = ""
                errorMessage = ""
            },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading
        )

        if (message.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = {

                when {

                    email.isBlank() -> {

                        errorMessage =
                            "Please enter your email address."
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches() -> {

                        errorMessage =
                            "Please enter a valid email address."
                    }

                    else -> {

                        isLoading = true
                        message = ""
                        errorMessage = ""

                        authRepository.sendPasswordReset(

                            email = email.trim(),

                            onSuccess = {

                                isLoading = false

                                message =
                                    "Password reset email sent. Please check your inbox."
                            },

                            onError = { error ->

                                isLoading = false

                                errorMessage = error
                            }
                        )
                    }
                }

            },

            modifier = Modifier.fillMaxWidth(),

            enabled = !isLoading
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    strokeWidth = 2.dp
                )

            } else {

                Text("Send Reset Email")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(
            onClick = onBackToLogin,

            modifier = Modifier.fillMaxWidth(),

            enabled = !isLoading
        ) {

            Text("Back to Login")
        }
    }
}