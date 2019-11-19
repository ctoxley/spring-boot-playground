package com.spring.boot

import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.internet.MimeMessage

class ControllerTest {

    private lateinit var healthController: HealthController
    private lateinit var emailController: EmailController
    private lateinit var validEmailRequest: EmailRequest
    private lateinit var invalidEmailRequest: EmailRequest
    private lateinit var mockSuccessValidator: Validator<EmailRequest>
    private lateinit var mockEmailTransformer: EmailTransformer
    private lateinit var mockMimeMessage: MimeMessage
    private lateinit var mimeMessageSupplier: MimeMessageSupplier
    private lateinit var mockJavaMailSender: JavaMailSender

    @Before
    fun setUp() {
        healthController = HealthController()
        validEmailRequest = anEmailRequest()
        invalidEmailRequest = anEmailRequestWithBlankSubject()
        mimeMessageSupplier = { mockMimeMessage }
        mockSuccessValidator = mock {
            on { invoke(validEmailRequest) } doReturn Success(validEmailRequest)
            on { invoke(invalidEmailRequest) } doReturn aFailureWithBlackSubjectIssue(invalidEmailRequest)
        }
        mockMimeMessage = mock()
        mockEmailTransformer = mock {
            on { invoke(eq(validEmailRequest), any())  } doReturn mockMimeMessage
        }
        mockJavaMailSender = mock()
        emailController = EmailController(mockSuccessValidator, mockEmailTransformer, mockJavaMailSender)
    }

    @Test
    fun `email send`() {
        emailController.send(validEmailRequest)
        verify(mockJavaMailSender).send(mockMimeMessage)
    }

    @Test
    fun `email request transformed`() {
        emailController.send(validEmailRequest)
        verify(mockEmailTransformer).invoke(eq(validEmailRequest), any())
    }

    @Test
    fun `email request has attachments`() {
        assertThat(validEmailRequest.hasAttachments()).isTrue()
    }

    @Test
    fun `email request can be empty`() {
        val emailRequest = fromJson("{}", EmailRequest::class.java)
        assertThat(emailRequest).isEqualTo(anEmptyEmailRequest())
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