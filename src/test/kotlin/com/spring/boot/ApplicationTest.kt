package com.spring.boot

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.icegreen.greenmail.util.ServerSetupTest
import com.icegreen.greenmail.junit.GreenMailRule
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.GreenMailUtil.getBody
import org.junit.Rule
import com.icegreen.greenmail.util.ServerSetup
import org.assertj.core.api.Assertions.assertThat
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringBootPlaygroundApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Rule @JvmField
	final val greenMail = GreenMailRule(ServerSetup(1025, null, "smtp"))

	@Test
	fun `health check`() {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk)
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("status").value("up"))
	}

	@Test
	fun `email sent`() {
		val emailRequest = anEmailRequest()
		mockMvc.perform(post("/email").content(toJson(emailRequest)).contentType(APPLICATION_JSON_UTF8))
		val emailSent = greenMail.receivedMessages.first()
		assertThat(firstRecipient(emailSent)).isEqualTo(InternetAddress(emailRequest.to))
		assertThat(emailSent.subject).isEqualTo(emailRequest.subject)
		assertThat(getBody(emailSent)).contains(emailRequest.body)
		assertThat(getBody(emailSent)).contains(emailRequest.attachments.first().content)
		assertThat(getBody(emailSent)).contains(emailRequest.attachments.first().extension)
		assertThat(getBody(emailSent)).contains(emailRequest.attachments.first().name)
	}

	@Test
	fun `email created`() {
		val emailRequest = anEmailRequest()
		mockMvc.perform(post("/email").content(toJson(anEmailRequest())).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated)
	}

	@Test
	fun `send email fails validation`() {
		val emailRequest = anEmailRequestWithBlankSubject()
		mockMvc.perform(post("/email").content(toJson(emailRequest)).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest)
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(toJson(anEmailResponseWithBlankSubjectIssue())))
	}

	private fun firstRecipient(emailSent: MimeMessage) = emailSent.getRecipients(Message.RecipientType.TO)[0]
}
