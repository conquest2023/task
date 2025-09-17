package com.ai.hello.controller

import com.ai.hello.model.chat.ChatListRequest
import com.ai.hello.model.chat.ChatRequest
import com.ai.hello.model.chat.ChatResponse
import com.ai.hello.model.chat.ChatThreadResponse
import com.ai.hello.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/chat")
class ChatController (
    private val chatService: ChatService
    ) {

        @PostMapping
        fun createChat(@RequestBody request: ChatRequest): ResponseEntity<ChatResponse> {
            val response = chatService.createChat(request)
            return ResponseEntity.status(HttpStatus.CREATED).body(response)
        }


    @GetMapping
    fun getChats(@ModelAttribute request: ChatListRequest): ResponseEntity<List<ChatThreadResponse>> {
        val response = chatService.getChats(request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{threadId}")
    fun deleteThread(@PathVariable threadId: Long): ResponseEntity<Void> {
//        chatService.deleteThread(threadId)
        return ResponseEntity.noContent().build()
    }
}