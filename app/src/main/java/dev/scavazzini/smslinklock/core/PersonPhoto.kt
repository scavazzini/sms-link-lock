package dev.scavazzini.smslinklock.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PersonPhoto(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(64.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(50),
            ),
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(.6f),
        )
    }
}

@Preview
@Composable
fun PersonPhotoPreviewSmall(modifier: Modifier = Modifier) {
    PersonPhoto(Modifier.size(32.dp))
}

@Preview
@Composable
fun PersonPhotoPreviewLarge(modifier: Modifier = Modifier) {
    PersonPhoto(Modifier.size(260.dp))
}
