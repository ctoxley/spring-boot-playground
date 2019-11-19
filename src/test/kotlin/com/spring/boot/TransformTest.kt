package com.spring.boot

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import javax.mail.BodyPart
import javax.mail.Message.RecipientType.TO
import javax.mail.Multipart
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.streams.toList


class TransformTest {

    private lateinit var emailRequest: EmailRequest
    private lateinit var mockMimeMessageSupplier: MimeMessageSupplier
    private lateinit var mockMimeMessage: MimeMessage

    @Before
    fun setUp() {
        emailRequest = anEmailRequest()
        mockMimeMessage = mock()
        mockMimeMessageSupplier = mock {
            on { invoke() } doReturn mockMimeMessage
        }
    }

    @Test
    fun `attachment filename transformed`() {
        toMimeMessage(emailRequest, mockMimeMessageSupplier)
        withRootMixedMultipartOfMockMimeMessage {
            assertThat(secondBodyPart(it).fileName).isEqualTo("${firstAttachment().name}${firstAttachment().extension}")
        }
    }

    @Test
    fun `attachment content transformed`() {
        toMimeMessage(emailRequest, mockMimeMessageSupplier)
        withRootMixedMultipartOfMockMimeMessage {
            assertThat(firstLineOf(secondBodyPart(it))).isEqualTo(firstAttachment().content)
        }
    }

    @Test
    fun `body not supplied`() {
        toMimeMessage(anEmailRequestWithoutBody(), mockMimeMessageSupplier)
        withRootMixedMultipartOfMockMimeMessage {
            assertThat(thirdLineOf(firstBodyPart(it))).isEqualTo("no body supplied")
        }
    }

    @Test
    fun `body transformed`() {
        toMimeMessage(emailRequest, mockMimeMessageSupplier)
        withRootMixedMultipartOfMockMimeMessage {
            assertThat(thirdLineOf(firstBodyPart(it))).isEqualTo(emailRequest.body)
        }
    }

    @Test
    fun `subject not supplied`() {
        toMimeMessage(anEmailRequestWithoutSubject(), mockMimeMessageSupplier)
        verify(mockMimeMessage).subject = "no subject supplied"
    }

    @Test
    fun `subject transformed`() {
        toMimeMessage(emailRequest, mockMimeMessageSupplier)
        verify(mockMimeMessage).subject = emailRequest.subject
    }

    @Test
    fun `to transformed`() {
        toMimeMessage(emailRequest, mockMimeMessageSupplier)
        verify(mockMimeMessage).setRecipient(TO, InternetAddress(emailRequest.to))
    }

    private fun firstAttachment() = emailRequest.attachments.first()
    private fun secondBodyPart(multipart: Multipart) = multipart.getBodyPart(1)
    private fun firstBodyPart(multipart: Multipart) = multipart.getBodyPart(0)
    private fun thirdLineOf(bodyPart: BodyPart) = nLineOf(bodyPart, 2)
    private fun firstLineOf(bodyPart: BodyPart) = nLineOf(bodyPart, 0)
    private fun nLineOf(bodyPart: BodyPart, index: Int) = bodyPart.inputStream.bufferedReader().lines().toList()[index]

    private fun withRootMixedMultipartOfMockMimeMessage(assertMultiPart: (Multipart) -> Unit) =
        argumentCaptor<Multipart>().apply {
            verify(mockMimeMessage).setContent(capture())
            assertMultiPart(firstValue)
        }
}