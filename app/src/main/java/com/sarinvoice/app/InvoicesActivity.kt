package com.sarinvoice.app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.ComponentActivity

class InvoicesActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoices)
        store = Store(this)

        val listView = findViewById<ListView>(R.id.listViewInvoices)
        val db = store.load()
        val list = db.invoices.map { "${it.number} - ${it.customer} (${sar(it.total)})" }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val inv = db.invoices[position]
            val intent = Intent(this, InvoiceDetailActivity::class.java).apply {
                putExtra("invoice_id", inv.id)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val listView = findViewById<ListView>(R.id.listViewInvoices)
        val db = store.load()
        val list = db.invoices.map { "${it.number} - ${it.customer} (${sar(it.total)})" }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
    }
}
