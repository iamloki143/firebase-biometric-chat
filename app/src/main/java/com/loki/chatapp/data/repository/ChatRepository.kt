package com.loki.chatapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.loki.chatapp.domain.model.Message

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()
    fun sendMessage(chatId: String, message: String,userId: String, otherUserId: String){
        val chatRef = db.collection("chats").document(chatId)
        chatRef.get().addOnSuccessListener{ doc ->
            if (!doc.exists()){
                val chatData = hashMapOf(
                    "participants" to listOf(userId,otherUserId)
                )
                chatRef.set(chatData)
            }
            val msg = hashMapOf(
                "senderId" to userId,
                "text" to message,
                "timestamp" to System.currentTimeMillis()
            )
            chatRef
                .collection("messages")
                .add(msg)
        }
    }
    fun listenMessage(chatId: String,onUpdate:(List<Message>) -> Unit){
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error!= null) return@addSnapshotListener

                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(Message::class.java)
                } ?: emptyList()
                onUpdate(messages)
            }

    }
}