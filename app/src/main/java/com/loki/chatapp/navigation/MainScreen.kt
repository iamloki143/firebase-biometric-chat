package com.loki.chatapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loki.chatapp.R
import com.loki.chatapp.presentation.screen.addcontact.AddContactScreen
import com.loki.chatapp.presentation.screen.chatscreen.ChatListScreen
import com.loki.chatapp.presentation.screen.deviceauthscreen.DeviceAuthScreen
import com.loki.chatapp.presentation.screen.languagescreen.LanguageScreen
import com.loki.chatapp.presentation.screen.profilescreen.ProfileScreen
import com.loki.chatapp.presentation.screen.requestscreen.RequestScreen
import com.loki.chatapp.presentation.screen.settings.SettingsScreen
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    rootNavController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationBarItem(
                    selected = currentRoute == Screen.ChatList.route,
                    onClick  = {
                        navController.navigate(Screen.ChatList.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon  = { Icon(Icons.Default.Chat, null) },
                    label = { Text(stringResource(R.string.chat)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Requests.route,
                    onClick  = {
                        navController.navigate(Screen.Requests.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon  = { Icon(Icons.Default.AddAlert, null) },
                    label = { Text(stringResource(R.string.requests)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Profile.route,
                    onClick  = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.ChatList.route)
                            launchSingleTop = true
                        }
                    },
                    icon  = { Icon(Icons.Default.PersonPin, null) },
                    label = { Text(stringResource(R.string.profile)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { padding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.ChatList.route,
            modifier         = Modifier.padding(padding)
        ) {
            composable(Screen.ChatList.route) {
                ChatListScreen(
                    onUserClick = { userId, userName ->
                        rootNavController.navigate(Screen.Chat.createRoute(userId, userName))
                    },
                    onLogout  = onLogout,
                    onAddClick = { navController.navigate(Screen.AddContact.route) }
                )
            }

            composable(Screen.Requests.route) { RequestScreen() }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout        = onLogout,
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBack         = { navController.popBackStack() },
                    onAuthClick = { navController.navigate(Screen.DeviceAuth.route) },
                    onLanguageClick = { navController.navigate(Screen.Language.route) },
                    viewModel      = settingsViewModel
                )
            }

            composable(Screen.AddContact.route) {
                AddContactScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Language.route) {
                LanguageScreen(
                    onBack    = { navController.popBackStack() },
                    viewModel = settingsViewModel
                )
            }
            composable(Screen.DeviceAuth.route) {
                DeviceAuthScreen(
                    onBack = { navController.popBackStack() },
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
