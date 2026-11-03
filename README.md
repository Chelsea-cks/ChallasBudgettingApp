# Challas Budget

**Take Control of Every Rand.**

Challas Budget is an Android budgeting application designed to help users manage their personal income and expenses in a simple and organised way.

## Features

* User registration and login
* Firebase Authentication
* Password reset and logout
* Dashboard with income, expenses and balance
* Add and manage expenses
* Add and manage income
* Financial reports
* Currency settings
* Dark mode
* Notification settings
* RESTful API integration

## Technologies

* **Kotlin** – Android development
* **Jetpack Compose & Material 3** – User interface
* **Firebase Authentication** – User authentication
* **Retrofit** – REST API communication
* **ASP.NET Core Web API** – Backend
* **Entity Framework Core** – Database access
* **Microsoft SQL Server** – Database
* **GitHub** – Version control
* **GitHub Actions** – Automated testing and builds

## Architecture

```text
Android App
    ↓
Retrofit
    ↓
ASP.NET Core REST API
    ↓
Entity Framework Core
    ↓
SQL Server
```

Firebase Authentication is used to securely manage user credentials.

## API

The application communicates with the backend using RESTful endpoints for:

* Users
* Expenses
* Income

Expense and income records can be created, retrieved, updated and deleted through the API.

## Database

The SQL Server database contains:

* **Users**
* **Expenses**
* **Income**

Financial records are linked to users using `UserID`.

## Security & Validation

Firebase Authentication manages user passwords securely. Passwords are not stored as plaintext in the application's database.

The application validates user input, including email addresses, required fields and transaction amounts.

## Testing

The application is tested to ensure that:

* Registration and login work correctly
* Invalid input is handled safely
* Expenses and income can be recorded
* API communication works
* Database operations work correctly
* Application navigation works correctly

## GitHub

GitHub is used to manage and track the project's source code and development progress.

GitHub Actions is used to automate the application's build and testing process.

## Future Enhancements

Planned features for the final version include:

* Google Sign-In
* Offline mode and synchronisation
* Push notifications
* Multilingual support
* Additional South African languages
* Play Store preparation

## Author

**Chelsea**

**Project:** Challas Budget
**Platform:** Android
**Backend:** ASP.NET Core Web API
**Database:** Microsoft SQL Server
