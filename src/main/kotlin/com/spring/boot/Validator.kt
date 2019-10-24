package com.spring.boot

typealias Validator<T> = (T) -> Try<T>

private data class Validation<T>(val value: T?, val valueName: String, val operation: (T?) -> Boolean)

fun validate(emailRequest: EmailRequest): Try<EmailRequest> {
    val maybeFailures = process(validationsFor(emailRequest))
    return if (maybeFailures.isEmpty()) Success(emailRequest) else Failure(emailRequest, maybeFailures)
}

private fun process(validations: List<Validation<String>>): List<String> {
    return validations
            .filter {
                it.operation(it.value)
            }.map {
                "Attribute ${it.valueName} is mandatory"
            }
}

private fun validationsFor(emailRequest: EmailRequest): List<Validation<String>> {
    return listOf(
            Validation(emailRequest.to, "to") { it.isNullOrBlank() },
            Validation(emailRequest.subject, "subject") { it.isNullOrBlank() })
}