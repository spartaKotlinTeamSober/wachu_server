package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse

@Service
class ReviewServiceImpl : ReviewService {
    override fun getReviewList(): List<ReviewResponse> {
        TODO()
    }

    @Transactional
    override fun getReview(id: Long): ReviewResponse {
        TODO()
    }

    @Transactional
    override fun createReview(reviewRequest: ReviewRequest): ReviewResponse {
        TODO()
    }

    @Transactional
    override fun deleteReview(id: Long) {
        TODO()
    }
}