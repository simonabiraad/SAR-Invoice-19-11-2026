package com.sarinvoice.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        store = Store(this)

        val db = store.load()
        val etName = findViewById<EditText>(R.id.etBusinessName)
        val etVatNo = findViewById<EditText>(R.id.etVatNo)
        val etVatRate = findViewById<EditText>(R.id.etVatRate)

        etName.setText(db.business.name)
        etVatNo.setText(db.business.vatNo)
        etVatRate.setText(db.business.vatRate.toString())

        findViewById<Button>(R.id.btnSaveSettings).setOnClickListener {
            val name = etName.text.toString().ifBlank { "My Company" }
            val vatNo = etVatNo.text.toString()
            val vatRate = etVatRate.text.toString().toDoubleOrNull() ?: 15.0
            val b = Business(name, vatNo, vatRate)
            store.save(db.copy(business = b))
            finish()
        }
    }
}
