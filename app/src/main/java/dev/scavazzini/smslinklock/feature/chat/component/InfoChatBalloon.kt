package dev.scavazzini.smslinklock.feature.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.scavazzini.smslinklock.InfoMessageOption

@Composable
internal fun InfoChatBalloon(
    text: String,
    modifier: Modifier = Modifier,
    options: List<InfoMessageOption> = emptyList(),
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .widthIn(max = 320.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(8.dp),
        ) {
            Text(
                text = text,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.background(Color.DarkGray),
            ) {
                options.forEach { option ->
                    Button(
                        onClick = option.onClick,
                        modifier = Modifier.padding(vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray,
                        ),
                    ) {
                        Text(option.text)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InfoChatBalloonPreview() {
    InfoChatBalloon(text = "Connection established")
}

@Preview
@Composable
private fun InfoChatBalloonWithOptionsPreview() {
    InfoChatBalloon(
        text = "Alice wants to start a secure chat with you",
        options = listOf(
            InfoMessageOption("Reject", {}),
            InfoMessageOption("Accept", {}),
        ),
    )
}


@Preview()
@Composable
private fun InfoChatBalloonLongTextPreview() {
    InfoChatBalloon(
        text = "Duis vel dolor tincidunt, dignissim turpis sed, semper metus. Praesent aliquam fringilla aliquet. Nullam consectetur lectus ut ipsum accumsan, et blandit augue posuere.",
        options = listOf(
            InfoMessageOption("Huh?", {}),
            InfoMessageOption("Understood", {}),
        ),
    )
}
