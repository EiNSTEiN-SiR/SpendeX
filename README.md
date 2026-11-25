# SpendeX by Emtrix

SpendeX is a lightweight personal finance and expense tracker for Android. It is built with Kotlin and Jetpack Compose, with local JSON persistence and no account or cloud requirement.

## Features

- At-a-glance balance, current-month income, and current-month expenses
- BDT and USD display currency toggle
- Light and AMOLED dark themes, with the last selected theme saved between launches
- BDT and USD display preference saved between launches
- Custom adaptive Android launcher icon
- Add income and expense transactions with category, note, and date
- Category-based spending breakdown
- Search and filter the transaction ledger
- CSV export through the Android share interface
- Local JSON storage in the app's private files directory
- Animated branded launch screen

## Tech Stack

- Kotlin 2.0.21
- Jetpack Compose and Material 3
- Android Gradle Plugin 8.7.3
- Gradle 8.9
- Kotlinx Serialization JSON
- Kotlin Coroutines and StateFlow
- Minimum Android API 26, compile/target SDK 35
- JDK 17

## Build

Open the repository in Android Studio with JDK 17 configured, allow Gradle sync, then run the `app` configuration.

To build a debug APK from the command line:

```bash
gradle assembleDebug
```

The APK is generated at:

```text
app/build/outputs/apk/debug/SpendeX.apk
```

The GitHub Actions workflow builds the APK on every push and uploads it as an artifact named `SpendeX`.

## Architecture

```text
com.emtrix.spendex
├── model
│   └── Transaction.kt
├── data
│   └── TransactionStorage.kt
├── viewmodel
│   └── FinanceViewModel.kt
├── ui
│   ├── components
│   │   ├── AddTransactionSheet.kt
│   │   ├── CategoryBreakdownCard.kt
│   │   ├── SplashScreen.kt
│   │   ├── SummarySection.kt
│   │   └── TransactionItem.kt
│   └── theme
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt
```

The UI observes immutable `FinanceState` through `StateFlow`. `FinanceViewModel` owns filtering and summary calculations, while `TransactionStorage` handles asynchronous JSON reads and writes on the IO dispatcher. The storage file is private to the app sandbox and is removed when the app is uninstalled or its data is cleared.

## Notes

- Display currency changes the symbol and formatting only. It does not perform foreign-exchange conversion.
- CSV export shares the CSV content as text with compatible Android share targets.
- The debug build is intended for development and testing. Configure signing before distributing a production release.
