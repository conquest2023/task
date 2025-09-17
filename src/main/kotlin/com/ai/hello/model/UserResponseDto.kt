package com.ai.hello.model

import java.time.OffsetDateTime

data class UserResponseDto(
        val email: String,
        val name: String,
        val createdAt: OffsetDateTime,
        val role: UserRole,
        val password: String,
)