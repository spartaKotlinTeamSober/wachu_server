package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import sparta.nbcamp.wachu.domain.review.model.v1.Review

interface ReviewRepository {
    fun findById(id: Long): Review?
    fun findAll(pageable: Pageable): Page<Review>
    fun save(review: Review): Review
    fun delete(review: Review)
}