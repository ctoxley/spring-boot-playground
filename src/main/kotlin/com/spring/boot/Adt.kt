package com.spring.boot

sealed class Try<out T>

data class Success<out T>(val value: T) : Try<T>()
data class Failure<out T>(val value: T, val issues: List<String>) : Try<T>()
