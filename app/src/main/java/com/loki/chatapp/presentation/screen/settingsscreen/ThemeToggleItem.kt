package com.loki.chatapp.presentation.screen.settingsscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.chatapp.R
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel

@Composable
fun ThemeToggleItem(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDark by viewModel.isDarkTheme.collectAsState()

    if (isDark == null) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.theme),
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isDark!!,
            onCheckedChange = { viewModel.onThemeChanged(it) }
        )
    }
}
