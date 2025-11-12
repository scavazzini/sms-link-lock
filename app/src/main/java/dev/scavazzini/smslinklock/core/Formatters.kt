package dev.scavazzini.smslinklock.core

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.format(): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val fullDateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")

    if (LocalDateTime.now().minusDays(1).isBefore(this)) {
        return timeFormatter.format(this)
    }

    return fullDateTimeFormatter.format(this)
}
