package com.ai.hello.repository

import com.ai.hello.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean

    fun countByCreatedAtBetween(start: OffsetDateTime, end: OffsetDateTime): Long
}