package dev.scavazzini.smslinklock.feature.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.InfoChatMessage
import dev.scavazzini.smslinklock.TextChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatScreenViewModel(
    conversationId: String,
    application: Application,
) : AndroidViewModel(application) {
    private val _messages: MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _address: MutableStateFlow<String> = MutableStateFlow("Unknown")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    init {
        _messages.value = listOf(
            TextChatMessage("Bye", byYou = false, signed = true),
            TextChatMessage("Hello", byYou = true, signed = true),
            InfoChatMessage("Connection established", byYou = true, signed = true),
        )
    }

    fun onMessageChange(newValue: String) {
        _message.value = newValue
    }

    fun sendMessage() {
        _message.value = ""
    }
}
