package sparta.nbcamp.wachu.domain.review.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.review.repository.v1.ReviewRepository
import java.util.logging.Level
import java.util.logging.Logger

class ReviewTestRepositoryImpl(
    private val review: Review,
    private val reviewPage: Page<Review>,
) : ReviewRepository {
    override fun findById(id: Long): Review? {
        if (id !in 1..10) return null
        return review
    }

    override fun findAll(pageable: Pageable): Page<Review> {
        return reviewPage
    }

    override fun save(review: Review): Review {
        return review.apply { id = 0L }
    }

    override fun delete(review: Review) {
        Logger.getLogger("ReviewTestRepositoryImpl").log(Level.INFO, "delete")
    }
}