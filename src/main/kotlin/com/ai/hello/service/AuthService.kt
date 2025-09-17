package com.ai.hello.service

import com.ai.hello.model.LoginRequest
import com.ai.hello.model.SignUpRequest
import com.ai.hello.model.UserRole
import com.ai.hello.repository.UserRepository
import com.ai.hello.util.JwtTokenProvider
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun signUp(request: SignUpRequest): Long? {

        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }
        val hashedPassword = passwordEncoder.encode(request.password)
        val user = com.ai.hello.repository.entity.User(
                email = request.email,
                password = hashedPassword,
                name = request.name,
                role = UserRole.MEMBER // 기본 권한 MEMBER
        )
        return userRepository.save(user).id
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
                .orElseThrow { IllegalArgumentException("이메일 또는 패스워드가 잘못되었습니다.") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("이메일 또는 패스워드가 잘못되었습니다.")
        }

        return jwtTokenProvider.createToken(user.email, user.role)
    }
}