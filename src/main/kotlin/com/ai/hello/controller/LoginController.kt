package com.ai.hello.controller

import com.ai.hello.model.AuthTokenResponse
import com.ai.hello.model.LoginRequest
import com.ai.hello.model.SignUpRequest
import com.ai.hello.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
        private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<Map<String, Any>> {
        val userId = authService.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapOf("userId" to userId!!, "message" to "회원가입이 완료되었습니다."))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthTokenResponse> {

        val token = authService.login(request)
        return ResponseEntity.ok(AuthTokenResponse(accessToken = token))
    }

}