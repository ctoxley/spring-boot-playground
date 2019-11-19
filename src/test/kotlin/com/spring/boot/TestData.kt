package com.spring.boot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.mockito.internal.progress.SequenceNumber

private val objectMapper = jacksonObjectMapper()
private val oneIssueBlankSubject = listOf("Attribute subject is mandatory")

fun anEmailRequest() = EmailRequest("${appendNumTo("to@company")}.com", appendNumTo("subject"), appendNumTo("body"), oneAttachment())
fun anEmptyEmailRequest() = EmailRequest(null, null, null, emptyList())
fun anEmailRequestWithBlankSubject() = anEmailRequest().copy(subject = " ")
fun anEmailRequestWithoutSubject() = anEmailRequest().copy(subject = null)
fun anEmailRequestWithBlankTo() = anEmailRequest().copy(to = " ")
fun anEmailRequestWithoutTo() = anEmailRequest().copy(to = null)
fun anEmailRequestWithoutBody() = anEmailRequest().copy(body = null)
fun anEmailRequestWithInvalidEmail() = anEmailRequest().copy(to = appendNumTo("to"))
fun anEmailRequestWithoutAttachmentName() = anEmailRequest().copy(attachments = oneAttachmentWithoutName())
fun anEmailRequestWithBlankAttachmentName() = anEmailRequest().copy(attachments = oneAttachmentWithBlankName())
fun anEmailRequestWithoutAttachmentExtension() = anEmailRequest().copy(attachments = oneAttachmentWithoutExtension())
fun anEmailRequestWithBlankAttachmentExtension() = anEmailRequest().copy(attachments = oneAttachmentWithBlankExtension())

fun anAttachment() = Attachment(appendNumTo("name"), appendNumTo(".extension"), appendNumTo("content"))
fun oneAttachment() = listOf(anAttachment())
fun oneAttachmentWithoutName() = listOf(anAttachment().copy(name = null))
fun oneAttachmentWithBlankName() = listOf(anAttachment().copy(name = " "))
fun oneAttachmentWithoutExtension() = listOf(anAttachment().copy(extension = null))
fun oneAttachmentWithBlankExtension() = listOf(anAttachment().copy(extension = " "))

fun anEmailResponseWithBlankSubjectIssue() = BadRequestResponse(oneIssueBlankSubject)
fun aFailureWithBlackSubjectIssue(emailRequest: EmailRequest) = Failure(emailRequest, oneIssueBlankSubject)

fun appendNumTo(value: String): String {
    return "${value}${next()}"
}

fun next() = SequenceNumber.next()

fun <T> toJson(t: T) = objectMapper.writeValueAsString(t)

fun <T> fromJson(json: String, type: Class<T>) = objectMapper.readValue(json, type)