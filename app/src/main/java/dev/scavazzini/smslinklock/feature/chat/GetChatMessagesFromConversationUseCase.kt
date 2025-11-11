package dev.scavazzini.smslinklock.feature.chat

import android.content.Context
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.TextChatMessage
import dev.scavazzini.smslinklock.core.Address
import dev.scavazzini.smslinklock.core.GetSmsMessagesFromThreadUseCase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class GetChatMessagesFromConversationUseCase(
    private val getSmsMessagesFromThreadUseCase: GetSmsMessagesFromThreadUseCase,
) {
    operator fun invoke(conversationId: String, context: Context): List<ChatMessage> {
        val smsMessages = getSmsMessagesFromThreadUseCase(conversationId, context)

        return smsMessages.map {
            return@map TextChatMessage(
                message = it.body,
                address = Address(rawAddress = it.address),
                byYou = it.type == 2,
                signed = true,
                datetime = LocalDateTime.ofInstant(
                    /* instant = */ Instant.ofEpochMilli(it.date),
                    /* zone = */ ZoneId.systemDefault(),
                ),
            )
        }
    }
}
