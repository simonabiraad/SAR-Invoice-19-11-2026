package com.sarinvoice.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import android.app.AlertDialog
import java.util.UUID

class ItemsActivity : ComponentActivity() {
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        store = Store(this)

        val listView = findViewById<ListView>(R.id.listViewItems)
        fun refresh() {
            val db = store.load()
            val list = db.items.map { "${it.name} - ${sar(it.price)}" }
            listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        }
        refresh()

        findViewById<Button>(R.id.addItem).setOnClickListener {
            val input = EditText(this).apply { hint = "Item Name" }
            AlertDialog.Builder(this)
                .setTitle("New Item")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotBlank()) {
                        val db = store.load()
                        val item = Item(UUID.randomUUID().toString(), name, 100.0)
                        store.save(db.copy(items = db.items + item))
                        refresh()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
