package sparta.nbcamp.wachu.domain.review.controller.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse
import sparta.nbcamp.wachu.domain.review.service.v1.ReviewService

@RestController
@RequestMapping("/api/v1/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @GetMapping
    fun getReviewList(): ResponseEntity<List<ReviewResponse>> {
        TODO()
    }

    @GetMapping("/{reviewId}")
    fun getReview(
        @PathVariable reviewId: Long,
    ): ResponseEntity<ReviewResponse> {
        TODO()
    }

    @PostMapping
    fun createReview(
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<ReviewResponse> {
        TODO()
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable reviewId: Long,
    ) {
        TODO()
    }
}