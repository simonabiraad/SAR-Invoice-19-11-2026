package com.sarinvoice.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.util.UUID

class RecordExpenseActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_expense)
        store = Store(this)

        val etAccount = findViewById<EditText>(R.id.etExpenseAccount)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etVendor = findViewById<EditText>(R.id.etVendor)
        val etNotes = findViewById<EditText>(R.id.etNotes)

        findViewById<Button>(R.id.btnSaveExpense).setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val db = store.load()
            val newExpense = Expense(
                id = UUID.randomUUID().toString(),
                title = etAccount.text.toString().ifBlank { "Expense" },
                category = etAccount.text.toString().ifBlank { "General" },
                amount = amount,
                date = LocalDate.now().toString(),
                notes = etNotes.text.toString(),
                vendor = etVendor.text.toString()
            )
            store.save(db.copy(expenses = db.expenses + newExpense))
            finish()
        }
    }
}
