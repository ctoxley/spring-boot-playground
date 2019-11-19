package com.spring.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.beans
import org.springframework.mail.javamail.JavaMailSender

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}