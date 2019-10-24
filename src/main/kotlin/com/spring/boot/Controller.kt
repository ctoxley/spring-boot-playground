package com.spring.boot

import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

interface Response

data class HealthResponse(val status: String): Response

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping(produces=[APPLICATION_JSON_UTF8_VALUE])
    fun status(): ResponseEntity<Response> {
        return ok(HealthResponse("up"))
    }
}
