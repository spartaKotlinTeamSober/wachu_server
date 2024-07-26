package sparta.nbcamp.wachu.domain.review.dto.v1

import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMediaType
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia

data class ReviewMultiMediaResponse(
    val reviewId: Long,
    val mediaUrl: String,
    val mediaType: ReviewMediaType,
) {
    companion object {
        fun from(media: ReviewMultiMedia): ReviewMultiMediaResponse {
            return ReviewMultiMediaResponse(
                reviewId = media.reviewId,
                mediaUrl = media.mediaUrl,
                mediaType = media.mediaType,
            )
        }
    }
}