package sparta.nbcamp.wachu.domain.review.service.v1

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewMultiMediaResponse
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewRequest
import sparta.nbcamp.wachu.domain.review.dto.v1.ReviewResponse
import sparta.nbcamp.wachu.infra.security.jwt.UserPrincipal

interface ReviewService {
    fun getReviewPage(pageable: Pageable): Page<ReviewResponse>
    fun getReview(id: Long): ReviewResponse
    fun createReview(
        userPrincipal: UserPrincipal,
        reviewRequest: ReviewRequest,
        images: List<MultipartFile>?
    ): ReviewResponse

    fun deleteReview(userPrincipal: UserPrincipal, id: Long)
    fun createReviewMedia(
        userPrincipal: UserPrincipal,
        reviewId: Long,
        multipartFileList: List<MultipartFile>
    ): List<ReviewMultiMediaResponse>

    fun getReviewMultiMedia(reviewId: Long): List<ReviewMultiMediaResponse>
}