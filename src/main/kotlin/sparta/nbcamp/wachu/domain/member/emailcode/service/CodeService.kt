package sparta.nbcamp.wachu.domain.member.emailcode.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
class CodeService(
    private val mailSender: JavaMailSender,
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${spring.mail.username}") private val mailUsername: String
) {
    @Async("mailExecutor")
    fun sendCode(email: String): CompletableFuture<String> {
        val code = generateCode()
        saveCode(email, code)

        val codeTimeout = redisTemplate.getExpire(email, TimeUnit.MINUTES)

        val message = SimpleMailMessage().apply {
            from = mailUsername
            setTo(email)
            subject = "Your Verification Code"
            text = "Your verification code is: $code."
        }
        return try {
            mailSender.send(message)
            CompletableFuture.completedFuture("Verify within $codeTimeout minutes")
        } catch (e: MailSendException) {
            CompletableFuture.failedFuture(e)
        }
    }

    fun checkCode(email: String, code: String): Boolean {
        val savedCode = redisTemplate.opsForValue().get(email)
        return savedCode != null && savedCode == code
    }

    // 인증코드 임시로 1000~9999 사이의 랜덤한 숫자로 설정
    private fun generateCode(): String {
        return (1000..9999).random().toString()
    }

    // 인증코드 저장시간 임시로 10분으로 설정
    private fun saveCode(email: String, code: String) {
        redisTemplate.opsForValue().set(email, code, 10, TimeUnit.MINUTES)
    }
}