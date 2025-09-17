package com.ai.hello.controller

import com.ai.hello.model.report.UserActivityResponse
import com.ai.hello.service.ReportService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/admin/reports")
class ReportController(
        private val reportService: ReportService
) {

    // 1. 사용자 활동 기록 요청 (관리자 전용)
    @GetMapping("/activity")
    fun getUserActivity(): ResponseEntity<UserActivityResponse> {
        val response = reportService.getUserActivityReport()
        return ResponseEntity.ok(response)
    }

    // 2. 보고서 생성 요청 (CSV 반환, 관리자 전용)
    @GetMapping("/chat-csv")
    fun generateChatReport(): ResponseEntity<String> {
        val csvContent = reportService.generateChatReportCsv()

        val filename = "chat_report_${
            OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
        }.csv"

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("text/csv; charset=UTF-8")
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")

        // BOM (Byte Order Mark)을 추가하여 엑셀에서 한글 깨짐 방지
        val csvWithBOM = "\uFEFF" + csvContent

        return ResponseEntity(csvWithBOM, headers, HttpStatus.OK)
    }
}