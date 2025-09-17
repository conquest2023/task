package com.ai.hello.model.chat

import java.time.OffsetDateTime

data class ChatThreadResponse(
        val threadId: Long,
        val threadCreatedAt: OffsetDateTime,
        val chats: List<ChatResponse>
)