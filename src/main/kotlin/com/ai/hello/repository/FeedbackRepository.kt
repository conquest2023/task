package com.ai.hello.repository

import com.ai.hello.repository.entity.FeedBack
import com.ai.hello.repository.entity.Rating
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.awt.print.Pageable

interface FeedbackRepository : JpaRepository<FeedBack, Long> {


    fun existsByUserIdAndChatLineId(userId: Long, chatLineId: Long): Boolean

    fun findAllByUserId(userId: Long, pageable: org.springframework.data.domain.Pageable): Page<FeedBack>

    fun findAllByUserIdAndRating(userId: Long, rating: Rating, pageable: org.springframework.data.domain.Pageable): Page<FeedBack>

    fun findAllByRating(rating: Rating, pageable: org.springframework.data.domain.Pageable): Page<FeedBack>
}