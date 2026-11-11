package com.sarinvoice.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class DashboardActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        store = Store(this)

        val db = store.load()
        val outstanding = db.invoices.filter { !it.paid }.sumOf { it.total }
        val paid = db.invoices.filter { it.paid }.sumOf { it.total }

        findViewById<TextView>(R.id.txtOutstanding).text = sar(outstanding)
        findViewById<TextView>(R.id.txtPaid).text = sar(paid)

        findViewById<Button>(R.id.btnNewCustomer).setOnClickListener {
            startActivity(Intent(this, CustomersActivity::class.java))
        }
        findViewById<Button>(R.id.btnNewInvoice).setOnClickListener {
            startActivity(Intent(this, NewInvoiceActivity::class.java))
        }
        findViewById<Button>(R.id.btnNewExpense).setOnClickListener {
            startActivity(Intent(this, RecordExpenseActivity::class.java))
        }
        findViewById<Button>(R.id.navInvoices).setOnClickListener {
            startActivity(Intent(this, InvoicesActivity::class.java))
        }
        findViewById<Button>(R.id.navCustomers).setOnClickListener {
            startActivity(Intent(this, CustomersActivity::class.java))
        }
        findViewById<Button>(R.id.navItems).setOnClickListener {
            startActivity(Intent(this, ItemsActivity::class.java))
        }
        findViewById<Button>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val db = store.load()
        val outstanding = db.invoices.filter { !it.paid }.sumOf { it.total }
        val paid = db.invoices.filter { it.paid }.sumOf { it.total }
        findViewById<TextView>(R.id.txtOutstanding).text = sar(outstanding)
        findViewById<TextView>(R.id.txtPaid).text = sar(paid)
    }
}
