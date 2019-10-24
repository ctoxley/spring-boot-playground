package com.spring.boot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.mockito.internal.progress.SequenceNumber

private val objectMapper = jacksonObjectMapper()
private val oneIssueBlankSubject = listOf("Attribute subject is mandatory")

fun anEmailRequest() = EmailRequest(appendNumTo("to"), appendNumTo("subject"), appendNumTo("body"), listOf(appendNumTo("attachment")))
fun anEmailRequestWithBlankSubject() = EmailRequest(appendNumTo("to"), " ", appendNumTo("body"), listOf(appendNumTo("attachment")))
fun anEmailRequestWithNoSubject() = EmailRequest(appendNumTo("to"), null, appendNumTo("body"), listOf(appendNumTo("attachment")))
fun anEmailRequestWithBlankTo() = EmailRequest(" ", appendNumTo("subject"), appendNumTo("body"), listOf(appendNumTo("attachment")))
fun anEmailRequestWithNoTo() = EmailRequest(null, appendNumTo("subject"), appendNumTo("body"), listOf(appendNumTo("attachment")))

fun anEmailResponseWithBlankSubjectIssue() = BadRequestResponse(oneIssueBlankSubject)
fun aFailureWithBlackSubjectIssue(emailRequest: EmailRequest) = Failure(emailRequest, oneIssueBlankSubject)

fun appendNumTo(value: String): String {
    return "${value}${next()}"
}

fun next() = SequenceNumber.next()

fun <T> toJson(t: T) = objectMapper.writeValueAsString(t)