package com.loki.chatapp.presentation.screen.authscreen

import androidx.compose.foundation.border
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loki.chatapp.presentation.state.AuthState
import com.loki.chatapp.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreen( viewModel: AuthViewModel = hiltViewModel(), onAuthSuccess: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = viewModel.state
    Column {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Card(
                modifier = Modifier.fillMaxWidth().padding(12.dp).border(1.dp,Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab==0,
                            onClick = { selectedTab=0},
                            text = {Text("Login")}
                        )
                        Tab(
                            selected = selectedTab ==1,
                            onClick = {selectedTab=1},
                            text = {Text("Sign Up")}

                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Enter EmailID", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Enter Password", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    if (selectedTab==1){
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Enter EmailID", color = Color.LightGray) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.Gray
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(onClick = {
                        if (email.isBlank() || password.isBlank()) return@Button

                        if (selectedTab ==0){
                            viewModel.login(email,password)
                        }else{
                            if (password!=confirmPassword) return@Button
                            viewModel.signup(email,password)
                            selectedTab=0
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (selectedTab==0)"Login" else "Sign Up")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    when(state){
                        AuthState.Loading -> CircularProgressIndicator()
                        AuthState.Success -> {
                            LaunchedEffect(Unit) {
                                onAuthSuccess()
                            }
                        }
                        is AuthState.Error -> Text(state.message,color = Color.Red)
                        else -> {}
                    }
                }
            }
        }
    }
}