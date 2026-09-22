package com.example.challasbudgettingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challasbudgettingapp.ui.auth.ForgotPasswordScreen
import com.example.challasbudgettingapp.ui.auth.LoginScreen
import com.example.challasbudgettingapp.ui.auth.RegisterScreen
import com.example.challasbudgettingapp.ui.auth.SplashScreen
import com.example.challasbudgettingapp.ui.dashboard.DashboardScreen
import com.example.challasbudgettingapp.ui.expense.AddExpenseScreen
import com.example.challasbudgettingapp.ui.income.AddIncomeScreen
import com.example.challasbudgettingapp.ui.reports.ReportsScreen
import com.example.challasbudgettingapp.ui.settings.SettingsScreen

@Composable
fun ChallasNavGraph(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit
) {

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {

            SplashScreen(
                onLoggedIn = {
                    navController.navigate(Routes.Dashboard.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                },

                onLoggedOut = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.Login.route) {

            LoginScreen(
                onLoginSuccess = {
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

        composable(Routes.Register.route) {

            RegisterScreen(
                onRegisterSuccess = {
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

        composable("forgot_password") {

            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

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
                }
            )
        }

        composable(Routes.AddExpense.route) {

            AddExpenseScreen(
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.AddIncome.route) {

            AddIncomeScreen(
                onSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.Reports.route) {

            ReportsScreen()
        }

        composable(Routes.Settings.route) {

            SettingsScreen(
                darkMode = darkMode,

                onDarkModeChanged = { enabled ->
                    onDarkModeChanged(enabled)
                },

                onLogout = {

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