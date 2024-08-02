package sparta.nbcamp.wachu.domain.review.repository.v1

import org.springframework.data.jpa.repository.JpaRepository
import sparta.nbcamp.wachu.domain.review.model.v1.Review

interface ReviewJpaRepository : JpaRepository<Review, Long>