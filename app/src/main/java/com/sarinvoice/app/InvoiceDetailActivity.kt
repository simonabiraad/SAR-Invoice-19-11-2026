package com.sarinvoice.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class InvoiceDetailActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_detail)
        store = Store(this)

        val invId = intent.getStringExtra("invoice_id") ?: return
        var db = store.load()
        var inv = db.invoices.find { it.id == invId } ?: return

        val txtNumber = findViewById<TextView>(R.id.txtInvNumber)
        val txtCustomer = findViewById<TextView>(R.id.txtInvCustomer)
        val txtTotal = findViewById<TextView>(R.id.txtInvTotal)

        fun bind() {
            txtNumber.text = inv.number
            txtCustomer.text = "${inv.customer} · ${inv.date} (${if (inv.paid) "PAID" else "UNPAID"})"
            txtTotal.text = "Total: ${sar(inv.total)}"
        }
        bind()

        findViewById<Button>(R.id.btnTogglePaid).setOnClickListener {
            db = store.load()
            val updatedInvoices = db.invoices.map { if (it.id == invId) it.copy(paid = !it.paid) else it }
            store.save(db.copy(invoices = updatedInvoices))
            inv = updatedInvoices.find { it.id == invId }!!
            bind()
        }

        findViewById<Button>(R.id.btnSharePdf).setOnClickListener {
            Pdf.share(this, inv, db.business)
        }

        findViewById<Button>(R.id.btnDeleteInvoice).setOnClickListener {
            db = store.load()
            store.save(db.copy(invoices = db.invoices.filter { it.id != invId }))
            finish()
        }
    }
}
