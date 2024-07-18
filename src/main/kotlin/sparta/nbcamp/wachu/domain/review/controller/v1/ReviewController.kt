package sparta.nbcamp.wachu.domain.review.controller.v1

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

@RestController
@RequestMapping("/api/v1/reviews")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @GetMapping
    fun getReviewList(): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(reviewService.getReviewList())
    }

    @GetMapping("/{reviewId}")
    fun getReview(
        @PathVariable reviewId: Long,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.getReview(reviewId))
    }

    @PostMapping
    fun createReview(
        @AuthenticationPrincipal userprincipal: UserPrincipal,
        @RequestBody reviewRequest: ReviewRequest,
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reviewService.createReview(userprincipal, reviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @AuthenticationPrincipal userprincipal: UserPrincipal,
        @PathVariable reviewId: Long,
    ): ResponseEntity<Unit> {
        reviewService.deleteReview(userprincipal, reviewId)
        return ResponseEntity.noContent().build()
    }
}