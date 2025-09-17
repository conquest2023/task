package com.ai.hello.repository

import com.ai.hello.repository.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import java.time.OffsetDateTime

interface ChatRepository : JpaRepository<Chat, Long> {


    fun findTopByThread_UserIdOrderByCreatedAtDesc(userId: Long): Chat?

    fun findAllByThreadIdInOrderByCreatedAtAsc(threadIds: List<Long>): List<Chat>

    fun deleteAllByThreadId(threadId: Long)

    fun countByCreatedAtBetween(start: OffsetDateTime, end: OffsetDateTime): Long
    fun findAllByCreatedAtBetween(start: OffsetDateTime, end: OffsetDateTime): List<Chat>
}