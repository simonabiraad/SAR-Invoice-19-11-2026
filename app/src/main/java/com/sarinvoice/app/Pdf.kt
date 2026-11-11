package com.sarinvoice.app

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

object Pdf {
    fun share(ctx: Context, inv: Invoice, biz: Business) {
        val doc = PdfDocument()
        var pageNo = 1
        var page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNo).create())
        var y = 60f
        val p = Paint().apply { color = Color.BLACK; isAntiAlias = true }
        fun text(s: String, x: Float, size: Float = 12f, bold: Boolean = false, right: Boolean = false) {
            p.textSize = size; p.isFakeBoldText = bold
            p.textAlign = if (right) Paint.Align.RIGHT else Paint.Align.LEFT
            page.canvas.drawText(s, x, y, p)
        }
        fun newLine(h: Float = 20f) {
            y += h
            if (y > 780f) {
                doc.finishPage(page); pageNo++
                page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNo).create()); y = 60f
            }
        }
        text(biz.name, 40f, 22f, true); newLine(22f)
        if (biz.vatNo.isNotBlank()) { text("VAT No: ${biz.vatNo}", 40f); newLine() }
        newLine(10f)
        text("TAX INVOICE ${inv.number}", 40f, 16f, true); newLine(24f)
        text("Date: ${inv.date}", 40f); newLine()
        text("Bill to: ${inv.customer}", 40f); newLine(30f)
        text("Item", 40f, bold = true); text("Qty", 330f, bold = true, right = true)
        text("Price", 430f, bold = true, right = true); text("Total", 555f, bold = true, right = true); newLine()
        inv.lines.forEach {
            text(it.name.take(38), 40f); text(trim(it.qty), 330f, right = true)
            text(String.format(Locale.US, "%,.2f", it.price), 430f, right = true)
            text(String.format(Locale.US, "%,.2f", it.total), 555f, right = true); newLine()
        }
        newLine(10f)
        text("Subtotal", 430f, right = true); text(sar(inv.subtotal), 555f, right = true); newLine()
        text("VAT (${trim(inv.vatRate)}%)", 430f, right = true); text(sar(inv.vat), 555f, right = true); newLine()
        text("Total", 430f, bold = true, right = true); text(sar(inv.total), 555f, bold = true, right = true); newLine()
        doc.finishPage(page)
        val dir = File(ctx.cacheDir, "pdf").apply { mkdirs() }
        val out = File(dir, "${inv.number}.pdf")
        out.outputStream().use { doc.writeTo(it) }
        doc.close()
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", out)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"; putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        ctx.startActivity(Intent.createChooser(send, "Share invoice").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun trim(d: Double) = if (d % 1.0 == 0.0) d.toLong().toString() else d.toString()
}
