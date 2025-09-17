package com.ai.hello.model.feedback

import com.ai.hello.repository.entity.Rating

enum class FeedbackCreateRequest(

    val chatLineId: Long,
    val rating: Rating,
)