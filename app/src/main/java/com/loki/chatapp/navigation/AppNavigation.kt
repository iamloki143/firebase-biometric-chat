package com.loki.chatapp.navigation

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.loki.chatapp.presentation.screen.authscreen.AuthScreen
import com.loki.chatapp.presentation.screen.authscreen.WelcomeScreen
import com.loki.chatapp.presentation.screen.chatscreen.ChatScreen
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val viewModel: SettingsViewModel = hiltViewModel()

    var startDestination by remember { mutableStateOf<String?>(null) }

    var shouldAuthenticate by remember { mutableStateOf(false) }
    var navigateToMain by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSettings()
    }

    LaunchedEffect(viewModel.authEnabled) {

        val auth = viewModel.authEnabled
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (auth == null) return@LaunchedEffect

        when {
            currentUser == null -> {
                startDestination = Screen.Welcome.route
            }

            auth -> {
                shouldAuthenticate = true
            }

            else -> {
                startDestination = Screen.Main.route
            }
        }
    }

    if (shouldAuthenticate && activity != null) {

        LaunchedEffect(Unit) {

            val executor = ContextCompat.getMainExecutor(context)

            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        startDestination = Screen.Main.route
                        navigateToMain = true
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        if(errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_CANCELED){
                            activity.finish()
                            Toast.makeText(
                                context,
                                "Authentication Canceled",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onAuthenticationFailed() {}
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock App")
                .setSubtitle("Use your fingerprint or PIN")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    if (startDestination == null) {
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

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {

        composable(Screen.Main.route) {
            MainScreen(
                rootNavController = navController,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen {
                navController.navigate(Screen.Auth.route)
            }
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Chat.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            ChatScreen(
                userId = userId,
                userName = userName,
                onBack = { navController.popBackStack() }
            )
        }
    }

    LaunchedEffect(navigateToMain) {
        if (navigateToMain) {
            navController.navigate(Screen.Main.route) {
                popUpTo(0)
            }
            navigateToMain = false
        }
    }
}