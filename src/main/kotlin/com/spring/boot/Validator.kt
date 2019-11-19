package com.spring.boot

import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress

typealias Validator<T> = (T) -> Try<T>

private data class Validation<T>(val value: T?, val onFailureMessage: String, val operation: (T?) -> Boolean)

fun validate(emailRequest: EmailRequest): Try<EmailRequest> {
    val maybeFailures = process(validationsFor(emailRequest))
    return if (maybeFailures.isEmpty()) Success(emailRequest) else Failure(emailRequest, maybeFailures)
}

private fun process(validations: List<Validation<String>>): List<String> {
    return validations
            .filter {
                it.operation(it.value)
            }.map {
                it.onFailureMessage
            }
}

private fun validationsFor(emailRequest: EmailRequest): List<Validation<String>> {
    return emailValidations(emailRequest) + attachmentValidations(emailRequest.attachments)

}

private fun attachmentValidations(attachments: List<Attachment>?): List<Validation<String>> {
    return attachments?.let { attachments -> attachments.flatMap { attachmentValidations(it) } }.orEmpty()
}

private fun attachmentValidations(attachment: Attachment): List<Validation<String>> {
    return listOf(
            Validation(attachment.name, mandatoryMessage("attachment.name"), ::isNotPresent),
            Validation(attachment.extension, mandatoryMessage("attachment.extension"), ::isNotPresent))
}

private fun emailValidations(emailRequest: EmailRequest): List<Validation<String>> {
    return listOf(
            Validation(emailRequest.to, mandatoryMessage("to"), ::isNotPresent),
            Validation(emailRequest.to, "Attribute to is invalid", ::invalidEmail),
            Validation(emailRequest.subject, mandatoryMessage("subject"), ::isNotPresent))
}

private fun isNotPresent(value: String?) = value.isNullOrBlank()

private fun invalidEmail(address: String?): Boolean {
    return try {
        address?.let { InternetAddress(address).validate() }
        false
    } catch (e: AddressException) {
        true
    }
}

private fun mandatoryMessage(attributeName: String) = "Attribute $attributeName is mandatory"