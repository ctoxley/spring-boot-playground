package com.spring.boot

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED

class ControllerTest {

    private lateinit var healthController: HealthController
    private lateinit var emailController: EmailController
    private lateinit var validEmailRequest: EmailRequest
    private lateinit var invalidEmailRequest: EmailRequest
    private lateinit var mockSuccessValidator: Validator<EmailRequest>

    @Before
    fun setUp() {
        healthController = HealthController()
        validEmailRequest = anEmailRequest()
        invalidEmailRequest = anEmailRequestWithBlankSubject()
        mockSuccessValidator = mock {
            on { invoke(validEmailRequest) } doReturn Success(validEmailRequest)
            on { invoke(invalidEmailRequest) } doReturn aFailureWithBlackSubjectIssue(invalidEmailRequest)
        }
        emailController = EmailController(mockSuccessValidator)
    }

    @Test
    fun `status response ok`() {
        val response = healthController.status()
        assertThat(response.statusCode).isEqualTo(OK)
    }

    @Test
    fun `status up response`() {
        val response = healthController.status()
        assertThat(response.body).isEqualTo(HealthResponse("up"))
    }

    @Test
    fun `on validation failure respond with bad request`() {
        val response = emailController.send(invalidEmailRequest)
        assertThat(response.statusCode).isEqualTo(BAD_REQUEST)
    }

    @Test
    fun `on validation failure respond with issue`() {
        val response = emailController.send(invalidEmailRequest)
        assertThat(response.body).isEqualTo(anEmailResponseWithBlankSubjectIssue())
    }

    @Test
    fun `on validation success respond with created`() {
        val response = emailController.send(validEmailRequest)
        assertThat(response.statusCode).isEqualTo(CREATED)
    }
}