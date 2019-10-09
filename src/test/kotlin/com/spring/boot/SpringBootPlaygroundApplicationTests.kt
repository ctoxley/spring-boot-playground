package com.spring.boot

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest
class SpringBootPlaygroundApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun `health check status`() {
		mockMvc.perform(get("/health/status").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("status").value("up"))
	}
}
