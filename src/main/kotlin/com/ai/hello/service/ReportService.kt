package com.ai.hello.service

import com.ai.hello.model.report.ReportChatRecord
import com.ai.hello.model.report.UserActivityResponse
import com.ai.hello.repository.ChatRepository
import com.ai.hello.repository.LoginHistoryRepository
import com.ai.hello.repository.UserRepository
import com.ai.hello.util.AuthUtils
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Service
import java.io.StringWriter
import java.nio.file.AccessDeniedException
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Service
class ReportService (
        private val userRepository: UserRepository,
        private val chatRepository: ChatRepository,
        private val loginHistoryRepository: LoginHistoryRepository,
        private val authUtils: AuthUtils
){

    fun getUserActivityReport(): UserActivityResponse {
        // 1. 관리자 권한 검증
        if (authUtils.getCurrentUserRole() != "ADMIN") {
            throw AccessDeniedException("사용자 활동 기록은 관리자만 요청 가능합니다.")
        }

        val endTime = OffsetDateTime.now()
        val startTime = endTime.minus(1, ChronoUnit.DAYS)


        val signUpCount = userRepository.countByCreatedAtBetween(startTime, endTime)


        val loginCount = loginHistoryRepository.countByLoginAtBetween(startTime, endTime)

        val chatCreationCount = chatRepository.countByCreatedAtBetween(startTime, endTime)

        return UserActivityResponse(
                date = endTime,
                signUpCount = signUpCount,
                loginCount = loginCount,
                chatCreationCount = chatCreationCount
        )
    }

    fun generateChatReportCsv(): String {
        if (authUtils.getCurrentUserRole() != "ADMIN") {
            throw AccessDeniedException("보고서 생성은 관리자만 요청 가능합니다.")
        }

        val endTime = OffsetDateTime.now()
        val startTime = endTime.minus(1, ChronoUnit.DAYS) // 24시간 전


        // 하루 동안의 모든 대화 조회
        val chats = chatRepository.findAllByCreatedAtBetween(startTime, endTime)

        val userIds = chats.mapNotNull { it.thread?.userId }.distinct()
        val users = userRepository.findAllById(userIds).associateBy { it.id }

        val records = chats.mapNotNull { chat ->
            val thread = chat.thread ?: return@mapNotNull null
            val user = users[thread.userId] ?: return@mapNotNull null

            ReportChatRecord(
                    userName = user.name,
                    userEmail = user.email,
                    threadId = thread.id!!,
                    chatLineId = chat.id!!,
                    question = chat.question.replace("\n", " "), // 줄바꿈 제거
                    answer = chat.answer.replace("\n", " "),
                    createdAt = chat.createdAt
            )
        }

        // 4. CSV 형식으로 변환 및 반환
        return convertRecordsToCsv(records)
    }
    private fun convertRecordsToCsv(records: List<ReportChatRecord>): String {
        val stringWriter = StringWriter()

        val csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(
                        "userName", "userEmail", "threadId", "chatLineId",
                        "question", "answer", "createdAt"
                )
                .build()

        CSVPrinter(stringWriter, csvFormat).use { csvPrinter ->
            records.forEach { record ->
                csvPrinter.printRecord(
                        record.userName,
                        record.userEmail,
                        record.threadId,
                        record.chatLineId,
                        record.question,
                        record.answer,
                        record.createdAt
                )
            }
        }
        return stringWriter.toString()
    }

}