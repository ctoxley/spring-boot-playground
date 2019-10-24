package com.spring.boot

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus.OK

class ControllerTest {

    private lateinit var healthController: HealthController

    @Before
    fun setUp() {
        healthController = HealthController()
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
}