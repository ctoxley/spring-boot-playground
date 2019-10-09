package com.spring.boot

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping(produces=[MediaType.APPLICATION_JSON_VALUE])
    fun status(): String {
        return "{ \"status\": \"up\"}"
    }
}