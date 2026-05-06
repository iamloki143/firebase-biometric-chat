package com.loki.chatapp.presentation.screen.profilescreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel
import com.loki.chatapp.utils.ProfileCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val settingsViewModel: SettingsViewModel= hiltViewModel()
    val user = FirebaseAuth.getInstance().currentUser


    val isEnabled by remember { derivedStateOf { settingsViewModel.authEnabled ?: false } }

    LaunchedEffect(Unit) {
        settingsViewModel.loadSettings()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Profile", color = Color.White) },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(onClick = { it() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { onLogout() }) {
                            Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            ProfileCircle(name = user?.email ?: "User")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.email ?: "",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLogout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xAA000000)
                )
            ) {
                Text("Logout", color = Color.White)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "App Lock",
                    color=Color.White
                )
                Switch(
                    checked = isEnabled,
                    onCheckedChange = {settingsViewModel.onToggleChanged(it)}
                )
            }
        }
    }
}