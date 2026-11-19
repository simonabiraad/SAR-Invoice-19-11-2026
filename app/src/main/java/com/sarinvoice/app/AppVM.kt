package com.sarinvoice.app

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

val Context.settingsStore by preferencesDataStore("settings")
private val DARK = booleanPreferencesKey("dark_mode")

class AppVM(app: Application) : AndroidViewModel(app) {
    private val store = Store(app)
    var db by mutableStateOf(store.load()); private set

    // Default on first install: ON = Black & Blue Neon
    val dark = app.settingsStore.data.map { it[DARK] ?: true }.stateIn(viewModelScope, SharingStarted.Eagerly, true)
    fun setDark(v: Boolean) { viewModelScope.launch { getApplication<Application>().settingsStore.edit { it[DARK] = v } } }

    private fun upd(n: Db) { db = n; store.save(n) }
    private fun id() = UUID.randomUUID().toString()

    fun addCustomer(
        name: String,
        email: String,
        phone: String,
        customerType: String = "Business",
        salutation: String = "",
        firstName: String = "",
        lastName: String = "",
        companyName: String = "",
        mobile: String = "",
        currency: String = "SAR",
        paymentTerms: String = "",
        enablePortal: Boolean = false,
        remarks: String = ""
    ) = upd(
        db.copy(
            customers = db.customers + Customer(
                id(), name, email, phone, customerType, salutation, firstName, lastName, companyName, mobile, currency, paymentTerms, enablePortal, remarks
            )
        )
    )
    fun deleteCustomer(id: String) = upd(db.copy(customers = db.customers.filter { it.id != id }))
    fun addItem(name: String, price: Double) = upd(db.copy(items = db.items + Item(id(), name, price)))
    fun deleteItem(id: String) = upd(db.copy(items = db.items.filter { it.id != id }))
    fun addInvoice(customer: String, lines: List<Line>) {
        val n = "INV-%04d".format(db.invoices.size + 1)
        upd(db.copy(invoices = db.invoices + Invoice(id(), n, customer, LocalDate.now().toString(), lines, db.business.vatRate, false)))
    }
    fun togglePaid(id: String) = upd(db.copy(invoices = db.invoices.map { if (it.id == id) it.copy(paid = !it.paid) else it }))
    fun deleteInvoice(id: String) = upd(db.copy(invoices = db.invoices.filter { it.id != id }))
    fun saveBusiness(b: Business) = upd(db.copy(business = b))

    // Bills
    fun addBill(vendor: String, amount: Double) {
        val n = "BILL-%04d".format(db.bills.size + 1)
        upd(db.copy(bills = db.bills + Bill(id(), n, vendor, LocalDate.now().toString(), amount, false)))
    }
    fun toggleBillPaid(id: String) = upd(db.copy(bills = db.bills.map { if (it.id == id) it.copy(paid = !it.paid) else it }))
    fun deleteBill(id: String) = upd(db.copy(bills = db.bills.filter { it.id != id }))

    var selectedFiscalPeriod by mutableStateOf("This Fiscal Year")

    // Expenses
    fun addExpense(
        title: String,
        category: String,
        amount: Double,
        date: String,
        notes: String,
        paidThrough: String,
        currency: String,
        tax: String,
        vendor: String,
        reference: String,
        customer: String
    ) = upd(
        db.copy(
            expenses = db.expenses + Expense(
                id(), title, category, amount, date, notes, paidThrough, currency, tax, vendor, reference, customer
            )
        )
    )
    fun deleteExpense(id: String) = upd(db.copy(expenses = db.expenses.filter { it.id != id }))

    // Time Tracking & Timer
    var timerSeconds by mutableIntStateOf(0); private set
    var isTimerRunning by mutableStateOf(false); private set
    var timerProject by mutableStateOf("General Project")
    private var timerJob: Job? = null
    fun startTimer() {
        if (isTimerRunning) return
        isTimerRunning = true
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                timerSeconds++
            }
        }
    }
    fun pauseTimer() {
        timerJob?.cancel()
        isTimerRunning = false
    }
    fun stopAndLogTimer() {
        timerJob?.cancel()
        isTimerRunning = false
        if (timerSeconds > 0) {
            val hours = timerSeconds / 3600.0
            logTime(timerProject, if (hours < 0.01) 0.01 else hours)
        }
        timerSeconds = 0
    }
    fun logTime(project: String, hours: Double) =
        upd(db.copy(timeLogs = db.timeLogs + TimeLog(id(), project, hours, LocalDate.now().toString(), false)))
    fun deleteTimeLog(id: String) = upd(db.copy(timeLogs = db.timeLogs.filter { it.id != id }))
}
