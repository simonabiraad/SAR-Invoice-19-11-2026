# Walkthrough - New Customer Screen Precise Design Match

Successfully recreated the "New Customer" screen to match Reference Image 1 precisely.

## Changes Made

### 1. New Customer Screen (`NewCustomerScreen` in `MainActivity.kt`)
- Added all fields and UI components matching Reference Image 1:
  - **Tax Rate** with helper text.
  - **Company ID** with info icon.
  - **Currency** dropdown.
  - **Accounts Receivable** dropdown.
  - **Opening Balance** with currency tag.
  - **Payment Terms** dropdown.
  - **Enable Portal?** with info icon and checkbox.
  - **Portal Language** with info icon and dropdown.
  - **Add Website & Social** action button.
  - **+ Add Billing & Shipping address** prominent card button.

### 2. Verification
- Rebuilt the app (`app:assembleDebug`) and ran unit tests (`app:testDebugUnitTest`), confirming a 100% successful build with zero errors.
