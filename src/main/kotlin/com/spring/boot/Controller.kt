package com.spring.boot

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*

interface Response

data class EmailRequest(val to: String?, val subject: String?, val body: String?, val attachments: List<String>?)

data class BadRequestResponse(val issues: List<String>): Response
data class HealthResponse(val status: String): Response

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping(produces=[APPLICATION_JSON_UTF8_VALUE])
    fun status(): ResponseEntity<Response> {
        return ok(HealthResponse("up"))
    }
}

@RestController
@RequestMapping("/email")
class EmailController(private val validator: Validator<EmailRequest> = ::validate) {

    @PostMapping(consumes=[APPLICATION_JSON_UTF8_VALUE], produces=[APPLICATION_JSON_UTF8_VALUE])
    fun send(@RequestBody emailRequest: EmailRequest): ResponseEntity<Response> {
        return when (val successOfFail = validator(emailRequest)) {
            is Success -> ResponseEntity(CREATED)
            is Failure -> badRequest().body(BadRequestResponse(successOfFail.issues))
        }
    }
}