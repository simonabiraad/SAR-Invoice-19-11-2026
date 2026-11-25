package com.sarinvoice.app

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class SarInvoiceWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val store = Store(context)
            val db = store.load()
            val totalReceivables = db.invoices.filter { !it.paid }.sumOf { it.total }

            val views = RemoteViews(context.packageName, R.layout.sar_invoice_widget)
            views.setTextViewText(R.id.widgetTotalLabel, "Total Receivables: ${sar(totalReceivables)}")
            views.setTextViewText(R.id.widgetCurrentAmount, sar(totalReceivables))
            views.setTextViewText(R.id.widgetOverdueAmount, sar(0.0))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
