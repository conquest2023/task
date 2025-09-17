package com.ai.hello.model.feedback

import com.ai.hello.repository.entity.FeedbackStatus
import com.ai.hello.repository.entity.Rating
import org.springframework.data.domain.Sort

data class FeedbackListRequest(

        val page: Int = 0,
        val size: Int = 20,
        val sortDirection: Sort.Direction = Sort.Direction.DESC,
        val filterByRating: Rating? = null,
        val filterByStatus: FeedbackStatus? = null
)
