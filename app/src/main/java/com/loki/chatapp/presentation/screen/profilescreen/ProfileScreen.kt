package com.loki.chatapp.presentation.screen.profilescreen

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val settingsViewModel: SettingsViewModel= hiltViewModel()
    val user = FirebaseAuth.getInstance().currentUser
    var username by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }


    val isEnabled by remember { derivedStateOf { settingsViewModel.authEnabled ?: false } }

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
                    color = Color.White
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

                            tint = Color.White
                        )
                    }
                },

                colors = OutlinedTextFieldDefaults.colors(

                    focusedBorderColor = Color.White,

                    unfocusedBorderColor =
                        Color.White.copy(alpha = 0.5f),

                    disabledBorderColor =
                        Color.White.copy(alpha = 0.5f),

                    focusedTextColor = Color.White,

                    unfocusedTextColor = Color.White,

                    disabledTextColor = Color.White,

                    cursorColor = Color.White
                )
            )
            if (error.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = error,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onLogout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xAA000000)
                )
            ) {
                Text("Logout", color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
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
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.padding(10.dp).padding(top = 20.dp)

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.fingerprint), contentDescription = "Fingerprint",
                                    modifier = Modifier.size(40.dp))
                            }
                            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                                Text(
                                    text = "User Device Authentication",
                                    color=Color.White,
                                    fontWeight = Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "Required when opening the app",
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                            Switch(
                                checked = isEnabled,
                                onCheckedChange = {settingsViewModel.onToggleChanged(it)},
                                modifier = Modifier.padding(16.dp).padding(top = 20.dp)
                            )
                        }
                        Divider(
                            color = Color.White.copy(alpha = 0.2f),
                            thickness = 1.dp
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
                                text = "Your authentication data remains security managed by your device.",
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ){
                    val imageprint = if (isEnabled) painterResource(R.drawable.greenlock) else painterResource(R.drawable.redlock)
                    val cardColor=if (isEnabled) Color(0xFF91D06C) else Color(0xFFF44336)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(10.dp).padding(top = 20.dp)

                    ) {
                        Image(
                            painter = imageprint,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            text = if (isEnabled) "Device Authentication Enabled" else "Device Authentication Disabled",
                            fontWeight = Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = if (isEnabled) "Your app is protected using your device security authentication." else "Turn on device authentication to prevent unauthorized access.",
                            color = Color.White.copy(alpha = 0.6f),
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