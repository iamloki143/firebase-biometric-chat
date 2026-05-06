package com.loki.chatapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun ProfileCircle(
    name: String,
    profileImageUrl: String = "",
    isOnline: Boolean = false
) {
    val colors = listOf(
        Color(0xFFEF5350),
        Color(0xFFAB47BC),
        Color(0xFF5C6BC0),
        Color(0xFF29B6F6),
        Color(0xFF66BB6A),
        Color(0xFFFFCA28),
        Color(0xFFFF7043)
    )

    val color = colors[(name.hashCode().absoluteValue) % colors.size]

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        if (profileImageUrl.isNotEmpty()) {
            coil.compose.AsyncImage(
                model = profileImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}