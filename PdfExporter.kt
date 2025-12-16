package com.example.quranmemorizer.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.quranmemorizer.data.AppPrefs
import com.example.quranmemorizer.plan.TOTAL_PAGES
import java.time.LocalDate

object PdfExporter {
    fun exportProgress(context: Context, prefs: AppPrefs): Uri? {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { textSize = 14f }

        val done = prefs.lastCompletedPage
        val remaining = (TOTAL_PAGES - done).coerceAtLeast(0)
        val percent = if (TOTAL_PAGES == 0) 0 else (done * 100 / TOTAL_PAGES)
        val now = LocalDate.now().toString()

        var y = 60
        fun line(t: String) { canvas.drawText(t, 40f, y.toFloat(), paint); y += 26 }

        line("Quran Memorizer - Progress Report")
        line("Date: $now")
        line("Start date: ${if (prefs.startDateIso.isBlank()) "-" else prefs.startDateIso}")
        line("Reminder time: ${String.format("%02d:%02d", prefs.reminderHour, prefs.reminderMinute)}")
        line("Completed pages: $done / $TOTAL_PAGES ($percent%)")
        line("Remaining pages: $remaining")
        line("Plan: 1 page per day")

        doc.finishPage(page)

        val fileName = "quran_progress_$now.pdf"
        val uri = createPdfUri(context, fileName) ?: run { doc.close(); return null }

        context.contentResolver.openOutputStream(uri)?.use { out -> doc.writeTo(out) }
            ?: run { doc.close(); return null }

        doc.close()
        return uri
    }

    private fun createPdfUri(context: Context, fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= 29) put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
        }
        return context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    }
}
