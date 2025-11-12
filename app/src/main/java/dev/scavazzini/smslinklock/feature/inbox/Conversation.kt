package dev.scavazzini.smslinklock.feature.inbox

import dev.scavazzini.smslinklock.core.Address
import java.time.LocalDateTime

data class Conversation(
    val id: String,
    val address: Address,
    val snippet: String,
    val unreadCount: Int,
    val lastMessageDate: LocalDateTime,
)
