package dev.scavazzini.smslinklock.feature.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.TextChatMessage
import java.time.LocalDateTime

class NewMessageBroadcastReceiver(
    private val conversationId: String,
    private val onMessageReceived: (chatMessage: ChatMessage) -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            SendSmsUseCase.INTENT_SENT_ACTION -> sentSmsReceived(intent)
        }
    }

    private fun sentSmsReceived(intent: Intent) {
        if (intent.isMessageFromAnotherConversation()) {
            return
        }

        val message = intent.extras?.getString(SendSmsUseCase.MESSAGE_EXTRA) ?: return
        val address = intent.extras?.getString(SendSmsUseCase.ADDRESS_EXTRA) ?: return

        onMessageReceived(
            TextChatMessage(
                message = message,
                address = address,
                byYou = true,
                signed = true,
                datetime = LocalDateTime.now(),
            )
        )
    }

    private fun Intent.isMessageFromAnotherConversation(): Boolean {
        return extras?.getString(SendSmsUseCase.THREAD_ID_EXTRA) != conversationId
    }
}
