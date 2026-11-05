# Release Notes

## v3.0 (Final PoE) - changes since the prototype

### Innovative features in this version
- **Real Google single sign-on** using Credential Manager and Firebase Auth. First-time Google users are created in the REST API automatically.
- **Offline mode with sync backed by SQLite.** Expenses and income saved without a connection are queued in a local database and pushed to the API on reconnect. Rows stay queued if a sync fails.
- **Real-time push notifications** through Firebase Cloud Messaging, including Android 13+ permission handling.
- **Multi-language support:** English, isiZulu and Afrikaans, switchable in Settings and saved across restarts.
- **Receipt photos** uploaded to Azure Blob Storage; savings goals stored in a NoSQL (MongoDB) database through the REST API.

### Fixes and improvements
- Removed a login bypass in the SSO button that signed users in as user 1 on error.
- Income now uses the logged-in user's ID and validates amount, source and date.
- Dark mode and currency settings now persist after restarting the app.
- Login and dashboard text moved into string resources (translated).
- Unit tests now exercise real app code (`Validators`, `BudgetMath`).
- GitHub Actions runs unit tests, builds the APK and uploads the test report and APK.
- Release build: R8 shrinking, signing config read from `keystore.properties`, version 3.0 (code 2).
