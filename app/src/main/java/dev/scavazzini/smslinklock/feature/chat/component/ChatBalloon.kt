package dev.scavazzini.smslinklock.feature.chat.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.scavazzini.smslinklock.ChatMessage
import dev.scavazzini.smslinklock.InfoChatMessage
import dev.scavazzini.smslinklock.TextChatMessage

@Composable
fun ChatBalloon(
    message: ChatMessage,
    modifier: Modifier = Modifier,
) {
    when (message) {
        is TextChatMessage -> {
            TextChatBalloon(
                text = message.message,
                sentByYou = message.byYou,
                signed = message.signed,
                datetime = message.datetime,
                modifier = modifier,
            )
        }
        is InfoChatMessage -> {
            InfoChatBalloon(
                text = message.message,
                options = message.options,
                modifier = modifier,
            )
        }
    }
}
