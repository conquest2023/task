package com.ai.hello.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.OffsetDateTime

@Entity
data class ChatThread(
        @Id @GeneratedValue val id: Long? = null,
        val userId: Long,
        val createdAt: OffsetDateTime = OffsetDateTime.now()

)