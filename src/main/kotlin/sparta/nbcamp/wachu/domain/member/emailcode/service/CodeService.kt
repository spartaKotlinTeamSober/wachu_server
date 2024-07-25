package sparta.nbcamp.wachu.domain.member.emailcode.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CodeService(
    private val mailSender: JavaMailSender,
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun sendCode(email: String) {
        val code = generateCode()
        saveCode(email, code)

        val message = SimpleMailMessage().apply {
            from = System.getenv("MAIL_USERNAME")
            setTo(email)
            subject = "Your Verification Code"
            text = "Your verification code is: $code"
        }
        try {
            mailSender.send(message)
        } catch (e: MailSendException) {
            throw MailSendException("Error : $e", e)
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