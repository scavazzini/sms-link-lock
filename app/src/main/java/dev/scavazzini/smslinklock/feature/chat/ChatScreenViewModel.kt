package dev.scavazzini.smslinklock.feature.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.TextChatMessage
import dev.scavazzini.smslinklock.core.GetSmsMessagesFromThreadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ChatScreenViewModel(
    conversationId: String,
    getSmsMessagesFromThreadUseCase: GetSmsMessagesFromThreadUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private val _messages: MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _address: MutableStateFlow<String> = MutableStateFlow("Unknown")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    init {
        _messages.value = getSmsMessagesFromThreadUseCase(conversationId, application).map {
            TextChatMessage(
                message = it.body,
                byYou = it.type == 2,
                signed = true,
                datetime = LocalDateTime.ofInstant(
                    /* instant = */ Instant.ofEpochMilli(it.date),
                    /* zone = */ ZoneId.systemDefault(),
                ),
            )
        }
    }

    fun onMessageChange(newValue: String) {
        _message.value = newValue
    }

    fun sendMessage() {
        _message.value = ""
    }
}
