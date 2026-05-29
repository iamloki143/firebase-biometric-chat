package com.loki.chatapp.presentation.screen.chatscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import com.loki.chatapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: String,
    userName: String,
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var message by remember { mutableStateOf("") }
    val messages = viewModel.messages.sortedBy { it.timestamp }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val chatId = listOf(currentUserId, userId)
        .sorted()
        .joinToString("_")
    var navigatingBack by remember {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.startListening(chatId)
    }
    var isFirstLoad by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()){
            if(isFirstLoad){
            listState.scrollToItem(messages.size - 1)
                isFirstLoad=false
            }else{
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .imePadding()
    ) {
        TopAppBar(
            title = { Text(userName, color = MaterialTheme.colorScheme.onSurface) },
            navigationIcon = {
                IconButton(
                    onClick = {

                        if (!navigatingBack) {

                            navigatingBack = true

                            focusManager.clearFocus(force = true)
                            onBack()

                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Divider(color = MaterialTheme.colorScheme.outline)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { msg ->
                val isMe = msg.senderId == currentUserId

                val formattedTime = remember ( msg.timestamp ){
                    try {
                        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        sdf.format(msg.timestamp)
                    }catch (e:Exception){
                        ""
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Column(
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                    ) {
                        Text(
                            text = msg.text,
                            color = if (isMe) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .background(
                                    if (isMe) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(12.dp)
                        )

                        if(formattedTime.isNotEmpty()){
                            Text(
                                text = formattedTime,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Divider(color = MaterialTheme.colorScheme.outline)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding()
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
                    Text(stringResource(id=R.string.message), color = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = {
                if (message.isNotBlank()) {
                    viewModel.sendMessage(chatId, message, userId)
                    message = ""
                }
            }) {
                Icon(Icons.Default.Send, null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}