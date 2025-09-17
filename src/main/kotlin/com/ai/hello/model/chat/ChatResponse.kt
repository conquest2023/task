package com.ai.hello.model.chat

import java.time.OffsetDateTime

data class ChatResponse(
        val question: String,
        val answer: String,
        val threadId: String,
        val createdAt: OffsetDateTime
)
