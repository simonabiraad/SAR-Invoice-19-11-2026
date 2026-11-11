package com.sarinvoice.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.util.UUID

class NewInvoiceActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_invoice)
        store = Store(this)

        val etCustomer = findViewById<EditText>(R.id.etCustomer)
        val etItemName = findViewById<EditText>(R.id.etItemName)
        val etQty = findViewById<EditText>(R.id.etQty)
        val etPrice = findViewById<EditText>(R.id.etPrice)

        findViewById<Button>(R.id.btnSaveInvoice).setOnClickListener {
            val customer = etCustomer.text.toString().ifBlank { "Walk-in customer" }
            val itemName = etItemName.text.toString().ifBlank { "Service" }
            val qty = etQty.text.toString().toDoubleOrNull() ?: 1.0
            val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0

            val db = store.load()
            val number = "INV-%04d".format(db.invoices.size + 1)
            val line = Line(itemName, qty, price)
            val inv = Invoice(
                id = UUID.randomUUID().toString(),
                number = number,
                customer = customer,
                date = LocalDate.now().toString(),
                lines = listOf(line),
                vatRate = db.business.vatRate,
                paid = false
            )
            store.save(db.copy(invoices = db.invoices + inv))
            finish()
        }
    }
}
