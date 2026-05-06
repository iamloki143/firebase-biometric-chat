package com.loki.chatapp.presentation.screen.requestscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.chatapp.presentation.viewmodel.ChatViewModel
import com.loki.chatapp.utils.ProfileCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null
) {
    val requests = viewModel.requests

    LaunchedEffect(Unit) {
        viewModel.loadRequests()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Requests", color = Color.White) },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(onClick = { it() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
                Divider(
                    color = Color.White.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
            }

        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(requests) { user ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ProfileCircle(user.name, user.profileImageUrl)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(user.name, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(200.dp))

                    Button(
                        onClick = {
                            viewModel.acceptRequest(user.userId)
                            viewModel.loadContacts()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xAA000000)
                        )
                    ) {
                        Text("Accept", color = Color.White)
                    }
                }
            }
        }
    }
}