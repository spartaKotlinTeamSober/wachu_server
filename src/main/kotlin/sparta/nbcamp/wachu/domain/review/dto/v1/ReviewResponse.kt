package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val wine: WineResponse,
    val memberId: Long,
    val title: String,
    val description: String,
    val score: Double,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id!!,
                wine = WineResponse.from(review.wine),
                memberId = review.memberId,
                title = review.title,
                description = review.description,
                score = review.score,
                createdAt = review.createdAt,
            )
        }
    }
}
