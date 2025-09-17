package com.ai.hello.repository.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
data class LoginHistory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val userId: Long,

        @Column(nullable = false)
        val loginAt: OffsetDateTime = OffsetDateTime.now()
)
