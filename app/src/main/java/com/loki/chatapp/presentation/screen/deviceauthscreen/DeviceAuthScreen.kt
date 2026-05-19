package com.loki.chatapp.presentation.screen.deviceauthscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.loki.chatapp.R
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel
import com.airbnb.lottie.compose.*
import com.loki.chatapp.auth.DeviceAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAuthScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val isEnabled by settingsViewModel.authEnabled.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_device_auth)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).padding(padding).background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(24.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Card(
                                colors = CardDefaults.cardColors( containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.padding(10.dp).padding(top = 20.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.fingerprint), contentDescription = "Fingerprint",
                                    modifier = Modifier.size(40.dp))
                            }
                            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                                Text(
                                    text = stringResource(id=R.string.user_device_auth),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = stringResource(id=R.string.user_dev_auth_summary),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                            val context = LocalContext.current
                            val activity = context as FragmentActivity

                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { newValue ->
                                    DeviceAuthManager.authenticate(
                                        activity = activity,
                                        reason = if (newValue)
                                            "Enable biometric lock"
                                        else
                                            "Disable biometric lock"
                                    ) { success ->

                                        if (success) {
                                            settingsViewModel.onToggleChanged(newValue)
                                        } else { }
                                    }
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .padding(top = 20.dp)
                            )
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outline
                        )
                        Row(
                            modifier = Modifier.padding(8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.safeauth), contentDescription = "Safe Data",
                                modifier = Modifier.size(20.dp))
                            Text(
                                text = stringResource(id=R.string.safe_check),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.lock_anim)
                    )

                    val animState = animateLottieCompositionAsState(
                        composition = composition,
                        isPlaying = true,
                        iterations = 1,
                        restartOnPlay = true,
                        speed = if (isEnabled) 1f else -1f,
                        clipSpec = LottieClipSpec.Frame(
                            min = 8,
                            max = 180
                        )
                    )

                    LottieAnimation(
                        composition = composition,
                        progress = animState.progress,
                        modifier = Modifier.size(100.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            text = if (isEnabled) stringResource(id = R.string.dev_auth_enabled) else stringResource(
                                id = R.string.dev_auth_disabled
                            ),
                            fontWeight = Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = if (isEnabled) stringResource(id = R.string.enabled_summary) else stringResource(
                                id = R.string.disabled_summary
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(19.dp).padding(start = 10.dp).fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
    }




