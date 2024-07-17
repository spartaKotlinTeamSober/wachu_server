package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.review.model.v1.Review

data class ReviewResponse(
    val id: Long,
    val wineId: Long,
    val memberId: Long,
    val title: String,
    val description: String,
    val score: Double,
    val createdAt: String,
) {
    companion object {
        fun from(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id!!,
                wineId = review.wineId,
                memberId = review.memberId,
                title = review.title,
                description = review.description,
                score = review.score,
                createdAt = review.createdAt.toString(),
            )
        }
    }
}
