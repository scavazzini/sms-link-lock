@file:OptIn(ExperimentalMaterial3Api::class)

package dev.scavazzini.smslinklock.feature.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
object InboxScreenRoute

@Composable
fun InboxScreen(
    viewModel: InboxScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val conversations by viewModel.conversations.collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Messages") })
        }
    ) { contentPadding ->
        Column(modifier = modifier) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 24.dp,
                    top = contentPadding.calculateTopPadding() + 24.dp,
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 24.dp,
                    bottom = contentPadding.calculateBottomPadding() + 24.dp,
                ),
            ) {
                items(conversations) { conversation ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { viewModel.openChat(conversation.id) }
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(conversation.address, fontWeight = FontWeight.Bold)

                            Text(
                                text = conversation.snippet,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                            )
                        }
                        if (conversation.unreadCount > 0) {
                            Spacer(Modifier.width(24.dp))
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
                    }
                }
            }
        }
    }
}
