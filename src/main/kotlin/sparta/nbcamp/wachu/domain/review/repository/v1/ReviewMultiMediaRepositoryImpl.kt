package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia

@Repository
class ReviewMultiMediaRepositoryImpl(
    private val reviewMultiMediaJpaRepository: ReviewMultiMediaJpaRepository,
) : ReviewMultiMediaRepository {
    override fun mediaSave(media: List<ReviewMultiMedia>): List<ReviewMultiMedia> {
        return reviewMultiMediaJpaRepository.saveAll(media)
    }

    override fun mediaFindAll(reviewId: Long): List<ReviewMultiMedia> {
        return reviewMultiMediaJpaRepository.findAllByReviewId(reviewId)
    }

    override fun deleteAllByReviewId(reviewId: Long) {
        return reviewMultiMediaJpaRepository.deleteAllByReviewId(reviewId)
    }
}