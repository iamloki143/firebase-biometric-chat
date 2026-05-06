package com.loki.chatapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loki.chatapp.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(private val auth: FirebaseAuth): AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signup(email: String, password: String): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email,password).await()
            val uid = result.user?.uid ?: throw Exception("User not created")
            val user = hashMapOf(
                "userId" to uid,
                "email" to email,
                "name" to "",
                "profileImageUrl" to ""
            )
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(user)
                .await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}