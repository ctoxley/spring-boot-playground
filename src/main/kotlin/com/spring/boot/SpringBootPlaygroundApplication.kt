package com.spring.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<SpringBootPlaygroundApplication>(*args)
}
