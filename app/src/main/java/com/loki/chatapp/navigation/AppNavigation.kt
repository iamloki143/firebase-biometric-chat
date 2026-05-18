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
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    val lockViewModel: AppLockViewModel = hiltViewModel()
    val authEnabled by lockViewModel.authEnabled.collectAsState()
    val isUnlocked  by lockViewModel.isUnlocked.collectAsState()

    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    var showLogoutDialog by remember { mutableStateOf(false) }
    if (authEnabled == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }
    val shouldShowLock = authEnabled == true && isLoggedIn && !isUnlocked

    if (shouldShowLock && activity != null) {
        LockScreen(
            activity = activity,
            onUnlocked = { lockViewModel.onUnlocked() }
        )
        return
    }

    val startDestination = if (isLoggedIn) Screen.Main.route else Screen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Main.route) {
            MainScreen(
                rootNavController = navController,
                onLogout = { showLogoutDialog = true }
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
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        FirebaseAuth.getInstance().signOut()
                        lockViewModel.onLogout()  // re-arms lock for next session
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                ) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }
}