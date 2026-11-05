package com.example.challasbudgettingapp.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.challasbudgettingapp.R
import com.example.challasbudgettingapp.data.model.User
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.challasbudgettingapp.data.auth.AuthRepository
import com.example.challasbudgettingapp.data.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    val authRepository = AuthRepository()
    val userRepository = UserRepository()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
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
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sign_in_sub)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
                errorMessage = ""
            },

            label = {
                Text(stringResource(R.string.email))
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
                errorMessage = ""
            },

            label = {
                Text(stringResource(R.string.password))
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

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            onClick = onForgotPasswordClick,
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.forgot_password))
        }

        if (errorMessage.isNotEmpty()) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

                    password.isBlank() -> {
                        errorMessage =
                            "Please enter your password."
                    }

                    else -> {

                        isLoading = true
                        errorMessage = ""

                        authRepository.login(

                            email = email.trim(),

                            password = password,

                            onSuccess = {

                                // Get the email of the Firebase user.
                                val firebaseEmail =
                                    authRepository.getCurrentUser()?.email

                                if (firebaseEmail.isNullOrBlank()) {

                                    isLoading = false

                                    errorMessage =
                                        "Could not retrieve your account email."

                                } else {

                                    // Find the matching user in SQL Server.
                                    scope.launch {

                                        try {

                                            val databaseUser =
                                                userRepository.getUserByEmail(
                                                    firebaseEmail
                                                )

                                            isLoading = false

                                            // Pass the SQL Server user ID
                                            // to the navigation system.
                                            onLoginSuccess(
                                                databaseUser.userID
                                            )

                                        } catch (e: Exception) {

                                            isLoading = false

                                            errorMessage =
                                                "Login successful, but your user details could not be retrieved. Please try again."
                                        }
                                    }
                                }
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
                Text(stringResource(R.string.login))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SINGLE SIGN-ON (SSO): real Google sign-in via Credential Manager + Firebase
        OutlinedButton(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    try {
                        // default_web_client_id is generated by the google-services plugin
                        // once Google sign-in is enabled in Firebase and google-services.json is re-downloaded.
                        val clientIdRes = context.resources.getIdentifier(
                            "default_web_client_id", "string", context.packageName
                        )
                        if (clientIdRes == 0) {
                            errorMessage = "Google sign-in is not configured yet."
                            isLoading = false
                            return@launch
                        }

                        val option = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(context.getString(clientIdRes))
                            .build()
                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(option)
                            .build()

                        val result = CredentialManager.create(context)
                            .getCredential(context, request)
                        val credential = result.credential

                        if (credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        ) {
                            val token = GoogleIdTokenCredential.createFrom(credential.data).idToken
                            authRepository.signInWithGoogle(
                                idToken = token,
                                onSuccess = {
                                    scope.launch {
                                        try {
                                            val user = authRepository.getCurrentUser()
                                            val email = user?.email.orEmpty()
                                            // Find the API user, or create one for first-time Google users.
                                            val dbUser = try {
                                                userRepository.getUserByEmail(email)
                                            } catch (e: Exception) {
                                                Log.w("LoginScreen", "Creating API user for SSO: ${e.message}")
                                                userRepository.createUser(
                                                    User(
                                                        fullName = user?.displayName ?: email,
                                                        email = email
                                                    )
                                                )
                                            }
                                            isLoading = false
                                            onLoginSuccess(dbUser.userID)
                                        } catch (e: Exception) {
                                            Log.e("LoginScreen", "SSO user lookup failed", e)
                                            authRepository.logout()
                                            isLoading = false
                                            errorMessage = "Signed in, but we could not reach the server."
                                        }
                                    }
                                },
                                onError = { msg ->
                                    isLoading = false
                                    errorMessage = msg
                                }
                            )
                        } else {
                            isLoading = false
                            errorMessage = "Unexpected sign-in response."
                        }
                    } catch (e: GetCredentialException) {
                        Log.e("LoginScreen", "Google credential error", e)
                        isLoading = false
                        errorMessage = "Google sign-in was cancelled or failed."
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Google SSO error", e)
                        isLoading = false
                        errorMessage = "Something went wrong. Please try again."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.google_sso))
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.create_account))
        }
    }
}