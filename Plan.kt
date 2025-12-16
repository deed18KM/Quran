package com.example.quranmemorizer.plan

import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val TOTAL_PAGES = 604

data class DailyPageTask(val date: LocalDate, val page: Int)

fun getTaskForDate(startDate: LocalDate, date: LocalDate): DailyPageTask? {
    val dayIndex = ChronoUnit.DAYS.between(startDate, date).toInt()
    val page = dayIndex + 1
    if (page !in 1..TOTAL_PAGES) return null
    return DailyPageTask(date, page)
}
