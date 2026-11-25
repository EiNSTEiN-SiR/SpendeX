# SpendeX

A lightweight, offline-first personal finance tracker built with Kotlin and Jetpack Compose.

## What's included
- Monthly income, expense and overall balance summary
- Expense category breakdown
- Add income and expense transactions with date and optional note
- Search and filter transaction history
- Delete transactions with undo
- CSV export through Android's share sheet
- BDT / USD display toggle
- Persistent light and dark theme preference
- Local JSON storage, no account or internet connection required

## Build
The GitHub Actions workflow builds a debug APK with JDK 17 and Gradle 8.9. Run it from the Actions tab or push to the configured default branch.

APK output: `app/build/outputs/apk/debug/SpendeX.apk`
