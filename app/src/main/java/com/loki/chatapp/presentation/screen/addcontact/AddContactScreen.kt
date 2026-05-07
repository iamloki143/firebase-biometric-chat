package com.loki.chatapp.presentation.screen.addcontact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.viewmodel.ChatViewModel
import com.loki.chatapp.utils.ProfileCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    viewModel: ChatViewModel= hiltViewModel(),
    onBack:() -> Unit
) {
    var search by remember { mutableStateOf("") }
    val users = viewModel.allUsers
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
        viewModel.loadSentRequests()
        viewModel.loadContactIds()
    }
    val filteredUsers = users.filter {
        it.name.contains(search,ignoreCase = true)
    }
    Scaffold(
        containerColor = Color.Transparent,

        topBar = {
            Column {

                TopAppBar(
                    title = { Text("ChatApp", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.KeyboardBackspace, null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                Divider(
                    color = Color.White.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
            }
        }
    ) {padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Search Users", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(0.3f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.1f),
                    unfocusedContainerColor = Color.White.copy(0.1f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            LazyColumn {
                items(filteredUsers){user ->
                    val isRequested = viewModel.sentRequestIds.contains(user.userId)
                    val isContact = viewModel.contactIds.contains(user.userId)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            ProfileCircle(user.name, profileImageUrl = user.profileImageUrl, isOnline = false)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(user.name, color = Color.White)
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        when{
                            isContact -> {
                                Text("Added", color = Color.White)
                            }
                            isRequested -> {
                                Text("Requested", color = Color.White)
                            }

                            else -> {
                                IconButton(onClick = {
                                    val currentUserId =
                                        FirebaseAuth.getInstance().currentUser?.uid ?: return@IconButton
                                    viewModel.sendRequest(currentUserId,user.userId)
                                }) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = "add", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}