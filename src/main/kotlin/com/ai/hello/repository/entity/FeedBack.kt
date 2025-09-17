package com.ai.hello.repository.entity

import jakarta.persistence.*
import java.time.OffsetDateTime

data class FeedBack(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val userId: Long,

        @Column(nullable = false)
        val chatLineId: Long,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val rating: Rating,

        @Column(nullable = false)
        val createdAt: OffsetDateTime = OffsetDateTime.now(),

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var status: FeedbackStatus = FeedbackStatus.PENDING

)