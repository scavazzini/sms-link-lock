package dev.scavazzini.smslinklock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.scavazzini.smslinklock.core.GetSmsMessagesFromThreadUseCase
import dev.scavazzini.smslinklock.feature.chat.ChatScreen
import dev.scavazzini.smslinklock.feature.chat.ChatScreenRoute
import dev.scavazzini.smslinklock.feature.chat.ChatScreenViewModel
import dev.scavazzini.smslinklock.feature.inbox.GetConversationsUseCase
import dev.scavazzini.smslinklock.feature.inbox.InboxScreen
import dev.scavazzini.smslinklock.feature.inbox.InboxScreenRoute
import dev.scavazzini.smslinklock.feature.inbox.InboxScreenViewModel
import dev.scavazzini.smslinklock.ui.theme.SMSLinkLockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            SMSLinkLockTheme {
                NavHost(
                    navController = navController,
                    startDestination = InboxScreenRoute,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable<InboxScreenRoute> { backStackEntry ->
                        InboxScreen(
                            viewModel = InboxScreenViewModel(
                                navController = navController,
                                getConversationsUseCase = GetConversationsUseCase(),
                                application = application,
                            ),
                        )
                    }
                    composable<ChatScreenRoute> { backStackEntry ->
                        val route: ChatScreenRoute = backStackEntry.toRoute()

                        ChatScreen(
                            viewModel = ChatScreenViewModel(
                                conversationId = route.conversationId,
                                getSmsMessagesFromThreadUseCase = GetSmsMessagesFromThreadUseCase(),
                                application = application,
                            ),
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
