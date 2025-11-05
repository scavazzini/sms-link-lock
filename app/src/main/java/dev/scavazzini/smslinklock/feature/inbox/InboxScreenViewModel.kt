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
    application: Application,
) : AndroidViewModel(application) {
    private val _conversations: MutableStateFlow<List<Conversation>> = MutableStateFlow(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    init {
        _conversations.value = listOf(
            Conversation(id = "1", "550001", "Hello", 1),
            Conversation(id = "2", "550002", "Bye", 1),
        )
    }

    fun openChat(conversationId: String) {
        navController.navigate(route = ChatScreenRoute(conversationId))
    }
}
