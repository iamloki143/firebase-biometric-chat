package com.loki.chatapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.screen.authscreen.AuthScreen
import com.loki.chatapp.presentation.screen.authscreen.WelcomeScreen
import com.loki.chatapp.presentation.screen.chatscreen.ChatScreen
import com.loki.chatapp.presentation.screen.lock.LockScreen
import com.loki.chatapp.presentation.screen.profilesetup.UsernameSetupScreen
import com.loki.chatapp.presentation.viewmodel.AppLockViewModel
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel

@Composable
fun AppNavigation(
    activity: FragmentActivity,
    settingsViewModel: SettingsViewModel,
    lockViewModel: AppLockViewModel
) {
    val navController = rememberNavController()

    val authEnabled by lockViewModel.authEnabled.collectAsState()
    val isUnlocked  by lockViewModel.isUnlocked.collectAsState()

    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val shouldShowLock = authEnabled && isLoggedIn && !isUnlocked
    if (shouldShowLock) {
        LockScreen(
            activity   = activity,
            onUnlocked = { lockViewModel.onUnlocked() }
        )
        return
    }

    val startDestination = if (isLoggedIn) Screen.Main.route else Screen.Welcome.route

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()

                    lockViewModel.onLogout()

                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) {
                            inclusive = true
                        }
                    }
                },
                rootNavController = navController,
                settingsViewModel = settingsViewModel
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen {
                navController.navigate(Screen.Auth.route)
            }
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = { isSignUp ->
                    if (isSignUp) {
                        navController.navigate(Screen.UsernameSetup.route) {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        }

        composable(Screen.Chat.route) { backStackEntry ->
            val userId   = backStackEntry.arguments?.getString("userId")   ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ChatScreen(
                userId   = userId,
                userName = userName,
                onBack   = { navController.popBackStack() }
            )
        }

        composable(Screen.UsernameSetup.route) {
            UsernameSetupScreen {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.UsernameSetup.route) { inclusive = true }
                }
            }
        }
    }
}
