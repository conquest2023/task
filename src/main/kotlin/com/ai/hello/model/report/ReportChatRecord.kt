package com.ai.hello.model.report

import java.time.OffsetDateTime

data class ReportChatRecord(

        val userName: String,
        val userEmail: String,
        val threadId: Long,
        val chatLineId: Long,
        val question: String,
        val answer: String,
        val createdAt: OffsetDateTime
)
