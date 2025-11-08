package dev.scavazzini.smslinklock.ui.theme

import androidx.compose.ui.graphics.Color
import java.security.MessageDigest

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun String.calculateProfileColor(): Color {
    val digest = MessageDigest.getInstance("MD5")
    digest.update(toByteArray())

    val hash = digest.digest().toHexString().takeLast(6)
    return Color(0xFF000000.toInt() or hash.toLong(16).toInt())
}
