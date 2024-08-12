package sparta.nbcamp.wachu.domain.review.repository

import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewMultiMediaRepository
import java.util.logging.Level
import java.util.logging.Logger

class ReviewMultiMediaTestRepository(
    private val reviewMultiMedia: List<ReviewMultiMedia>
) : ReviewMultiMediaRepository {
    override fun mediaFindAll(reviewId: Long): List<ReviewMultiMedia> {
        return reviewMultiMedia
    }

    override fun deleteAllByReviewId(reviewId: Long) {
        Logger.getLogger("ReviewMultiMediaTestRepository").log(Level.INFO, "delete")
    }

    override fun mediaSave(media: List<ReviewMultiMedia>): List<ReviewMultiMedia> {
        return reviewMultiMedia
    }
}