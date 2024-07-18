package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import sparta.nbcamp.wachu.domain.review.model.v1.Review

@Repository
class ReviewRepositoryImpl(
    val reviewJpaRepository: ReviewJpaRepository,
) : ReviewRepository {
    override fun findById(id: Long): Review? {
        return reviewJpaRepository.findByIdOrNull(id)
    }

    override fun findAll(): List<Review> {
        return reviewJpaRepository.findAll()
    }

    override fun save(review: Review): Review {
        return reviewJpaRepository.save(review)
    }

    override fun delete(review: Review) {
        return reviewJpaRepository.delete(review)
    }
}