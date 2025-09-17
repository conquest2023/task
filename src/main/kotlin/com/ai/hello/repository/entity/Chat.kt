package com.ai.hello.repository.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
data class Chat(
        @Id @GeneratedValue val id: Long? = null,
        val threadId: Long,
        val question: String,
        val answer: String,
        val createdAt: OffsetDateTime = OffsetDateTime.now(),


        @ManyToOne // Chat은 하나의 Thread에 속함
        @JoinColumn(name = "thread_id", nullable = false)
        val thread: ChatThread? = null
)