package com.loki.chatapp.presentation.screen.chatscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: String,
    userName: String,
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var message by remember { mutableStateOf("") }
    val messages = viewModel.messages
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val chatId = listOf(currentUserId, userId)
        .sorted()
        .joinToString("_")

    LaunchedEffect(Unit) {
        viewModel.startListening(chatId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
    ) {
        TopAppBar(
            title = { Text(userName, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Divider(color = Color.White.copy(0.2f))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { msg ->
                val isMe = msg.senderId == currentUserId

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = msg.text,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                if (isMe) Color(0xAA4CAF50)
                                else Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Divider(color = Color.White.copy(0.2f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .navigationBarsPadding() // handles bottom gesture bar
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                placeholder = {
                    Text("Message", color = Color.White.copy(0.6f))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(0.5f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                if (message.isNotBlank()) {
                    viewModel.sendMessage(chatId, message, userId)
                    message = ""
                }
            }) {
                Icon(Icons.Default.Send, null, tint = Color.White)
            }
        }
    }
}