# Implementation Plan - Receivables/Payables Detail Bottom Sheet & Overdue Filters

Implement the interactive Receivables & Payables detail bottom sheet (with Current vs Overdue breakdown and overdue split by days) and wire up Overdue Invoices and Overdue Bills navigation with filter tabs.

## User Review Required

- **Receivables & Payables Detail Bottom Sheet**:
  - Tapping Total Receivables or Total Payables opens a bottom sheet with tabs for **TOTAL RECEIVABLES** and **TOTAL PAYABLES**.
  - Displays total amount, Current vs Overdue split, and Overdue Split by days (1–15 Days, 16–30 Days, 31–45 Days, > 45 Days).
- **Overdue Invoices & Bills Navigation**:
  - Tapping Overdue Invoices opens the Invoices list with the **Overdue** filter tab selected.
  - Tapping Overdue Bills opens the Bills list with the **Overdue** filter tab selected.

## Proposed Changes

### 1. `MainActivity.kt`
- Create `ReceivablesPayablesBottomSheet` composable.
- Update `SummaryCardsSection` click listeners to open the bottom sheet or navigate to Overdue Invoices/Bills.
- Update `Invoices` and `Bills` screens to support All, Unpaid, and Overdue filter tabs.

## Verification Plan

### Automated Tests
- Build test: `gradle_build("app:assembleDebug")`
- Unit tests: `gradle_build("app:testDebugUnitTest")`

### Manual Verification
- Test tapping Total Receivables/Payables to open the breakdown sheet, and tapping Overdue Invoices/Bills to view filtered lists.
