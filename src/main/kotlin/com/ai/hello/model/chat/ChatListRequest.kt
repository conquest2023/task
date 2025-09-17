package com.ai.hello.model.chat

import org.springframework.data.domain.Sort

data class ChatListRequest(
        val page: Int = 0,
        val size: Int = 20,
        val sortDirection: Sort.Direction = Sort.Direction.DESC
)