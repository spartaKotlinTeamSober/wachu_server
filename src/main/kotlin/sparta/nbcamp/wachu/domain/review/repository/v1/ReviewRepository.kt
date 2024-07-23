package sparta.nbcamp.wachu.domain.review.repository.v1

import sparta.nbcamp.wachu.domain.review.model.v1.Review
import sparta.nbcamp.wachu.domain.review.model.v1.ReviewMultiMedia

interface ReviewRepository {
    fun findById(id: Long): Review?
    fun findAll(): List<Review>
    fun save(review: Review): Review
    fun delete(review: Review)
    fun mediaSave(media: List<ReviewMultiMedia>): List<ReviewMultiMedia>
}