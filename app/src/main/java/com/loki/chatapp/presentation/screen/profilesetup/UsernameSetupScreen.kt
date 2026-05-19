package com.loki.chatapp.presentation.screen.profilesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.loki.chatapp.R

@Composable
fun UsernameSetupScreen (
    onNext:() -> Unit
){
    var name by remember{mutableStateOf("")}
    var loading by remember{ mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(id=R.string.username_setup_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.filter { char -> char.isLetterOrDigit() ||char == '_'} },
                    placeholder = {
                        Text(stringResource(id=R.string.username), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary

                    )
                )
                if (error.isNotEmpty()){
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val trimmedName =name.trim().lowercase()
                        if (!trimmedName.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                            error = "Only letters, numbers, and underscore allowed"
                            return@Button
                        }
                        if (trimmedName.isBlank()){
                            error = "username is required"
                            return@Button
                        }
                        if (trimmedName.length < 5){
                            error = "Username must be at least 5 characters"
                            return@Button
                        }
                        loading = true
                        error=""

                        val db = FirebaseFirestore.getInstance()
                        db.collection("users")
                            .whereEqualTo("name",trimmedName)
                            .get()
                            .addOnSuccessListener {result ->
                                if (!result.isEmpty){
                                    loading = false
                                    error="Username already taken"
                                }else{
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@addOnSuccessListener
                                    val db = FirebaseFirestore.getInstance()
                                    db.collection("users")
                                        .document(uid)
                                        .set(
                                            mapOf("name" to trimmedName),
                                            SetOptions.merge()
                                        )
                                        .addOnSuccessListener {
                                            loading= false
                                            onNext()
                                        }
                                        .addOnFailureListener {
                                            loading=false
                                            error="Something went wrong"
                                        }
                                }
                            }
                            .addOnFailureListener {
                                loading=false
                                error="Failed to check username"
                            }


                    },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                ) {
                    if (loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(stringResource(id=R.string.next))
                    }
                }
            }
        }
    }
}