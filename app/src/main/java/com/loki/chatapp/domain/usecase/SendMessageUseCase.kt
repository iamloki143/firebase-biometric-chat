package com.loki.chatapp.domain.usecase

import com.loki.chatapp.data.repository.ChatRepository

class SendMessageUseCase(
    private val repo: ChatRepository
) {
    operator fun invoke(chatId: String,message:String,userId:String,otherUserId: String){
        repo.sendMessage(chatId,message,userId,otherUserId)
    }
}