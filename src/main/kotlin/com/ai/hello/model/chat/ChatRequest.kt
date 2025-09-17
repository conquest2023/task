package com.ai.hello.model.chat

data class ChatRequest (
    val prompt: String,
    val isStreaming: Boolean = false,
    val model: String? = null
)