package com.loki.chatapp.domain.usecase

import com.loki.chatapp.data.repository.ChatRepository
import com.loki.chatapp.domain.model.Message

class ListenMessagesUseCase(
    private val repo: ChatRepository
) {
    operator fun invoke(chatId: String,onUpdate:(List<Message>) -> Unit){
        repo.listenMessage(chatId,onUpdate)
    }
}