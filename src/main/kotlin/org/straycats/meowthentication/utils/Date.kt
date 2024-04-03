package org.straycats.meowthentication.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

internal fun Date.plusYear(year: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.YEAR, year)
    return calendar.time
}

internal fun Date.plusHour(hour: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR, hour)
    return calendar.time
}

internal fun Date.plusMonth(month: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, month)
    return calendar.time
}

internal fun Date.toLocalDateTime(): LocalDateTime = this.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()
