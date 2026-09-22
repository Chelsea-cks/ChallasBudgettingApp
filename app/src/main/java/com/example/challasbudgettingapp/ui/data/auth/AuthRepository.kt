package com.example.challasbudgettingapp.ui.data.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val tag = "ChallasBudgetAuth"


    fun register(
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        Log.d(tag, "Starting registration for: $email")

        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener { result ->

                Log.d(
                    tag,
                    "Firebase account created: ${result.user?.uid}"
                )

                val profileUpdate =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                result.user
                    ?.updateProfile(profileUpdate)
                    ?.addOnCompleteListener { profileResult ->

                        if (profileResult.isSuccessful) {

                            Log.d(
                                tag,
                                "User profile updated successfully"
                            )

                        } else {

                            Log.w(
                                tag,
                                "Account created but profile update failed",
                                profileResult.exception
                            )
                        }

                        onSuccess()
                    }
                    ?: onSuccess()
            }
            .addOnFailureListener { exception ->

                Log.e(
                    tag,
                    "Registration failed",
                    exception
                )

                onError(
                    getFriendlyErrorMessage(
                        exception.message
                    )
                )
            }
    }


    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        Log.d(
            tag,
            "Starting login for: $email"
        )

        auth.signInWithEmailAndPassword(
            email,
            password
        )
            .addOnSuccessListener {

                Log.d(
                    tag,
                    "Login successful"
                )

                onSuccess()
            }
            .addOnFailureListener { exception ->

                Log.e(
                    tag,
                    "Login failed",
                    exception
                )

                onError(
                    getFriendlyErrorMessage(
                        exception.message
                    )
                )
            }
    }


    fun sendPasswordReset(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        Log.d(
            tag,
            "Sending password reset email"
        )

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {

                Log.d(
                    tag,
                    "Password reset email sent"
                )

                onSuccess()
            }
            .addOnFailureListener { exception ->

                Log.e(
                    tag,
                    "Password reset failed",
                    exception
                )

                onError(
                    getFriendlyErrorMessage(
                        exception.message
                    )
                )
            }
    }


    fun logout() {

        Log.d(
            tag,
            "Logging out user"
        )

        auth.signOut()
    }


    fun getCurrentUser() =
        auth.currentUser


    fun isUserLoggedIn(): Boolean {

        return auth.currentUser != null
    }


    private fun getFriendlyErrorMessage(
        message: String?
    ): String {

        return when {

            message?.contains(
                "email address is badly formatted",
                ignoreCase = true
            ) == true ->

                "Please enter a valid email address."

            message?.contains(
                "already in use",
                ignoreCase = true
            ) == true ->

                "An account already exists with this email."

            message?.contains(
                "email-already-in-use",
                ignoreCase = true
            ) == true ->

                "An account already exists with this email."

            message?.contains(
                "password is invalid",
                ignoreCase = true
            ) == true ->

                "The password is incorrect."

            message?.contains(
                "wrong-password",
                ignoreCase = true
            ) == true ->

                "The password is incorrect."

            message?.contains(
                "invalid-credential",
                ignoreCase = true
            ) == true ->

                "The email or password is incorrect."

            message?.contains(
                "no user record",
                ignoreCase = true
            ) == true ->

                "No account was found with this email."

            message?.contains(
                "user-not-found",
                ignoreCase = true
            ) == true ->

                "No account was found with this email."

            message?.contains(
                "weak-password",
                ignoreCase = true
            ) == true ->

                "Your password is too weak. Please use at least 6 characters."

            message?.contains(
                "network",
                ignoreCase = true
            ) == true ->

                "Please check your internet connection."

            message?.contains(
                "too-many-requests",
                ignoreCase = true
            ) == true ->

                "Too many attempts. Please try again later."

            else ->

                "Something went wrong. Please try again."
        }
    }
}