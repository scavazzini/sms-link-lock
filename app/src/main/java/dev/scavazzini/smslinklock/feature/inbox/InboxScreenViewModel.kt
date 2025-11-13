package dev.scavazzini.smslinklock.feature.inbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.feature.chat.ChatScreenRoute
import dev.scavazzini.smslinklock.feature.chat.NewMessageBroadcastReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InboxScreenViewModel(
    private val navController: NavController,
    getConversationsUseCase: GetConversationsUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private val _conversations: MutableStateFlow<Map<String, Conversation>> =
        MutableStateFlow(emptyMap())

    val conversations: StateFlow<List<Conversation>> = _conversations.map {
        it.values.toList().sortedByDescending { it.lastMessageDate }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val smsReceiver = NewMessageBroadcastReceiver(onMessageReceived = this::updateConversation)

    init {
        _conversations.value = getConversationsUseCase(application).associateBy {
            it.address.e164Format
        }
    }

    fun openChat(conversationId: String) {
        navController.navigate(route = ChatScreenRoute(conversationId))
    }

    private fun updateConversation(chatMessage: ChatMessage) {
        val address = chatMessage.address
        val e164Address = address.e164Format

        val conversation = _conversations.value[e164Address] ?: return
        val newConversation = conversation.copy(
            address = address,
            lastMessageDate = chatMessage.datetime,
            snippet = chatMessage.message,
            unreadCount = conversation.unreadCount + 1,
        )

        _conversations.value = _conversations.value.toMutableMap().apply {
            this.put(e164Address, newConversation)
        }
    }
}
