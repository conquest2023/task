package com.ai.hello.repository

import com.ai.hello.repository.entity.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface LoginHistoryRepository : JpaRepository<LoginHistory, Long> {
    fun countByLoginAtBetween(start: OffsetDateTime, end: OffsetDateTime): Long
}