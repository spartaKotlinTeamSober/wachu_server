package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.member.dto.ProfileResponse
import sparta.nbcamp.wachu.domain.member.entity.Member
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.wine.dto.WineResponse
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val wine: WineResponse,
    val member: ProfileResponse,
    val title: String,
    val description: String,
    val score: Double,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(review: Review, member: Member): ReviewResponse {
            return ReviewResponse(
                id = review.id!!,
                wine = WineResponse.from(review.wine),
                member = ProfileResponse.from(member),
                title = review.title,
                description = review.description,
                score = review.score,
                createdAt = review.createdAt,
            )
        }
    }
}
