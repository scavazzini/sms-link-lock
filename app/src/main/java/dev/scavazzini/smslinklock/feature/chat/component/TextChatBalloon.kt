package dev.scavazzini.smslinklock.feature.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.scavazzini.smslinklock.core.format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun TextChatBalloon(
    text: String,
    sentByYou: Boolean,
    signed: Boolean,
    datetime: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (sentByYou) Alignment.End else Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (sentByYou) {
                BalloonInfo(signed)
            }

            Balloon(text, sentByYou)

            if (!sentByYou) {
                BalloonInfo(signed)
            }
        }

        Text(text = datetime.format())
    }
}

@Composable
fun BalloonInfo(signed: Boolean, modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(24.dp)
            .clickable(onClick = {}),
        imageVector = if (signed) Icons.Default.Lock else Icons.Default.LockOpen,
        contentDescription = null,
    )
}

@Composable
private fun Balloon(
    text: String,
    sentByYou: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = defaultColors(sentByYou)
    val shape = defaultShape(sentByYou)

    Column(
        modifier = modifier
            .background(
                color = colors.backgroundColor,
                shape = shape,
            )
            .border(width = 1.dp, color = colors.borderColor, shape = shape)
            .padding(16.dp),
    ) {
        SelectionContainer(
            Modifier.widthIn(min = 10.dp, max = 280.dp)
        ) {
            Text(
                text = text,
                color = Color.Black,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

private data class TextChatBalloonColors(
    val backgroundColor: Color,
    val borderColor: Color,
)

private fun defaultColors(sentByYou: Boolean): TextChatBalloonColors {
    if (sentByYou) {
        return TextChatBalloonColors(
            backgroundColor = Color(0xFF90D5FF),
            borderColor = Color(0xFF77B1D4),
        )
    }
    return TextChatBalloonColors(
        backgroundColor = Color(0xFFFFFFC5),
        borderColor = Color(0xFFB3B389),
    )
}

private fun defaultShape(sentByYou: Boolean): RoundedCornerShape {
    val largerRadius = 32.dp
    val smallerRadius = 0.dp

    if (sentByYou) {
        return RoundedCornerShape(
            topStart = largerRadius,
            topEnd = largerRadius,
            bottomEnd = smallerRadius,
            bottomStart = largerRadius,
        )
    }
    return RoundedCornerShape(
        topStart = largerRadius,
        topEnd = largerRadius,
        bottomEnd = largerRadius,
        bottomStart = smallerRadius,
    )
}

@Preview
@Composable
private fun TextChatBalloonShortMessagePreview() {
    TextChatBalloon(
        text = "Short message",
        sentByYou = true,
        signed = true,
        datetime = LocalDateTime.now().minusMinutes(50),
    )
}

@Preview
@Composable
private fun TextChatBalloonLongMessagePreview() {
    TextChatBalloon(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus eget ullamcorper massa. Morbi elementum dui et viverra malesuada. Aliquam mollis interdum ante, ut tempus arcu lacinia sed. Aenean neque eros, condimentum ac porta non, rhoncus eu dolor.\n\nSuspendisse pellentesque lorem at nunc suscipit gravida. Integer malesuada, ipsum non pharetra pellentesque, purus tortor placerat leo, eu blandit eros neque sit amet ex. Aliquam porttitor auctor vestibulum. Nunc ut malesuada est. Donec iaculis sem sem, non imperdiet lectus semper consectetur. Etiam vel congue dolor. Aliquam bibendum quam sed fermentum efficitur. Nullam vitae nulla lorem.\n\nDonec interdum porttitor arcu, et euismod lectus mattis sed.",
        sentByYou = false,
        signed = true,
        datetime = LocalDateTime.now().minusDays(3),
    )
}

@Preview
@Composable
private fun TextChatBalloonUnsignedPreview() {
    TextChatBalloon(
        text = "Unsigned message",
        sentByYou = false,
        signed = false,
        datetime = LocalDateTime.now().minusYears(3),
    )
}
