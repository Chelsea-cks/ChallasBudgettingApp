# Challas Budget – Personal Finance & Budgeting App

**"Take Control of Every Rand."**

Challas Budget is a full-featured Android personal finance management application designed to empower users to track incomes, manage expenses, set savings goals, and visualize financial growth cleanly and intuitively. Built using modern Android architecture, Jetpack Compose, Firebase, and REST API integration with Azure Blob Storage.

---

## Demonstration Video

* **Video Link:** [YouTube Demonstration Video (Unlisted)](https://youtube.com/shorts/TBveMCwrivQ?feature=share)
* *Note:* The demonstration video showcases full app functionality running on a physical Android mobile device with voiceover explanation, database/API backend verification, Firebase auth, FCM notifications, offline sync, and multi-language support.

---

##  Key Features & Innovations

### 1. Authentication & Single Sign-On (SSO)
* Secure registration and login powered by **Firebase Authentication**.
* Encrypted password protection managed securely by Firebase auth services.
* **Single Sign-On (SSO):** Quick one-tap Google SSO sign-in capability.
* Password reset and secure session management.

### 2. REST API & Backend Integration
* Connected to an **ASP.NET Core RESTful Web API** with Entity Framework Core and Microsoft SQL Server.
* Full CRUD operations for Users, Expenses, Incomes, and Savings Goals.

### 3.  Azure Blob Storage (Receipt Uploads)
* Integrated **Azure Blob Storage** for uploading and retrieving high-resolution receipt images.
* Powered by `Coil` image loader for fast asynchronously rendered image previews.

### 4.  Offline Mode with Synchronization
* Users can log expenses and incomes even without an active internet connection.
* Offline data is stored locally (`OfflineSyncManager`) and automatically synchronized with the REST API once network connectivity is restored.

### 5.  Real-Time Push Notifications (FCM)
* Integrated **Firebase Cloud Messaging (FCM)** for instant push notifications and budget reminders (`BudgetQuestMessagingService`).
* Full Android 13+ (`POST_NOTIFICATIONS`) runtime permission consent handling.

### 6. 🇿🇦 Multi-Language Support (South African Languages)
* Full multilingual support for South African languages:
  * 🇬🇧 **English** (`values/strings.xml`)
  * 🇿🇦 **isiZulu** (`values-zu/strings.xml`)
  * 🇿🇦 **Afrikaans** (`values-af/strings.xml`)
* Dynamic language switching in Settings.

### 7.  Appearance & User Experience
* Custom **Dark Mode / Light Mode** switching using Compose `Material3` color schemes.
* Currency selection (ZAR `R` / USD `$`).
* Responsive layouts with clear user feedback and invalid input handling.

---

##  Architecture & Technology Stack

```text
[ Android App (Jetpack Compose + Kotlin) ]
      │
      ├──> Firebase Auth (Authentication & SSO)
      ├──> Firebase Messaging (Real-time FCM Notifications)
      ├──> Local Offline Cache (OfflineSyncManager)
      │
      └──> Retrofit 2 REST Client
                │
                ├──> Azure Blob Storage (Receipt Images)
                └──> ASP.NET Core Web API (Entity Framework Core)
                          │
                          └──> Microsoft SQL Server
```

---

##  Automated Testing & GitHub Actions (CI/CD)

Continuous Integration is set up using **GitHub Actions** located at `.github/workflows/build.yml`.

### Automated Workflow Pipeline:
1. **Trigger:** Fires automatically on every `push` or `pull_request` to `main` / `master`.
2. **Environment:** Ubuntu-latest runner with JDK 17 and Android SDK.
3. **Tasks Executed:**
   * `./gradlew test` – Executes automated unit tests for budget calculation and input validation logic.
   * `./gradlew assembleDebug` – Compiles the Android debug APK to ensure no build errors exist across environments.

---

##  Release Notes (PoE Progress Update)

### Version 1.0 (Final PoE Release)
* **New:** Azure Blob Storage receipt upload integration.
* **New:** Single Sign-On (SSO) support via Google & Firebase Auth.
* **New:** Real-time push notification system using Firebase Cloud Messaging (FCM).
* **New:** Offline storage and automatic cloud synchronization manager.
* **New:** Multi-language support (English, isiZulu, Afrikaans).
* **Fix:** Resolved Dark Mode color scheme application across Jetpack Compose screens.
* **Fix:** Corrected notification permission consent prompt on Android 13+ (API 33+).
* **Fix:** Fixed Firebase Auth session persistence on user logout.
* **Test:** Added automated JUnit test suites for input validation and budget balance calculations.

---

##  Google Play Store Publication Preparation

The application is configured and ready for release on the Google Play Store:
* **Package Name:** `com.example.challasbudgettingapp`
* **Target SDK:** API 35/37 (Android 15)
* **Min SDK:** API 26 (Android 8.0)
* **Build Tasks:** Gradle `assembleRelease` & `bundleRelease` configured for signed APK / Android App Bundle generation.
* **Assets:** Custom app launcher icons, high-density adaptive drawables, and Play Store graphics.

---

##  AI Tools Utilization Write-Up (Max 500 Words)

During the development of Challas Budget, artificial intelligence tools—specifically Android Studio AI Assistant and Large Language Models (LLMs)—were utilized as supportive development aids. 

### Areas of AI Assistance:
1. **Code Architecture & Refactoring:** AI tools provided guidance on structuring Kotlin coroutines with Retrofit REST clients, implementing clean Jetpack Compose state management (`rememberSaveable`), and configuring FCM (`FirebaseMessagingService`) and notification channels according to modern Android standards.
2. **Debugging & Error Diagnostics:** AI was consulted to diagnose Jetpack Compose theme inheritance issues (wrapping custom `ChallasBudgettingAppTheme` around `NavHost`), as well as resolving Android 13+ `POST_NOTIFICATIONS` runtime permission handling.
3. **Multi-Language Translations:** AI assisted in providing initial South African language string translations (isiZulu and Afrikaans) for UI labels in `strings.xml`.
4. **CI/CD Workflow Configuration:** AI provided template references for creating the GitHub Actions YAML workflow (`.github/workflows/build.yml`) to ensure smooth automated build and testing execution on GitHub runners.

All AI-generated snippets and suggestions were reviewed, modified, integrated, and verified by the author to ensure code quality, consistency, and alignment with project guidelines.

---

##  Author

* **Student / Author:** Chelsea
* **Project:** Challas Budget
* **Institution:** IIE (The Independent Institute of Education)
* **Year:** 2026

---


## AI usage declaration
AI was used to understand how to implement firebase. AI was consulated with the implementation of the azure storage, the SSO.
