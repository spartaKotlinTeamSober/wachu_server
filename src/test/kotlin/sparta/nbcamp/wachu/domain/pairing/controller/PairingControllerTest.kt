package sparta.nbcamp.wachu.domain.pairing.controller

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import sparta.nbcamp.wachu.domain.pairing.model.v1.Pairing

class PairingControllerTest {
    val defaultPairingList = List(10) { index ->
        Pairing(
            wineId = index.toLong(),
            memberId = index.toLong(),
            title = "test$index",
            description = "testDescription$index",
            photoUrl = "testPhoto$index$"
        ).apply { id = index.toLong() }
    }

    @Test
    fun `페이지넘버와 사이즈에 해당하는 pairing 목록을 반환한다`() {

        val testPageable = PageRequest.of(2, 3)
        val testList = listOf(defaultPairingList.slice(6..8))
        val testPage = PageImpl(testList, testPageable, 10)

        testList shouldBe testPage.content
    }
}