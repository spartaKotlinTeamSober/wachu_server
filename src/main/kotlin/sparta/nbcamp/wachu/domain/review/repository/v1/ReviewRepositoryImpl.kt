package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia

@Repository
class ReviewRepositoryImpl(
    val reviewJpaRepository: ReviewJpaRepository,
    val reviewMultiMediaJpaRepository: ReviewMultiMediaJpaRepository,
    val reviewQueryDslRepository: ReviewQueryDslRepository,
) : ReviewRepository {
    override fun findById(id: Long): Review? {
        return reviewJpaRepository.findByIdOrNull(id)
    }

    override fun findAll(pageable: Pageable): Page<Review> {
        return reviewQueryDslRepository.findPage(pageable)
    }

    override fun save(review: Review): Review {
        return reviewJpaRepository.save(review)
    }

    override fun delete(review: Review) {
        return reviewJpaRepository.delete(review)
    }

    override fun mediaSave(media: List<ReviewMultiMedia>): List<ReviewMultiMedia> {
        return reviewMultiMediaJpaRepository.saveAll(media)
    }

    override fun mediaFindAll(reviewId: Long): List<ReviewMultiMedia> {
        return reviewMultiMediaJpaRepository.findAllByReviewId(reviewId)
    }
}