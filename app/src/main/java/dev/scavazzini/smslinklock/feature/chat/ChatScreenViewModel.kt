package dev.scavazzini.smslinklock.feature.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.core.Address
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatScreenViewModel(
    private val conversationId: String,
    getChatMessagesFromConversationUseCase: GetChatMessagesFromConversationUseCase,
    private val sendSmsUseCase: SendSmsUseCase,
    private val application: Application,
) : AndroidViewModel(application) {
    private val _messages: MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _address: MutableStateFlow<Address?> = MutableStateFlow(null)
    val address: StateFlow<Address?> = _address.asStateFlow()

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    val smsReceiver = NewMessageBroadcastReceiver(
        conversationId = conversationId,
        onMessageReceived = this::putMessage,
    )

    init {
        _messages.value = getChatMessagesFromConversationUseCase(conversationId, application)
        _messages.value.firstOrNull()?.let { _address.value = it.address }
    }

    fun onMessageChange(newValue: String) {
        _message.value = newValue
    }

    fun sendMessage() {
        val message = _message.value
        val address = _address.value

        if (message.isBlank() || address == null || message.isBlank()) {
            return
        }

        sendSmsUseCase(message, address.e164Format, conversationId, application)
        _message.value = ""
    }

    private fun putMessage(chatMessage: ChatMessage) {
        if (chatMessage.address != _address.value) {
            return
        }

        _messages.value = messages.value.toMutableList().apply {
            add(index = 0, element = chatMessage)
        }
    }
}
