package sparta.nbcamp.wachu.domain.security

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ImsiTest {

    @Test
    fun imsi() {
        val a = 1
        val b = 2
        a + b shouldBe 3
    }
}