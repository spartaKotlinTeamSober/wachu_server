package sparta.nbcamp.wachu.domain.review.repository.v1

import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia

interface ReviewMultiMediaRepository {
    fun mediaSave(media: List<ReviewMultiMedia>): List<ReviewMultiMedia>
    fun mediaFindAll(reviewId: Long): List<ReviewMultiMedia>
    fun deleteAllByReviewId(reviewId: Long)
}