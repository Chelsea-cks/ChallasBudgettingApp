
package com.example.challasbudgettingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challasbudgettingapp.data.auth.AuthRepository
import com.example.challasbudgettingapp.ui.auth.ForgotPasswordScreen
import com.example.challasbudgettingapp.ui.auth.LoginScreen
import com.example.challasbudgettingapp.ui.auth.RegisterScreen
import com.example.challasbudgettingapp.ui.auth.SplashScreen
import com.example.challasbudgettingapp.ui.dashboard.DashboardScreen
import com.example.challasbudgettingapp.ui.expense.AddExpenseScreen
import com.example.challasbudgettingapp.ui.income.AddIncomeScreen
import com.example.challasbudgettingapp.ui.reports.ReportsScreen
import com.example.challasbudgettingapp.ui.settings.SettingsScreen
import com.example.challasbudgettingapp.ui.savings.SavingsGoalsScreen

@Composable
fun ChallasNavGraph(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit
) {

    val navController: NavHostController = rememberNavController()

    // Stores the SQL Server ID of the currently logged-in user.
    var currentUserId by rememberSaveable {
        mutableStateOf(0)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // SPLASH SCREEN
        composable(Routes.Splash.route) {
            SplashScreen(
                onLoggedIn = { userId ->
                    currentUserId = userId

                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onLoggedOut = {
                    currentUserId = 0

                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // LOGIN SCREEN
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = { userId ->
                    currentUserId = userId

                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                }
            )
        }

        // REGISTER SCREEN
        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { userId ->
                    currentUserId = userId

                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Register.route) {
                            inclusive = true
                        }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // FORGOT PASSWORD SCREEN
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // DASHBOARD
        composable(Routes.Dashboard.route) {
            DashboardScreen(
                onAddExpense = {
                    navController.navigate(Routes.AddExpense.route)
                },
                onAddIncome = {
                    navController.navigate(Routes.AddIncome.route)
                },
                onReports = {
                    navController.navigate(Routes.Reports.route)
                },
                onSettings = {
                    navController.navigate(Routes.Settings.route)
                },
                onSavingsGoals = {
                    navController.navigate(Routes.SavingsGoals.route)
                }
            )
        }

        // SAVINGS GOALS SCREEN
        composable(Routes.SavingsGoals.route) {
            SavingsGoalsScreen(
                userId = currentUserId.toString(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ADD EXPENSE SCREEN
        composable(Routes.AddExpense.route) {
            AddExpenseScreen(
                userID = currentUserId,
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        // ADD INCOME SCREEN
        composable(Routes.AddIncome.route) {
            AddIncomeScreen(
                userID = currentUserId,
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        // REPORTS SCREEN
        composable(Routes.Reports.route) {
            ReportsScreen()
        }

        // SETTINGS SCREEN
        composable(Routes.Settings.route) {
            SettingsScreen(
                darkMode = darkMode,
                onDarkModeChanged = { enabled ->
                    onDarkModeChanged(enabled)
                },
                onLogout = {
                    AuthRepository().logout()
                    currentUserId = 0

                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Dashboard.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
