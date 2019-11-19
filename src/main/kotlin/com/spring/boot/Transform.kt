package com.spring.boot

import org.springframework.mail.javamail.MimeMessageHelper
import java.io.ByteArrayInputStream
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.text.Charsets.UTF_8

val NO_ATTACHMENT_CONTENT = "".byteInputStream(UTF_8)

typealias EmailTransformer = (EmailRequest, MimeMessageSupplier) -> MimeMessage
typealias MimeMessageSupplier = () -> MimeMessage

fun toMimeMessage(emailRequest: EmailRequest, mimeMessageSupplier: MimeMessageSupplier): MimeMessage {
    val mimeMessage = mimeMessageSupplier()
    val mimeMessageHelper = MimeMessageHelper(mimeMessage, emailRequest.hasAttachments())
    mimeMessageHelper.setTo(emailRequest.to!!)
    mimeMessageHelper.setSubject(emailRequest.subject ?: "no subject supplied")
    mimeMessageHelper.setText(emailRequest.body ?: "no body supplied")
    emailRequest.attachments.forEach {
        mimeMessageHelper.addAttachment("${it.name}${it.extension}") {
            it.content?.byteInputStream(UTF_8) ?: NO_ATTACHMENT_CONTENT
        }
    }
    return mimeMessage
}
