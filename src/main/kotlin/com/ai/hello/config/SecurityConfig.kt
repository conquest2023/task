import com.ai.hello.model.UserRole
import com.ai.hello.util.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

//package com.ai.hello.config
//
//import com.ai.hello.model.UserRole
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//
@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { it.disable() } // JWT 사용 시 CSRF 비활성화
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션 사용 안 함
                .authorizeHttpRequests { auth ->
                    auth.requestMatchers("/auth/**").permitAll() // 회원가입, 로그인은 인증 없이 허용
                            .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name) // 관리자 권한 필요
                            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                }
                // JWT 검증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}