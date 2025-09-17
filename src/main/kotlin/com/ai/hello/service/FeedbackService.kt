package com.ai.hello.service

import com.ai.hello.model.feedback.FeedbackCreateRequest
import com.ai.hello.model.feedback.FeedbackListRequest
import com.ai.hello.model.feedback.FeedbackResponse
import com.ai.hello.model.feedback.FeedbackStatusUpdateRequest
import com.ai.hello.repository.ChatRepository
import com.ai.hello.repository.FeedbackRepository
import com.ai.hello.repository.entity.FeedBack
import com.ai.hello.repository.entity.FeedbackStatus
import com.ai.hello.util.AuthUtils
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException

@Service
class FeedbackService(
        private val feedbackRepository: FeedbackRepository,
        private val chatRepository: ChatRepository,
        private val authUtils: AuthUtils
) {
    @Transactional
    fun createFeedback(request: FeedbackCreateRequest): FeedbackResponse {
        val currentUserId = authUtils.getCurrentUserId()
        val currentUserRole = authUtils.getCurrentUserRole()

        val chatLineId = request.chatLineId

        val chatLine = chatRepository.findById(chatLineId)
                .orElseThrow { NoSuchElementException("대화(Chat Line)가 존재하지 않습니다: $chatLineId") }


        if (feedbackRepository.existsByUserIdAndChatLineId(currentUserId, chatLineId)) {
            throw IllegalArgumentException("이미 해당 대화에 대한 피드백을 생성했습니다.")
        }

        val feedback = FeedBack(
                userId = currentUserId,
                chatLineId = chatLineId,
                rating = request.rating,
                status = FeedbackStatus.PENDING
        )
        val savedFeedback = feedbackRepository.save(feedback)

        return mapToFeedbackResponse(savedFeedback)
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getFeedbacks(request: FeedbackListRequest): Page<FeedbackResponse> {
        val currentUserId = authUtils.getCurrentUserId()
        val currentUserRole = authUtils.getCurrentUserRole()

        val pageable = PageRequest.of(request.page, request.size, request.sortDirection, "createdAt")

        val feedbackPage: Page<FeedBack> = when (currentUserRole) {
            "ADMIN" ->
                request.filterByRating?.let { rating ->
                    feedbackRepository.findAllByRating(rating, pageable)
                } ?: feedbackRepository.findAll(pageable)
            else ->

                request.filterByRating?.let { rating ->
                    feedbackRepository.findAllByUserIdAndRating(currentUserId, rating, pageable)
                } ?: feedbackRepository.findAllByUserId(currentUserId, pageable)
        }


        return feedbackPage.map { mapToFeedbackResponse(it)
        }
    }


    @Transactional
    fun updateFeedbackStatus(feedbackId: Long, request: FeedbackStatusUpdateRequest): FeedbackResponse {
        val currentUserRole = authUtils.getCurrentUserRole()

        if (currentUserRole != "ADMIN") {
            throw AccessDeniedException("피드백 상태를 변경할 권한이 없습니다. (관리자 전용)")
        }

        val feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow { NoSuchElementException("피드백이 존재하지 않습니다: $feedbackId") }

        // 2. 상태 업데이트
        feedback.status = request.status
        val updatedFeedback = feedbackRepository.save(feedback)

        return mapToFeedbackResponse(updatedFeedback)
    }

    private fun mapToFeedbackResponse(feedback: FeedBack): FeedbackResponse {
        return FeedbackResponse(
                feedbackId = feedback.id!!,
                userId = feedback.userId,
                chatLineId = feedback.chatLineId,
                rating = feedback.rating,
                createdAt = feedback.createdAt,
                status = feedback.status
        )
    }
}