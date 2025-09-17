package com.ai.hello.repository.entity

import com.ai.hello.model.UserRole
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(unique = true, nullable = false)
        val email: String,

        @Column(nullable = false)
        val password: String,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val createdAt: OffsetDateTime = OffsetDateTime.now(),

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val role: UserRole
)
