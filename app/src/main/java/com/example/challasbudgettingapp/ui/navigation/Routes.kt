package com.example.challasbudgettingapp.ui.navigation

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")

    data object Login : Routes("login")

    data object Register : Routes("register")

    data object ForgotPassword : Routes("forgot_password")

    data object Dashboard : Routes("dashboard")

    data object AddExpense : Routes("add_expense")

    data object AddIncome : Routes("add_income")

    data object Reports : Routes("reports")

    data object Settings : Routes("settings")
}