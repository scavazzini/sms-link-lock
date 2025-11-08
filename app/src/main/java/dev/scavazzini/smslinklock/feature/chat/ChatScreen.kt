@file:OptIn(ExperimentalMaterial3Api::class)

package dev.scavazzini.smslinklock.feature.chat

import android.content.IntentFilter
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import dev.scavazzini.smslinklock.core.PersonPhoto
import dev.scavazzini.smslinklock.feature.chat.SendSmsUseCase.Companion.INTENT_SENT_ACTION
import dev.scavazzini.smslinklock.feature.chat.component.ChatBalloon
import kotlinx.serialization.Serializable

@Serializable
data class ChatScreenRoute(
    val conversationId: String,
)

@Composable
fun ChatScreen(
    viewModel: ChatScreenViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val message by viewModel.message.collectAsState("")
    val messages by viewModel.messages.collectAsState(emptyList())
    val address by viewModel.address.collectAsState()

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
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        PersonPhoto(Modifier.size(40.dp))
                        Text(address ?: "Unknown")
                    }
                },
            )
        },
        bottomBar = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 24.dp)
                ) {
                    TextField(
                        value = message,
                        onValueChange = viewModel::onMessageChange,
                        singleLine = true,
                        placeholder = { Text("Your message...") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { viewModel.sendMessage() }),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xfff4f4f4),
                            focusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 16.dp,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                    )
                    Button(
                        onClick = viewModel::sendMessage,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 0.dp,
                        ),
                        modifier = Modifier
                            .height(60.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Default.Send, "Send")
                    }
                }
            }
        },
    ) { contentPadding ->
        Column(
            Modifier.background(Color(0xffe8e8e8))
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 24.dp,
                    top = contentPadding.calculateTopPadding() + 24.dp,
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 24.dp,
                    bottom = contentPadding.calculateBottomPadding() + 24.dp,
                ),
            ) {
                items(items = messages) { message ->
                    ChatBalloon(message)
                }
            }
        }
    }
}
