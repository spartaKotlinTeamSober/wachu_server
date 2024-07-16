package sparta.nbcamp.wachu.domain.review.service.v1

import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse

interface ReviewService {
    fun getReviewList(): List<ReviewResponse>
    fun getReview(id: Long): ReviewResponse
    fun createReview(reviewRequest: ReviewRequest): ReviewResponse
    fun deleteReview(id: Long)
}