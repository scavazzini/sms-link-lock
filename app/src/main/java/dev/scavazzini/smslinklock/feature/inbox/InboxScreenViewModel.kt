package dev.scavazzini.smslinklock.feature.inbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import dev.scavazzini.smslinklock.core.GetSmsMessagesUseCase
import dev.scavazzini.smslinklock.feature.chat.ChatScreenRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InboxScreenViewModel(
    private val navController: NavController,
    getSmsMessagesUseCase: GetSmsMessagesUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private val _conversations: MutableStateFlow<List<Conversation>> = MutableStateFlow(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    init {
        val conversations = getSmsMessagesUseCase(application).entries.map { (threadId, messages) ->
            val lastMessage = messages.last()

            Conversation(
                id = threadId,
                address = lastMessage.address,
                snippet = lastMessage.body,
                messageCount = messages.size,
            )
        }

        _conversations.value = conversations
    }

    fun openChat(conversationId: String) {
        navController.navigate(route = ChatScreenRoute(conversationId))
    }
}
