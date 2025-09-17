package com.ai.hello.model.feedback

import com.ai.hello.repository.entity.FeedbackStatus
import com.ai.hello.repository.entity.Rating
import java.time.OffsetDateTime

data class FeedbackResponse(

        val feedbackId: Long,
        val userId: Long,
        val chatLineId: Long,
        val rating: Rating,
        val createdAt: OffsetDateTime,
        val status: FeedbackStatus
)
