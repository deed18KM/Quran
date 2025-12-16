package com.example.quranmemorizer.quran

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MushafLine(
    val type: String,
    val text: String? = null
)

@Serializable
data class MushafPage(
    val page: Int,
    val lines: List<MushafLine>
)

class QuranPagesRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getPageLines(pageNumber: Int): List<String> {
        val fileName = "mushaf/page-${pageNumber.toString().padStart(3, '0')}.json"
        return try {
            val raw = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val page = json.decodeFromString<MushafPage>(raw)

            page.lines.mapNotNull { line ->
                when (line.type) {
                    "basmala" -> "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ"
                    else -> line.text
                }
            }
        } catch (e: Exception) {
            listOf("⚠️ لم يتم العثور على ملف الصفحة $pageNumber", "ضع ملفات الصفحات داخل:", "app/src/main/assets/mushaf/")
        }
    }
}
