package sparta.nbcamp.wachu.domain.review.controller

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest

class ReviewControllerTest {
    @Test
    fun `reviewRequest 의 score가 0_0~5_0 사이이고 0_5 단위인지 확인한다`() {
        shouldThrow<IllegalArgumentException> {
            ReviewRequest(
                wineId = 1L,
                title = "newtest",
                description = "newtest",
                score = 1.3
            )
        }
    }
}