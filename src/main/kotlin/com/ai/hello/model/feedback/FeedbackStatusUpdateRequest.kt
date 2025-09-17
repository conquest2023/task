package com.ai.hello.model.feedback

import com.ai.hello.repository.entity.FeedbackStatus

data class FeedbackStatusUpdateRequest(
        val status: FeedbackStatus
)
