package com.sarinvoice.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import android.app.AlertDialog
import java.util.UUID

class CustomersActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers)
        store = Store(this)

        val listView = findViewById<ListView>(R.id.listViewCustomers)
        fun refresh() {
            val db = store.load()
            val list = db.customers.map { "${it.name} (${it.email} · ${it.phone})" }
            listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        }
        refresh()

        findViewById<Button>(R.id.btnAddCustomer).setOnClickListener {
            val input = EditText(this).apply { hint = "Customer Name" }
            AlertDialog.Builder(this)
                .setTitle("New Customer")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotBlank()) {
                        val db = store.load()
                        val c = Customer(UUID.randomUUID().toString(), name, "", "")
                        store.save(db.copy(customers = db.customers + c))
                        refresh()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
