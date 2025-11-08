package dev.scavazzini.smslinklock.feature.inbox

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import dev.scavazzini.smslinklock.feature.chat.ChatScreenRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InboxScreenViewModel(
    private val navController: NavController,
    getConversationsUseCase: GetConversationsUseCase,
    application: Application,
) : AndroidViewModel(application) {
    private val _conversations: MutableStateFlow<List<Conversation>> = MutableStateFlow(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    init {
        _conversations.value = getConversationsUseCase(application)
    }

    fun openChat(conversationId: String) {
        navController.navigate(route = ChatScreenRoute(conversationId))
    }
}
