package com.sarinvoice.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class DashboardActivity : ComponentActivity() {
    private lateinit var store: Store
    private lateinit var auth: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        store = Store(this)
        auth = AuthManager(this)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        findViewById<Button>(R.id.btnOpenDrawer).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val db = store.load()
        val outstanding = db.invoices.filter { !it.paid }.sumOf { it.total }
        val paid = db.invoices.filter { it.paid }.sumOf { it.total }

        findViewById<TextView>(R.id.txtOutstanding).text = sar(outstanding)
        findViewById<TextView>(R.id.txtPaid).text = sar(paid)

        // Quick create
        findViewById<Button>(R.id.btnNewCustomer).setOnClickListener {
            startActivity(Intent(this, CustomersActivity::class.java))
        }
        findViewById<Button>(R.id.btnNewInvoice).setOnClickListener {
            startActivity(Intent(this, NewInvoiceActivity::class.java))
        }
        findViewById<Button>(R.id.btnNewBill).setOnClickListener {
            startActivity(Intent(this, InvoicesActivity::class.java))
        }
        findViewById<Button>(R.id.btnNewExpense).setOnClickListener {
            startActivity(Intent(this, RecordExpenseActivity::class.java))
        }

        // Sidebar Menu Actions matching reference image
        findViewById<Button>(R.id.menuHome).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<Button>(R.id.menuItems).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, ItemsActivity::class.java))
        }
        findViewById<Button>(R.id.menuBanking).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, InvoicesActivity::class.java))
        }
        findViewById<Button>(R.id.menuSales).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, InvoicesActivity::class.java))
        }
        findViewById<Button>(R.id.menuPurchases).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, RecordExpenseActivity::class.java))
        }
        findViewById<Button>(R.id.menuTimeTracking).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<Button>(R.id.menuAccountant).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<Button>(R.id.menuDocuments).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<Button>(R.id.menuReports).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<Button>(R.id.menuSettings).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<Button>(R.id.menuLogout).setOnClickListener {
            auth.logout()
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
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
