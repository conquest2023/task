package com.ai.hello.service

import com.ai.hello.model.chat.ChatListRequest
import com.ai.hello.model.chat.ChatRequest
import com.ai.hello.model.chat.ChatResponse
import com.ai.hello.model.chat.ChatThreadResponse
import com.ai.hello.repository.ChatRepository
import com.ai.hello.repository.ChatThreadRepository
import com.ai.hello.repository.entity.Chat
import com.ai.hello.repository.entity.ChatThread
import com.ai.hello.util.AuthUtils
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class ChatService(
        private val authUtils: AuthUtils,
        private val chatRepository: ChatRepository,
        private val chatThreadRepository: ChatThreadRepository,
        @Value("\${openai.api-key}") private val apiKey: String,
        @Value("\${openai.model}") private val defaultModel: String
) {
    private val webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader("Authorization", "Bearer $apiKey")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Transactional
    fun createChat(request: ChatRequest): ChatResponse {
        val currentUserId =authUtils.getCurrentUserId()

        val newThread = chatThreadRepository.save(ChatThread(userId = currentUserId))
        val modelToUse = request.model ?: defaultModel
        val answer = callOpenAiApi(request.prompt, modelToUse)


        val newChat = Chat(
                threadId = newThread.id!!,
                question = request.prompt,
                answer = answer
        )

        val savedChat = chatRepository.save(newChat)

        return ChatResponse(
                question = savedChat.question,
                answer = savedChat.answer,
                threadId = newThread.id!!.toString(),
                createdAt = savedChat.createdAt
        )
    }

    private fun callOpenAiApi(prompt: String, model: String): String {

        return "AI가 생성한 ${prompt}에 대한 답변입니다. (모델: $model)"
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getChats(request: ChatListRequest): List<ChatThreadResponse> {
        val currentUserId =  authUtils.getCurrentUserId()
        val currentUserRole = authUtils.getCurrentUserRole()
        val pageable = PageRequest.of(request.page, request.size, request.sortDirection, "createdAt")

        // 1. 권한에 따른 스레드 목록 조회
        val threadPage = when (currentUserRole) {
            "ADMIN" -> chatThreadRepository.findAll(pageable)
            else -> chatThreadRepository.findPageableAllByUserId(currentUserId, pageable)
        }

        val threads = threadPage.content
        val threadIds = threads.map { it.id!! }
        if (threadIds.isEmpty()) return emptyList()


        val allChats = chatRepository.findAllByThreadIdInOrderByCreatedAtAsc(threadIds)
                .groupBy { it.threadId }

        return threads.map { thread ->
            ChatThreadResponse(
                    threadId = thread.id!!,
                    threadCreatedAt = thread.createdAt,
                    chats = allChats[thread.id]?.map {
                        ChatResponse(it.id!!.toString(), it.question, it.answer, it.createdAt)
                    } ?: emptyList()
            )
        }
    }

}
