package com.ai.hello.model.report

import java.time.OffsetDateTime

data class UserActivityResponse(

        val date: OffsetDateTime,
        val signUpCount: Long,
        val loginCount: Long,
        val chatCreationCount: Long
)
