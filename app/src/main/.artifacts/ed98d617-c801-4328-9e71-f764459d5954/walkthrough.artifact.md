# Walkthrough - SAR Invoice Dashboard Feature Parity

Successfully integrated all features, sections, and options from the reference software into the SAR Invoice Dashboard (Home tab).

## Changes Made

### 1. Data Models & Storage (`Data.kt`)
- Added `Bill`, `Expense`, and `TimeLog` data classes.
- Updated `Db` and `Store` JSON persistence to load and save bills, expenses, and time logs.

### 2. ViewModel Logic (`AppVM.kt`)
- Added methods and state for creating/managing bills, recording expenses, logging time, and running the live project timer (`startTimer`, `stopAndLogTimer`, `pauseTimer`).

### 3. Dashboard UI & Features (`MainActivity.kt`)
- **Top Sub-Tabs**: Added **Dashboard**, **Announcements**, and **Help** tabs right below the app bar.
- **Quick Create Section**: Added interactive buttons for **New Customer**, **New Invoice**, **New Bill**, and **New Expense** with fully functional creation dialogs.
- **Cash Flow Section**: Added period selector ("This Fiscal Year"), description card, and action buttons (`+ Add Invoice`, `+ Add Bill`).
- **Project Summary & Timer Section**:
  - Live project timer widget showing elapsed time (`00:00`), `Start Timer` / `Stop & Log`, and `Log Time` dialog.
  - Unbilled Hours and Unbilled Expenses summary cards.
- **Top Expenses Section**: Added expense breakdown, period selector, and `Record Expense` action.
- **Announcements & Help Tabs**: Fully implemented informational tabs for updates and support.

## Verification Results

### Automated Tests
- Build test: `app:assembleDebug` completed successfully.
- Unit tests: `app:testDebugUnitTest` passed successfully.
