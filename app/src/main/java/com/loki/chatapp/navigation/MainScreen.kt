package com.loki.chatapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loki.chatapp.presentation.screen.addcontact.AddContactScreen
import com.loki.chatapp.presentation.screen.chatscreen.ChatListScreen
import com.loki.chatapp.presentation.screen.profilescreen.ProfileScreen
import com.loki.chatapp.presentation.screen.requestscreen.RequestScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    rootNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = Color.Transparent,

        bottomBar = {
            Divider(
                color = Color.White.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp
            ) {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.ChatList.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Chat, null, tint = Color.White) },
                    label = { Text("Chat", color = Color.White) }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Requests.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.AddAlert, null, tint = Color.White) },
                    label = { Text("Requests", color = Color.White) }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.PersonPin, null, tint = Color.White) },
                    label = { Text("Profile", color = Color.White) }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.ChatList.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.ChatList.route) {
                ChatListScreen(
                    onUserClick = { userId, userName ->
                        rootNavController.navigate(
                            Screen.Chat.createRoute(userId, userName)
                        )
                    },
                    onLogout = onLogout,
                    onAddClick = {
                        navController.navigate(Screen.AddContact.route)
                    }
                )
            }

            composable(Screen.Requests.route) {
                RequestScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }

            composable(Screen.AddContact.route) {
                AddContactScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}