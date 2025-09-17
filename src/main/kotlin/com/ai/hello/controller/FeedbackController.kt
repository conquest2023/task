package com.ai.hello.controller

import com.ai.hello.model.feedback.FeedbackCreateRequest
import com.ai.hello.model.feedback.FeedbackListRequest
import com.ai.hello.model.feedback.FeedbackResponse
import com.ai.hello.model.feedback.FeedbackStatusUpdateRequest
import com.ai.hello.service.FeedbackService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feedbacks")
class FeedbackController(
        private val feedbackService: FeedbackService
) {
    @PostMapping
    fun createFeedback(@RequestBody request: FeedbackCreateRequest): ResponseEntity<FeedbackResponse> {
        val response = feedbackService.createFeedback(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getFeedbacks(@ModelAttribute request: FeedbackListRequest): ResponseEntity<Page<FeedbackResponse>> {
        val responsePage = feedbackService.getFeedbacks(request)
        return ResponseEntity.ok(responsePage)
    }

    // 3. 피드백 상태 변경 (관리자 전용)
    @PatchMapping("/{feedbackId}/status")
    fun updateFeedbackStatus(
            @PathVariable feedbackId: Long,
            @RequestBody request: FeedbackStatusUpdateRequest
    ): ResponseEntity<FeedbackResponse> {
        val response = feedbackService.updateFeedbackStatus(feedbackId, request)
        return ResponseEntity.ok(response)
    }
}