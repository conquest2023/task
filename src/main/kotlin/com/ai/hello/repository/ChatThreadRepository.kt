package com.ai.hello.repository

import com.ai.hello.repository.entity.ChatThread
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatThreadRepository : JpaRepository<ChatThread, Long> {
    fun findAllByUserId(userId: Long): List<ChatThread>



    fun findPageableAllByUserId(userId: Long, pageable: Pageable): Page<ChatThread>
}
