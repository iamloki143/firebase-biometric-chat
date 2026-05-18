package com.loki.chatapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.loki.chatapp.R

@Composable
fun BackgroundWrapper(content: @Composable () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {

        val isDarkTheme = isSystemInDarkTheme()
        val backgroundImage = if (isDarkTheme) {
            R.drawable.bg_pattern
        } else {
            R.drawable.bg_light
        }
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkTheme) {Color.Black.copy(alpha = 0.5f)} else {Color.White.copy(alpha = 0.2f)})
        )
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}