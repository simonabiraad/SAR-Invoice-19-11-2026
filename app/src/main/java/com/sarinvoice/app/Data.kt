package com.sarinvoice.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.Locale

data class Customer(val id: String, val name: String, val email: String, val phone: String)
data class Item(val id: String, val name: String, val price: Double)
data class Line(val name: String, val qty: Double, val price: Double) { val total get() = qty * price }
data class Invoice(
    val id: String, val number: String, val customer: String, val date: String,
    val lines: List<Line>, val vatRate: Double, val paid: Boolean
) {
    val subtotal get() = lines.sumOf { it.total }
    val vat get() = subtotal * vatRate / 100
    val total get() = subtotal + vat
}
data class Bill(val id: String, val number: String, val vendor: String, val date: String, val amount: Double, val paid: Boolean)
data class Expense(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val notes: String,
    val paidThrough: String = "Cash",
    val currency: String = "SAR",
    val tax: String = "None",
    val vendor: String = "",
    val reference: String = "",
    val customer: String = ""
)
data class TimeLog(val id: String, val project: String, val hours: Double, val date: String, val billed: Boolean)

data class Business(val name: String, val vatNo: String, val vatRate: Double)
data class Db(
    val business: Business,
    val customers: List<Customer>,
    val items: List<Item>,
    val invoices: List<Invoice>,
    val bills: List<Bill> = emptyList(),
    val expenses: List<Expense> = emptyList(),
    val timeLogs: List<TimeLog> = emptyList()
)

fun sar(d: Double) = "SAR " + String.format(Locale.US, "%,.2f", d)

private fun <T> JSONArray.mapObj(f: (JSONObject) -> T) = (0 until length()).map { f(getJSONObject(it)) }

class Store(ctx: Context) {
    private val file = File(ctx.filesDir, "data.json")

    fun load(): Db {
        val empty = Db(Business("My Company", "", 15.0), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
        if (!file.exists()) return empty
        return try {
            val o = JSONObject(file.readText())
            val b = o.getJSONObject("biz")
            Db(
                Business(b.getString("name"), b.getString("vat"), b.getDouble("rate")),
                o.getJSONArray("customers").mapObj { Customer(it.getString("id"), it.getString("name"), it.getString("email"), it.getString("phone")) },
                o.getJSONArray("items").mapObj { Item(it.getString("id"), it.getString("name"), it.getDouble("price")) },
                o.getJSONArray("invoices").mapObj { i ->
                    Invoice(
                        i.getString("id"), i.getString("number"), i.getString("customer"), i.getString("date"),
                        i.getJSONArray("lines").mapObj { Line(it.getString("name"), it.getDouble("qty"), it.getDouble("price")) },
                        i.getDouble("rate"), i.getBoolean("paid")
                    )
                },
                runCatching { o.getJSONArray("bills").mapObj { Bill(it.getString("id"), it.getString("number"), it.getString("vendor"), it.getString("date"), it.getDouble("amount"), it.getBoolean("paid")) } }.getOrElse { emptyList() },
                runCatching {
                    o.getJSONArray("expenses").mapObj {
                        Expense(
                            it.getString("id"),
                            it.getString("title"),
                            it.optString("category", "General"),
                            it.getDouble("amount"),
                            it.getString("date"),
                            it.optString("notes", ""),
                            it.optString("paidThrough", "Cash"),
                            it.optString("currency", "SAR"),
                            it.optString("tax", "None"),
                            it.optString("vendor", ""),
                            it.optString("reference", ""),
                            it.optString("customer", "")
                        )
                    }
                }.getOrElse { emptyList() },
                runCatching { o.getJSONArray("timeLogs").mapObj { TimeLog(it.getString("id"), it.getString("project"), it.getDouble("hours"), it.getString("date"), it.getBoolean("billed")) } }.getOrElse { emptyList() }
            )
        } catch (_: Exception) { empty }
    }

    fun save(db: Db) {
        val o = JSONObject()
        o.put("biz", JSONObject().put("name", db.business.name).put("vat", db.business.vatNo).put("rate", db.business.vatRate))
        o.put("customers", JSONArray(db.customers.map { JSONObject().put("id", it.id).put("name", it.name).put("email", it.email).put("phone", it.phone) }))
        o.put("items", JSONArray(db.items.map { JSONObject().put("id", it.id).put("name", it.name).put("price", it.price) }))
        o.put("invoices", JSONArray(db.invoices.map { i ->
            JSONObject().put("id", i.id).put("number", i.number).put("customer", i.customer).put("date", i.date)
                .put("rate", i.vatRate).put("paid", i.paid)
                .put("lines", JSONArray(i.lines.map { JSONObject().put("name", it.name).put("qty", it.qty).put("price", it.price) }))
        }))
        o.put("bills", JSONArray(db.bills.map { b ->
            JSONObject().put("id", b.id).put("number", b.number).put("vendor", b.vendor).put("date", b.date).put("amount", b.amount).put("paid", b.paid)
        }))
        o.put("expenses", JSONArray(db.expenses.map { e ->
            JSONObject()
                .put("id", e.id)
                .put("title", e.title)
                .put("category", e.category)
                .put("amount", e.amount)
                .put("date", e.date)
                .put("notes", e.notes)
                .put("paidThrough", e.paidThrough)
                .put("currency", e.currency)
                .put("tax", e.tax)
                .put("vendor", e.vendor)
                .put("reference", e.reference)
                .put("customer", e.customer)
        }))
        o.put("timeLogs", JSONArray(db.timeLogs.map { t ->
            JSONObject().put("id", t.id).put("project", t.project).put("hours", t.hours).put("date", t.date).put("billed", t.billed)
        }))
        file.writeText(o.toString())
    }
}
