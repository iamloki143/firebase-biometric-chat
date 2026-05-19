package com.loki.chatapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.chatapp.navigation.AppNavigation
import com.loki.chatapp.presentation.viewmodel.AppLockViewModel
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel
import com.loki.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val lockViewModel: AppLockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor     = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        setContent {
            val isDark by settingsViewModel.isDarkTheme.collectAsState(initial = false)
            val lang by settingsViewModel.language.collectAsState(initial = "en")

            val context = LocalContext.current

            LaunchedEffect(lang) {
                val locale = java.util.Locale(lang)
                java.util.Locale.setDefault(locale)

                val newConfig = Configuration(context.resources.configuration)
                newConfig.setLocale(locale)

                context.resources.updateConfiguration(
                    newConfig,
                    context.resources.displayMetrics
                )
            }

            ChatAppTheme(isDarkTheme = isDark) {
                AppNavigation(
                    activity = this@MainActivity,
                    settingsViewModel = settingsViewModel,
                    lockViewModel = lockViewModel
                )
            }
        }
    }
}
