package dev.scavazzini.smslinklock.feature.inbox

data class Conversation(
    val id: String,
    val address: String,
    val snippet: String,
    val unreadCount: Int,
)
