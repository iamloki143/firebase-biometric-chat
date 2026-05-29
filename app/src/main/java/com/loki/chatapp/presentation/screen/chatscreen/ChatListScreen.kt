package com.loki.chatapp.presentation.screen.chatscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loki.chatapp.presentation.viewmodel.ChatViewModel
import com.loki.chatapp.utils.ProfileCircle
import com.loki.chatapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onUserClick: (String, String) -> Unit,
    onLogout: () -> Unit,
    onAddClick: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    val users = viewModel.users

    val filteredUsers = users.filter {
        it.name.contains(search, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        viewModel.loadContacts()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(id=R.string.app_name),color = MaterialTheme.colorScheme.onSurface) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                )
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.dp
                )
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    start = 6.dp,
                    end = 6.dp
                )
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text(stringResource(id=R.string.search_user), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            LazyColumn {
                items(filteredUsers) { user ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUserClick(user.userId, user.name)
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ProfileCircle(user.name, user.profileImageUrl)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(user.name, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}