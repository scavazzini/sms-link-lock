@file:OptIn(ExperimentalMaterial3Api::class)

package dev.scavazzini.smslinklock.feature.inbox

import android.content.IntentFilter
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import dev.scavazzini.smslinklock.core.PersonPhoto
import dev.scavazzini.smslinklock.core.format
import dev.scavazzini.smslinklock.feature.chat.SendSmsUseCase.Companion.INTENT_SENT_ACTION
import dev.scavazzini.smslinklock.ui.theme.calculateProfileColor
import kotlinx.serialization.Serializable

@Serializable
object InboxScreenRoute

@Composable
fun InboxScreen(
    viewModel: InboxScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val conversations by viewModel.conversations.collectAsState(initial = emptyList())
    val colors by remember(conversations) {
        mutableStateOf(
            conversations.associate { conversation ->
                conversation.address to conversation.address.e164Format.calculateProfileColor()
            }
        )
    }

    DisposableEffect(viewModel.smsReceiver) {
        val intentFilter = IntentFilter().apply {
            addAction(SMS_RECEIVED_ACTION)
            addAction(INTENT_SENT_ACTION)
        }

        registerReceiver(
            /* context = */ context,
            /* receiver = */ viewModel.smsReceiver,
            /* filter = */ intentFilter,
            /* flags = */ ContextCompat.RECEIVER_EXPORTED,
        )

        onDispose { context.unregisterReceiver(viewModel.smsReceiver) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Messages") })
        }
    ) { contentPadding ->
        Column(modifier = modifier) {
            LazyColumn(contentPadding = contentPadding) {
                items(
                    items = conversations,
                    key = { it.id },
                ) { conversation ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .animateItem()
                            .clickable { viewModel.openChat(conversation.id) }
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        PersonPhoto(
                            color = colors.get(conversation.address) ?: Color.LightGray,
                            modifier = Modifier.size(56.dp),
                        )
                        Column(Modifier.weight(1f)) {
                            Text(conversation.address.internationalFormat, fontWeight = FontWeight.Bold)

                            Text(
                                text = conversation.snippet,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            if (conversation.unreadCount > 0) {
                                Text(
                                    text = conversation.unreadCount.toString(),
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(50),
                                        )
                                        .padding(horizontal = 8.dp),
                                )
                            }
                            Text(
                                text = conversation.lastMessageDate.format(),
                            )
                        }

                    }
                }
            }
        }
    }
}
