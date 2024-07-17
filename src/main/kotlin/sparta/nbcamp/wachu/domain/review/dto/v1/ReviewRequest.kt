package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.review.model.v1.Review

data class ReviewRequest(
    val wineId: Long,
    val title: String,
    val description: String,
    val score: Double,
) {
    companion object {
        fun toEntity(memberId: Long, reviewRequest: ReviewRequest): Review {
            return Review(
                wineId = reviewRequest.wineId,
                memberId = memberId,
                title = reviewRequest.title,
                description = reviewRequest.description,
                score = reviewRequest.score,
            )
        }
    }

    fun hasOKScore(score: Double): Boolean {
        return this.score in 0.0..5.0 && this.score % 0.5 == 0.0
    }
}
