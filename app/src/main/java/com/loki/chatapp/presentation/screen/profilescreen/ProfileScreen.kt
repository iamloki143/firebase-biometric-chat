package com.loki.chatapp.presentation.screen.profilescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.loki.chatapp.R
import com.loki.chatapp.presentation.viewmodel.SettingsViewModel
import com.loki.chatapp.utils.ProfileCircle
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onSettingsClick: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val settingsViewModel: SettingsViewModel= hiltViewModel()
    val user = FirebaseAuth.getInstance().currentUser
    var username by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val isEnabled by settingsViewModel.authEnabled.collectAsState()

    LaunchedEffect(Unit) {
        settingsViewModel.loadSettings()
    }
    LaunchedEffect(Unit) {

        val uid = user?.uid ?: return@LaunchedEffect

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {

                username = it.getString("name") ?: ""
            }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(stringResource(id=R.string.profile), color = MaterialTheme.colorScheme.onSurface) },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(onClick = { it() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { onLogout() }) {
                            Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )

                )
                Divider(
                    color = MaterialTheme.colorScheme.outline
                )
            }

        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    start = 6.dp,
                    end = 6.dp
                )
                .padding(6.dp).padding(bottom = 0.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            ProfileCircle(name = username)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(

                value = username,

                onValueChange = {

                    username = it.filter { char ->
                        char.isLetterOrDigit() || char == '_'
                    }
                },

                enabled = editing,

                singleLine = true,

                modifier = Modifier.fillMaxWidth(),

                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),

                trailingIcon = {

                    IconButton(

                        onClick = {

                            if (editing) {

                                val trimmedName = username.trim().lowercase()

                                if (trimmedName.isBlank()) {
                                    error = "Username required"
                                    return@IconButton
                                }

                                if (trimmedName.length < 5) {
                                    error = "Minimum 5 characters"
                                    return@IconButton
                                }

                                if (!trimmedName.matches(
                                        Regex("^[a-zA-Z0-9_]+$")
                                    )
                                ) {
                                    error =
                                        "Only letters, numbers, underscore"
                                    return@IconButton
                                }

                                loading = true

                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .whereEqualTo("name", trimmedName)
                                    .get()
                                    .addOnSuccessListener { result ->

                                        val uid = user?.uid ?: return@addOnSuccessListener

                                        val alreadyTaken =
                                            result.documents.any {
                                                it.id != uid
                                            }

                                        if (alreadyTaken) {

                                            loading = false
                                            error = "Username already taken"

                                        } else {

                                            FirebaseFirestore.getInstance()
                                                .collection("users")
                                                .document(uid)
                                                .set(
                                                    mapOf(
                                                        "name" to trimmedName
                                                    ),
                                                    SetOptions.merge()
                                                )
                                                .addOnSuccessListener {

                                                    loading = false
                                                    editing = false
                                                    error = ""
                                                }
                                                .addOnFailureListener {

                                                    loading = false
                                                    error = "Update failed"
                                                }
                                        }
                                    }

                            } else {

                                editing = true
                            }
                        }

                    ) {

                        Icon(
                            imageVector =
                                if (editing)
                                    Icons.Default.Check
                                else
                                    Icons.Default.Edit,

                            contentDescription = null,

                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            if (error.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = MaterialTheme.colorScheme.outline)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingsClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint =  MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Divider(color = MaterialTheme.colorScheme.outline)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogout() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
}