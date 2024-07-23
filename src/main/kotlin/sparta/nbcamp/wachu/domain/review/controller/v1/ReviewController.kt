package sparta.nbcamp.wachu.domain.review.controller.v1

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewMultiMediaResponse
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

    @PostMapping("/{reviewId}",consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createReviewMedia(
        @AuthenticationPrincipal userprincipal: UserPrincipal,
        @PathVariable reviewId: Long,
        @RequestPart(name = "image", required = false) multipartFileList: List<MultipartFile>
    ):ResponseEntity<List<ReviewMultiMediaResponse>> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reviewService.createReviewMedia(userprincipal, reviewId, multipartFileList))
    }
}