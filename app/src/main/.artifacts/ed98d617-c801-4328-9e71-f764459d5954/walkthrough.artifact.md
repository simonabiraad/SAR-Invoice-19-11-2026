# Walkthrough - Cash Flow & Income/Expense Integration

Successfully added the **Income and Expense** section (matching Reference Image 2) alongside the **Cash Flow** section on the Dashboard, fully connected to the application's invoice, bill, and expense systems.

## Changes Made

### 1. Income and Expense Section (`MainActivity.kt`)
- Created `IncomeAndExpenseSection` composable featuring:
  - **Accrual / Cash Toggle**: Allows switching between Accrual basis (counting all invoices and bills/expenses) and Cash basis (counting only paid invoices and paid bills/expenses).
  - **Automatic Totals**: Computes and displays total Income and Expense in real time.
  - **Fiscal Period Header**: Displays the selected fiscal period badge.

### 2. Cash Flow & Invoice Integration (`MainActivity.kt`)
- Cash Flow table automatically synchronizes with issued invoices (Incoming from paid invoices) and outgoing expenses/bills.
- Real-time updates occur instantly whenever transactions are added, updated, or deleted without double-counting.

### 3. Verification
- Rebuilt the app (`app:assembleDebug`) and ran unit tests (`app:testDebugUnitTest`), confirming a 100% successful build with zero errors.
