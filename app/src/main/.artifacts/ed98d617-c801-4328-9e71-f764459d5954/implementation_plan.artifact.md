# Implementation Plan - Navigation Drawer Sidebar Menu matching Reference Screenshot

Recreate the exact Navigation Drawer sidebar menu from the reference screenshot within the Compose-based SAR Invoice application (`MainActivity.kt`).

## User Review Required

- **Sidebar Menu Items**:
  - Profile Header (User Avatar / Icon, Name, Email)
  - Home
  - Items
  - Banking
  - Sales
  - Purchases
  - Time Tracking
  - Accountant
  - Documents
  - Reports
  - Settings
  - Create a New Account
- **Hamburger Toggle**:
  - TopAppBar navigation icon (`☰`) opens the Navigation Drawer smoothly.
  - Every menu item is fully functional, navigating to its corresponding screen or activity.

## Proposed Changes

### 1. `MainActivity.kt`
- Wrap `Scaffold` inside Material 3 `ModalNavigationDrawer` and `ModalDrawerSheet`.
- Add hamburger menu icon to `TopAppBar`.
- Build profile header and all navigation drawer items matching Screenshot 1.

## Verification Plan

### Automated Tests
- Build test: `gradle_build("app:assembleDebug")`
- Unit tests: `gradle_build("app:testDebugUnitTest")`

### Manual Verification
- Test opening the sidebar menu, verifying profile details, and testing navigation for every menu option.
