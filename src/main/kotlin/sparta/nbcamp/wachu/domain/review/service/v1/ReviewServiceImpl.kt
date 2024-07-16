package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.stereotype.Service
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse

@Service
class ReviewServiceImpl : ReviewService {
    override fun getReviewList(): List<ReviewResponse> {
        TODO()
    }

    override fun getReview(id: Long): ReviewResponse {
        TODO()
    }

    override fun createReview(reviewRequest: ReviewRequest): ReviewResponse {
        TODO()
    }

    override fun deleteReview(id: Long) {
        TODO()
    }
}