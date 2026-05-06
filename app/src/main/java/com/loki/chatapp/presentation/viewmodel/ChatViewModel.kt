package com.loki.chatapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.util.CollectionUtils.mapOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loki.chatapp.domain.model.Message
import com.loki.chatapp.domain.model.User
import com.loki.chatapp.domain.usecase.ListenMessagesUseCase
import com.loki.chatapp.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.checkerframework.checker.units.qual.Current
import javax.inject.Inject
import kotlin.collections.mapOf

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val listenMessagesUseCase: ListenMessagesUseCase
): ViewModel() {

    var sentRequestIds by mutableStateOf<List<String>>(emptyList())
        private set
    var contactIds by mutableStateOf<List<String>>(emptyList())
        private set

    var allUsers by mutableStateOf<List<User>>(emptyList())
        private set
    fun loadAllUsers(){
        val currentUserId= FirebaseAuth.getInstance().currentUser?.uid ?:return
        FirebaseFirestore.getInstance()
            .collection("users")
            .addSnapshotListener { snapshots, _ ->
                allUsers=snapshots?.documents
                    ?.mapNotNull { it.toObject(User::class.java) }
                    ?.filter { it.userId!=currentUserId }
                    ?:emptyList()
            }
    }

    var users by mutableStateOf<List<User>>(emptyList())
        private set
    fun loadContacts(){
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db =FirebaseFirestore.getInstance()
        db.collection("contacts")
            .document(currentUserId)
            .collection("userList")
            .addSnapshotListener { snapshots, _ ->
                val contactIds = snapshots?.documents?.map{ it.id }?:emptyList()
                if (contactIds.isEmpty()){
                    users=emptyList()
                    return@addSnapshotListener
                }
                db.collection("users")
                    .whereIn("userId",contactIds)
                    .addSnapshotListener { userSnap, _ ->
                        users = userSnap?.documents
                            ?.mapNotNull { it.toObject(User::class.java) }
                            ?:emptyList()
                    }
            }
    }
    fun sendMessage(chatId:String,text: String,otherUserId: String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        sendMessageUseCase(chatId,text,userId,otherUserId)
    }
    var messages by mutableStateOf<List<Message>>(emptyList())
        private set
    fun startListening(chatId: String){
        listenMessagesUseCase(chatId){newMessages ->
            messages=newMessages
        }
    }
    fun sendRequest(currentUserId: String, otherUserId: String){
        val request = hashMapOf(
            "fromUserId" to currentUserId,
            "toUserId" to otherUserId,
            "status" to "pending",
            "timestamp" to System.currentTimeMillis()
        )
        FirebaseFirestore.getInstance()
            .collection("requests")
            .add(request)
    }
    fun loadSentRequests(){
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("requests")
            .whereEqualTo("fromUserId", currentUserId)
            .whereEqualTo("status", "pending")
            .addSnapshotListener { snapshots, _ ->
                sentRequestIds =snapshots?.documents?.mapNotNull {
                    it.getString("toUserId")
                } ?:emptyList()
            }
    }
    fun loadContactIds() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("contacts")
            .document(currentUserId)
            .collection("userList")
            .addSnapshotListener { snapshot, _ ->

                contactIds = snapshot?.documents?.mapNotNull {
                    it.id
                } ?: emptyList()
            }
    }
    var requests by mutableStateOf<List<User>>(emptyList())
        private set
    fun loadRequests(){
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?:return
        FirebaseFirestore.getInstance()
            .collection("requests")
            .whereEqualTo("toUserId", currentUserId)
            .whereEqualTo("status","pending")
            .addSnapshotListener { snapshots, _ ->
                val senderIds =snapshots?.documents?.mapNotNull {
                    it.getString("fromUserId")
                }?:emptyList()
                loadRequestUsers(senderIds)
            }
    }
    private fun loadRequestUsers(ids: List<String>) {

        if (ids.isEmpty()) {
            requests = emptyList()
            return
        }
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereIn("userId", ids)
            .addSnapshotListener { snapshot, _ ->
                requests = snapshot?.documents
                    ?.mapNotNull { it.toObject(User::class.java) }
                    ?: emptyList()
            }
    }
    fun acceptRequest(otherUserId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        val data = mapOf("addedAt" to System.currentTimeMillis())

        db.collection("contacts")
            .document(currentUserId)
            .collection("userList")
            .document(otherUserId)
            .set(data)

        db.collection("contacts")
            .document(otherUserId)
            .collection("userList")
            .document(currentUserId)
            .set(data)

        db.collection("requests")
            .whereEqualTo("fromUserId", otherUserId)
            .whereEqualTo("toUserId", currentUserId)
            .get()
            .addOnSuccessListener { result ->
                result.documents.forEach {
                    it.reference.update("status", "accepted")
                }
            }
    }
}