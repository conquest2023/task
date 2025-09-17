package com.ai.hello.util

import com.ai.hello.repository.UserRepository
import org.apache.catalina.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class AuthUtils(
        private val userRepository: UserRepository
) {


    fun getCurrentUserPrincipal(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal
        if (principal !is User) {
            throw IllegalStateException("인증 정보가 유효하지 않습니다.")
        }
        return principal
    }

    fun getCurrentUserEmail(): String = getCurrentUserPrincipal().username


    fun getCurrentUserId(): Long {
        val email = getCurrentUserEmail()

        val user = userRepository.findByEmail(email)
                .orElseThrow {
                    NoSuchElementException("현재 인증된 사용자의 정보를 데이터베이스에서 찾을 수 없습니다. Email: $email")
                }

        return user.id ?: throw IllegalStateException("사용자 ID가 존재하지 않습니다.")
    }

    fun getCurrentUserRole(): String {
//        return getCurrentUserPrincipal().authorities.firstOrNull()?.authority?.removePrefix("ROLE_") ?: "MEMBER"

        return "hello";
    }
}