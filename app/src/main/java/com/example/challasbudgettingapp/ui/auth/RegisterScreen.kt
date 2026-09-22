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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.ui.data.auth.AuthRepository

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    val authRepository = AuthRepository()

    var fullName by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by rememberSaveable {
        mutableStateOf(false)
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
            text = "Create Your Account",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Start managing your money with Challas Budget."
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = fullName,

            onValueChange = {
                fullName = it
                errorMessage = ""
            },

            label = {
                Text("Full Name")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
                errorMessage = ""
            },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
                errorMessage = ""
            },

            label = {
                Text("Password")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading,

            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    },

                    enabled = !isLoading
                ) {

                    Text(
                        if (passwordVisible) {
                            "Hide"
                        } else {
                            "Show"
                        }
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword,

            onValueChange = {
                confirmPassword = it
                errorMessage = ""
            },

            label = {
                Text("Confirm Password")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading,

            visualTransformation =
                if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {

                IconButton(
                    onClick = {
                        confirmPasswordVisible =
                            !confirmPasswordVisible
                    },

                    enabled = !isLoading
                ) {

                    Text(
                        if (confirmPasswordVisible) {
                            "Hide"
                        } else {
                            "Show"
                        }
                    )
                }
            }
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                when {

                    fullName.isBlank() -> {
                        errorMessage =
                            "Please enter your full name."
                    }

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

                    password.length < 6 -> {
                        errorMessage =
                            "Password must be at least 6 characters."
                    }

                    password != confirmPassword -> {
                        errorMessage =
                            "Passwords do not match."
                    }

                    else -> {

                        isLoading = true
                        errorMessage = ""

                        authRepository.register(

                            fullName = fullName.trim(),

                            email = email.trim(),

                            password = password,

                            onSuccess = {

                                isLoading = false

                                onRegisterSuccess()
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

                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Already have an account? Login")
        }
    }
}