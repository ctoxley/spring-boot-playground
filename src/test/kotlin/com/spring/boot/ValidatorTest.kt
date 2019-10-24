package com.spring.boot

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test

class ValidateTest {

    @Test
    fun `failure to null`() {
        val failure = validate(anEmailRequestWithNoTo())
        assertThat(failure).isInstanceOf(Failure::class.java)
    }

    @Test
    fun `failure to blank`() {
        val failure = validate(anEmailRequestWithBlankTo())
        assertThat(failure).isInstanceOf(Failure::class.java)
    }

    @Test
    fun `failure to reason provided`() {
        when (val failure = validate(anEmailRequestWithBlankTo())) {
            is Failure -> assertThat(failure.issues).contains(aMandatoryIssue("to"))
            else -> fail("Failure expected")
        }
    }

    @Test
    fun `failure subject null`() {
        val failure = validate(anEmailRequestWithNoSubject())
        assertThat(failure).isInstanceOf(Failure::class.java)
    }

    @Test
    fun `failure subject blank`() {
        val failure = validate(anEmailRequestWithBlankSubject())
        assertThat(failure).isInstanceOf(Failure::class.java)
    }

    @Test
    fun `failure subject reason provided`() {
        when (val failure = validate(anEmailRequestWithBlankSubject())) {
            is Failure -> assertThat(failure.issues).contains(aMandatoryIssue("subject"))
            else -> fail("Failure expected")
        }
    }

    @Test
    fun `success of valid email`() {
        val success = validate(anEmailRequest())
        assertThat(success).isInstanceOf(Success::class.java)
    }

    @Test
    fun `failure invalid email original retained`() {
        val emailRequest = anEmailRequestWithBlankSubject()
        when (val failure = validate(emailRequest)) {
            is Failure -> assertThat(failure.value).isSameAs(emailRequest)
            else -> fail("Failure expected")
        }
    }

    @Test
    fun `success of valid email original retained`() {
        val emailRequest = anEmailRequest()
        when (val success = validate(emailRequest)) {
            is Success -> assertThat(success.value).isSameAs(emailRequest)
            else -> fail("Success expected")
        }
    }

    private fun aMandatoryIssue(valueName: String) = "Attribute $valueName is mandatory"
}