package dev.scavazzini.smslinklock

import java.time.LocalDateTime

sealed interface ChatMessage {
    val message: String
    val byYou: Boolean
    val signed: Boolean
    val datetime: LocalDateTime
}

data class TextChatMessage(
    override val message: String,
    override val byYou: Boolean,
    override val signed: Boolean,
    override val datetime: LocalDateTime = LocalDateTime.now(),
) : ChatMessage

data class InfoChatMessage(
    override val message: String,
    override val byYou: Boolean,
    override val signed: Boolean,
    override val datetime: LocalDateTime = LocalDateTime.now(),
    val options: List<InfoMessageOption> = emptyList(),
) : ChatMessage

data class InfoMessageOption(
    val text: String,
    val onClick: () -> Unit,
)
